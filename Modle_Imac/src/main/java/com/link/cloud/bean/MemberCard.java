package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/3/7.
 */

public class MemberCard {
    @SerializedName("cardName")
    String cardName;
    @SerializedName("cardNumber")
    String cardNumber;
    @SerializedName("beginTime")
    String beginTime;
    @SerializedName("endTime")
    String endTime;

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCardName() {
        return cardName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public String getCardNumber() {
        return cardNumber;
    }
}
