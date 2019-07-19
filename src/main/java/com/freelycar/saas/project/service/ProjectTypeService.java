package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.PageableTools;
import com.freelycar.saas.basic.wrapper.PaginationRJO;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.ProjectType;
import com.freelycar.saas.project.repository.ProjectTypeRepository;
import com.freelycar.saas.util.UpdateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.freelycar.saas.basic.wrapper.ResultCode.RESULT_DATA_NONE;

/**
 * @author tangwei - Toby
 * @date 2018-12-11
 * @email toby911115@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectTypeService {
    private Logger logger = LoggerFactory.getLogger(ProjectTypeService.class);

    @Autowired
    private ProjectTypeRepository projectTypeRepository;

    /**
     * 新增/修改项目类型对象
     *
     * @param projectType
     * @return
     */
    @CachePut(value = "projectType", key = "#projectType.id")
    public ResultJsonObject modify(ProjectType projectType) {
        try {
            //验重
            if (this.checkRepeatName(projectType)) {
                return ResultJsonObject.getErrorResult(null, "已包含类型名称为：“" + projectType.getName() + "”的数据，不能重复添加。");
            }

            //是否有ID，判断时新增还是修改
            String id = projectType.getId();
            if (StringUtils.isEmpty(id)) {
                projectType.setDelStatus(Constants.DelStatus.NORMAL.isValue());
                projectType.setCreateTime(new Timestamp(System.currentTimeMillis()));
            } else {
                Optional<ProjectType> optional = projectTypeRepository.findById(id);
                //判断数据库中是否有该对象
                if (!optional.isPresent()) {
                    logger.error("修改失败，原因：" + ProjectType.class + "中不存在id为 " + id + " 的对象");
                    return ResultJsonObject.getErrorResult(null);
                }
                ProjectType source = optional.get();
                //将目标对象（projectType）中的null值，用源对象中的值替换
                UpdateTool.copyNullProperties(source, projectType);
            }
            //执行保存
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

    /**
     * 获取项目类型详情
     *
     * @param id
     * @return
     */
    @Cacheable(value = "projectType", key = "#id")
    public ResultJsonObject getDetail(String id) {
        Optional<ProjectType> optional = projectTypeRepository.findById(id);
        //判断数据库中是否有该对象
        if (!optional.isPresent()) {
            logger.error(ProjectType.class + "中不存在id为 " + id + " 的对象");
            return ResultJsonObject.getErrorResult(null);
        }
        ProjectType result = optional.get();
        return ResultJsonObject.getDefaultResult(result);
    }

    /**
     * 查询项目类型列表
     *
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PaginationRJO list(String storeId, Integer currentPage, Integer pageSize) {
        logger.debug("storeId:" + storeId);
        Page<ProjectType> projectTypePage = projectTypeRepository.findAllByDelStatusAndStoreIdOrderByCreateTimeAsc(Constants.DelStatus.NORMAL.isValue(), storeId, PageableTools.basicPage(currentPage, pageSize));
        return PaginationRJO.of(projectTypePage);
    }

    /**
     * 删除操作（软删除）
     *
     * @param id
     * @return
     */
    @Transactional
    @CacheEvict(value = "projectType", key = "#id")
    public ResultJsonObject delete(String id) {
        try {
            int result = projectTypeRepository.delById(id);
            if (result != 1) {
                return ResultJsonObject.getErrorResult(id, "删除失败," + RESULT_DATA_NONE);
            }
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(id, "删除失败，删除操作出现异常");
        }
        return ResultJsonObject.getDefaultResult(id, "删除成功");
    }
}
