package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.ConsumerOrder;
import com.freelycar.saas.project.model.OrderListParam;
import com.freelycar.saas.project.model.OrderObject;
import com.freelycar.saas.project.model.PayOrder;
import com.freelycar.saas.project.service.ConsumerOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangwei - Toby
 * @date 2018-12-28
 * @email toby911115@gmail.com
 */
@Api(value = "门店单据管理", description = "门店单据管理接口", tags = "门店单据管理接口")
@RestController
@RequestMapping("/order")
public class ConsumerOrderController {

    @Autowired
    private ConsumerOrderService consumerOrderService;

    /**
     * 开单
     *
     * @param orderObject
     * @return
     */
    @ApiOperation(value = "新增/修改员工信息", produces = "application/json")
    @PostMapping("/handleOrder")
    @LoggerManage(description = "调用方法：新增/修改员工信息")
    public ResultJsonObject handleOrder(@RequestBody OrderObject orderObject) {
        return consumerOrderService.handleOrder(orderObject);
    }

    /**
     * 获取单据详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询员工信息", produces = "application/json")
    @GetMapping("/detail")
    @LoggerManage(description = "调用方法：查询员工信息")
    public ResultJsonObject detail(@RequestParam String id) {
        return consumerOrderService.getOrderObjectDetail(id);
    }

    /**
     * 结算
     *
     * @param payOrder
     * @return
     */
    @ApiOperation(value = "单据结算（支付）", produces = "application/json")
    @PostMapping("/payment")
    @LoggerManage(description = "调用方法：单据结算（支付）")
    public ResultJsonObject payment(@RequestBody PayOrder payOrder) {
        return consumerOrderService.payment(payOrder);
    }

    /**
     * 挂单
     *
     * @param payOrder
     * @return
     */
    @ApiOperation(value = "单据挂单", produces = "application/json")
    @PostMapping("/pendingOrder")
    @LoggerManage(description = "调用方法：单据挂单")
    public ResultJsonObject pendingOrder(@RequestBody PayOrder payOrder) {
        return consumerOrderService.pendingOrder(payOrder);
    }

    @ApiOperation(value = "单据列表（分页）", produces = "application/json")
    @PostMapping("/list")
    @LoggerManage(description = "调用方法：单据列表（分页）")
    public ResultJsonObject list(
            @RequestParam String storeId,
            @RequestParam Integer currentPage,
            @RequestParam(required = false) Integer pageSize,
            @RequestBody OrderListParam params
    ) {
        return consumerOrderService.list(storeId, currentPage, pageSize, params);
    }

    /**
     * 完工
     *
     * @param consumerOrder
     * @return
     */
    @ApiOperation(value = "设置单据完工状态", produces = "application/json")
    @PostMapping("/serviceFinish")
    @LoggerManage(description = "调用方法：设置单据完工状态")
    public ResultJsonObject serviceFinish(@RequestBody ConsumerOrder consumerOrder) {
        return consumerOrderService.serviceFinish(consumerOrder);
    }

    /**
     * 交车
     *
     * @param consumerOrder
     * @return
     */
    @ApiOperation(value = "设置单据交车状态", produces = "application/json")
    @PostMapping("/handOver")
    @LoggerManage(description = "调用方法：设置单据交车状态")
    public ResultJsonObject handOver(@RequestBody ConsumerOrder consumerOrder) {
        return consumerOrderService.handOver(consumerOrder);
    }


    @ApiOperation(value = "根据车牌查出相关信息", produces = "application/json")
    @GetMapping("/loadClientInfoByLicensePlate")
    @LoggerManage(description = "调用方法：根据车牌查出相关信息")
    public ResultJsonObject loadClientInfoByLicensePlate(@RequestParam String licensePlate, @RequestParam String storeId) {
        return consumerOrderService.loadClientInfoByLicensePlate(licensePlate, storeId);
    }




}
