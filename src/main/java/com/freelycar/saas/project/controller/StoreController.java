package com.freelycar.saas.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.PaginationRJO;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Store;
import com.freelycar.saas.project.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangwei - Toby
 * @date 2019-01-07
 * @email toby911115@gmail.com
 */
@Api(value = "门店信息", description = "门店信息服务接口", tags = "管理端")
@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;


    /**
     * 新增/修改门店
     *
     * @param store
     * @return
     */
    @ApiOperation(value = "新增/修改门店", produces = "application/json")
    @PostMapping("/modify")
    @LoggerManage(description = "调用方法：新增/修改门店")
    public ResultJsonObject modify(@RequestBody Store store) {
        Store storeRes = storeService.saveOrUpdate(store);
        if (null == storeRes) {
            return ResultJsonObject.getErrorResult(null, "保存失败");
        }
        return ResultJsonObject.getDefaultResult(storeRes);
    }


    /**
     * 删除门店
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "单个删除", produces = "application/json")
    @GetMapping("/delete")
    @LoggerManage(description = "调用方法：单个删除门店信息")
    public ResultJsonObject delete(String id) {
        return storeService.delete(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除", produces = "application/json")
    @PostMapping("/batchDelete")
    @LoggerManage(description = "调用方法：批量删除门店信息")
    public ResultJsonObject batchDelete(@RequestBody JSONObject ids) {
        if (null == ids) {
            return ResultJsonObject.getErrorResult(null, "ids参数为NULL");
        }
        return storeService.delByIds(ids.getString("ids"));
    }

    /**
     * 查询
     *
     * @param name
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取门店列表（分页）", produces = "application/json")
    @GetMapping("/list")
    @LoggerManage(description = "调用方法：获取门店列表（分页）")
    public ResultJsonObject list(
            @RequestParam(required = false) String name,
            @RequestParam Integer currentPage,
            @RequestParam(required = false) Integer pageSize) {
        if (StringUtils.isEmpty(name)) {
            name = "";
        }
        return ResultJsonObject.getDefaultResult(PaginationRJO.of(storeService.list(name, currentPage, pageSize)));
    }
}
