package com.freelycar.saas.wechat.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.service.WxUserInfoService;
import com.freelycar.saas.wxutils.HttpRequest;
import org.apache.http.HttpEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wechat/login")
public class WeChatLoginController {
    private final String appid = "YPVPvcghD0yT1CtQKUOpOUGI-gzGzoHsz";
    private final String appkey = "AnrwmLo01qL7RuKNbV0NwWR4";
    private final String ContentType = "application/json";
    private final String leancouldUrlRes = "https://leancloud.cn/1.1/requestSmsCode";
    private final String leancouldUrlVer = "https://leancloud.cn/1.1/verifySmsCode";
    @Autowired
    private WxUserInfoService wxUserInfoService;
    private Logger log = LogManager.getLogger(WeChatLoginController.class);

    //发送验证码请求
    @RequestMapping(value = "/getSmsCode", method = RequestMethod.POST)
    public ResultJsonObject getVerification(String phone) {
        JSONObject param = new JSONObject();
        param.put("mobilePhoneNumber", phone);
        HttpEntity entity = HttpRequest.getEntity(param);
        Map<String, Object> head = setLeancloudHead();
        String result = HttpRequest.postCall(leancouldUrlRes, entity, head);
        log.debug("leancloud的返回码：" + result);
        JSONObject json;
        try {
            json = JSONObject.parseObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            log.error("发送验证码,解析返回结果错误");
            return ResultJsonObject.getErrorResult(null, "解析返回结果错误");
        }

        if (StringUtils.hasText(json.getString("error"))) {
            return ResultJsonObject.getErrorResult(json);
        }
        return ResultJsonObject.getDefaultResult(json);
    }

    /**
     * 注册验证结果请求
     *
     * @param phone
     * @param smscode
     * @param openId
     * @param headimgurl
     * @param nickName
     * @return
     */
    @RequestMapping(value = "/verifySmsCode", method = RequestMethod.POST)
    public ResultJsonObject verifySmsCode(
            @RequestParam String phone,
            @RequestParam String smscode,
            @RequestParam String openId,
            @RequestParam String headimgurl,
            @RequestParam String nickName) {
        log.info("验证短信码方法接收到的微信用户信息：");
        log.info("openId:" + openId);
        log.info("headimgurl:" + headimgurl);
        log.info("nickName:" + nickName);
        JSONObject json = this.verifySmsCode(phone, smscode);
        if (StringUtils.hasText(json.getString("error"))) {
            log.debug(phone + ";code:" + smscode + " 验证失败。。。");
            return ResultJsonObject.getErrorResult(json);
        } else {
            return wxUserInfoService.wechatLogin(phone, openId, headimgurl, nickName);
        }
    }


    @PostMapping("/changePhone")
    public ResultJsonObject changePhone(@RequestParam String phone, @RequestParam String smsCode, @RequestParam String id) {
        JSONObject json = this.verifySmsCode(phone, smsCode);
        if (StringUtils.hasText(json.getString("error"))) {
            log.debug(phone + ";code:" + smsCode + " 验证失败。。。");
            return ResultJsonObject.getErrorResult(json);
        } else {
            return wxUserInfoService.changePhone(phone, id);
        }
    }

    /**
     * 验证码验证方法
     *
     * @param phone
     * @param smscode
     * @return
     */
    private JSONObject verifySmsCode(String phone, String smscode) {
        Map<String, Object> head = setLeancloudHead();
        String result = HttpRequest.postCall(leancouldUrlVer + "/" + smscode + "?mobilePhoneNumber=" + phone, null, head);
        log.debug("绑定手机短信验证, phone:" + phone + ", smscode:" + smscode + "。 短信验证结果：" + result);
        JSONObject json = null;
        try {
            json = JSONObject.parseObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            log.error("解析验证验证码返回结果错误");
        }
        log.debug("解析后结果：" + json);

        return json;
    }

    /**
     * 设置Lean Cloud短信服务的请求头
     *
     * @return
     */
    private Map<String, Object> setLeancloudHead() {
        Map<String, Object> head = new HashMap<String, Object>();
        head.put("X-LC-Id", appid);
        head.put("X-LC-Key", appkey);
        head.put("Content-Type", ContentType);
        return head;
    }
}
