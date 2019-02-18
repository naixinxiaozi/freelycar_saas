package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.ConsumerOrder;
import com.freelycar.saas.project.model.OrderObject;
import com.freelycar.saas.project.service.ArkService;
import com.freelycar.saas.project.service.ConsumerOrderService;
import com.freelycar.saas.project.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        try {
            return consumerOrderService.arkHandleOrder(orderObject);
        } catch (Exception e) {
            logger.error("用户预约智能柜服务出现异常", e);
            e.printStackTrace();
        }
        return ResultJsonObject.getErrorResult(null, "用户预约智能柜服务出现异常，请稍后重试或联系门店。");
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
        try {
            return consumerOrderService.pickCar(orderId, staffId);
        } catch (Exception e) {
            logger.error("技师取车出现异常", e);
            e.printStackTrace();
        }
        return ResultJsonObject.getErrorResult(null, "技师取车出现异常，请稍后重试或联系门店。");
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
