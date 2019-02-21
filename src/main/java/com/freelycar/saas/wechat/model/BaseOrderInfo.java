package com.freelycar.saas.wechat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tangwei - Toby
 * @date 2019-02-18
 * @email toby911115@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseOrderInfo {
    private String id;
    private String licensePlate;
    private String carBrand;
    private String carType;
    private String clientName;
    private String projectNames;
    private String cardName;
    private String couponName;
    private Date createTime;
    private Date pickTime;
    private Date finishTime;
    private Integer state;
    private Double actualPrice;
    private Double totalPrice;
    private Integer payState;
}
