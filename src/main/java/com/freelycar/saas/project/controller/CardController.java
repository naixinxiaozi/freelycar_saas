package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Card;
import com.freelycar.saas.project.service.CardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangwei - Toby
 * @date 2018-12-27
 * @email toby911115@gmail.com
 */
@Api(value = "会员卡接口", tags = "会员卡服务接口")
@RestController
@RequestMapping("/card")
public class CardController {
    private Logger logger = LoggerFactory.getLogger(CardController.class);
    @Autowired
    private CardService cardService;

    @ApiOperation(value = "开卡", produces = "application/json")
    @PostMapping("/handleCard")
    @LoggerManage(description = "调用方法：开卡")
    public ResultJsonObject handleCard(@RequestBody Card card) {

        try {
            return cardService.handleCard(card);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult("生成会员卡购买单据失败，无法开卡。");

        }
    }

    /**
     * 获取会员卡详情
     * @param id
     * @return
     */
    @ApiOperation(value = "获取会员卡详情", produces = "application/json")
    @GetMapping("/detail")
    @LoggerManage(description = "调用方法：获取会员卡详情")
    public ResultJsonObject detail(@RequestParam String id) {
        return cardService.getDetail(id);
    }

    @ApiOperation(value = "获取客户的所有会员卡", produces = "application/json")
    @GetMapping("/getMyCards")
    @LoggerManage(description = "调用方法：获取客户的所有会员卡")
    public ResultJsonObject getMyCards(
            @RequestParam String id,
            @RequestParam String storeId
    ) {
        return cardService.getMyCards(id, storeId);
    }
}
