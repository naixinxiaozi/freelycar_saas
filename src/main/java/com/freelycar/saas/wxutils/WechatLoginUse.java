package com.freelycar.saas.wxutils;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

public class WechatLoginUse {

    private static Logger log = LogManager.getLogger(WechatLoginUse.class);

    public static JSONObject wechatInfo(String code) {
        //返回前台
        JSONObject wechatInfo = new JSONObject();

        //access token
        JSONObject resultJson = WechatConfig.getAccessToken(code);
        String openid = resultJson.getString("openid");
        String accessToken = resultJson.getString("access_token");
        log.debug("获取登陆时accesstoken: " + accessToken + " ; openid: " + openid);

        if (StringUtils.hasText(openid) && StringUtils.hasText(accessToken)) {
            //user info
            JSONObject userInfoJson = WechatConfig.getWXUserInfo(accessToken, openid);
//		    String name = EmojiFilter.filterEmoji(userInfoJson.getString("nickname"));
            String name = userInfoJson.getString("nickname");
            String head = userInfoJson.getString("headimgurl");
            log.debug("获取微信昵称: " + name + " ; 头像: " + head);

            if (StringUtils.hasText(name) && StringUtils.hasText(head)) {
                wechatInfo.put("openid", openid);
                wechatInfo.put("nickname", name);
                wechatInfo.put("headimgurl", head);
                wechatInfo.put("message", "success");

            } else {
                return resultJson;
            }

        } else {
            return resultJson;
        }

        return wechatInfo;

    }
}
