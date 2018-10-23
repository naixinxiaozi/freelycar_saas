package com.freelycar.saas.project.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 订单
 * @author tangwei - Toby
 * @date 2018/10/22
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class ConsumerProjectInfo implements Serializable {
    private static final long serialVersionUID = 12L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @NotNull
    private String id;

    @Column(nullable = false)
    private Boolean delStatus;

    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /**
     * 服务项目所在订单ID
     */
    @Column
    private String consumerOrderId;

    /**
     * 服务项目ID
     */
    @Column
    private String projectId;

    /**
     * 服务项目名称
     */
    @Column
    private String projectName;

    /**
     * 支付方式
     */
    @Column
    private String payMethod;

    /**
     * 会员卡ID
     */
    @Column
    private String cardId;

    /**
     * 会员卡名称
     */
    @Column
    private String cardName;

    /**
     * 会员卡号
     */
    @Column
    private String cardNumber;

    /**
     * 会员卡卡此次消费扣除次数
     */
    @Column
    private Integer payCardTimes;

    /**
     * 优惠券ID
     */
    @Column
    private String couponId;

    /**
     * 优惠券名称
     */
    @Column
    private String couponName;

    /**
     * 金额
     */
    @Column
    private Double Price;

    /**
     * 工时单价
     */
    @Column
    private Double pricePerUnit;

    /**
     * 参考工时
     */
    @Column
    private Float referWorkTime;

    public ConsumerProjectInfo() {
    }

    @Override
    public String toString() {
        return "ConsumerProjectInfo{" +
                "id='" + id + '\'' +
                ", delStatus=" + delStatus +
                ", createTime=" + createTime +
                ", consumerOrderId='" + consumerOrderId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", cardId='" + cardId + '\'' +
                ", cardName='" + cardName + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", payCardTimes=" + payCardTimes +
                ", couponId='" + couponId + '\'' +
                ", couponName='" + couponName + '\'' +
                ", Price=" + Price +
                ", pricePerUnit=" + pricePerUnit +
                ", referWorkTime=" + referWorkTime +
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

    public String getConsumerOrderId() {
        return consumerOrderId;
    }

    public void setConsumerOrderId(String consumerOrderId) {
        this.consumerOrderId = consumerOrderId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getPayCardTimes() {
        return payCardTimes;
    }

    public void setPayCardTimes(Integer payCardTimes) {
        this.payCardTimes = payCardTimes;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Float getReferWorkTime() {
        return referWorkTime;
    }

    public void setReferWorkTime(Float referWorkTime) {
        this.referWorkTime = referWorkTime;
    }
}
