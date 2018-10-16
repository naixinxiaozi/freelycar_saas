package com.freelycar.saas.permission.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.permission.entity.SysUser;
import com.freelycar.saas.permission.service.RegisterServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangwei [toby911115@gmail.com]
 * @date 2018/9/29
 */
@RestController("/register")
public class RegisterController {
    @Autowired
    private RegisterServcie registerServcie;

    @RequestMapping(value = "/registerUser",method = RequestMethod.POST)
    public ResultJsonObject register(@RequestBody SysUser sysUser) {
        return registerServcie.register(sysUser);
    }
}
