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
 * @date 2018/10/22
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class Comment implements Serializable {
    private static final long serialVersionUID = 12L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @NotNull
    private String id;

    @Column(nullable = false)
    private Boolean delStatus;

    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());


    /**
     * 评价内容
     */
    @Column
    private String comment;

    /**
     * 评价时间
     */
    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp commentTime;

    /**
     * 星级评价
     */
    @Column(nullable = false,columnDefinition = "double default 0")
    private Double stars;

    @Column
    private String consumerOrderId;
}
