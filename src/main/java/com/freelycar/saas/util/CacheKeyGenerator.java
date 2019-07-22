package com.freelycar.saas.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.security.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author tangwei - Toby
 * @date 2019-07-22
 * @email toby911115@gmail.com
 */
public class CacheKeyGenerator implements KeyGenerator {
    private static final Logger logger = LoggerFactory.getLogger(CacheKeyGenerator.class);
    private static final String DOT = ".";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        Map<String, Object> map = new HashMap<>();
        String targetName = target.getClass().toGenericString();
        String methodName = method.getName();
        map.put("target", targetName);//放入target的名字
        map.put("method", methodName);//放入method的名字
        if (params != null && params.length > 0) {//把所有参数放进去
            int i = 0;
            for (Object o : params) {
                map.put("params-" + i, o);
                i++;
            }
        }
        String str = JSONObject.toJSON(map).toString();
        byte[] hash = null;
        String s;
        try {
            hash = MessageDigest.getInstance("MD5").digest(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        if (hash != null) {
            s = MD5Encoder.encode(hash);//使用MD5生成位移key
        } else {
            if (params != null) {
                return SimpleKeyGenerator.generateKey(params);
            } else {
                return targetName + DOT + methodName;
            }
        }
        logger.debug("缓存key：" + s);
        return s;
    }

}
