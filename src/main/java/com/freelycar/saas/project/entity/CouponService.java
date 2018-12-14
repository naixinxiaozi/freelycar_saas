package com.freelycar.saas.project.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 优惠券代售表
 * @author tangwei - Toby
 * @date 2018/10/23
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class CouponService implements Serializable {
    private static final long serialVersionUID = 13L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @NotNull
    private String id;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean delStatus;

    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime;

    @Column
    private String storeId;

    @Column
    private Timestamp buyStartTime;

    @Column
    private Timestamp buyEndTime;

    @Column
    private String content;

    @Column
    private String name;

    @Column
    private Integer type;

    @Column
    private Integer validTime;

    @Column
    private Double price;

    public CouponService() {
    }

    @Override
    public String toString() {
        return "CouponService{" +
                "id='" + id + '\'' +
                ", delStatus=" + delStatus +
                ", createTime=" + createTime +
                ", storeId='" + storeId + '\'' +
                ", buyStartTime=" + buyStartTime +
                ", buyEndTime=" + buyEndTime +
                ", content='" + content + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", validTime=" + validTime +
                ", price=" + price +
                '}';
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

    public Timestamp getBuyStartTime() {
        return buyStartTime;
    }

    public void setBuyStartTime(Timestamp buyStartTime) {
        this.buyStartTime = buyStartTime;
    }

    public Timestamp getBuyEndTime() {
        return buyEndTime;
    }

    public void setBuyEndTime(Timestamp buyEndTime) {
        this.buyEndTime = buyEndTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getValidTime() {
        return validTime;
    }

    public void setValidTime(Integer validTime) {
        this.validTime = validTime;
    }

    public Double getPrice() { return price; }

    public void setPrice(Double price) { this.price = price; }



}

