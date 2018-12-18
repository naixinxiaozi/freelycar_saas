package com.freelycar.saas.jwt.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * JSON返回结果类
 *
 * @author tangwei - Toby
 * @date 2018-12-17
 * @email toby911115@gmail.com
 */
public class JSONResult {
    public static String fillResultString(Integer status, String message, Object result) {
        JSONObject jsonObject = new JSONObject() {{
            put("status", status);
            put("message", message);
            put("result", result);
        }};
        return jsonObject.toString();
    }
}