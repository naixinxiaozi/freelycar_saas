package com.freelycar.saas.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * 雇员表
 * 注：与微信用户表结构基本相同；与staff表存在一对多关系
 *
 * @author tangwei - Toby
 * @date 2019-06-17
 * @email toby911115@gmail.com
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private static final long serialVersionUID = 36L;

    /**
     * 主键ID
     */
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @NotNull
    @Length(max = 50)
    private String id;

    /**
     * 创建时间
     */
    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /**
     * 删除标记位
     */
    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean delStatus = false;

    /**
     * 默认门店id
     */
    @Column
    private String defaultStoreId;

    /**
     * 默认门店对应的staffId
     */
    @Column
    private String defaultStaffId;

    /**
     * 性别，从微信授权获取
     */
    @Column(length = 10)
    private String gender;

    /**
     * 微信头像地址，从微信授权获取
     */
    @Column
    private String headImgUrl;

    /**
     * 城市，从微信授权获取
     */
    @Column
    private String city;

    /**
     * 省份，从微信授权获取
     */
    @Column
    private String province;

    /**
     * 昵称，从微信授权获取
     */
    @Column
    private String nickName;

    /**
     * 真实姓名，同staff表中的name一致
     */
    @Column
    private String trueName;

    /**
     * openId（只用于推送通知用）
     */
    @Column
    private String openId;

    /**
     * 手机号（作为与staff表数据对应的唯一凭证）
     */
    @Column(length = 20)
    private String phone;

    /**
     * 微信智能柜登录用户名
     */
    @Column
    private String account;

    /**
     * 微信智能柜登录密码
     */
    @Column
    private String password;

    /**
     * 微信通知标记位
     */
    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean notification;
}
