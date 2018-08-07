package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/3/14.
 */

public class Isopenmessage extends ResultResponse {
    @SerializedName("data")
    Isopendata isopendata;

    public void setIsopendata(Isopendata isopendata) {
        this.isopendata = isopendata;
    }

    public Isopendata getIsopendata() {
        return isopendata;
    }
}
