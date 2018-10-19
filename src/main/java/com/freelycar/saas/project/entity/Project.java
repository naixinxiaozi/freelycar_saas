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
 * @date 2018/10/18
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class Project implements Serializable {
    private static final long serialVersionUID = 7L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @NotNull
    private String id;

    @Column(length = 1,nullable = false, columnDefinition = "int default 0")
    private Integer delStatus;

    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    @Column
    private String comment;

    @Column
    private String name;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    private Float pricePerUnit;

    /**
     * 参考工时
     */
    @Column
    private Integer referWorkTime;

    @Column
    private String projectTypeId;

    /**
     * 使用次数
     */
    @Column
    private Integer useTimes;

    /**
     * 智能柜上架标记
     */
    @Column(length = 1,nullable = false,columnDefinition = "int default 0")
    private Integer saleStatus;

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", delStatus=" + delStatus +
                ", createTime=" + createTime +
                ", comment='" + comment + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", pricePerUnit=" + pricePerUnit +
                ", referWorkTime=" + referWorkTime +
                ", projectTypeId='" + projectTypeId + '\'' +
                ", useTimes=" + useTimes +
                ", saleStatus=" + saleStatus +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
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

    public Float getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Float pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Integer getReferWorkTime() {
        return referWorkTime;
    }

    public void setReferWorkTime(Integer referWorkTime) {
        this.referWorkTime = referWorkTime;
    }

    public String getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(String projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public Integer getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Integer useTimes) {
        this.useTimes = useTimes;
    }

    public Integer getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(Integer saleStatus) {
        this.saleStatus = saleStatus;
    }

    public Project() {
    }
}
