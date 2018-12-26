package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Project;
import com.freelycar.saas.project.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private static Logger logger = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    ProjectService projectService;
    private String errorMsg;

    /**
     * 新增/修改项目
     *
     * @param project
     * @return
     */
    @PostMapping(value = "/modify")
    @LoggerManage(description = "调用方法：项目新增/修改")
    public ResultJsonObject saveOrUpdate(@RequestBody Project project) {
        if (null == project) {
            errorMsg = "接收到的参数：project为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return projectService.modify(project);
    }

    /**
     * 获取项目类型对象
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/detail")
    @LoggerManage(description = "调用方法：获取项目详情")
    public ResultJsonObject detail(@RequestParam String id) {
        if (null == id) {
            errorMsg = "接收到的参数：id为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return projectService.getDetail(id);
    }

    /**
     * 获取项目列表
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list")
    @LoggerManage(description = "调用方法：获取项目列表")
    public ResultJsonObject list(
            @RequestParam String storeId,
            @RequestParam Integer currentPage,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String projectTypeId
    ) {
        if (StringUtils.isEmpty(StringUtils.trimWhitespace(name))) {
            name = "";
        }
        return ResultJsonObject.getDefaultResult(projectService.list(storeId, currentPage, pageSize,name,projectTypeId));
    }

    /**
     * 删除操作（软删除）
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete")
    @LoggerManage(description = "调用方法：删除项目信息")
    public ResultJsonObject delete(@RequestParam String id) {
        return projectService.delete(id);
    }

    /**
     * 服务上架智能柜
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/upperArk")
    @LoggerManage(description = "调用方法：服务上架智能柜")
    public ResultJsonObject upperArk(@RequestParam String id) {
        return projectService.upperArk(id);
    }

    /**
     * 服务下架智能柜
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/lowerArk")
    @LoggerManage(description = "调用方法：服务下架智能柜")
    public ResultJsonObject lowerArk(@RequestParam String id) {
        return projectService.lowerArk(id);
    }

}
