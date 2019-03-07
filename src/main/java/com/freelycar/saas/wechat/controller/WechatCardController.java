package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.ObjectNotFoundException;
import com.freelycar.saas.project.service.CardServiceService;
import com.freelycar.saas.project.service.ConsumerProjectInfoService;
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
@RequestMapping("/wechat/card")
public class WechatCardController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConsumerProjectInfoService consumerProjectInfoService;

    @Autowired
    private CardServiceService cardServiceService;

    @GetMapping("/getCardRecord")
    @LoggerManage(description = "获取会员卡的使用记录")
    public ResultJsonObject getCardRecord(@RequestParam String cardId) {
        try {
            return ResultJsonObject.getDefaultResult(consumerProjectInfoService.getProjectInfosByCardId(cardId));
        } catch (ArgumentMissingException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, e.getMessage());
        }
    }

    @GetMapping("/generateCardOrder")
    public ResultJsonObject generateCardOrder(@RequestParam String clientId, @RequestParam String cardServiceId) {
        try {
            return ResultJsonObject.getDefaultResult(cardServiceService.generateCardOrder(clientId, cardServiceId));
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
