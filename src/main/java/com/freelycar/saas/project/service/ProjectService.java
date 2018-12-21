package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.DelStatus;
import com.freelycar.saas.basic.wrapper.PageableTools;
import com.freelycar.saas.basic.wrapper.PaginationRJO;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.CouponService;
import com.freelycar.saas.project.entity.Project;
import com.freelycar.saas.project.repository.ProjectRepository;
import com.freelycar.saas.util.UpdateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.freelycar.saas.basic.wrapper.ResultCode.RESULT_DATA_NONE;

@Service
public class ProjectService {
    private static Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * 新增/修改项目对象
     *
     * @param project
     * @return
     */
    public ResultJsonObject modify(Project project) {
        try {
            //验重
            if (this.checkRepeatName(project)) {
                return ResultJsonObject.getErrorResult(null, "已包含名称为：“" + project.getName() + "”的数据，不能重复添加。");
            }

            //是否有ID，判断时新增还是修改
            String id = project.getId();
            if (StringUtils.isEmpty(id)) {
                project.setDelStatus(DelStatus.EFFECTIVE.isValue());
                project.setCreateTime(new Timestamp(System.currentTimeMillis()));
            } else {
                Optional<Project> optional = projectRepository.findById(id);
                //判断数据库中是否有该对象
                if (!optional.isPresent()) {
                    logger.error("修改失败，原因：" + Project.class + "中不存在id为 " + id + " 的对象");
                    return ResultJsonObject.getErrorResult(null);
                }
                Project source = optional.get();
                //将目标对象（projectType）中的null值，用源对象中的值替换
                UpdateTool.copyNullProperties(source, project);
            }
            //执行保存/修改
            return ResultJsonObject.getDefaultResult(projectRepository.saveAndFlush(project));
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(null);
        }
    }

    /**
     * 验证项目是否重复
     * true：重复；false：不重复
     *
     * @param project
     * @return
     */
    private boolean checkRepeatName(Project project) {
        List<Project> projectList;
        if (null != project.getId()) {
            projectList = projectRepository.checkRepeatName(project.getId(), project.getName(), project.getStoreId());
        } else {
            projectList = projectRepository.checkRepeatName(project.getName(), project.getStoreId());
        }
        return projectList.size() != 0;
    }

    /**
     * 获取项目详情
     *
     * @param id
     * @return
     */
    public ResultJsonObject getDetail(String id) {
        return ResultJsonObject.getDefaultResult(projectRepository.findById(id));
    }


    /**
     * 查询项目列表
     *
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PaginationRJO list(String storeId, Integer currentPage, Integer pageSize,String name,String projectTypeId) {
        logger.debug("storeId:" + storeId);
        Page<Project> projectPage = projectRepository.findAllByDelStatusAndStoreIdAndNameContainingAndProjectTypeId(DelStatus.EFFECTIVE.isValue(), storeId,name,projectTypeId, PageableTools.basicPage(currentPage, pageSize));
        return PaginationRJO.of(projectPage);
    }

    /**
     * 删除操作（软删除）
     *
     * @param id
     * @return
     */
    @Transactional
    public ResultJsonObject delete(String id) {
        try {
            int result = projectRepository.delById(id);
            if (result != 1) {
                return ResultJsonObject.getErrorResult(id, "删除失败," + RESULT_DATA_NONE);
            }
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(id, "删除失败，删除操作出现异常");
        }
        return ResultJsonObject.getDefaultResult(id, "删除成功");
    }
}
