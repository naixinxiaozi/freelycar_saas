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
 * @date 2018/10/17
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class Car implements Serializable {
    private static final long serialVersionUID = 5L;

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @NotNull
    private String id;

    /**
     * 有效标记位
     */
    @Column(nullable = false)
    private Boolean delStatus;

    /**
     * 创建时间
     */
    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /**
     * 车辆商标（精确到某一系列，比如：马自达6）
     */
    @Column
    private String carBrand;

    /**
     * 车辆
     */
    @Column
    private String carType;

    @Column
    private String driveLicenseNumber;

    @Column
    private String engineNumber;

    @Column
    private String frameNumber;

    @Column
    private Float insuranceAmount;

    @Column
    private Timestamp insuranceStartTime;

    @Column
    private Timestamp insuranceEndTime;

    @Column
    private Integer lastMiles;

    @Column
    private Integer miles;

    @Column
    private Timestamp licenseDate;

    @Column
    private String licensePlate;

    @Column(nullable = false)
    private Boolean newCar;

    @Column(nullable = false)
    private Boolean defaultCar;

    @Column
    private String clientId;

    @Column
    private String insuranceCity;

    @Column
    private String insuranceCompany;

    @Column
    private Timestamp defaultDate;

    @Column(nullable = false)
    private Boolean needInspectionRemind;

    @Column(nullable = false)
    private Boolean needInsuranceRemind;

    @Column
    private String carMark;

    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", delStatus=" + delStatus +
                ", createTime=" + createTime +
                ", carBrand='" + carBrand + '\'' +
                ", carType='" + carType + '\'' +
                ", driveLicenseNumber='" + driveLicenseNumber + '\'' +
                ", engineNumber='" + engineNumber + '\'' +
                ", frameNumber='" + frameNumber + '\'' +
                ", insuranceAmount=" + insuranceAmount +
                ", insuranceStartTime=" + insuranceStartTime +
                ", insuranceEndTime=" + insuranceEndTime +
                ", lastMiles=" + lastMiles +
                ", miles=" + miles +
                ", licenseDate=" + licenseDate +
                ", licensePlate='" + licensePlate + '\'' +
                ", newCar=" + newCar +
                ", defaultCar=" + defaultCar +
                ", clientId='" + clientId + '\'' +
                ", insuranceCity='" + insuranceCity + '\'' +
                ", insuranceCompany='" + insuranceCompany + '\'' +
                ", defaultDate=" + defaultDate +
                ", needInspectionRemind=" + needInspectionRemind +
                ", needInsuranceRemind=" + needInsuranceRemind +
                ", carMark='" + carMark + '\'' +
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

    public String getDriveLicenseNumber() {
        return driveLicenseNumber;
    }

    public void setDriveLicenseNumber(String driveLicenseNumber) {
        this.driveLicenseNumber = driveLicenseNumber;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber;
    }

    public Float getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(Float insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public Timestamp getInsuranceStartTime() {
        return insuranceStartTime;
    }

    public void setInsuranceStartTime(Timestamp insuranceStartTime) {
        this.insuranceStartTime = insuranceStartTime;
    }

    public Timestamp getInsuranceEndTime() {
        return insuranceEndTime;
    }

    public void setInsuranceEndTime(Timestamp insuranceEndTime) {
        this.insuranceEndTime = insuranceEndTime;
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

    public Timestamp getLicenseDate() {
        return licenseDate;
    }

    public void setLicenseDate(Timestamp licenseDate) {
        this.licenseDate = licenseDate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Boolean getNewCar() {
        return newCar;
    }

    public void setNewCar(Boolean newCar) {
        this.newCar = newCar;
    }

    public Boolean getDefaultCar() {
        return defaultCar;
    }

    public void setDefaultCar(Boolean defaultCar) {
        this.defaultCar = defaultCar;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getInsuranceCity() {
        return insuranceCity;
    }

    public void setInsuranceCity(String insuranceCity) {
        this.insuranceCity = insuranceCity;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public Timestamp getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(Timestamp defaultDate) {
        this.defaultDate = defaultDate;
    }

    public Boolean getNeedInspectionRemind() {
        return needInspectionRemind;
    }

    public void setNeedInspectionRemind(Boolean needInspectionRemind) {
        this.needInspectionRemind = needInspectionRemind;
    }

    public Boolean getNeedInsuranceRemind() {
        return needInsuranceRemind;
    }

    public void setNeedInsuranceRemind(Boolean needInsuranceRemind) {
        this.needInsuranceRemind = needInsuranceRemind;
    }

    public String getCarMark() {
        return carMark;
    }

    public void setCarMark(String carMark) {
        this.carMark = carMark;
    }

    public Car() {
    }
}
