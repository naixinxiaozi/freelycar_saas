package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.CouponService;
import com.freelycar.saas.project.service.CouponServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couponService")
public class CouponServiceController {
    private static Logger logger = LoggerFactory.getLogger(CouponServiceController.class);
    @Autowired
    CouponServiceService couponServiceService;
    private String errorMsg;

    /**
     * 新增/修改抵用券
     *
     * @param couponService
     * @return
     */
    @PostMapping(value = "/modify")
    @LoggerManage(description = "调用方法：抵用券新增/修改")
    public ResultJsonObject saveOrUpdate(@RequestBody CouponService couponService) {
        if (null == couponService) {
            errorMsg = "接收到的参数：cardService为NULL";
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
    @GetMapping(value = "/detail")
    @LoggerManage(description = "调用方法：获取抵用券详情")
    public ResultJsonObject detail(@RequestParam String id) {
        if (null == id) {
            errorMsg = "接收到的参数：id为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return couponServiceService.getDetail(id);
    }

    /**
     * 获取抵用券列表
     * @param storeId
     * @param currentPage
     * @return
     */
    @GetMapping(value = "/list")
    @LoggerManage(description = "调用方法：获取抵用券列表")
    public ResultJsonObject list(@RequestParam String storeId, @RequestParam Integer currentPage, @RequestParam(required = false) Integer pageSize) {
        return ResultJsonObject.getDefaultResult(couponServiceService.list(storeId, currentPage,pageSize));
    }

}
