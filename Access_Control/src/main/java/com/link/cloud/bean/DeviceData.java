package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/5/9.
 */

public class DeviceData extends ResultResponse{
    @SerializedName("data")
    Devicedmessage deviceData;

    public void setDeviceData(Devicedmessage deviceData) {
        this.deviceData = deviceData;
    }

    public Devicedmessage getDeviceData() {
        return deviceData;
    }

}

