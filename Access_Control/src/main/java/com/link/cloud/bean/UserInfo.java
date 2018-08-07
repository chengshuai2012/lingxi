package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/3/7.
 */

public class UserInfo extends ResultResponse{
    @SerializedName("uid")
    String uid;
    @SerializedName("name")
    String name;
    @SerializedName("userType")
    int userType;
    @SerializedName("numberValue")
    String numberValue;
    @SerializedName("cabinetNumber")
    String cabinetNumber;

    public String getNumberValue() {
        return numberValue;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public int getUserType() {
        return userType;
    }

    public String getCabinetNumber() {
        return cabinetNumber;
    }

    public void setNumberValue(String numberValue) {
        this.numberValue = numberValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void setCabinetNumber(String cabinetNumber) {
        this.cabinetNumber = cabinetNumber;
    }
}
