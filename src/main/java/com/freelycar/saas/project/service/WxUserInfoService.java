package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Car;
import com.freelycar.saas.project.entity.Client;
import com.freelycar.saas.project.entity.WxUserInfo;
import com.freelycar.saas.project.repository.CarRepository;
import com.freelycar.saas.project.repository.CardRepository;
import com.freelycar.saas.project.repository.ClientRepository;
import com.freelycar.saas.project.repository.WxUserInfoRepository;
import com.freelycar.saas.util.RoundTool;
import com.freelycar.saas.util.UpdateTool;
import com.freelycar.saas.wechat.model.PersonalInfo;
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
@Transactional
public class WxUserInfoService {

    @Autowired
    private WxUserInfoRepository wxUserInfoRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

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
}
