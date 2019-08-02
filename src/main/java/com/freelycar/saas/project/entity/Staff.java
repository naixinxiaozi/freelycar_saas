package com.freelycar.saas.project.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

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
@Data
public class Staff implements Serializable {
    private static final long serialVersionUID = 3L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @NotNull
    @Length(max = 50)
    private String id;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean delStatus;

    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime;

    @Column
    private String openId;

    @Column
    private String storeId;

    @Column
    private String comment;

    @Column
    private String gender;

    @Column
    private String level;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String position;

    @Column
    private String staffNumber;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean isArk;

    @Column
    private String account;

    @Column
    private String password;

    @Transient
    private String storeName;

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
        sb.append(",\"openId\":\"")
                .append(openId).append('\"');
        sb.append(",\"comment\":\"")
                .append(comment).append('\"');
        sb.append(",\"gender\":\"")
                .append(gender).append('\"');
        sb.append(",\"level\":\"")
                .append(level).append('\"');
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"phone\":\"")
                .append(phone).append('\"');
        sb.append(",\"position\":\"")
                .append(position).append('\"');
        sb.append(",\"staffNumber\":\"")
                .append(staffNumber).append('\"');
        sb.append(",\"isArk\":")
                .append(isArk);
        sb.append(",\"account\":\"")
                .append(account).append('\"');
        sb.append(",\"password\":\"")
                .append(password).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
