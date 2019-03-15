package com.freelycar.saas.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tangwei - Toby
 * @date 2019-03-15
 * @email toby911115@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrderListObject {

    /**
     * 单据编号
     */
    private String id;

    /**
     * 单据时间
     */
    private Date createTime;

    /**
     * 车牌号码
     */
    private String licensePlate;

    /**
     * 智能柜订单
     */
    private Integer orderType;

    /**
     * 订单金额
     */
    private Double totalPrice;

    /**
     * 支付金额
     */
    private Double actualPrice;

    /**
     * 车辆状态
     */
    private Integer state;

    /**
     * 结算状态
     */
    private Integer payState;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 项目类别
     */
    private String projectType;

    /**
     * 停车位置
     */
    private String parkingLocation;

    /**
     * 接车时间
     */
    private Date pickTime;

    /**
     * 完工时间
     */
    private Date finishTime;

    /**
     * 交车时间
     */
    private Date deliverTime;

}
