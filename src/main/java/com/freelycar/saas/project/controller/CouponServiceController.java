package com.freelycar.saas.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.CouponService;
import com.freelycar.saas.project.service.CouponServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(value = "门店优惠券服务", description = "门店优惠券服务接口", tags = "门店优惠券服务接口")
@RestController
@RequestMapping("/couponService")
public class CouponServiceController {
    private Logger logger = LoggerFactory.getLogger(CouponServiceController.class);
    @Autowired
    CouponServiceService couponServiceService;

    /**
     * 新增/修改抵用券
     *
     * @param couponService
     * @return
     */
    @ApiOperation(value = "新增/修改优惠券", produces = "application/json")
    @PostMapping(value = "/modify")
    @LoggerManage(description = "调用方法：抵用券新增/修改")
    public ResultJsonObject saveOrUpdate(@RequestBody CouponService couponService) {
        if (null == couponService) {
            String errorMsg = "接收到的参数：couponService为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return couponServiceService.modify(couponService);
    }

    /**
     * 获取抵用券对象
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取优惠券详情", produces = "application/json")
    @GetMapping(value = "/detail")
    @LoggerManage(description = "调用方法：获取抵用券详情")
    public ResultJsonObject detail(@RequestParam String id) {
        return couponServiceService.getDetail(id);
    }

    /**
     * 获取抵用券列表
     * @param storeId
     * @param currentPage
     * @return
     */
    @ApiOperation(value = "获取优惠券列表（分页）", produces = "application/json")
    @GetMapping(value = "/list")
    @LoggerManage(description = "调用方法：获取抵用券列表")
    public ResultJsonObject list(
            @RequestParam String storeId,
            @RequestParam Integer currentPage,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String name
    ) {
        if (StringUtils.isEmpty(StringUtils.trimWhitespace(name))) {
            name = "";
        }
        return ResultJsonObject.getDefaultResult(couponServiceService.list(storeId, currentPage,pageSize,name));
    }

    /**
     * 删除操作（软删除）
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除抵用券信息", produces = "application/json")
    @GetMapping(value = "/delete")
    @LoggerManage(description = "调用方法：删除抵用券信息")
    public ResultJsonObject delete(@RequestParam String id) {
        return couponServiceService.delete(id);
    }


    @ApiOperation(value = "批量删除抵用券信息", produces = "application/json")
    @PostMapping("/batchDelete")
    @LoggerManage(description = "调用方法：批量删除抵用券信息")
    public ResultJsonObject batchDelete(@RequestBody JSONObject ids) {
        if (null == ids) {
            return ResultJsonObject.getErrorResult(null, "ids参数为NULL");
        }
        return couponServiceService.delByIds(ids.getString("ids"));
    }

    /**
     * 上架
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "上架抵用券", produces = "application/json")
    @GetMapping(value = "/upperShelf")
    @LoggerManage(description = "调用方法：上架抵用券信息")
    public ResultJsonObject upperShelf(@RequestParam String id) {
        return couponServiceService.upperShelf(id);
    }

    /**
     * 下架
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "下架抵用券", produces = "application/json")
    @GetMapping(value = "/lowerShelf")
    @LoggerManage(description = "调用方法：下架抵用券信息")
    public ResultJsonObject lowerShelf(@RequestParam String id) {
        return couponServiceService.lowerShelf(id);
    }
}
