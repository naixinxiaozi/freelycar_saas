package com.freelycar.saas.project.service;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultCode;
import com.freelycar.saas.project.entity.Client;
import com.freelycar.saas.project.entity.ConsumerOrder;
import com.freelycar.saas.project.repository.ConsumerOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author tangwei - Toby
 * @date 2019-02-02
 * @email toby911115@gmail.com
 */
@Service
@Transactional
public class PayService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ConsumerOrderRepository orderRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private WxUserInfoService wxUserInfoService;

    public JSONObject paySuccess(String orderId) {
        JSONObject res = new JSONObject();
        logger.debug("付款成功，进入paySuccess后续处理。");
        double amount;
        String clientId;
        String licensePlate = null;
        Date payDate = new Date();
        int payMethod = Constants.PayMethod.WECHAT_PAY.getCode();
        String programName;
        ConsumerOrder order;
        synchronized (PayService.class) {
            order = orderRepository.findById(orderId).orElse(null);
            if (null == order) {
                res.put(Constants.RESPONSE_CODE_KEY, ResultCode.RESULT_DATA_NONE.code());
                res.put(Constants.RESPONSE_MSG_KEY, ResultCode.RESULT_DATA_NONE.message());
                return res;
            }
            if (order.getState() >= Constants.PayState.FINISH_PAY.getValue()) {
                logger.debug("已处理微信回调，订单已处理。直接返回成功。");
                res.put(Constants.RESPONSE_CODE_KEY, ResultCode.SUCCESS.code());
                res.put(Constants.RESPONSE_MSG_KEY, ResultCode.SUCCESS.message());
                return res;
            }
            order.setPayState(Constants.PayState.FINISH_PAY.getValue());
            amount = order.getTotalPrice();
            clientId = order.getClientId();
            licensePlate = order.getLicensePlate();
        }

        Client client = clientService.findById(clientId);
//        WxUserInfo wxUserInfo = wxUserInfoService

//        String openId = wxUserDao.findUserByPhone(clientDao.findById(clientId).getPhone()).getOpenId();
//        WechatTemplateMessage.paySuccess(order, openId);


        res.put(Constants.RESPONSE_CODE_KEY, ResultCode.SUCCESS.code());
        res.put(Constants.RESPONSE_MSG_KEY, ResultCode.SUCCESS.message());
        return res;
    }
}
