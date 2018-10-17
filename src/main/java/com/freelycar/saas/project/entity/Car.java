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
 * @date 2018/10/17
 * @email toby911115@gmail.com
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class Car implements Serializable {
    private static final long serialVersionUID = 5L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @NotNull
    private String id;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private Integer delStatus;

    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    @Column
    private String carBrand;

    @Column
    private String carType;

    @Column
    private String driveLicenseNumber;

    @Column
    private String engineNumber;

    @Column
    private String frameNumber;

    @Column
    private Float insuranceAmount;

    @Column
    private Timestamp insuranceStartTime;

    @Column
    private Timestamp insuranceEndTime;



}
