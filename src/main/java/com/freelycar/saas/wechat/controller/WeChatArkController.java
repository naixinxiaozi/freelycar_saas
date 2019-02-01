package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.model.OrderObject;
import com.freelycar.saas.project.service.ConsumerOrderService;
import com.freelycar.saas.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangwei - Toby
 * @date 2019-01-25
 * @email toby911115@gmail.com
 */

@RestController
@RequestMapping("/wechat/ark")
public class WeChatArkController {
    @Autowired
    private ConsumerOrderService consumerOrderService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("/getActiveOrder")
    public ResultJsonObject getActiveOrder(@RequestParam String clientId) {
        return consumerOrderService.getActiveOrder(clientId);
    }

    @PostMapping("/orderService")
    public ResultJsonObject orderService(@RequestBody OrderObject orderObject) {
        return consumerOrderService.arkHandleOrder(orderObject);
    }

    @GetMapping("/cancelOrderService")
    public ResultJsonObject cancelOrderService(@RequestParam String id) {
        return consumerOrderService.cancelOrder(id);
    }

    @GetMapping("/getProjects")
    public ResultJsonObject getProjects(@RequestParam String storeId) {
        return projectService.getProjects(storeId);
    }



}
