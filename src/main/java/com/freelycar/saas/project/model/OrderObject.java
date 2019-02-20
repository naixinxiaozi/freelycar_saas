package com.freelycar.saas.project.model;

import com.freelycar.saas.project.entity.*;

import java.util.List;

/**
 * 快速开单的JavaBean
 * @author tangwei - Toby
 * @date 2018-12-28
 * @email toby911115@gmail.com
 */
public class OrderObject {
    private ConsumerOrder consumerOrder;

    private List<ConsumerProjectInfo> consumerProjectInfos;

    private List<AutoParts> autoParts;

    private String arkSn;

    private Card card;

    private Coupon coupon;

    public OrderObject() {
    }

    public ConsumerOrder getConsumerOrder() {
        return consumerOrder;
    }

    public void setConsumerOrder(ConsumerOrder consumerOrder) {
        this.consumerOrder = consumerOrder;
    }

    public List<ConsumerProjectInfo> getConsumerProjectInfos() {
        return consumerProjectInfos;
    }

    public void setConsumerProjectInfos(List<ConsumerProjectInfo> consumerProjectInfos) {
        this.consumerProjectInfos = consumerProjectInfos;
    }

    public List<AutoParts> getAutoParts() {
        return autoParts;
    }

    public void setAutoParts(List<AutoParts> autoParts) {
        this.autoParts = autoParts;
    }

    public String getArkSn() {
        return arkSn;
    }

    public void setArkSn(String arkSn) {
        this.arkSn = arkSn;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"consumerOrder\":")
                .append(consumerOrder);
        sb.append(",\"consumerProjectInfos\":")
                .append(consumerProjectInfos);
        sb.append(",\"autoParts\":")
                .append(autoParts);
        sb.append(",\"arkSn\":\"")
                .append(arkSn).append('\"');
        sb.append(",\"card\":")
                .append(card);
        sb.append(",\"coupon\":")
                .append(coupon);
        sb.append('}');
        return sb.toString();
    }
}
