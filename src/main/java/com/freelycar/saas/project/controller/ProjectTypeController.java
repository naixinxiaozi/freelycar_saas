package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.ProjectType;
import com.freelycar.saas.project.entity.WxUserInfo;
import com.freelycar.saas.project.service.ProjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tangwei - Toby
 * @date 2018-12-11
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/projectType")
public class ProjectTypeController {
    @Autowired
    ProjectTypeService projectTypeService;

    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    @LoggerManage(description = "项目类型新增/修改")
    public ResultJsonObject save(ProjectType projectType) {
        if (null == projectType) {
            return ResultJsonObject.getErrorResult(null, "接收到的项目类型对象为NULL");
        }
        return projectTypeService.modify(projectType);
    }
}
