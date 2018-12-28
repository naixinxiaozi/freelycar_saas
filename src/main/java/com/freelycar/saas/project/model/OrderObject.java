package com.freelycar.saas.project.model;

import com.freelycar.saas.project.entity.AutoParts;
import com.freelycar.saas.project.entity.ConsumerOrder;
import com.freelycar.saas.project.entity.ConsumerProjectInfo;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018-12-28
 * @email toby911115@gmail.com
 */
public class OrderObject {
    private ConsumerOrder consumerOrder;

    private List<ConsumerProjectInfo> consumerProjectInfos;

    private List<AutoParts> autoParts;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"consumerOrder\":")
                .append(consumerOrder);
        sb.append(",\"consumerProjectInfos\":")
                .append(consumerProjectInfos);
        sb.append(",\"autoParts\":")
                .append(autoParts);
        sb.append('}');
        return sb.toString();
    }
}
