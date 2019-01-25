package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Card;
import com.freelycar.saas.project.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangwei - Toby
 * @date 2018-12-27
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/card")
public class CardController {
    private Logger logger = LoggerFactory.getLogger(CardController.class);
    @Autowired
    private CardService cardService;
    private String errorMsg;

    @PostMapping("/handleCard")
    public ResultJsonObject handleCard(@RequestBody Card card) {

        return cardService.handleCard(card);
    }

    /**
     * 获取会员卡详情
     * @param id
     * @return
     */
    @GetMapping(value = "/detail")
    @LoggerManage(description = "调用方法：获取会员卡详情")
    public ResultJsonObject detail(@RequestParam String id) {
        if (null == id) {
            errorMsg = "接收到的参数：id为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return cardService.getDetail(id);
    }
}
