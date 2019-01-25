package com.freelycar.saas.project.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author toby9
 * 2018/9/21
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class WxUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @NotNull
    private String id;

    /**
     * 创建时间
     */
    @Column(nullable = false, columnDefinition = "datetime default NOW()")
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /**
     * 生日，从微信授权获取
     */
    @Column
    private Date birthday;

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
     * 昵称，client表中也会冗余一份
     */
    @Column
    private String nickName;

    /**
     * 真实姓名，client表中也会冗余一份
     */
    @Column
    private String trueName;

    /**
     * openId（只用于推送通知用）
     */
    @Column
    private String openId;

    /**
     * 手机号（作为与client表数据对应的唯一凭证）
     */
    @Column(length = 20)
    private String phone;
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
     * 默认门店对应的clientId
     */
    @Column
    private String defaultClientId;

    public WxUserInfo() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"createTime\":\"")
                .append(createTime).append('\"');
        sb.append(",\"birthday\":\"")
                .append(birthday).append('\"');
        sb.append(",\"gender\":\"")
                .append(gender).append('\"');
        sb.append(",\"headImgUrl\":\"")
                .append(headImgUrl).append('\"');
        sb.append(",\"nickName\":\"")
                .append(nickName).append('\"');
        sb.append(",\"trueName\":\"")
                .append(trueName).append('\"');
        sb.append(",\"openId\":\"")
                .append(openId).append('\"');
        sb.append(",\"phone\":\"")
                .append(phone).append('\"');
        sb.append(",\"delStatus\":")
                .append(delStatus);
        sb.append(",\"defaultStoreId\":\"")
                .append(defaultStoreId).append('\"');
        sb.append(",\"defaultClientId\":\"")
                .append(defaultClientId).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Boolean delStatus) {
        this.delStatus = delStatus;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getDefaultStoreId() {
        return defaultStoreId;
    }

    public void setDefaultStoreId(String defaultStoreId) {
        this.defaultStoreId = defaultStoreId;
    }

    public String getDefaultClientId() {
        return defaultClientId;
    }

    public void setDefaultClientId(String defaultClientId) {
        this.defaultClientId = defaultClientId;
    }
}
