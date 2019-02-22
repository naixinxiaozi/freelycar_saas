package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.service.StoreService;
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
    @GetMapping("/listWeCahtImgs")
    public ResultJsonObject listWeCahtImgs() {
        return storeService.listWeCahtImgs();
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
}
