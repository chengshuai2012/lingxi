package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/3/7.
 */

public class Devicedmessage extends ResultResponse{
    @SerializedName("deviceId")
    String deviceId;
    @SerializedName("useSign")
    boolean useSign;
    @SerializedName("numberType")
    int numberType;

    public void setUseSign(Boolean useSign) {
        this.useSign = useSign;
    }

    public Boolean getUseSign() {
        return useSign;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setNumberType(int numberType) {
        this.numberType = numberType;
    }

    public int getNumberType() {
        return numberType;
    }
}
