package com.freelycar.saas.project.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 汽车配件表
 *
 * @author tangwei - Toby
 * @date 2018/12/5
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class AutoParts implements Serializable {
    private static final long serialVersionUID = 21L;
    /**
     * 主键ID
     */
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @NotNull
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
     * 配件类别
     */
    @Column
    private String type;

    /**
     * 配件名称
     */
    @Column
    private String name;

    /**
     * 配件数量
     */
    @Column
    private Integer count;

    /**
     * 配件单价
     */
    @Column
    private Float unitPrice;

    /**
     * 配件总价
     */
    @Column
    private Float totalPrice;

    /**
     * 订单ID
     */
    @Column
    private String ConsumerOrderId;


    public AutoParts() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getConsumerOrderId() {
        return ConsumerOrderId;
    }

    public void setConsumerOrderId(String consumerOrderId) {
        ConsumerOrderId = consumerOrderId;
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
        sb.append(",\"type\":\"")
                .append(type).append('\"');
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"count\":")
                .append(count);
        sb.append(",\"unitPrice\":")
                .append(unitPrice);
        sb.append(",\"totalPrice\":")
                .append(totalPrice);
        sb.append(",\"ConsumerOrderId\":\"")
                .append(ConsumerOrderId).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
