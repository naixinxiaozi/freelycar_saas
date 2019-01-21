package com.freelycar.saas.permission.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.permission.entity.SysUser;
import com.freelycar.saas.permission.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangwei [toby911115@gmail.com]
 * @date 2018/9/28
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/modify")
    public ResultJsonObject modify(@RequestBody SysUser sysUser) {
        return sysUserService.addOrModify(sysUser);
    }

}
