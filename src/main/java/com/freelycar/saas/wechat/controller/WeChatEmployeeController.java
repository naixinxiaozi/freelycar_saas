package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Employee;
import com.freelycar.saas.project.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author tangwei - Toby
 * @date 2019-06-17
 * @email toby911115@gmail.com
 */
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
}
