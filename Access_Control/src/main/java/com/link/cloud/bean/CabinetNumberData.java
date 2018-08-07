package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/3/29.
 */

public class CabinetNumberData extends ResultResponse{
    @SerializedName("data")
    CabinetNumberMessage[] cabinetNumberMessage;

    public void setCabinetNumberMessage(CabinetNumberMessage[] cabinetNumberMessage) {
        this.cabinetNumberMessage = cabinetNumberMessage;
    }

    public CabinetNumberMessage[] getCabinetNumberMessage() {
        return cabinetNumberMessage;
    }
}
