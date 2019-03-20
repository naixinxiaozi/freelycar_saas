package com.freelycar.saas.project.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "单据编号", orderNum = "0")
    private String id;

    /**
     * 单据时间
     */
    @Excel(name = "单据时间", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "1")
    private Date createTime;

    /**
     * 车牌号码
     */
    @Excel(name = "车牌号码", orderNum = "2")
    private String licensePlate;

    /**
     * 智能柜订单
     */
    @Excel(name = "智能柜订单", replace = {"否_1", "是_2", "否_3"}, orderNum = "3")
    private Integer orderType;

    /**
     * 订单金额
     */
    @Excel(name = "订单金额", orderNum = "4")
    private Double totalPrice;

    /**
     * 支付金额
     */
    @Excel(name = "支付金额", orderNum = "5")
    private Double actualPrice;

    /**
     * 车辆状态
     */
    @Excel(name = "车辆状态", replace = {"预约_0", "接车_1", "完工_2", "交车_3", "取消_4"}, orderNum = "6")
    private Integer state;

    /**
     * 结算状态
     */
    @Excel(name = "结算状态", replace = {"未结算_1", "已结算_2"}, orderNum = "7")
    private Integer payState;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码", orderNum = "8")
    private String phone;

    /**
     * 项目类别
     */
    @Excel(name = "项目类别", orderNum = "9")
    private String project;

    /**
     * 停车位置
     */
    @Excel(name = "停车位置", orderNum = "10")
    private String parkingLocation;

    /**
     * 接车时间
     */
    @Excel(name = "接车时间", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "11")
    private Date pickTime;

    /**
     * 完工时间
     */
    @Excel(name = "完工时间", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "12")
    private Date finishTime;

    /**
     * 交车时间
     */
    @Excel(name = "交车时间", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "13")
    private Date deliverTime;

}
