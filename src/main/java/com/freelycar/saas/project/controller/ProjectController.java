package com.freelycar.saas.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Project;
import com.freelycar.saas.project.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(value = "门店项目管理", description = "门店项目管理接口", tags = "门店端")
@RestController
@RequestMapping("/project")
public class ProjectController {
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    ProjectService projectService;
    private String errorMsg;

    /**
     * 新增/修改项目
     *
     * @param project
     * @return
     */
    @ApiOperation(value = "新增/修改项目", produces = "application/json")
    @PostMapping(value = "/modify")
    @LoggerManage(description = "调用方法：新增/修改项目")
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
    @ApiOperation(value = "获取项目详情", produces = "application/json")
    @GetMapping(value = "/detail")
    @LoggerManage(description = "调用方法：获取项目详情")
    public ResultJsonObject detail(@RequestParam String id) {
        return projectService.getDetail(id);
    }

    /**
     * 获取项目列表
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取项目列表（分页）", produces = "application/json")
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
    @ApiOperation(value = "删除项目信息", produces = "application/json")
    @GetMapping(value = "/delete")
    @LoggerManage(description = "调用方法：删除项目信息")
    public ResultJsonObject delete(@RequestParam String id) {
        return projectService.delete(id);
    }


    @ApiOperation(value = "批量删除卡类信息", produces = "application/json")
    @PostMapping("/batchDelete")
    @LoggerManage(description = "调用方法：批量删除卡类信息")
    public ResultJsonObject batchDelete(@RequestBody JSONObject ids) {
        if (null == ids) {
            return ResultJsonObject.getErrorResult(null, "ids参数为NULL");
        }
        return projectService.delByIds(ids.getString("ids"));
    }

    /**
     * 服务上架智能柜
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "服务项目上架智能柜，在微信智能柜预约服务时可以看到", produces = "application/json")
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
    @ApiOperation(value = "服务项目下架智能柜，在微信智能柜预约服务时不再可以看到", produces = "application/json")
    @GetMapping(value = "/lowerArk")
    @LoggerManage(description = "调用方法：服务下架智能柜")
    public ResultJsonObject lowerArk(@RequestParam String id) {
        return projectService.lowerArk(id);
    }


    /**
     * 上架（在微信显示）
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "微信上架，能在门店信息处显示", produces = "application/json")
    @GetMapping(value = "/upperShelf")
    @LoggerManage(description = "调用方法：上架（在微信显示")
    public ResultJsonObject upperShelf(@RequestParam String id) {
        return projectService.upperShelf(id);
    }

    /**
     * 下架（不在微信显示）
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "微信上架，能在门店信息处不再显示", produces = "application/json")
    @GetMapping(value = "/lowerShelf")
    @LoggerManage(description = "调用方法：下架（不在微信显示）")
    public ResultJsonObject lowerShelf(@RequestParam String id) {
        return projectService.lowerShelf(id);
    }

}
