package com.freelycar.saas.project.model;

/**
 * @author tangwei - Toby
 * @date 2019-01-15
 * @email toby911115@gmail.com
 */
public class OrderListParam {
    private String orderId;

    private String licensePlate;

    private String projectId;

    private Integer payState;

    private Integer dateType;

    private String startTime;

    private String endTime;

    private Integer orderState;

    private Integer OrderType;


    public OrderListParam() {
    }

    public OrderListParam(String orderId, String licensePlate, String projectId, Integer payState, Integer dateType, String startTime, String endTime, Integer orderState, Integer orderType) {
        this.orderId = orderId;
        this.licensePlate = licensePlate;
        this.projectId = projectId;
        this.payState = payState;
        this.dateType = dateType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.orderState = orderState;
        OrderType = orderType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"orderId\":\"")
                .append(orderId).append('\"');
        sb.append(",\"licensePlate\":\"")
                .append(licensePlate).append('\"');
        sb.append(",\"projectId\":\"")
                .append(projectId).append('\"');
        sb.append(",\"payState\":")
                .append(payState);
        sb.append(",\"dateType\":")
                .append(dateType);
        sb.append(",\"startTime\":\"")
                .append(startTime).append('\"');
        sb.append(",\"endTime\":\"")
                .append(endTime).append('\"');
        sb.append(",\"orderState\":")
                .append(orderState);
        sb.append(",\"OrderType\":")
                .append(OrderType);
        sb.append('}');
        return sb.toString();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public Integer getDateType() {
        return dateType;
    }

    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Integer getOrderType() {
        return OrderType;
    }

    public void setOrderType(Integer orderType) {
        OrderType = orderType;
    }
}
