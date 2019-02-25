package com.freelycar.saas.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(columnDefinition = "float default 0.0")
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
     * 项目类别名称
     */
    @Transient
    private String projectTypeName;

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

}
