package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.project.entity.ConsumerProjectInfo;
import com.freelycar.saas.project.repository.ConsumerProjectInfoRepository;
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
@Transactional(rollbackFor = Exception.class)
public class ConsumerProjectInfoService {
    @Autowired
    private ConsumerProjectInfoRepository consumerProjectInfoRepository;

    /**
     * 保存消费订单服务项目对象
     *
     * @param consumerProjectInfo
     * @return
     */
    public ConsumerProjectInfo saveOrUpdate(ConsumerProjectInfo consumerProjectInfo) {
        if (null == consumerProjectInfo) {
            return null;
        }
        String id = consumerProjectInfo.getId();

        if (StringUtils.isEmpty(id)) {
            consumerProjectInfo.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            consumerProjectInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        } else {
            Optional<ConsumerProjectInfo> optional = consumerProjectInfoRepository.findById(id);
            if (!optional.isPresent()) {
                return null;
            }
            ConsumerProjectInfo source = optional.get();
            UpdateTool.copyNullProperties(source, consumerProjectInfo);
        }
        return consumerProjectInfoRepository.save(consumerProjectInfo);
    }

    /**
     * 查询指定订单的相关项目信息
     *
     * @param orderId
     * @return
     */
    public List<ConsumerProjectInfo> getAllProjectInfoByOrderId(String orderId) {
        return consumerProjectInfoRepository.findAllByDelStatusAndConsumerOrderIdOrderByCreateTimeAsc(Constants.DelStatus.NORMAL.isValue(), orderId);
    }

    /**
     * 计算项目列表的总金额
     *
     * @param consumerProjectInfos
     * @return
     */
    public double sumAllProjectPrice(List<ConsumerProjectInfo> consumerProjectInfos) {
        if (null == consumerProjectInfos) {
            return 0;
        }
        double totalPrice = 0;
        for (ConsumerProjectInfo consumerProjectInfo : consumerProjectInfos) {
            Double price = consumerProjectInfo.getPrice();
            if (null == price) {
                price = (double) 0;
            }
            totalPrice += price;
        }
        return totalPrice;
    }


    /**
     * 查询会员卡项目消费记录
     *
     * @param cardId
     * @return
     * @throws ArgumentMissingException
     */
    public List<ConsumerProjectInfo> getProjectInfosByCardId(String cardId) throws ArgumentMissingException {
        if (StringUtils.isEmpty(cardId)) {
            throw new ArgumentMissingException("参数cardId为空值。查询会员卡项目消费记录失败。");
        }
        return consumerProjectInfoRepository.findAllByDelStatusAndAndCardIdOrderByCreateTimeDesc(Constants.DelStatus.NORMAL.isValue(), cardId);
    }

    /**
     * @param couponId
     * @return
     * @throws ArgumentMissingException
     */
    public List<ConsumerProjectInfo> getProjectInfosByCouponId(String couponId) throws ArgumentMissingException {
        if (StringUtils.isEmpty(couponId)) {
            throw new ArgumentMissingException("参数couponId为空值。查询抵用券项目消费记录失败。");
        }
        return consumerProjectInfoRepository.findAllByDelStatusAndAndCouponIdOrderByCreateTimeDesc(Constants.DelStatus.NORMAL.isValue(), couponId);
    }
}
