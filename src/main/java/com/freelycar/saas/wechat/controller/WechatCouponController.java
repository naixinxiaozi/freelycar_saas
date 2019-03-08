package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.ObjectNotFoundException;
import com.freelycar.saas.project.service.CouponServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangwei - Toby
 * @date 2019-03-07
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/wechat/coupon")
public class WechatCouponController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CouponServiceService couponServiceService;


    @GetMapping("/generateCouponOrder")
    public ResultJsonObject generateCouponOrder(@RequestParam String clientId, @RequestParam String couponServiceId) {
        try {
            return ResultJsonObject.getDefaultResult(couponServiceService.generateCouponOrder(clientId, couponServiceId));
        } catch (ObjectNotFoundException | ArgumentMissingException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null);
        }
    }
}
