package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Shaozy on 2016/8/14.
 */
public class ResultResponse implements Serializable {
    @SerializedName("status")
    public int status;
    @SerializedName("msg")
    public String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
