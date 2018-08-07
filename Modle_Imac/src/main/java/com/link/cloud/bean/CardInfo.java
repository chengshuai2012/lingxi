package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shaozy on 2016/8/10.
 */
public class CardInfo extends ResultResponse {

    @SerializedName("cardID")
    public String cardID;

    @SerializedName("name")
    public String name;

//    @SerializedName("cardtype")
//    public String cardtype;

    @SerializedName("startTime")
    public String startTime;

    @SerializedName("endTime")
    public String endTime;

//    @SerializedName("cardBalance")
//    public String cardBalance;

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getCardtype() {
//        return cardtype;
//    }
//
//    public void setCardtype(String cardtype) {
//        this.cardtype = cardtype;
//    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

//    public String getCardBalance() {
//        return cardBalance;
//    }
//
//    public void setCardBalance(String cardBalance) {
//        this.cardBalance = cardBalance;
//    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "cardID='" + cardID + '\'' +
                ", name='" + name + '\'' +
//                ", cardtype='" + cardtype + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
//                ", cardBalance='" + cardBalance + '\'' +
                '}';
    }
}
