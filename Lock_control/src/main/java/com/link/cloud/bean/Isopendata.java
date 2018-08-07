package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/3/14.
 */

public class Isopendata extends ResultResponse{
    @SerializedName("uid")
    String uid;
    @SerializedName("name")
    String name;
    @SerializedName("numberType")
    int numberType;
    @SerializedName("numberValue")
    String numberValue;
    @SerializedName("cabinetNumber")
    String cabinetNumber;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setCabinetNumber(String cabinetNumber) {
        this.cabinetNumber = cabinetNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberValue(String numberValue) {
        this.numberValue = numberValue;
    }

    public void setNumberType(int numberType) {
        this.numberType = numberType;
    }

    public String getUid() {
        return uid;
    }

    public int getNumberType() {
        return numberType;
    }

    public String getCabinetNumber() {
        return cabinetNumber;
    }

    public String getName() {
        return name;
    }

    public String getNumberValue() {
        return numberValue;
    }

}
