package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Employee;
import com.freelycar.saas.project.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
