package com.freelycar.saas.project.entity;

import com.sun.tools.corba.se.idl.constExpr.Times;
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

    @Column(nullable = false)
    private Boolean delStatus;

    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    @Column
    private String storeId;

    /**
     * 车辆信息对应主键ID
     */
    @Column
    private String cardId;

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
     * 交付时间
     */
    @Column
    private Timestamp deliverTime;

    /**
     * 完工时间
     */
    @Column
    private Timestamp finishTime;

    /**
     * 取车时间
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
    @Column
    private Integer state;

    /**
     * 订单总价
     */
    @Column(nullable = false,columnDefinition = "double default 0")
    private Double totalPrice;

    /**
     * 现在价格
     */
    @Column
    private Double presentPrice;

    /**
     * 实付价格
     */
    @Column
    private Double actualPrice;

    /**
     * 支付方式
     */
    @Column
    private String payMethod;

    /**
     * 支付状态
     */
    @Column
    private Integer payState;

    /**
     * 取车人员ID
     */
    @Column
    private String pickCarStaffId;


    @Override
    public String toString() {
        return "ConsumerOrder{" +
                "id='" + id + '\'' +
                ", delStatus=" + delStatus +
                ", createTime=" + createTime +
                ", storeId='" + storeId + '\'' +
                ", cardId='" + cardId + '\'' +
                ", carBrand='" + carBrand + '\'' +
                ", carType='" + carType + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", deliverTime=" + deliverTime +
                ", finishTime=" + finishTime +
                ", pickTime=" + pickTime +
                ", lastMiles=" + lastMiles +
                ", miles=" + miles +
                ", parkingLocation='" + parkingLocation + '\'' +
                ", state=" + state +
                ", totalPrice=" + totalPrice +
                ", presentPrice=" + presentPrice +
                ", actualPrice=" + actualPrice +
                ", payMethod='" + payMethod + '\'' +
                ", payState=" + payState +
                ", pickCarStaffId='" + pickCarStaffId + '\'' +
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

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
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

    public Double getPresentPrice() {
        return presentPrice;
    }

    public void setPresentPrice(Double presentPrice) {
        this.presentPrice = presentPrice;
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
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

    public ConsumerOrder() {
    }
}
