package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.ProjectType;
import com.freelycar.saas.project.repository.ProjectTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018-12-11
 * @email toby911115@gmail.com
 */
@Service
public class ProjectTypeService {
    @Autowired
    private ProjectTypeRepository projectTypeRepository;

    /**
     * 新增/修改项目类型对象
     *
     * @param projectType
     * @return
     */
    public ResultJsonObject modify(ProjectType projectType) {
        try {
            //验重
            if (this.checkRepeatName(projectType)) {
                return ResultJsonObject.getErrorResult(null, "已包含类型名称为：“" + projectType.getName() + "”的数据，不能重复添加。");
            }
            //执行保存/修改
            return ResultJsonObject.getDefaultResult(projectTypeRepository.saveAndFlush(projectType));
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(null);
        }
    }

    /**
     * 验证项目类别是否重复
     * true：重复；false：不重复
     *
     * @param projectType
     * @return
     */
    private boolean checkRepeatName(ProjectType projectType) {
        List<ProjectType> projectTypeList;
        if (null != projectType.getId()) {
            projectTypeList = projectTypeRepository.checkRepeatName(projectType.getId(), projectType.getName(), projectType.getStoreId());
        } else {
            projectTypeList = projectTypeRepository.checkRepeatName(projectType.getName(), projectType.getStoreId());
        }
        return projectTypeList.size() != 0;
    }
}
