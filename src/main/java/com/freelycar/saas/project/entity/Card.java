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
 * @author tangwei - Toby
 * @date 2018/10/19
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class Card implements Serializable {
    private static final long serialVersionUID = 9L;

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

    /**
     * 会员卡所属门店
     */
    @Column
    private String storeId;

    /**
     * 卡号
     */
    @Column
    private String cardNumber;

    /**
     * 有效截止日期
     */
    @Column
    private Timestamp expirationDate;

    /**
     * 支付时间
     */
    @Column
    private Timestamp payDate;

    /**
     * 办卡的支付方式
     */
    @Column
    private Integer payMethod;

    /**
     * 是哪种卡服务
     */
    @Column
    private String cardServiceId;

    /**
     * 属于哪个客户
     */
    @Column
    private String clientId;

    /**
     * 是否过期
     */
    @Column(nullable = false,columnDefinition = "bit default 0")
    private Boolean failed;

    /**
     * 余额
     */
    @Column
    private Float balance;

    /**
     * 办理人员
     */
    @Column
    private String staffId;


    @Column
    private String name;

    @Column
    private Float price;

    @Column
    private Float actualPrice;

    @Column
    private Integer type;

    @Column
    private Integer validTime;

    @Column
    private String content;

    public Card() {
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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Timestamp getPayDate() {
        return payDate;
    }

    public void setPayDate(Timestamp payDate) {
        this.payDate = payDate;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public String getCardServiceId() {
        return cardServiceId;
    }

    public void setCardServiceId(String cardServiceId) {
        this.cardServiceId = cardServiceId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Boolean getFailed() {
        return failed;
    }

    public void setFailed(Boolean failed) {
        this.failed = failed;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Float actualPrice) {
        this.actualPrice = actualPrice;
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
        sb.append(",\"cardNumber\":\"")
                .append(cardNumber).append('\"');
        sb.append(",\"expirationDate\":\"")
                .append(expirationDate).append('\"');
        sb.append(",\"payDate\":\"")
                .append(payDate).append('\"');
        sb.append(",\"payMethod\":")
                .append(payMethod);
        sb.append(",\"cardServiceId\":\"")
                .append(cardServiceId).append('\"');
        sb.append(",\"clientId\":\"")
                .append(clientId).append('\"');
        sb.append(",\"failed\":")
                .append(failed);
        sb.append(",\"balance\":")
                .append(balance);
        sb.append(",\"staffId\":\"")
                .append(staffId).append('\"');
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"price\":")
                .append(price);
        sb.append(",\"actualPrice\":")
                .append(actualPrice);
        sb.append(",\"type\":")
                .append(type);
        sb.append(",\"validTime\":")
                .append(validTime);
        sb.append(",\"content\":\"")
                .append(content).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
