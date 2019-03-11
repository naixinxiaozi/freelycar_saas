package com.freelycar.saas.wechat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author tangwei - Toby
 * @date 2019-03-11
 * @email toby911115@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponInfo {
    private String id;

    private String storeId;

    /**
     * 抵用券服务ID
     */
    private String couponServiceId;

    /**
     * 客户ID
     */
    private String clientId;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 过期时间
     */
    private Timestamp deadline;

    /**
     * 购买价格
     */
    private double price;

    /**
     * 使用状态（0：未使用；1：已使用；2：挂单中）
     */
    private Integer status;

    /**
     * 备注
     */
    private String content;

    /**
     * 原价
     */
    private float originalPrice;

}
