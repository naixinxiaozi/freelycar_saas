package com.freelycar.saas.project.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.model.OrderObject;
import com.freelycar.saas.project.service.ConsumerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/handleOrder")
    public ResultJsonObject handleOrder(@RequestBody OrderObject orderObject) {
        return consumerOrderService.handleOrder(orderObject);
    }
}
