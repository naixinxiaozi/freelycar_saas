package com.freelycar.saas.basic.wrapper;

/**
 * 排序Dto对象
 * @author tangwei - Toby
 * @date 2018/11/29
 * @email toby911115@gmail.com
 */
public class SortDto {

    /**
     * 排序方式
     */
    private String orderType;

    /**
     * 排序字段
     */
    private String orderField;

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public SortDto(String orderType, String orderField) {
        this.orderType = orderType;
        this.orderField = orderField;
    }

    /**
     * 默认为DESC排序
     * @param orderField
     */
    public SortDto(String orderField) {
        this.orderField = orderField;
        this.orderType = "desc";
    }


}