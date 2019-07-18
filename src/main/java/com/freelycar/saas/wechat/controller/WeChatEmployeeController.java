package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.ObjectNotFoundException;
import com.freelycar.saas.project.entity.Employee;
import com.freelycar.saas.project.model.HistoryOrder;
import com.freelycar.saas.project.service.ConsumerOrderService;
import com.freelycar.saas.project.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-06-17
 * @email toby911115@gmail.com
 */
@Api
@RestController
@RequestMapping("/wechat/employee")
public class WeChatEmployeeController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ConsumerOrderService consumerOrderService;

    @ApiOperation(value = "技师登录方法（新）", produces = "application/json")
    @PostMapping("/login")
    @LoggerManage(description = "调用方法：技师登录方法（新）")
    public ResultJsonObject login(@RequestBody Employee employee) {
        return employeeService.login(employee);
    }

    @ApiOperation(value = "技师选择默认门店", produces = "application/json")
    @PostMapping("/selectStore")
    @LoggerManage(description = "调用方法：技师选择默认门店")
    public ResultJsonObject selectStore(@RequestBody Employee employee) {
        return employeeService.selectStore(employee);
    }

    @ApiOperation(value = "查询雇员的个人信息", produces = "application/json")
    @GetMapping("/detail")
    @LoggerManage(description = "调用方法：查询雇员的个人信息")
    public ResultJsonObject detail(@RequestParam String id) {
        try {
            return employeeService.detail(id);
        } catch (ArgumentMissingException | ObjectNotFoundException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, e.getMessage());
        }
    }

    @ApiOperation(value = "服务状态切换", produces = "application/json")
    @GetMapping("/switchServiceStatus")
    @LoggerManage(description = "调用方法：服务状态切换")
    public ResultJsonObject switchServiceStatus(@RequestParam String id) {
        try {
            return employeeService.switchServiceStatus(id);
        } catch (ArgumentMissingException | ObjectNotFoundException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, e.getMessage());
        }
    }

    @ApiOperation(value = "获取技师历史订单", produces = "application/json")
    @GetMapping("/listHistoryOrders")
    @LoggerManage(description = "调用方法：获取技师历史订单")
    public ResultJsonObject listHistoryOrders(@RequestParam String staffId, @RequestParam(required = false) String keyword) {
        try {
            List<HistoryOrder> list = consumerOrderService.listHistoryOrder(staffId, keyword);
            return ResultJsonObject.getDefaultResult(list);
        } catch (ArgumentMissingException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, e.getMessage());
        }
    }

}
