package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/3/10.
 */

public class Down_UserInfo extends ResultResponse {
    @SerializedName("uid")
    String uid;
    @SerializedName("userName")
    String userName;
    @SerializedName("userType")
    String userType;
    @SerializedName("number_type")
    String number_type;
    @SerializedName("number_value")
    String number_value;
    @SerializedName("appid")
    String appid;
//    @SerializedName("")
    @SerializedName("shopId")
    String shoId;
    @SerializedName("deviceId")
    String deviceId;
    @SerializedName("fingerId")
    int fingerId;
    @SerializedName("feature")
    String feature;

    public void setNumber_type(String number_type) {
        this.number_type = number_type;
    }

    public String getNumber_type() {
        return number_type;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public void setNumber_value(String number_value) {
        this.number_value = number_value;
    }

    public String getNumber_value() {
        return number_value;
    }

    public int getFingerId() {
        return fingerId;
    }

    public void setFingerId(int fingerId) {
        this.fingerId = fingerId;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }


    public void setShoId(String shoId) {
        this.shoId = shoId;
    }

    public String getUid() {
        return uid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAppid() {
        return appid;
    }
    public String getFeature() {
        return feature;
    }
    public String getShoId() {
        return shoId;
    }
}
