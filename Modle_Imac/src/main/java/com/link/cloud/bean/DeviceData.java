package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/5/9.
 */

public class DeviceData extends ResultResponse{
    @SerializedName("data")
    Devicedmessage deviceData;

    @SerializedName("memberCardInfo")
    MemberCard memberCard;

    public void setDeviceData(Devicedmessage deviceData) {
        this.deviceData = deviceData;
    }

    public Devicedmessage getDeviceData() {
        return deviceData;
    }

    public void setMemberCard(MemberCard memberCard) {
        this.memberCard = memberCard;
    }
    public MemberCard getMemberCard() {
        return memberCard;
    }

}

