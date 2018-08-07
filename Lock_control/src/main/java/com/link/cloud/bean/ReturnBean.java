package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shaozy on 2016/8/10.
 */
public class ReturnBean extends ResultResponse {

    @SerializedName("deviceID")
    public String deviceID;

    @SerializedName("pwd")
    public String pwd;

    @SerializedName("memID")
    public String memID;

    @SerializedName("signtime")
    public String signtime;

    @SerializedName("balance")
    public double balance;

    @SerializedName("cardInfo")
    public ArrayList<CardInfo> cardInfo;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMemID() {
        return memID;
    }

    public void setMemID(String memID) {
        this.memID = memID;
    }

    public String getSigntime() {
        return signtime;
    }

    public void setSigntime(String signtime) {
        this.signtime = signtime;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
