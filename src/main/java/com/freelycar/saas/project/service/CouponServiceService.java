package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.PageableTools;
import com.freelycar.saas.basic.wrapper.PaginationRJO;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.CouponService;
import com.freelycar.saas.project.repository.CouponServiceRepository;
import com.freelycar.saas.util.UpdateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.freelycar.saas.basic.wrapper.ResultCode.RESULT_DATA_NONE;

@Service
public class CouponServiceService {
    private static Logger logger = LoggerFactory.getLogger(CouponServiceService.class);

    @Autowired
    private CouponServiceRepository couponServiceRepository;

    /**
     * 新增/修改抵用券对象
     *
     * @param couponService
     * @return
     */
    public ResultJsonObject modify(CouponService couponService) {
        try {
            //验重
            if (this.checkRepeatName(couponService)) {
                return ResultJsonObject.getErrorResult(null, "已包含名称为：“" + couponService.getName() + "”的数据，不能重复添加。");
            }

            //是否有ID，判断时新增还是修改
            String id = couponService.getId();
            if (StringUtils.isEmpty(id)) {
                couponService.setDelStatus(Constants.DelStatus.NORMAL.isValue());
                couponService.setCreateTime(new Timestamp(System.currentTimeMillis()));
               // couponService.setBookOnline(false);
            } else {
                Optional<CouponService> optional = couponServiceRepository.findById(id);
                //判断数据库中是否有该对象
                if (!optional.isPresent()) {
                    logger.error("修改失败，原因：" + CouponService.class + "中不存在id为 " + id + " 的对象");
                    return ResultJsonObject.getErrorResult(null);
                }
                CouponService source = optional.get();
                //将目标对象（projectType）中的null值，用源对象中的值替换
                UpdateTool.copyNullProperties(source, couponService);
            }
            //执行保存/修改
            return ResultJsonObject.getDefaultResult(couponServiceRepository.saveAndFlush(couponService));
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(null);
        }
    }

    /**
     * 验证项目是否重复
     * true：重复；false：不重复
     *
     * @param couponService
     * @return
     */
    private boolean checkRepeatName(CouponService couponService) {
        List<CouponService> couponServiceList;
        if (null != couponService.getId()) {
            couponServiceList = couponServiceRepository.checkRepeatName(couponService.getId(), couponService.getName(), couponService.getStoreId());
        } else {
            couponServiceList = couponServiceRepository.checkRepeatName(couponService.getName(), couponService.getStoreId());
        }
        return couponServiceList.size() != 0;
    }

    /**
     * 获取抵用券类详情
     *
     * @param id
     * @return
     */
    public ResultJsonObject getDetail(String id) {
        return ResultJsonObject.getDefaultResult(couponServiceRepository.findById(id));
    }


    /**
     * 查询抵用券类列表
     *
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PaginationRJO list(String storeId, Integer currentPage, Integer pageSize, String name) {
        logger.debug("storeId:" + storeId);
        Page<CouponService> couponServicePage = couponServiceRepository.findAllByDelStatusAndStoreIdAndNameContaining(Constants.DelStatus.NORMAL.isValue(), storeId, name, PageableTools.basicPage(currentPage, pageSize));
        return PaginationRJO.of(couponServicePage);
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
            int result = couponServiceRepository.delById(id);
            if (result != 1) {
                return ResultJsonObject.getErrorResult(id, "删除失败," + RESULT_DATA_NONE);
            }
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(id, "删除失败，删除操作出现异常");
        }
        return ResultJsonObject.getDefaultResult(id, "删除成功");
    }

}
