package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.service.CardServiceService;
import com.freelycar.saas.project.service.StoreService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangwei - Toby
 * @date 2019-01-07
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/wechat/store")
public class WeChatStoreController {
    @Autowired
    private StoreService storeService;

    @Autowired
    private CardServiceService cardServiceService;

    /**
     * 获取所有门店信息
     *
     * @param storeName
     * @return
     */
    @GetMapping("/listAllStore")
    public ResultJsonObject listAllStore(@RequestParam String storeName) {
        return ResultJsonObject.getDefaultResult(storeService.findAllByName(storeName));
    }

    /**
     * 获取门店详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getDetail")
    public ResultJsonObject getDetail(String id) {
        return storeService.getDetail(id);
    }

    /**
     * 获取门店宣传图
     *
     * @param storeId
     * @return
     */
    @GetMapping("/getImgs")
    public ResultJsonObject getImgs(String storeId) {
        return storeService.getImgs(storeId);
    }

    /**
     * 获取公众号宣传图
     *
     * @return
     */
    @GetMapping("/listWeChatImgs")
    public ResultJsonObject listWeChatImgs() {
        return storeService.listWeChatImgs();
    }

    /**
     * 获取所有门店分布信息
     *
     * @return
     */
    @GetMapping("/listAllStoreLocation")
    public ResultJsonObject listAllStoreLocation() {
        return storeService.listAllStoreLocation();
    }

    @ApiOperation(value = "获取门店在售的会员卡列表", produces = "application/json")
    @GetMapping("/listOnSaleCards")
    @LoggerManage(description = "调用wechat接口：获取门店在售的会员卡列表")
    public ResultJsonObject listOnSaleCards(String storeId) {
        return ResultJsonObject.getDefaultResult(cardServiceService.findOnSaleCards(storeId));
    }
}
