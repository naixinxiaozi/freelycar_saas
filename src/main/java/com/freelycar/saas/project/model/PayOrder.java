package com.freelycar.saas.project.model;

import com.freelycar.saas.project.entity.ConsumerOrder;
import com.freelycar.saas.project.entity.Coupon;

import java.util.List;

/**
 * 单据结算或挂单的JavaBean
 *
 * @author tangwei - Toby
 * @date 2019-01-10
 * @email toby911115@gmail.com
 */
public class PayOrder {
    private ConsumerOrder consumerOrder;

    private List<Coupon> useCoupons;

    public PayOrder() {
    }

    public ConsumerOrder getConsumerOrder() {
        return consumerOrder;
    }

    public void setConsumerOrder(ConsumerOrder consumerOrder) {
        this.consumerOrder = consumerOrder;
    }

    public List<Coupon> getUseCoupons() {
        return useCoupons;
    }

    public void setUseCoupons(List<Coupon> useCoupons) {
        this.useCoupons = useCoupons;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"consumerOrder\":")
                .append(consumerOrder);
        sb.append(",\"useCoupons\":")
                .append(useCoupons);
        sb.append('}');
        return sb.toString();
    }
}
