package com.freelycar.saas.project.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 优惠券实物表（客户购买的信息）
 *
 * @author tangwei - Toby
 * @date 2018/10/23
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class Coupon implements Serializable {
    private static final long serialVersionUID = 15L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @NotNull
    @Length(max = 50)
    private String id;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean delStatus;

    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime;

    @Column
    private String storeId;

    /**
     * 抵用券服务ID
     */
    @Column
    private String couponServiceId;

    /**
     * 客户ID
     */
    @Column
    private String clientId;

    /**
     * 优惠券名称
     */
    @Column
    private String name;

    /**
     * 过期时间
     */
    @Column
    private Timestamp deadline;

    /**
     * 购买价格
     */
    @Column
    private Double price;

    /**
     * 抵用券对应的项目ID
     */
    @Column
    private String projectId;

    /**
     * 单据ID，用于挂单和结算关联
     */
    @Column
    private String orderId;

    /**
     * 使用状态（0：未使用；1：已使用；2：挂单中）
     */
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer status;

    /**
     * 备注
     */
    @Column
    private String content;

    public Coupon() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Boolean delStatus) {
        this.delStatus = delStatus;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCouponServiceId() {
        return couponServiceId;
    }

    public void setCouponServiceId(String couponServiceId) {
        this.couponServiceId = couponServiceId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"delStatus\":")
                .append(delStatus);
        sb.append(",\"createTime\":\"")
                .append(createTime).append('\"');
        sb.append(",\"storeId\":\"")
                .append(storeId).append('\"');
        sb.append(",\"couponServiceId\":\"")
                .append(couponServiceId).append('\"');
        sb.append(",\"clientId\":\"")
                .append(clientId).append('\"');
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"deadline\":\"")
                .append(deadline).append('\"');
        sb.append(",\"price\":")
                .append(price);
        sb.append(",\"projectId\":\"")
                .append(projectId).append('\"');
        sb.append(",\"orderId\":\"")
                .append(orderId).append('\"');
        sb.append(",\"status\":")
                .append(status);
        sb.append(",\"content\":\"")
                .append(content).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
