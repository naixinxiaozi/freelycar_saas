package com.freelycar.saas.project.model;

import org.springframework.format.annotation.DateTimeFormat;

public class CustomerList {
    private String name;
    private String phone;
    private String licensePlate;
    private String carBrand;
    private boolean isMember;
    private Integer customerTimes;
    private DateTimeFormat lastVisit;
    private Float balance;

    public CustomerList(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public Integer getCustomerTimes() {
        return customerTimes;
    }

    public void setCustomerTimes(Integer customerTimes) {
        this.customerTimes = customerTimes;
    }

    public DateTimeFormat getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(DateTimeFormat lastVisit) {
        this.lastVisit = lastVisit;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "CustomerList{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", carBrand='" + carBrand + '\'' +
                ", isMember=" + isMember +
                ", customerTimes=" + customerTimes +
                ", lastVisit=" + lastVisit +
                ", balance=" + balance +
                '}';
    }
}
