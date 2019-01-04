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
public class CarModel implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 品牌车系表ID
     */
    @Column
    private Integer carTypeId;

    /**
     * 具体车系款式
     */
    @Column
    private String model;

    public CarModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(Integer carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"carTypeId\":")
                .append(carTypeId);
        sb.append(",\"model\":\"")
                .append(model).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
