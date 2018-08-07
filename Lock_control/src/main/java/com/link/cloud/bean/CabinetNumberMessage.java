package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by 30541 on 2018/3/29.
 */

public class CabinetNumberMessage extends RealmObject {
    @SerializedName("cabinetLockPlate")
    String cabinetLockPlate;
    @SerializedName("circuitNumber")
    String circuitNumber;
    @SerializedName("cabinetNumber")
    String cabinetNumber;
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
    public void setCabinetLockPlate(String cabinetLockPlate) {
        this.cabinetLockPlate = cabinetLockPlate;
    }
    public void setCabinetNumber(String cabinetNumber) {
        this.cabinetNumber = cabinetNumber;
    }

    public void setCircuitNumber(String circuitNumber) {
        this.circuitNumber = circuitNumber;
    }

    public String getCabinetLockPlate() {
        return cabinetLockPlate;
    }

    public String getCabinetNumber() {
        return cabinetNumber;
    }

    public String getCircuitNumber() {
        return circuitNumber;
    }
}
