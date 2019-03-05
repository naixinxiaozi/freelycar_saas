package com.freelycar.saas.wechat.model;

import lombok.Data;

/**
 * @author tangwei - Toby
 * @date 2019-02-15
 * @email toby911115@gmail.com
 */
@Data
public class OrderPay {
    private String openId;

    private String orderId;

    private float totalPrice;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"openId\":\"")
                .append(openId).append('\"');
        sb.append(",\"orderId\":\"")
                .append(orderId).append('\"');
        sb.append(",\"totalPrice\":")
                .append(totalPrice);
        sb.append('}');
        return sb.toString();
    }
}
