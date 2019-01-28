package com.freelycar.saas.project.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author tangwei - Toby
 * @date 2019-01-28
 * @email toby911115@gmail.com
 */
@Entity
public class OrderSn implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime default NOW()")
    private Timestamp createTime;

    @Column
    private String storeId;

    @Column(length = 3)
    private String storeSn;

    @Column(length = 6)
    private String dateNumber;

    @Column(length = 4)
    private String orderNumber;

    public OrderSn() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStoreSn() {
        return storeSn;
    }

    public void setStoreSn(String storeSn) {
        this.storeSn = storeSn;
    }

    public String getDateNumber() {
        return dateNumber;
    }

    public void setDateNumber(String dateNumber) {
        this.dateNumber = dateNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"createTime\":\"")
                .append(createTime).append('\"');
        sb.append(",\"storeId\":\"")
                .append(storeId).append('\"');
        sb.append(",\"storeSn\":\"")
                .append(storeSn).append('\"');
        sb.append(",\"dateNumber\":\"")
                .append(dateNumber).append('\"');
        sb.append(",\"orderNumber\":\"")
                .append(orderNumber).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
