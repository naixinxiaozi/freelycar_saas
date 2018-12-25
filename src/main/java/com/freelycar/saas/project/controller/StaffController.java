package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Staff;
import com.freelycar.saas.project.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
public class StaffController {
    private static Logger logger = LoggerFactory.getLogger(StaffController.class);
    @Autowired
    StaffService staffService;
    private String errorMsg;

    /**
     * 新增/修改员工
     *
     * @param staff
     * @return
     */
    @PostMapping(value = "/modify")
    @LoggerManage(description = "调用方法：员工新增/修改")
    public ResultJsonObject saveOrUpdate(@RequestBody Staff staff) {
        if (null == staff) {
            errorMsg = "接收到的参数：员工为NULL";
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
    @GetMapping(value = "/detail")
    @LoggerManage(description = "调用方法：获取员工详情")
    public ResultJsonObject detail(@RequestParam String id) {
        if (null == id) {
            errorMsg = "接收到的参数：id为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return staffService.getDetail(id);
    }

    /**
     * 获取员工列表
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list")
    @LoggerManage(description = "调用方法：获取员工列表")
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
    @GetMapping(value = "/delete")
    @LoggerManage(description = "调用方法：删除员工信息")
    public ResultJsonObject delete(@RequestParam String id) {
        return staffService.delete(id);
    }


    /**
     * 智能柜技师关闭
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/closeArk")
    @LoggerManage(description = "调用方法：关闭智能柜技师")
    public ResultJsonObject closeArk(@RequestParam String id) {
        return staffService.closeArk(id);
    }
}
