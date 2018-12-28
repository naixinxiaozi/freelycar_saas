package com.freelycar.saas.basic.wrapper;

/**
 * @author tangwei - Toby
 * @date 2018-12-27
 * @email toby911115@gmail.com
 */
public enum PayMethod {

    CASH(1, "现金"),
    CREDIT_CARD(2,"刷卡"),
    ALIPAY(3,"支付宝"),
    SUNING_PAY(4,"易付宝"),
    WECHAT_PAY(5,"微信支付");

    private int code;

    private String value;

    PayMethod(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"code\":")
                .append(code);
        sb.append(",\"value\":\"")
                .append(value).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }}
