package com.freelycar.saas.util;

import com.freelycar.saas.project.entity.OrderSn;
import com.freelycar.saas.project.repository.OrderSnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单ID生成器
 * 订单号生成规则：订单类型编号（1位）+ 门店（3位）+ 日期（6位）+ 每日递增（4位）
 *
 * @author tangwei - Toby
 * @date 2019-01-28
 * @email toby911115@gmail.com
 */
@Component
public class OrderIDGenerator {
    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyMMdd");
    private final String[] orderTypeSn = new String[]{"S", "A", "C"};
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OrderSnRepository orderSnRepository;

    synchronized public String generate(String storeId, int orderType) throws Exception {
        if (StringUtils.isEmpty(storeId)) {
            throw new Exception("门店ID为空值，无法生成单据ID");
        }
        if (orderType < 1 || orderType > orderTypeSn.length + 1) {
            throw new Exception("参数orderType超过了规则下标");
        }
        String currentDateNumber = sdfDate.format(new Date());
        logger.debug("当前时间日期6位数：" + currentDateNumber);


        StringBuilder orderIdSn = new StringBuilder().append(orderTypeSn[orderType - 1]);

        OrderSn orderSn = orderSnRepository.findTopByStoreIdAndAndDateNumberOrderByCreateTimeDesc(storeId, currentDateNumber);
        if (null != orderSn) {
            String storeSn = orderSn.getStoreSn();
            orderIdSn.append(storeSn);

            orderIdSn.append(currentDateNumber);

            int currentOrderNumber = Integer.valueOf(orderSn.getOrderNumber());
            String nextOrderNumber = Number2StringFormatter.format4Number2String(currentOrderNumber + 1);
            orderIdSn.append(nextOrderNumber);

            orderSn.setOrderNumber(nextOrderNumber);
            orderSnRepository.save(orderSn);
        } else {
            orderSn = orderSnRepository.findTopByStoreIdOrderByCreateTimeDesc(storeId);
            if (null == orderSn) {
                throw new Exception("没找到门店ID为:" + storeId + " 的单据规则数据，无法生成单据ID");
            }

            OrderSn todayOrderSnObject = new OrderSn();
            todayOrderSnObject.setStoreId(storeId);
            todayOrderSnObject.setStoreSn(orderSn.getStoreSn());
            todayOrderSnObject.setDateNumber(currentDateNumber);
            todayOrderSnObject.setCreateTime(new Timestamp(System.currentTimeMillis()));
            todayOrderSnObject.setOrderNumber("0001");
            OrderSn todayOrderSnObjectRes = orderSnRepository.save(todayOrderSnObject);

            if (null == todayOrderSnObjectRes) {
                throw new Exception("生成当日单据规则失败，无法生成单据ID");
            }

            orderIdSn.append(todayOrderSnObjectRes.getStoreSn())
                    .append(todayOrderSnObjectRes.getDateNumber())
                    .append(todayOrderSnObjectRes.getOrderNumber());
        }

        return orderIdSn.toString();
    }
}
