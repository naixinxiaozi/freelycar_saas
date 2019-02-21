package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.jwt.TokenAuthenticationUtil;
import com.freelycar.saas.project.entity.Car;
import com.freelycar.saas.project.entity.Client;
import com.freelycar.saas.project.entity.WxUserInfo;
import com.freelycar.saas.project.repository.CarRepository;
import com.freelycar.saas.project.repository.CardRepository;
import com.freelycar.saas.project.repository.ClientRepository;
import com.freelycar.saas.project.repository.WxUserInfoRepository;
import com.freelycar.saas.util.NicknameFilter;
import com.freelycar.saas.util.RoundTool;
import com.freelycar.saas.util.UpdateTool;
import com.freelycar.saas.wechat.model.BaseOrderInfo;
import com.freelycar.saas.wechat.model.PersonalInfo;
import com.freelycar.saas.wechat.model.WeChatUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * @author tangwei
 * @date 2018/9/25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WxUserInfoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WxUserInfoRepository wxUserInfoRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ConsumerOrderService consumerOrderService;

    /**
     * 查找微信用户对象
     *
     * @param id
     * @return
     */
    public WxUserInfo findById(String id) {
        Optional<WxUserInfo> optionalWxUserInfo = wxUserInfoRepository.findById(id);
        return optionalWxUserInfo.orElse(null);
    }

    /**
     * 个人中心获取微信用户
     *
     * @param id
     * @return
     */
    public ResultJsonObject getPersonalInfo(String id) {
        //查找微信用户对象
        WxUserInfo wxUserInfo = this.findById(id);
        if (null == wxUserInfo) {
            return ResultJsonObject.getErrorResult(id, "找不到id为：" + id + "的微信用户信息。");
        }

        PersonalInfo personalInfo = new PersonalInfo();

        //获取车辆信息

        List<Car> carList;
        Float carBalance = null;
        String defaultStoreId = wxUserInfo.getDefaultStoreId();
        String phone = wxUserInfo.getPhone();
        if (StringUtils.isEmpty(defaultStoreId)) {
            //未选择默认门店，查询出来的车辆是去重的，后续相关操作需要以车牌号为基准
            carList = carRepository.listCarsByStoreIdWithoutSamePlate(phone);
        } else {
            //查询对应的client对象
            List<Client> clientList = clientRepository.findByPhoneAndStoreIdAndDelStatusOrderByCreateTimeAsc(phone, defaultStoreId, Constants.DelStatus.NORMAL.isValue());
            if (clientList.isEmpty()) {
                return ResultJsonObject.getErrorResult(id, "找不到对应的client用户信息");
            }
            String clientId = clientList.get(0).getId();
            //提供名下车辆的两种查询方式
            if (StringUtils.hasLength(clientId)) {
                carList = carRepository.findByClientIdAndDelStatus(clientId, Constants.DelStatus.NORMAL.isValue());

                //查询对应门店会员卡的余额
                Float balance = cardRepository.sumBalanceByClientId(clientId);
                if (null != balance) {
                    //格式化精度
                    carBalance = RoundTool.round(balance, 2, BigDecimal.ROUND_DOWN);
                }
            } else {
                carList = carRepository.listCarsByStoreIdAndPhone(defaultStoreId, phone);
            }
        }
        personalInfo.setWxUserInfo(wxUserInfo);
        personalInfo.setCars(carList);
        personalInfo.setCardBalance(carBalance);

        return ResultJsonObject.getDefaultResult(personalInfo);
    }

    /**
     * 更改“默认门店”
     *
     * @param wxUserInfo
     * @return
     */
    public ResultJsonObject chooseDefaultStore(WxUserInfo wxUserInfo) {
        String wxUserId = wxUserInfo.getId();
        String defaultStoreId = wxUserInfo.getDefaultStoreId();

        //查找对应的wxUserInfo对象
        Optional<WxUserInfo> optionalWxUserInfo = wxUserInfoRepository.findById(wxUserId);
        if (!optionalWxUserInfo.isPresent()) {
            return ResultJsonObject.getErrorResult(null, "未找到WxUserInfo表中，id为：" + wxUserId + "的数据");
        }
        WxUserInfo source = optionalWxUserInfo.get();
        //获取手机号码
        String phone = source.getPhone();
        if (StringUtils.isEmpty(phone)) {
            return ResultJsonObject.getErrorResult(null, "WxUserInfo表中，id为：" + wxUserId + "的数据内没有有效的手机号码（phone）信息");
        }

        //根据手机号码和选择的门店ID查询出用户Client信息
        Client client = clientRepository.findTopByPhoneAndStoreIdAndDelStatusOrderByCreateTimeAsc(phone, defaultStoreId, Constants.DelStatus.NORMAL.isValue());
        if (null == client) {
            //若没有找到，说明该门店下没有该用户信息，自动拷贝相关信息

            //查询其他门店的client信息
            Client otherStoreClient = clientRepository.findTopByPhoneAndDelStatusOrderByCreateTimeAsc(phone, Constants.DelStatus.NORMAL.isValue());
            if (null == otherStoreClient) {
                //其他门店也没有信息的话就按照微信用户信息生成一条
                Client newClient = new Client();
                newClient.setBirthday(wxUserInfo.getBirthday());
                newClient.setName(wxUserInfo.getNickName());
                newClient.setNickName(wxUserInfo.getNickName());
                newClient.setTrueName(wxUserInfo.getTrueName());
                newClient.setPhone(wxUserInfo.getPhone());
                newClient.setStoreId(defaultStoreId);
                client = clientService.saveOrUpdate(newClient);
            } else {
                //其他门店有的话，产生一条除“所属门店”外一样的信息
                Client newClient = clientService.copyNewObjectForOtherStore(otherStoreClient, defaultStoreId);
                client = clientService.saveOrUpdate(newClient);
                // 同步车辆
                List<Car> sourceCars = carService.listClientCars(otherStoreClient.getId());
                for (Car sourceCar : sourceCars) {
                    Car targetCar = carService.copyNewObjectForOtherStore(sourceCar, client);
                    carService.saveOrUpdate(targetCar);
                }

            }
        }

        String clientId = client.getId();
        wxUserInfo.setDefaultClientId(clientId);

        WxUserInfo res = this.modify(wxUserInfo);
        if (null == res) {
            return ResultJsonObject.getErrorResult(null);
        }
        return ResultJsonObject.getDefaultResult(res);
    }

    /**
     * 新增或修改
     *
     * @param wxUserInfo
     * @return
     */
    public WxUserInfo modify(WxUserInfo wxUserInfo) {
        if (null == wxUserInfo) {
            return null;
        }
        String id = wxUserInfo.getId();
        if (StringUtils.isEmpty(id)) {
            wxUserInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
            wxUserInfo.setDelStatus(Constants.DelStatus.NORMAL.isValue());
        } else {
            Optional<WxUserInfo> optionalWxUserInfo = wxUserInfoRepository.findById(id);
            if (!optionalWxUserInfo.isPresent()) {
                return null;
            }
            WxUserInfo source = optionalWxUserInfo.get();
            UpdateTool.copyNullProperties(source, wxUserInfo);
        }
        return wxUserInfoRepository.saveAndFlush(wxUserInfo);
    }

    /**
     * 通过id查询微信用户表信息
     *
     * @param id
     * @return
     */
    public ResultJsonObject getDetail(String id) {
        WxUserInfo wxUserInfo = this.findById(id);
        if (null == wxUserInfo) {
            return ResultJsonObject.getErrorResult(null, "未找到id为：" + id + " 的微信用户信息！");
        }
        return ResultJsonObject.getDefaultResult(wxUserInfo);
    }

    /**
     * 更改个人信息
     *
     * @param wxUserInfo
     * @return
     */
    public ResultJsonObject saveUserInfo(WxUserInfo wxUserInfo) {
        String id = wxUserInfo.getId();
        if (StringUtils.isEmpty(id)) {
            return ResultJsonObject.getErrorResult(null, "保存失败：参数中id为空值");
        }
        WxUserInfo res = this.modify(wxUserInfo);
        if (null == res) {
            return ResultJsonObject.getErrorResult(null);
        }

        String phone = wxUserInfo.getPhone();
        if (StringUtils.isEmpty(phone)) {
            return ResultJsonObject.getErrorResult(null, "WxUserInfo表中，id为：" + id + "的数据内没有有效的手机号码（phone）信息");
        }

        String trueName = wxUserInfo.getTrueName();
        String nickName = wxUserInfo.getNickName();
        String gender = wxUserInfo.getGender();

        List<Client> clients = clientRepository.findByPhoneAndDelStatusOrderByCreateTimeAsc(phone, Constants.DelStatus.NORMAL.isValue());
        for (Client client : clients) {
            client.setTrueName(trueName);
            client.setNickName(nickName);
            client.setGender(gender);
            clientRepository.save(client);
        }

        return ResultJsonObject.getDefaultResult(res);
    }


    /**
     * 微信登录（手机验证码登录）
     *
     * @param phone
     * @param openId
     * @param headimgurl
     * @param nickName
     * @return
     */
    public ResultJsonObject wechatLogin(String phone, String openId, String headimgurl, String nickName) {
        nickName = NicknameFilter.filter4BytesUTF8(nickName);
        WxUserInfo wxUser = wxUserInfoRepository.findWxUserInfoByDelStatusAndPhone(Constants.DelStatus.NORMAL.isValue(), phone);
        WxUserInfo res;
        if (wxUser == null) {
            //新用户，保存其信息
            WxUserInfo wxUserNew = new WxUserInfo();
            wxUserNew.setPhone(phone);
            wxUserNew.setHeadImgUrl(headimgurl);
            wxUserNew.setNickName(nickName);
            wxUserNew.setOpenId(openId);
            res = this.modify(wxUserNew);
        } else {
            //登录过的用户，更新其微信信息
            wxUser.setHeadImgUrl(headimgurl);
            wxUser.setNickName(nickName);
            wxUser.setOpenId(openId);
            res = wxUserInfoRepository.save(wxUser);
        }

        // 去获取jwt
        String jwt = TokenAuthenticationUtil.generateAuthentication(openId);

        return ResultJsonObject.getDefaultResult(new WeChatUser(jwt, res));
    }

    /**
     * 更改手机号
     *
     * @param phone
     * @param id
     * @return
     */
    public ResultJsonObject changePhone(String phone, String id) {
        WxUserInfo wxUserInfo = wxUserInfoRepository.findById(id).orElse(null);
        if (null == wxUserInfo) {
            return ResultJsonObject.getErrorResult(null, "未查找到id：" + id + " 的微信用户信息");
        }
        String oldPhone = wxUserInfo.getPhone();
        wxUserInfo.setPhone(phone);
        WxUserInfo wxUserInfoRes = wxUserInfoRepository.save(wxUserInfo);
        if (null == wxUserInfoRes) {
            return ResultJsonObject.getErrorResult(null, "微信用户信息更新失败，更换手机号失败");
        }
        // 相关client信息也需要更新
        List<Client> clients = clientRepository.findByPhoneAndDelStatusOrderByCreateTimeAsc(oldPhone, Constants.DelStatus.NORMAL.isValue());


        // 查找是否存在活跃的智能柜订单，如果存在，则提示用户此时不能够更换手机号
        for (Client client : clients) {
            String clientId = client.getId();
            ResultJsonObject resultJsonObject = consumerOrderService.getActiveOrder(clientId);
            if (null != resultJsonObject.getData()) {
                BaseOrderInfo activeArkOrderInfo = (BaseOrderInfo) resultJsonObject.getData();
                String errorMessage = "用户名下下存在未完结的智能柜服务订单，此时不能够更换手机号。请结束订单后再试，或联系门店处理。";
                logger.error(errorMessage + "clientId：" + clientId + "。orderId：" + activeArkOrderInfo.getId());
                throw new RuntimeException(errorMessage);
            }
        }

        //更新对应的clientId表中的手机号
        for (Client client : clients) {
            client.setPhone(phone);
            clientRepository.save(client);
        }

        return ResultJsonObject.getDefaultResult(wxUserInfoRes);
    }

    /**
     * 根据phone获取openId
     *
     * @param phone
     * @return
     */
    public String getOpenId(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        WxUserInfo wxUserInfo = wxUserInfoRepository.findWxUserInfoByDelStatusAndPhone(Constants.DelStatus.NORMAL.isValue(), phone);
        if (null == wxUserInfo) {
            return null;
        }
        return wxUserInfo.getOpenId();
    }

}
