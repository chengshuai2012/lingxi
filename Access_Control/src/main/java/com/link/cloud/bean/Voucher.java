package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shaozy on 2016/8/19.
 */
public class Voucher extends ResultResponse {
    @SerializedName("name")
    public String name;

    @SerializedName("phone")
    public String phone;

    @SerializedName("balance")
    public String balance;

    @SerializedName("cost")
    public String cost;


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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }


    @Override
    public String toString() {
        return "Voucher{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", balance='" + balance + '\'' +
                ", cost='" + cost + '\'' +
                '}';
    }
}
