package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.service.StaffService;
import com.freelycar.saas.wechat.model.StaffLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangwei - Toby
 * @date 2019-02-01
 * @email toby911115@gmail.com
 */
@Api
@RestController
@RequestMapping("/wechat/staff")
public class WeChatStaffController {

    @Autowired
    private StaffService staffService;


    @ApiOperation(value = "技师端登录方法", produces = "application/json")
    @PostMapping("/login")
    @LoggerManage(description = "调用方法：技师端登录方法")
    public ResultJsonObject login(@RequestBody StaffLogin staffLogin) {
        String account = staffLogin.getAccount();
        String password = staffLogin.getPassword();

        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            return ResultJsonObject.getErrorResult(null, "登录失败：接收到的参数中，用户名或密码为空。");
        }
        return staffService.login(account, password);
    }
}
