package com.freelycar.saas.permission.controller;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.basic.wrapper.PaginationRJO;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.permission.entity.SysUser;
import com.freelycar.saas.permission.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangwei [toby911115@gmail.com]
 * @date 2018/9/28
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 新增/修改账户信息
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/modify")
    public ResultJsonObject modify(@RequestBody SysUser sysUser) {
        return sysUserService.addOrModify(sysUser);
    }

    /**
     * 删除某一个账号
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public ResultJsonObject delete(@RequestParam long id) {
        return sysUserService.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/batchDelete")
    public ResultJsonObject batchDelete(@RequestBody JSONObject ids) {
        if (null == ids) {
            return ResultJsonObject.getErrorResult(null, "ids参数为NULL");
        }
        return sysUserService.delByIds(ids.getString("ids"));
    }

    /**
     * 查询
     *
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResultJsonObject list(
            @RequestParam(required = false) String storeId,
            @RequestParam Integer currentPage,
            @RequestParam(required = false) Integer pageSize) {
        return ResultJsonObject.getDefaultResult(PaginationRJO.of(sysUserService.list(storeId, currentPage, pageSize)));
    }

}
