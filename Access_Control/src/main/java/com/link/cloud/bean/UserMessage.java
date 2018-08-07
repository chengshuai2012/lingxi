package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/2/9.
 */

public class UserMessage extends ResultResponse {
    @SerializedName("uid")
    public String userid;

    @SerializedName("name")
    public String userName;

    @SerializedName("phone")
    public String userphone;

    @SerializedName("sex")
    public String usersex;

    @SerializedName("img")
    public String userimg;

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUsersex(String usersex) {
        this.usersex = usersex;
    }

    public String getUsersex() {
        return usersex;
    }

    @Override
    public String toString() {
        return "Usermessag{" +
                "memID='" + userid + '\'' +
                ", name='" + userName + '\'' +
                ", phone='" + userphone + '\'' +
                ", sex='" + usersex + '\'' +
                ", img='" + userimg + '\'' +'}'
                ;
    }
}
