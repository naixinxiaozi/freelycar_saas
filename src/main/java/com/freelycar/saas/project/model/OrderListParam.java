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

    private String payState;

    private int dateType;

    private String startDate;

    private String endDate;

    public OrderListParam(String orderId, String licensePlate, String projectId, String payState, int dateType, String startDate, String endDate) {
        this.orderId = orderId;
        this.licensePlate = licensePlate;
        this.projectId = projectId;
        this.payState = payState;
        this.dateType = dateType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public OrderListParam() {
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
        sb.append(",\"payState\":\"")
                .append(payState).append('\"');
        sb.append(",\"dateType\":")
                .append(dateType);
        sb.append(",\"startDate\":\"")
                .append(startDate).append('\"');
        sb.append(",\"endDate\":\"")
                .append(endDate).append('\"');
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

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
