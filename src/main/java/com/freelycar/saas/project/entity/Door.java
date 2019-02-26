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
 * @date 2019-01-22
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class Door implements Serializable {
    private static final long serialVersionUID = 24L;
    /**
     * 主键ID
     */
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @NotNull
    @Length(max = 50)
    private String id;

    /**
     * 删除标记位（0：有效；1：无效）
     */
    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean delStatus;

    /**
     * 创建时间
     */
    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime;

    /**
     * 智能柜ID
     */
    @Column
    private String arkId;

    /**
     * 关联订单ID
     */
    @Column
    private String orderId;

    /**
     * 智能柜编号
     */
    @Column
    private String arkSn;

    /**
     * 智能柜门编号
     */
    @Column
    private String doorSn;

    /**
     * 柜门状态
     */
    @Column(length = 2, nullable = false, columnDefinition = "int default 0")
    private Integer state;

    public Door() {
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

    public String getArkId() {
        return arkId;
    }

    public void setArkId(String arkId) {
        this.arkId = arkId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getArkSn() {
        return arkSn;
    }

    public void setArkSn(String arkSn) {
        this.arkSn = arkSn;
    }

    public String getDoorSn() {
        return doorSn;
    }

    public void setDoorSn(String doorSn) {
        this.doorSn = doorSn;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
        sb.append(",\"arkId\":\"")
                .append(arkId).append('\"');
        sb.append(",\"orderId\":\"")
                .append(orderId).append('\"');
        sb.append(",\"arkSn\":\"")
                .append(arkSn).append('\"');
        sb.append(",\"doorSn\":\"")
                .append(doorSn).append('\"');
        sb.append(",\"state\":")
                .append(state);
        sb.append('}');
        return sb.toString();
    }
}
