package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.WxUserInfo;
import com.freelycar.saas.project.service.CardService;
import com.freelycar.saas.project.service.CouponService;
import com.freelycar.saas.project.service.WxUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangwei - Toby
 * @date 2019-01-07
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/wechat/wxuser")
public class WeChatWxUserInfoController {

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CardService cardService;

    @GetMapping("/getPersonalInfo")
    public ResultJsonObject getPersonalInfo(@RequestParam String id) {
        return wxUserInfoService.getPersonalInfo(id);
    }

    @GetMapping("/getDetail")
    public ResultJsonObject getDetail(@RequestParam String id) {
        return wxUserInfoService.getDetail(id);
    }

    @PostMapping("/chooseDefaultStore")
    public ResultJsonObject chooseDefaultStore(@RequestBody WxUserInfo wxUserInfo) {
        return wxUserInfoService.chooseDefaultStore(wxUserInfo);
    }

    @GetMapping("/getMyCoupons")
    public ResultJsonObject getMyCoupons(
            @RequestParam String id,
            @RequestParam String storeId
    ) {
        return couponService.getMyCoupons(id, storeId);
    }

    @GetMapping("/getMyCards")
    public ResultJsonObject getMyCards(
            @RequestParam String id,
            @RequestParam String storeId
    ) {
        return cardService.getMyCards(id, storeId);
    }
}
