package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shaozy on 2016/8/10.
 */
public class Member extends ResultResponse {
    @SerializedName("data")
    Memberdata memberdata;
    public void setMemberdata(Memberdata memberdata) {
        this.memberdata = memberdata;
    }

    public Memberdata getMemberdata() {
        return memberdata;
    }
}
