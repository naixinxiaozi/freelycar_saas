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
     * 订单支付状态
     */
    public enum PayState {
        NOTPAY(1, "未结算"), FINISHPAY(2, "已结算");

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

        CASH(1, "现金"),
        CREDIT_CARD(2, "刷卡"),
        ALIPAY(3, "支付宝"),
        SUNING_PAY(4, "易付宝"),
        WECHAT_PAY(5, "微信支付");

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
}
