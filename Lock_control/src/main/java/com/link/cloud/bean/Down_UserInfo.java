package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/3/10.
 */

public class Down_UserInfo extends ResultResponse{
    @SerializedName("uid")
    String uid;
    @SerializedName("userName")
    String userName;
    @SerializedName("appid")
    String appid;
    @SerializedName("shopId")
    String shoId;
    @SerializedName("deviceId")
    String deviceId;
    @SerializedName("fingerId")
    String fingerId;
    @SerializedName("feature")
    String feature;

    public void setFingerId(String fingerId) {
        this.fingerId = fingerId;
    }

    public String getFingerId() {
        return fingerId;
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
