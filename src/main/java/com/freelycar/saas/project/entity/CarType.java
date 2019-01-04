package com.freelycar.saas.project.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author tangwei - Toby
 * @date 2019-01-04
 * @email toby911115@gmail.com
 */
@Entity
@Table
public class CarType implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 车牌品牌表ID
     */
    @Column
    private Integer carBrandId;

    /**
     * 车系名称
     */
    @Column
    private String type;


    public CarType() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCarBrandId() {
        return carBrandId;
    }

    public void setCarBrandId(Integer carBrandId) {
        this.carBrandId = carBrandId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"carBrandId\":")
                .append(carBrandId);
        sb.append(",\"type\":\"")
                .append(type).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
