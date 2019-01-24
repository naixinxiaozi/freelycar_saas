package com.freelycar.saas.project.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.ConsumerOrder;
import com.freelycar.saas.project.model.OrderListParam;
import com.freelycar.saas.project.model.OrderObject;
import com.freelycar.saas.project.model.PayOrder;
import com.freelycar.saas.project.service.ConsumerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangwei - Toby
 * @date 2018-12-28
 * @email toby911115@gmail.com
 */
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
    @PostMapping("/handleOrder")
    public ResultJsonObject handleOrder(@RequestBody OrderObject orderObject) {
        return consumerOrderService.handleOrder(orderObject);
    }

    /**
     * 获取单据详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public ResultJsonObject detail(@RequestParam String id) {
        return consumerOrderService.getOrderObjectDetail(id);
    }

    /**
     * 结算
     *
     * @param payOrder
     * @return
     */
    @PostMapping("/payment")
    public ResultJsonObject payment(@RequestBody PayOrder payOrder) {
        return consumerOrderService.payment(payOrder);
    }

    /**
     * 挂单
     *
     * @param payOrder
     * @return
     */
    @PostMapping("/pendingOrder")
    public ResultJsonObject pendingOrder(@RequestBody PayOrder payOrder) {
        return consumerOrderService.pendingOrder(payOrder);
    }

    @PostMapping("/list")
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
    @PostMapping("/serviceFinish")
    public ResultJsonObject serviceFinish(@RequestBody ConsumerOrder consumerOrder) {
        return consumerOrderService.serviceFinish(consumerOrder);
    }

    /**
     * 交车
     *
     * @param consumerOrder
     * @return
     */
    @PostMapping("/handOver")
    public ResultJsonObject handOver(@RequestBody ConsumerOrder consumerOrder) {
        return consumerOrderService.handOver(consumerOrder);
    }
}
