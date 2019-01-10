package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.AutoParts;
import com.freelycar.saas.project.entity.ConsumerOrder;
import com.freelycar.saas.project.entity.ConsumerProjectInfo;
import com.freelycar.saas.project.entity.Coupon;
import com.freelycar.saas.project.model.OrderObject;
import com.freelycar.saas.project.model.PayOrder;
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

    @Autowired
    private CouponService couponService;

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
            consumerOrder.setDelStatus(Constants.DelStatus.NORMAL.isValue());
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
        //设置order的额外信息
        consumerOrder.setOrderType(Constants.OrderType.SERVICE.getValue());
        consumerOrder.setPayState(Constants.PayState.NOT_PAY.getValue());
        //快速开单时订单状态直接为接车状态（不需要预约）
        consumerOrder.setState(Constants.OrderState.RECEIVE_CAR.getValue());

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

    /**
     * 根据clientId查找所有有效订单
     *
     * @param clientId
     * @return
     */
    public List<ConsumerOrder> findAllEffectiveOrdersByClientId(String clientId) {
        return consumerOrderRepository.findAllByClientIdAndDelStatusOrderByCreateTimeDesc(clientId, Constants.DelStatus.NORMAL.isValue());
    }

    /**
     * 查询某人的某类类型的所有订单
     *
     * @param clientId
     * @param type
     * @return
     */
    public List<ConsumerOrder> findAllOrdersByTypeAndClientId(String clientId, String type) {
        if (Constants.OrderType.SERVICE.getName().equalsIgnoreCase(type)) {
            return consumerOrderRepository.findAllByClientIdAndDelStatusAndOrderTypeOrderByCreateTimeDesc(clientId, Constants.DelStatus.NORMAL.isValue(), Constants.OrderType.SERVICE.getValue());
        }
        if (Constants.OrderType.ARK.getName().equalsIgnoreCase(type)) {
            return consumerOrderRepository.findAllByClientIdAndDelStatusAndOrderTypeOrderByCreateTimeDesc(clientId, Constants.DelStatus.NORMAL.isValue(), Constants.OrderType.ARK.getValue());
        }
        if (Constants.OrderType.CARD.getName().equalsIgnoreCase(type)) {
            return consumerOrderRepository.findAllByClientIdAndDelStatusAndOrderTypeOrderByCreateTimeDesc(clientId, Constants.DelStatus.NORMAL.isValue(), Constants.OrderType.CARD.getValue());
        }
        return null;
    }

    /**
     * 获取订单详情的数据（用于详情页展示和结算中心展示）
     *
     * @param id
     * @return
     */
    public ResultJsonObject getOrderObjectDetail(String id) {
        if (StringUtils.isEmpty(id)) {
            return ResultJsonObject.getErrorResult(null, "参数id为NULL");
        }

        OrderObject orderObject = new OrderObject();

        Optional<ConsumerOrder> optionalConsumerOrder = consumerOrderRepository.findById(id);
        if (!optionalConsumerOrder.isPresent()) {
            return ResultJsonObject.getErrorResult(null, "未找到id为：" + id + " 的订单数据");
        }

        List<ConsumerProjectInfo> consumerProjectInfos = consumerProjectInfoService.getAllProjectInfoByOrderId(id);

        List<AutoParts> autoPartsList = autoPartsService.getAllAutoPartsByOrderId(id);

        orderObject.setConsumerOrder(optionalConsumerOrder.get());
        orderObject.setConsumerProjectInfos(consumerProjectInfos);
        orderObject.setAutoParts(autoPartsList);

        return ResultJsonObject.getDefaultResult(orderObject);
    }

    /**
     * 结算（需要处理卡券抵扣等业务逻辑）
     *
     * @param payOrder
     * @return
     */
    public ResultJsonObject payment(PayOrder payOrder) {
        ConsumerOrder consumerOrder = payOrder.getConsumerOrder();
        if (null == consumerOrder) {
            return ResultJsonObject.getErrorResult(null, "consumerOrder对象为NULL！");
        }

        String orderId = consumerOrder.getId();

        /*
        处理抵用券的结算
         */
        //查询所有关联的抵用券，并将所有orderId和status初始化
        couponService.initCouponByOrderId(orderId);

        //将抵用券设置为挂单
        List<Coupon> coupons = payOrder.getUseCoupons();
        for (Coupon coupon : coupons) {
            coupon.setOrderId(orderId);
            coupon.setStatus(Constants.CouponStatus.BEEN_USED.getValue());
            couponService.saveOrUpdate(coupon);
        }

        //结算方式一
        Integer firstPayMethod = consumerOrder.getFirstPayMethod();

        //结算方式二
        Integer secondPayMethod = consumerOrder.getSecondPayMethod();


        return null;
    }

    /**
     * 挂单（本质是保存，但是需要考虑抵用券选用的情况）
     *
     * @param payOrder
     * @return
     */
    public ResultJsonObject pendingOrder(PayOrder payOrder) {
        ConsumerOrder consumerOrder = payOrder.getConsumerOrder();
        if (null == consumerOrder) {
            return ResultJsonObject.getErrorResult(null, "consumerOrder对象为NULL！");
        }
        consumerOrder = this.saveOrUpdate(consumerOrder);
        if (null == consumerOrder) {
            return ResultJsonObject.getErrorResult(null, "挂单操作失败：数据保存错误！");
        }

        String orderId = consumerOrder.getId();

        //查询所有关联的抵用券，并将所有orderId和status初始化
        couponService.initCouponByOrderId(orderId);

        //将抵用券设置为挂单
        List<Coupon> coupons = payOrder.getUseCoupons();
        for (Coupon coupon : coupons) {
            coupon.setOrderId(orderId);
            coupon.setStatus(Constants.CouponStatus.KEEP.getValue());
            couponService.saveOrUpdate(coupon);
        }

        return ResultJsonObject.getDefaultResult(orderId);
    }

}
