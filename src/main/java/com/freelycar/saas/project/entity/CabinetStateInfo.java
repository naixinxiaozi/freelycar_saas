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
 * @date 2018/10/23
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class CabinetStateInfo implements Serializable {
    private static final long serialVersionUID = 17L;

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
    private String cabinetId;

    @Column
    private String orderId;

    @Column
    private String cabinetSn;

    @Column
    private String gridSn;

    @Column
    private Integer state;

    @Column
    private String licensePlate;

    public CabinetStateInfo() {
    }

    @Override
    public String toString() {
        return "CabinetStateInfo{" +
                "id='" + id + '\'' +
                ", delStatus=" + delStatus +
                ", createTime=" + createTime +
                ", cabinetId='" + cabinetId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", cabinetSn='" + cabinetSn + '\'' +
                ", gridSn='" + gridSn + '\'' +
                ", state=" + state +
                ", licensePlate='" + licensePlate + '\'' +
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

    public String getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(String cabinetId) {
        this.cabinetId = cabinetId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCabinetSn() {
        return cabinetSn;
    }

    public void setCabinetSn(String cabinetSn) {
        this.cabinetSn = cabinetSn;
    }

    public String getGridSn() {
        return gridSn;
    }

    public void setGridSn(String gridSn) {
        this.gridSn = gridSn;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
