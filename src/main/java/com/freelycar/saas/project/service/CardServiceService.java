package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.DelStatus;
import com.freelycar.saas.basic.wrapper.PageableTools;
import com.freelycar.saas.basic.wrapper.PaginationRJO;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.CardService;
import com.freelycar.saas.project.repository.CardServiceRepository;
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
public class CardServiceService {
    private static Logger logger = LoggerFactory.getLogger(CardServiceService.class);

    @Autowired
    private CardServiceRepository cardServiceRepository;

    /**
     * 新增/修改卡类对象
     *
     * @param cardService
     * @return
     */
    public ResultJsonObject modify(CardService cardService) {
        try {
            //验重
            if (this.checkRepeatName(cardService)) {
                return ResultJsonObject.getErrorResult(null, "已包含名称为：“" + cardService.getName() + "”的数据，不能重复添加。");
            }

            //是否有ID，判断时新增还是修改
            String id = cardService.getId();
            if (StringUtils.isEmpty(id)) {
                cardService.setDelStatus(DelStatus.EFFECTIVE.isValue());
                cardService.setCreateTime(new Timestamp(System.currentTimeMillis()));
                cardService.setBookOnline(false);
            } else {
                Optional<CardService> optional = cardServiceRepository.findById(id);
                //判断数据库中是否有该对象
                if (!optional.isPresent()) {
                    logger.error("修改失败，原因：" + CardService.class + "中不存在id为 " + id + " 的对象");
                    return ResultJsonObject.getErrorResult(null);
                }
                CardService source = optional.get();
                //将目标对象（projectType）中的null值，用源对象中的值替换
                UpdateTool.copyNullProperties(source, cardService);
            }
            //执行保存/修改
            return ResultJsonObject.getDefaultResult(cardServiceRepository.saveAndFlush(cardService));
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(null);
        }
    }

    /**
     * 验证项目是否重复
     * true：重复；false：不重复
     *
     * @param cardService
     * @return
     */
    private boolean checkRepeatName(CardService cardService) {
        List<CardService> cardServiceList;
        if (null != cardService.getId()) {
            cardServiceList = cardServiceRepository.checkRepeatName(cardService.getId(), cardService.getName(), cardService.getStoreId());
        } else {
            cardServiceList = cardServiceRepository.checkRepeatName(cardService.getName(), cardService.getStoreId());
        }
        return cardServiceList.size() != 0;
    }

    /**
     * 获取卡类详情
     *
     * @param id
     * @return
     */
    public ResultJsonObject getDetail(String id) {
        return ResultJsonObject.getDefaultResult(cardServiceRepository.findById(id));
    }


    /**
     * 查询卡类列表
     *
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PaginationRJO list(String storeId, Integer currentPage, Integer pageSize, String name) {
        logger.debug("storeId:" + storeId);
        Page<CardService> cardServicePage = cardServiceRepository.findAllByDelStatusAndStoreIdAndNameContaining(DelStatus.EFFECTIVE.isValue(), storeId, name, PageableTools.basicPage(currentPage, pageSize));
        return PaginationRJO.of(cardServicePage);
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
            int result = cardServiceRepository.delById(id);
            if (result != 1) {
                return ResultJsonObject.getErrorResult(id, "删除失败," + RESULT_DATA_NONE);
            }
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(id, "删除失败，删除操作出现异常");
        }
        return ResultJsonObject.getDefaultResult(id, "删除成功");
    }


}
