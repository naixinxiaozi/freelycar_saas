package com.freelycar.saas.wxutils;

import java.util.Arrays;
import java.util.Map;

/**
 * 微信签名工具类
 *
 * @author 伟
 */
public class WeChatSignatureUtil {
    private static String connectParams(Map<String, Object> map) {
        Object[] key_arr = map.keySet().toArray();
        Arrays.sort(key_arr);
        String str = "";
        for (Object key : key_arr) {
            String val = map.get(key) + "";
            str += "&" + key + "=" + val;
        }
        return str.replaceFirst("&", "");
    }

    public static String getSig(Map<String, Object> map) {
        //签名秘钥，在微信商户平台里面设置
        String sigTemp = connectParams(map) + "&key=" + WechatConfig.KEY;
        String sign = null;
        sign = MD5.compute(sigTemp).toUpperCase();
        return sign;
    }

    public static boolean isCorrect(Map<String, Object> map) {
        String sig = map.get("sign").toString();
        map.remove("sign");
        return sig.equals(getSig(map));
    }

}
