package com.freelycar.saas.wsutils;

public class ResultBean {
    private String res;
    private String value;

    public ResultBean() {
        super();
    }

    public ResultBean(String res, String value) {
        super();
        this.res = res;
        this.value = value;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"res\":\"")
                .append(res).append('\"');
        sb.append(",\"value\":\"")
                .append(value).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
