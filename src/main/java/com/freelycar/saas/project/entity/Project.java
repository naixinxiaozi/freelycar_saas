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

    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean delStatus;

    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime;

    /**
     * 备注（内容）
     */
    @Column
    private String comment;

    /**
     * 项目名称
     */
    @Column
    private String name;

    /**
     * 金额
     */
    @Column(nullable = false, columnDefinition = "float default 0.0")
    private Float price;

    /**
     * 单价（冗余）
     */
    @Column(nullable = false, columnDefinition = "float default 0.0")
    private Float pricePerUnit;

    /**
     * 参考工时
     */
    @Column
    private Integer referWorkTime;

    /**
     * 项目类别ID
     */
    @Column
    private String projectTypeId;

    /**
     * 项目类别对象
     */
    @Transient
    private ProjectType projectType;

    /**
     * 使用次数
     */
    @Column
    private Integer useTimes;

    /**
     * 智能柜上架标记
     */
    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean saleStatus;

    @Column
    private String storeId;

    /**
     * 上架标记
     */
    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean bookOnline;


    public Project() {
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
        sb.append(",\"comment\":\"")
                .append(comment).append('\"');
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"price\":")
                .append(price);
        sb.append(",\"pricePerUnit\":")
                .append(pricePerUnit);
        sb.append(",\"referWorkTime\":")
                .append(referWorkTime);
        sb.append(",\"projectTypeId\":\"")
                .append(projectTypeId).append('\"');
        sb.append(",\"projectType\":")
                .append(projectType);
        sb.append(",\"useTimes\":")
                .append(useTimes);
        sb.append(",\"saleStatus\":")
                .append(saleStatus);
        sb.append(",\"storeId\":\"")
                .append(storeId).append('\"');
        sb.append(",\"bookOnline\":")
                .append(bookOnline);
        sb.append('}');
        return sb.toString();
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

    public Boolean getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(Boolean saleStatus) {
        this.saleStatus = saleStatus;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public Boolean getBookOnline() {
        return bookOnline;
    }

    public void setBookOnline(Boolean bookOnline) {
        this.bookOnline = bookOnline;
    }
}
