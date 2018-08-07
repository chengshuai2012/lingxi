package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/8/2.
 */

public class UserResponse extends ResultResponse {
    @SerializedName("userType")
    int userTyper;
    @SerializedName("uid")
    String uid;
    @SerializedName("name")
    String name;
    @SerializedName("needclerk")
    int needclerk;
    @SerializedName("sex")
    String sex;
    @SerializedName("phone")
    String phone;

    public void setUserTyper(int userTyper) {
        this.userTyper = userTyper;
    }

    public int getUserTyper() {
        return userTyper;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setNeedclerk(int needclerk) {
        this.needclerk = needclerk;
    }

    public int getNeedclerk() {
        return needclerk;
    }

    @Override
    public String toString() {
        return "UserLesson{"+
                "status:"+status+'\''+
                ",uid:"+uid+'\''+
                ",userTyper:"+userTyper+'\''+
                ",needclerk:"+needclerk+'\''+
                ",name:"+name+'\''+
                ",sex:"+sex+'\''+
                ",phone"+phone+'}';
    }
}
