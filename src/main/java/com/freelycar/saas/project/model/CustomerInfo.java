package com.freelycar.saas.project.model;

import com.freelycar.saas.project.entity.Car;
import com.freelycar.saas.project.entity.Card;
import com.freelycar.saas.project.entity.Client;
import com.freelycar.saas.project.entity.Coupon;

import java.util.List;


public class CustomerInfo {
    private Client client;

    private List<Car> car;

    private List<Card> card;

    private List<Coupon> coupon;

    public CustomerInfo() {
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Car> getCar() {
        return car;
    }

    public void setCar(List<Car> car) {
        this.car = car;
    }

    public List<Card> getCard() {
        return card;
    }

    public void setCard(List<Card> card) {
        this.card = card;
    }

    public List<Coupon> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<Coupon> coupon) {
        this.coupon = coupon;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"client\":")
                .append(client);
        sb.append(",\"car\":")
                .append(car);
        sb.append(",\"card\":")
                .append(card);
        sb.append(",\"coupon\":")
                .append(coupon);
        sb.append('}');
        return sb.toString();
    }
}
