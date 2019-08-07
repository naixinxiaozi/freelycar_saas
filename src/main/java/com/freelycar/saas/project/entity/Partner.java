package com.freelycar.saas.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 合伙人信息
 * 记录公司官网提交过来的合伙人信息
 *
 * @author tangwei - Toby
 * @date 2019-08-07
 * @email toby911115@gmail.com
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String city;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"phone\":\"")
                .append(phone).append('\"');
        sb.append(",\"city\":\"")
                .append(city).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
