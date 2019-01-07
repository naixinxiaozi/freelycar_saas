package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.service.WxUserInfoService;
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
@RequestMapping("/wechat/wxuser")
public class WeChatWxUserInfoController {

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @GetMapping("/getPersonalInfo")
    public ResultJsonObject getPersonalInfo(@RequestParam String id) {
        return null;
    }
}
