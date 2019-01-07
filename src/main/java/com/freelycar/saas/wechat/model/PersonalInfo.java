package com.freelycar.saas.wechat.model;

import com.freelycar.saas.project.entity.Car;
import com.freelycar.saas.project.entity.WxUserInfo;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-01-07
 * @email toby911115@gmail.com
 */
public class PersonalInfo {
    private WxUserInfo wxUserInfo;

    private List<Car> cars;

    private float cardBalance;

    public PersonalInfo() {
    }

    public PersonalInfo(WxUserInfo wxUserInfo, List<Car> cars, float cardBalance) {
        this.wxUserInfo = wxUserInfo;
        this.cars = cars;
        this.cardBalance = cardBalance;
    }

    public WxUserInfo getWxUserInfo() {
        return wxUserInfo;
    }

    public void setWxUserInfo(WxUserInfo wxUserInfo) {
        this.wxUserInfo = wxUserInfo;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public float getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(float cardBalance) {
        this.cardBalance = cardBalance;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"wxUserInfo\":")
                .append(wxUserInfo);
        sb.append(",\"cars\":")
                .append(cars);
        sb.append(",\"cardBalance\":")
                .append(cardBalance);
        sb.append('}');
        return sb.toString();
    }
}
