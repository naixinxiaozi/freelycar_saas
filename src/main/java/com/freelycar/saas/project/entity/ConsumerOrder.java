package com.freelycar.saas.project.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author tangwei - Toby
 * @date 2018/10/22
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class ConsumerOrder implements Serializable {
    private static final long serialVersionUID = 11L;

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

    /**
     * 车辆信息对应主键ID
     */
    @Column
    private String carId;

    /**
     * 车辆品牌
     */
    @Column
    private String carBrand;

    /**
     * 车辆型号
     */
    @Column
    private String carType;

    /**
     * 车牌号码
     */
    @Column
    private String licensePlate;


    /**
     * 用户信息对应主键ID
     */
    @Column
    private String clientId;

    /**
     * 用户姓名
     */
    @Column
    private String clientName;

    /**
     * 性别
     */
    @Column
    private String gender;

    /**
     * 手机号
     */
    @Column
    private String phone;

    /**
     * 是否会员
     */
    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean isMember;

    /**
     * 交车时间
     */
    @Column
    private Timestamp deliverTime;

    /**
     * 完工时间
     */
    @Column
    private Timestamp finishTime;

    /**
     * 接车时间
     */
    @Column
    private Timestamp pickTime;


    /**
     * 上次里程
     */
    @Column
    private Integer lastMiles;

    /**
     * 本次里程
     */
    @Column
    private Integer miles;

    /**
     * 停车位置
     */
    @Column
    private String parkingLocation;

    /**
     * 订单状态
     */
    @Column(columnDefinition = "int default 0")
    private Integer state;

    /**
     * 订单总价
     */
    @Column(nullable = false, columnDefinition = "double default 0")
    private Double totalPrice;

    /**
     * 实付价格=第一实付价格+第二实付价格
     */
    @Column
    private Double actualPrice;

    /**
     * 第一支付方式
     */
    @Column
    private Integer firstPayMethod;

    /**
     * 第一实付价格
     */
    @Column
    private Double firstActualPrice;

    /**
     * 第一支付使用的卡ID
     */
    @Column
    private String firstCardId;

    /**
     * 第二支付方式
     */
    @Column
    private Integer secondPayMethod;

    /**
     * 第二实付价格
     */
    @Column
    private Double secondActualPrice;

    /**
     * 第二支付使用的卡ID
     */
    @Column
    private String secondCardId;

    /**
     * 支付状态（0：未结算；1：已结算）
     */
    @Column(columnDefinition = "int default 0")
    private Integer payState;

    /**
     * 取车人员ID
     */
    @Column
    private String pickCarStaffId;

    /**
     * 订单类型（1.服务开单；2.智能柜开单；3.办卡/续卡/抵用券）
     */
    @Column
    private Integer orderType;

    /**
     * 办理的会员卡、抵用券业务的ID（卡ID或者券ID）
     */
    @Column
    private String cardOrCouponId;

    /**
     * 故障描述
     */
    @Column
    private String faultDescription;

    /**
     * 计划用车时间（智能柜预约服务）
     */
    @Column
    private Timestamp useTime;


    /**
     * 构造函数
     */
    public ConsumerOrder() {
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

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Timestamp deliverTime) {
        this.deliverTime = deliverTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public Timestamp getPickTime() {
        return pickTime;
    }

    public void setPickTime(Timestamp pickTime) {
        this.pickTime = pickTime;
    }

    public Integer getLastMiles() {
        return lastMiles;
    }

    public void setLastMiles(Integer lastMiles) {
        this.lastMiles = lastMiles;
    }

    public Integer getMiles() {
        return miles;
    }

    public void setMiles(Integer miles) {
        this.miles = miles;
    }

    public String getParkingLocation() {
        return parkingLocation;
    }

    public void setParkingLocation(String parkingLocation) {
        this.parkingLocation = parkingLocation;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getPickCarStaffId() {
        return pickCarStaffId;
    }

    public void setPickCarStaffId(String pickCarStaffId) {
        this.pickCarStaffId = pickCarStaffId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public Integer getFirstPayMethod() {
        return firstPayMethod;
    }

    public void setFirstPayMethod(Integer firstPayMethod) {
        this.firstPayMethod = firstPayMethod;
    }

    public Double getFirstActualPrice() {
        return firstActualPrice;
    }

    public void setFirstActualPrice(Double firstActualPrice) {
        this.firstActualPrice = firstActualPrice;
    }

    public Integer getSecondPayMethod() {
        return secondPayMethod;
    }

    public void setSecondPayMethod(Integer secondPayMethod) {
        this.secondPayMethod = secondPayMethod;
    }

    public Double getSecondActualPrice() {
        return secondActualPrice;
    }

    public void setSecondActualPrice(Double secondActualPrice) {
        this.secondActualPrice = secondActualPrice;
    }

    public String getFirstCardId() {
        return firstCardId;
    }

    public void setFirstCardId(String firstCardId) {
        this.firstCardId = firstCardId;
    }

    public String getSecondCardId() {
        return secondCardId;
    }

    public void setSecondCardId(String secondCardId) {
        this.secondCardId = secondCardId;
    }

    public String getCardOrCouponId() {
        return cardOrCouponId;
    }

    public void setCardOrCouponId(String cardOrCouponId) {
        this.cardOrCouponId = cardOrCouponId;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }

    public Boolean getMember() {
        return isMember;
    }

    public void setMember(Boolean member) {
        isMember = member;
    }

    public Timestamp getUseTime() {
        return useTime;
    }

    public void setUseTime(Timestamp useTime) {
        this.useTime = useTime;
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
        sb.append(",\"carId\":\"")
                .append(carId).append('\"');
        sb.append(",\"carBrand\":\"")
                .append(carBrand).append('\"');
        sb.append(",\"carType\":\"")
                .append(carType).append('\"');
        sb.append(",\"licensePlate\":\"")
                .append(licensePlate).append('\"');
        sb.append(",\"clientId\":\"")
                .append(clientId).append('\"');
        sb.append(",\"clientName\":\"")
                .append(clientName).append('\"');
        sb.append(",\"gender\":\"")
                .append(gender).append('\"');
        sb.append(",\"phone\":\"")
                .append(phone).append('\"');
        sb.append(",\"isMember\":")
                .append(isMember);
        sb.append(",\"deliverTime\":\"")
                .append(deliverTime).append('\"');
        sb.append(",\"finishTime\":\"")
                .append(finishTime).append('\"');
        sb.append(",\"pickTime\":\"")
                .append(pickTime).append('\"');
        sb.append(",\"lastMiles\":")
                .append(lastMiles);
        sb.append(",\"miles\":")
                .append(miles);
        sb.append(",\"parkingLocation\":\"")
                .append(parkingLocation).append('\"');
        sb.append(",\"state\":")
                .append(state);
        sb.append(",\"totalPrice\":")
                .append(totalPrice);
        sb.append(",\"actualPrice\":")
                .append(actualPrice);
        sb.append(",\"firstPayMethod\":")
                .append(firstPayMethod);
        sb.append(",\"firstActualPrice\":")
                .append(firstActualPrice);
        sb.append(",\"firstCardId\":\"")
                .append(firstCardId).append('\"');
        sb.append(",\"secondPayMethod\":")
                .append(secondPayMethod);
        sb.append(",\"secondActualPrice\":")
                .append(secondActualPrice);
        sb.append(",\"secondCardId\":\"")
                .append(secondCardId).append('\"');
        sb.append(",\"payState\":")
                .append(payState);
        sb.append(",\"pickCarStaffId\":\"")
                .append(pickCarStaffId).append('\"');
        sb.append(",\"orderType\":")
                .append(orderType);
        sb.append(",\"cardOrCouponId\":\"")
                .append(cardOrCouponId).append('\"');
        sb.append(",\"faultDescription\":\"")
                .append(faultDescription).append('\"');
        sb.append(",\"useTime\":\"")
                .append(useTime).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
