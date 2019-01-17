package com.freelycar.saas.project.model;

import java.math.BigInteger;
import java.sql.Timestamp;

public class CustomerList {
    private String id;
    private String name;
    private String phone;
    private String plates;
    private String brands;
    private String isMember;
    private BigInteger totalCount;
    private Timestamp lastVisit;
    private Double totalBalance;

    public CustomerList() {
    }

    public CustomerList(String id, String name, String phone, String plates, String brands, String isMember, BigInteger totalCount, Timestamp lastVisit, Double totalBalance) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.plates = plates;
        this.brands = brands;
        this.isMember = isMember;
        this.totalCount = totalCount;
        this.lastVisit = lastVisit;
        this.totalBalance = totalBalance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPlates() {
        return plates;
    }

    public void setPlates(String plates) {
        this.plates = plates;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public String getIsMember() {
        return isMember;
    }

    public void setIsMember(String isMember) {
        this.isMember = isMember;
    }

    public BigInteger getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigInteger totalCount) {
        this.totalCount = totalCount;
    }

    public Timestamp getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Timestamp lastVisit) {
        this.lastVisit = lastVisit;
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"phone\":\"")
                .append(phone).append('\"');
        sb.append(",\"plates\":\"")
                .append(plates).append('\"');
        sb.append(",\"brands\":\"")
                .append(brands).append('\"');
        sb.append(",\"isMember\":\"")
                .append(isMember).append('\"');
        sb.append(",\"totalCount\":")
                .append(totalCount);
        sb.append(",\"lastVisit\":\"")
                .append(lastVisit).append('\"');
        sb.append(",\"totalBalance\":")
                .append(totalBalance);
        sb.append('}');
        return sb.toString();
    }
}
