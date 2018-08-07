package com.link.cloud.greendaodemo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by 30541 on 2018/6/26.
 */
@Entity
public class SignUser {
    @Id
    Long id;
    String uid;
    String number_value;
    String shopId;
    String number_type;
    String userName;
    String appid;
    String feature;
    String fingerId;
    String deviceId;
    String userType;
    @Generated(hash = 888737245)
    public SignUser(Long id, String uid, String number_value, String shopId,
                    String number_type, String userName, String appid, String feature,
                    String fingerId, String deviceId, String userType) {
        this.id = id;
        this.uid = uid;
        this.number_value = number_value;
        this.shopId = shopId;
        this.number_type = number_type;
        this.userName = userName;
        this.appid = appid;
        this.feature = feature;
        this.fingerId = fingerId;
        this.deviceId = deviceId;
        this.userType = userType;
    }
    @Generated(hash = 93985625)
    public SignUser() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getNumber_value() {
        return this.number_value;
    }
    public void setNumber_value(String number_value) {
        this.number_value = number_value;
    }
    public String getShopId() {
        return this.shopId;
    }
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    public String getNumber_type() {
        return this.number_type;
    }
    public void setNumber_type(String number_type) {
        this.number_type = number_type;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getAppid() {
        return this.appid;
    }
    public void setAppid(String appid) {
        this.appid = appid;
    }
    public String getFeature() {
        return this.feature;
    }
    public void setFeature(String feature) {
        this.feature = feature;
    }
    public String getFingerId() {
        return this.fingerId;
    }
    public void setFingerId(String fingerId) {
        this.fingerId = fingerId;
    }
    public String getDeviceId() {
        return this.deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getUserType() {
        return this.userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }

}
