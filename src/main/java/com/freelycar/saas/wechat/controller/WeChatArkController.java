package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.ConsumerOrder;
import com.freelycar.saas.project.model.OrderObject;
import com.freelycar.saas.project.service.ArkService;
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

    @Autowired
    private ArkService arkService;

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

    @GetMapping("/orderFinish")
    public ResultJsonObject orderFinish(@RequestParam String id) {
        return consumerOrderService.orderFinish(id);
    }

    @GetMapping("/pickCar")
    public ResultJsonObject pickCar(@RequestParam String orderId, @RequestParam String staffId) {
        return consumerOrderService.pickCar(orderId, staffId);
    }

    @PostMapping("/finishCar")
    public ResultJsonObject finishCar(@RequestBody ConsumerOrder consumerOrder) {
        return consumerOrderService.finishCar(consumerOrder);
    }

    @GetMapping("/getCurrentArkLocation")
    public ResultJsonObject getCurrentArkLocation(@RequestParam String arkSn) {
        return arkService.getCurrentArkLocation(arkSn);
    }


}
