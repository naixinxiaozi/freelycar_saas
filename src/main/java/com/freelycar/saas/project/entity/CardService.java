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
 * @date 2018/10/19
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class CardService implements Serializable {
    private static final long serialVersionUID = 8L;

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
    private String comment;

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

    @Column(nullable = false)
    private Boolean bookOnline;

    @Column
    private String storeId;

    public CardService() {
    }

    @Override
    public String toString() {
        return "CardService{" +
                "id='" + id + '\'' +
                ", delStatus=" + delStatus +
                ", createTime=" + createTime +
                ", comment='" + comment + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", actualPrice=" + actualPrice +
                ", type=" + type +
                ", validTime=" + validTime +
                ", bookOnline=" + bookOnline +
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Boolean getBookOnline() {
        return bookOnline;
    }

    public void setBookOnline(Boolean bookOnline) {
        this.bookOnline = bookOnline;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
