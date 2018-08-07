package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

public class SignedResponse extends ResultResponse {

    @SerializedName("name")
    public String name;

    @SerializedName("phone")
    public String phone;

    @SerializedName("sex")
    public String sex;

    @SerializedName("position")
    public String position;

    @SerializedName("remainder")
    public String remainder;//卡剩余次数

    @SerializedName("endTime")
    public String endTime;//卡有效截止日期

    @SerializedName("deviceName")
    public String deviceName;

    @SerializedName("cabinetNumber")
    public String cabinetNumber;

    @SerializedName("cardType")
    public String cardType;//卡类型

    @SerializedName("cardBalance")
    public String cardBalance;//卡余额

    @SerializedName("cardCose")
    public String cardCose;

    public String getCardCose() {
        return cardCose;
    }

    public void setCardCose(String cardCose) {
        this.cardCose = cardCose;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public String getCabinetNumber() {
        return cabinetNumber;
    }

    public void setCabinetNumber(String cabinetNumber) {
        this.cabinetNumber = cabinetNumber;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRemainder() {
        return remainder;
    }

    public void setRemainder(String remainder) {
        this.remainder = remainder;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(String cardBalance) {
        this.cardBalance = cardBalance;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "SignedResponse{" +
                "phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", cabinetNumber='" + cabinetNumber + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", position='" + position + '\'' +
                ", remainder='" + remainder + '\'' +
                ", cardType='" + cardType + '\'' +
                ", cardBalance=" + cardBalance +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
