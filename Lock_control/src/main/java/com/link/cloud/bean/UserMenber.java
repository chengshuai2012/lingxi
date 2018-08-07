package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by 30541 on 2018/2/9.
 */

public class UserMenber extends ResultResponse {
    @SerializedName("type")
    public int Utype;

    @SerializedName("data")
    public ArrayList<UserMessage> data;

    public void setUtype(int utype) {
        Utype = utype;
    }

    public void setData(ArrayList<UserMessage> data) {
        this.data = data;
    }

    public int getUtype() {
        return Utype;
    }

    public ArrayList<UserMessage> getData() {
        return data;
    }
}
