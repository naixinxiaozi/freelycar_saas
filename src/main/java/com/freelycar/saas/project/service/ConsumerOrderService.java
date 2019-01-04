package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.DelStatus;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.AutoParts;
import com.freelycar.saas.project.entity.ConsumerOrder;
import com.freelycar.saas.project.entity.ConsumerProjectInfo;
import com.freelycar.saas.project.model.OrderObject;
import com.freelycar.saas.project.repository.ConsumerOrderRepository;
import com.freelycar.saas.util.UpdateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * @author tangwei - Toby
 * @date 2018-12-28
 * @email toby911115@gmail.com
 */
@Service
@Transactional
public class ConsumerOrderService {
    @Autowired
    private ConsumerOrderRepository consumerOrderRepository;

    @Autowired
    private ConsumerProjectInfoService consumerProjectInfoService;

    @Autowired
    private AutoPartsService autoPartsService;

    /**
     * 保存和修改
     *
     * @param consumerOrder
     * @return
     */
    public ConsumerOrder saveOrUpdate(ConsumerOrder consumerOrder) {
        if (null == consumerOrder) {
            return null;
        }
        String id = consumerOrder.getId();
        if (StringUtils.isEmpty(id)) {
            consumerOrder.setDelStatus(DelStatus.EFFECTIVE.isValue());
            consumerOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
        } else {
            Optional<ConsumerOrder> consumerOrderOptional = consumerOrderRepository.findById(id);
            if (!consumerOrderOptional.isPresent()) {
                return null;
            }
            ConsumerOrder source = consumerOrderOptional.get();
            UpdateTool.copyNullProperties(source, consumerOrder);
        }
        return consumerOrderRepository.saveAndFlush(consumerOrder);
    }

    /**
     * 快速开单
     *
     * @param orderObject
     * @return
     */
    public ResultJsonObject handleOrder(OrderObject orderObject) {
        ConsumerOrder consumerOrder = orderObject.getConsumerOrder();
        List<ConsumerProjectInfo> consumerProjectInfos = orderObject.getConsumerProjectInfos();
        List<AutoParts> autoParts = orderObject.getAutoParts();

        //TODO 订单号生成规则：门店（3位）+ 日期（6位）+ 每日递增（4位）


        ConsumerOrder consumerOrderRes = this.saveOrUpdate(consumerOrder);
        if (null == consumerOrderRes) {
            return ResultJsonObject.getErrorResult(null, "开单失败！保存订单信息失败。如有疑问，请联系管理员！");
        }

        String orderId = consumerOrder.getId();

        //保存订单项目信息
        if (null != consumerProjectInfos && !consumerProjectInfos.isEmpty()) {
            for (ConsumerProjectInfo consumerProjectInfo : consumerProjectInfos) {
                consumerProjectInfo.setConsumerOrderId(orderId);
                consumerProjectInfoService.saveOrUpdate(consumerProjectInfo);
            }
        }

        //保存项目相关配件
        if (null != autoParts && !autoParts.isEmpty()) {
            for (AutoParts autoPart : autoParts) {
                autoPart.setConsumerOrderId(orderId);
                autoPartsService.saveOrUpdate(autoPart);
            }
        }

        return ResultJsonObject.getDefaultResult(consumerOrderRes.getId(), "订单生成成功！");
    }
}
