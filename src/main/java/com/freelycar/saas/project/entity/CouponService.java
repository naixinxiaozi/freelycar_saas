package com.freelycar.saas.project.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 优惠券代售表
 *
 * @author tangwei - Toby
 * @date 2018/10/23
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class CouponService implements Serializable {
    private static final long serialVersionUID = 13L;

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
     * 备注
     */
    @Column
    private String content;

    /**
     * 抵用券名称
     */
    @Column
    private String name;

    /**
     * 抵用券类型（暂时冗余）
     */
    @Column
    private Integer type;

    /**
     * 有效期（月）
     * 0：永久
     */
    @Column
    private Integer validTime;

    /**
     * 售价
     */
    @Column
    private Double price;

    /**
     * 上架标记
     */
    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean bookOnline;

    /**
     * 抵用券对应的项目ID
     */
    @Column
    private String projectId;

    /**
     * 抵用券对应项目对象（不入库）
     */
    @Transient
    private Project project;

    public CouponService() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Boolean getBookOnline() {
        return bookOnline;
    }

    public void setBookOnline(Boolean bookOnline) {
        this.bookOnline = bookOnline;
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
        sb.append(",\"content\":\"")
                .append(content).append('\"');
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"type\":")
                .append(type);
        sb.append(",\"validTime\":")
                .append(validTime);
        sb.append(",\"price\":")
                .append(price);
        sb.append(",\"bookOnline\":")
                .append(bookOnline);
        sb.append(",\"projectId\":\"")
                .append(projectId).append('\"');
        sb.append(",\"project\":")
                .append(project);
        sb.append('}');
        return sb.toString();
    }
}

