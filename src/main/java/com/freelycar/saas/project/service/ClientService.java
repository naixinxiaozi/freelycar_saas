package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Car;
import com.freelycar.saas.project.entity.Card;
import com.freelycar.saas.project.entity.Client;
import com.freelycar.saas.project.entity.Coupon;
import com.freelycar.saas.project.model.CustomerInfo;
import com.freelycar.saas.project.model.CustomerList;
import com.freelycar.saas.project.model.NewClientInfo;
import com.freelycar.saas.project.repository.CarRepository;
import com.freelycar.saas.project.repository.CardRepository;
import com.freelycar.saas.project.repository.ClientRepository;
import com.freelycar.saas.project.repository.CouponRepository;
import com.freelycar.saas.util.UpdateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * @author tangwei - Toby
 * @date 2018-12-25
 * @email toby911115@gmail.com
 */
@Service
@Transactional
public class ClientService {
    private static Logger logger = LoggerFactory.getLogger(ClientService.class);
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CarService carService;

    /**
     * 同时保存客户信息和车辆信息
     *
     * @param client
     * @param car
     * @return
     */
    public ResultJsonObject addClientAndCar(Client client, Car car) {
        //非空验证
        if (null == client || null == car) {
            return ResultJsonObject.getErrorResult("保存失败！客户信息或车辆信息为空！");
        }

        //保存用户信息
        Client clientRes = this.saveOrUpdate(client);
        if (null == clientRes) {
            return ResultJsonObject.getErrorResult("保存失败！保存客户信息失败！");
        }
        String clientId = clientRes.getId();
        car.setClientId(clientId);

        //保存车辆信息
        Car carRes = carService.saveOrUpdate(car);

        if (null == carRes) {
            return ResultJsonObject.getErrorResult("保存失败！保存车辆信息失败！");
        }

        //组装成对应model返回到前台
        NewClientInfo newClientInfo = new NewClientInfo();
        newClientInfo.setClient(clientRes);
        newClientInfo.setCar(carRes);

        return ResultJsonObject.getDefaultResult(newClientInfo);
    }

    /**
     * 保存/修改客户信息
     *
     * @param client
     * @return
     */
    public Client saveOrUpdate(Client client) {
        if (null == client) {
            return null;
        }

        String id = client.getId();
        if (StringUtils.isEmpty(id)) {
            client.setCreateTime(new Timestamp(System.currentTimeMillis()));
            client.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            client.setMember(false);
            client.setPoints(0);
            client.setState(0);
        } else {
            Optional<Client> optionalClient = clientRepository.findById(id);
            if (!optionalClient.isPresent()) {
                return null;
            }
            Client source = optionalClient.get();
            UpdateTool.copyNullProperties(source, client);
        }
        return clientRepository.saveAndFlush(client);
    }

    /**
     * 获取客户详情（不包括会员卡、车辆）
     *
     * @param id
     * @return
     */
    public ResultJsonObject getDetail(String id) {
        return ResultJsonObject.getDefaultResult(clientRepository.findById(id));
    }

    /**
     * 获取客户详情（包括会员卡、车辆）
     *
     * @param id
     * @return
     */
    public ResultJsonObject getCustomerInfo(String id) {


        Optional<Client> optionalClient=clientRepository.findById(id);
        if (!optionalClient.isPresent()) {
            return ResultJsonObject.getErrorResult(null, "不存在id为"+id+"的用户。");
        }
        Client client=optionalClient.get();

        List<Car> cars = carRepository.findByClientIdAndDelStatus(id, Constants.DelStatus.NORMAL.isValue());

        List<Card> cards = cardRepository.findByClientIdAndDelStatus(id, Constants.DelStatus.NORMAL.isValue());

        List<Coupon> coupons = couponRepository.findByClientIdAndDelStatus(id, Constants.DelStatus.NORMAL.isValue());

        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setClient(client);
        customerInfo.setCar(cars);
        customerInfo.setCard(cards);
        customerInfo.setCoupon(coupons);


        return ResultJsonObject.getDefaultResult(customerInfo);

    }

    /**
     * 获取客户列表
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @param name
     * @param phone
     * @return
     */
    public ResultJsonObject list(String storeId, Integer currentPage,Integer pageSize,String name,String phone,boolean isMember,String licensePlate) {
        logger.debug("storeId:" + storeId);
        //TODO 会员管理—客户管理—获取客户列表
        // Page<Client> clientPage = clientRepository.findAllByDelStatusAndStoreIdAndNameContainingAndPhoneContainingAndIsMember(Constants.DelStatus.NORMAL.isValue(), storeId,name,phone, isMember,PageableTools.basicPage(currentPage, pageSize));
        //Page<Client> clientPage=clientRepository.asd(name,phone,licensePlate,carBrand,isMember,consumeTimes,lastVisit,balance);



        CustomerList customerList=new CustomerList();
        customerList.setName(clientRepository.findName(name));
        customerList.setPhone(clientRepository.findPhone(phone));
        customerList.setLicensePlate(clientRepository.findLicensePlate(licensePlate));

        return ResultJsonObject.getDefaultResult(customerList);
    }
}
