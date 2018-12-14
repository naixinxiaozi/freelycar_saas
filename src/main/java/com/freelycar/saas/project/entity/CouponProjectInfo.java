package com.freelycar.saas.project.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 优惠券项目类
 * @author tangwei - Toby
 * @date 2018/10/23
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class CouponProjectInfo implements Serializable {
    private static final long serialVersionUID = 14L;

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
    private String couponServiceId;

    @Column
    private String couponId;

    /**
     * 优惠券包含的项目次数
     */
    @Column
    private Integer times;

    /**
     * 优惠项目原价
     */
    @Column
    private Double buyPrice;

    /**
     * 优惠项目现价
     */
    @Column
    private Double presentPrice;

    @Override
    public String toString() {
        return "CouponProjectInfo{" +
                "id='" + id + '\'' +
                ", delStatus=" + delStatus +
                ", createTime=" + createTime +
                ", couponServiceId='" + couponServiceId + '\'' +
                ", couponId='" + couponId + '\'' +
                ", times=" + times +
                ", buyPrice=" + buyPrice +
                ", presentPrice=" + presentPrice +
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

    public String getCouponServiceId() {
        return couponServiceId;
    }

    public void setCouponServiceId(String couponServiceId) {
        this.couponServiceId = couponServiceId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getPresentPrice() {
        return presentPrice;
    }

    public void setPresentPrice(Double presentPrice) {
        this.presentPrice = presentPrice;
    }

    public CouponProjectInfo() {
    }
}
