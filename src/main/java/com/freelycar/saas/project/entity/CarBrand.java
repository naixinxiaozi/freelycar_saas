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
public class CarBrand implements Serializable {
    private static final long serialVersionUID = 22L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 车辆品牌名称
     */
    @Column
    private String brand;

    /**
     * 名称拼音首字母
     */
    @Column
    private String pinyin;

    public CarBrand() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"brand\":\"")
                .append(brand).append('\"');
        sb.append(",\"pinyin\":\"")
                .append(pinyin).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
