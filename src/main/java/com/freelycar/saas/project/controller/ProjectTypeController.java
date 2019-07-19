package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.ProjectType;
import com.freelycar.saas.project.service.ProjectTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangwei - Toby
 * @date 2018-12-11
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/projectType")
public class ProjectTypeController {
    private Logger logger = LoggerFactory.getLogger(ProjectTypeController.class);
    @Autowired
    ProjectTypeService projectTypeService;
    private String errorMsg;

    /**
     * 新增/修改项目类型
     *
     * @param projectType
     * @return
     */
    @PostMapping(value = "/modify")
    @LoggerManage(description = "调用方法：项目类型新增/修改")
    public ResultJsonObject saveOrUpdate(@RequestBody ProjectType projectType) {
        if (null == projectType) {
            errorMsg = "接收到的参数：projectType为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return projectTypeService.modify(projectType);
    }

    /**
     * 获取项目类型对象
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/detail")
    @LoggerManage(description = "调用方法：获取项目类型详情")
    public ResultJsonObject detail(@RequestParam String id) {
        if (null == id) {
            errorMsg = "接收到的参数：id为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return projectTypeService.getDetail(id);
    }

    @GetMapping(value = "/list")
    @LoggerManage(description = "调用方法：获取项目类型列表")
    public ResultJsonObject list(@RequestParam String storeId, @RequestParam Integer currentPage, @RequestParam(required = false) Integer pageSize) {
        return ResultJsonObject.getDefaultResult(projectTypeService.list(storeId, currentPage, pageSize));
    }

    /**
     * 删除操作（软删除）
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete")
    @LoggerManage(description = "调用方法：删除项目类型信息")
    public ResultJsonObject delete(@RequestParam String id) {
        return projectTypeService.delete(id);
    }


}
