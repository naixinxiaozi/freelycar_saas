package com.freelycar.saas.basic.wrapper;

/**
 * @author tangwei - Toby
 * @date 2019-01-07
 * @email toby911115@gmail.com
 */
public class Constants {
    public enum DelStatus {
        /**
         * 有效的（数据库中对应0，bit类型）
         */
        NORMAL(false),
        /**
         * 无效的（数据库中对应1，bit类型）
         */
        DELETE(true);

        private final boolean value;

        DelStatus(boolean value) {
            this.value = value;
        }

        public boolean isValue() {
            return value;
        }
    }

    /**
     * 订单类型（1.服务开单；2.智能柜开单；3.办卡/续卡/抵用券）
     */
    public enum OrderType {
        SERVICE(1, "service"), ARK(2, "ark"), CARD(3, "card");

        private final Integer value;
        private final String name;

        OrderType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 订单状态
     */
    public enum OrderState {
        RESERVATION(0, "预约"),
        RECEIVE_CAR(1, "接车"),
        SERVICE_FINISH(2, "完工"),
        HAND_OVER(3, "交车"),
        CANCEL(4, "取消");

        private final Integer value;
        private final String name;

        OrderState(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 订单支付状态
     */
    public enum PayState {
        NOT_PAY(1, "未结算"), FINISH_PAY(2, "已结算");

        private final Integer value;
        private final String name;

        PayState(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 支付方式
     */
    public enum PayMethod {

        CARD(0, "储值卡"),
        CASH(1, "现金"),
        WECHAT_PAY(2, "微信"),
        ALIPAY(3, "支付宝"),
        SUNING_PAY(4, "易付宝"),
        CREDIT_CARD(5, "刷卡");

        private final Integer code;

        private final String value;

        PayMethod(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 抵用券使用状态（0：未使用；1：已使用；2：挂单中）
     */
    public enum CouponStatus {
        NOT_USE(0, "未使用"),
        BEEN_USED(1, "已使用"),
        KEEP(2, "挂单中");

        private final Integer value;
        private final String name;

        CouponStatus(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 单据查询时间类型
     */
    public enum DateType {
        ORDER(1, "单据时间"),
        PICK(2, "接车时间"),
        FINISH(3, "完工时间"),
        DELIVER(4, "交车时间");

        private final Integer value;

        private final String name;

        DateType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 智能柜门状态
     */
    public enum DoorState {
        EMPTY(0, "可使用"),
        USER_RESERVATION(1, "预约状态"),
        STAFF_FINISH(2, "完工状态"),
        DISABLED(6, "不可用");

        private final Integer value;

        private final String name;

        DoorState(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }
}
