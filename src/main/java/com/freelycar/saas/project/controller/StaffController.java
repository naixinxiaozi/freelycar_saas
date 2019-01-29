package com.freelycar.saas.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Staff;
import com.freelycar.saas.project.service.StaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(value = "门店员工管理", description = "门店员工管理接口", tags = "门店员工管理接口")
@RestController
@RequestMapping("/staff")
public class StaffController {
    private Logger logger = LoggerFactory.getLogger(StaffController.class);
    @Autowired
    StaffService staffService;

    /**
     * 新增/修改员工
     *
     * @param staff
     * @return
     */
    @ApiOperation(value = "新增/修改员工信息", produces = "application/json")
    @PostMapping(value = "/modify")
    @LoggerManage(description = "调用方法：新增/修改员工信息")
    public ResultJsonObject saveOrUpdate(@RequestBody Staff staff) {
        if (null == staff) {
            String errorMsg = "接收到的参数：员工为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return staffService.modify(staff);
    }

    /**
     * 获取员工类型对象
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取员工详情", produces = "application/json")
    @GetMapping(value = "/detail")
    @LoggerManage(description = "调用方法：获取员工详情")
    public ResultJsonObject detail(@RequestParam String id) {
        return staffService.getDetail(id);
    }

    /**
     * 获取员工列表
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取员工列表（分页）", produces = "application/json")
    @GetMapping(value = "/list")
    @LoggerManage(description = "调用方法：获取员工列表（分页）")
    public ResultJsonObject list(
            @RequestParam String storeId,
            @RequestParam Integer currentPage,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name
    ) {
        if (StringUtils.isEmpty(StringUtils.trimWhitespace(name))) {
            name = "";
        }
        if (StringUtils.isEmpty(StringUtils.trimWhitespace(id))) {
            id = "";
        }
        return ResultJsonObject.getDefaultResult(staffService.list(storeId, currentPage, pageSize,id,name));
    }

    /**
     * 删除操作（软删除）
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "单个删除员工信息", produces = "application/json")
    @GetMapping(value = "/delete")
    @LoggerManage(description = "调用方法：单个删除员工信息")
    public ResultJsonObject delete(@RequestParam String id) {
        return staffService.delete(id);
    }


    @ApiOperation(value = "批量删除卡类信息", produces = "application/json")
    @PostMapping("/batchDelete")
    @LoggerManage(description = "调用方法：批量删除卡类信息")
    public ResultJsonObject batchDelete(@RequestBody JSONObject ids) {
        if (null == ids) {
            return ResultJsonObject.getErrorResult(null, "ids参数为NULL");
        }
        return staffService.delByIds(ids.getString("ids"));
    }


    /**
     * 智能柜技师开通
     * @param id
     * @param account
     * @param password
     * @return
     **/
    @ApiOperation(value = "智能柜技师开通", produces = "application/json")
    @GetMapping(value = "/openArk")
    @LoggerManage(description = "调用方法：智能柜技师开通")
    public ResultJsonObject openArk(
            @RequestParam String id,
            @RequestParam String account,
            @RequestParam String password
    ) {
        return staffService.openArk(id,account,password);
    }


    /**
     * 智能柜技师关闭
     * @param id
     * @return
     */
    @ApiOperation(value = "智能柜技师关闭", produces = "application/json")
    @GetMapping(value = "/closeArk")
    @LoggerManage(description = "调用方法：智能柜技师关闭")
    public  ResultJsonObject closeArk(@RequestParam String id){
        return staffService.closeArk(id);
    }
}
