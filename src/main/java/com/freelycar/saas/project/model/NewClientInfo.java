package com.freelycar.saas.project.model;

import com.freelycar.saas.project.entity.Car;
import com.freelycar.saas.project.entity.Client;

/**
 * @author tangwei - Toby
 * @date 2018-12-25
 * @email toby911115@gmail.com
 */
public class NewClientInfo {
    private Client client;

    private Car car;

    public NewClientInfo() {
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"client\":")
                .append(client);
        sb.append(",\"car\":")
                .append(car);
        sb.append('}');
        return sb.toString();
    }
}
