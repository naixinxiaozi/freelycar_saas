package com.freelycar.saas.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.wxutils.MD5;
import com.freelycar.saas.wxutils.WechatConfig;
import com.freelycar.saas.wxutils.WechatLoginUse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author tangwei - Toby
 * @date 2019-01-31
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/wechat/config")
public class WeChatConfigController {
    private Logger logger = LoggerFactory.getLogger(WeChatConfigController.class);


    /**
     * 为微信前端提供调用JS-SDK所需的信息
     *
     * @param targetUrl
     * @return
     */
    @GetMapping(value = "/getJSSDKConfig")
    public String getJsSDKConfig(String targetUrl) {
        logger.debug("JSSDK Url:" + targetUrl);
        if (StringUtils.isEmpty(targetUrl)) {
            throw new RuntimeException("jsapiTicket获取失败,当前url为空！！");
        }

        String noncestr = UUID.randomUUID().toString();
        JSONObject ticketJson = WechatConfig.getJsApiTicketByWX();
        String ticket = ticketJson.getString("ticket");
        String timestamp = String.valueOf(System.currentTimeMillis());

        int index = targetUrl.indexOf("#");
        if (index > 0) {
            targetUrl = targetUrl.substring(0, index);
        }

        // 对给定字符串key手动排序
        String param = "jsapi_ticket=" + ticket + "&noncestr=" + noncestr
                + "&timestamp=" + timestamp + "&url=" + targetUrl;

        String signature = MD5.encode("SHA1", param);

        JSONObject jsSDKConfig = new JSONObject();

        jsSDKConfig.put("appId", WechatConfig.APP_ID);
        jsSDKConfig.put("nonceStr", noncestr);
        jsSDKConfig.put("timestamp", timestamp);
        jsSDKConfig.put("signature", signature);
        return jsSDKConfig.toString();
    }

    @GetMapping("/getWeChatUserInfo")
    public JSONObject getWeChatUserInfo(String code) {
        logger.debug("weChat code: " + code);
        return WechatLoginUse.wechatInfo(code);
    }

    @GetMapping("/isSubscribe")
    public JSONObject isSubscribe(String openId) {
        JSONObject wechatInfo = new JSONObject();
        boolean subscribe = WechatConfig.isUserFollow(openId);
        wechatInfo.put("subscribe", subscribe);
        return wechatInfo;
    }

}
