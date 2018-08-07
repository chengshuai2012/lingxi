package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/3/29.
 */

public class CabinetNumberMessage extends ResultResponse {
    @SerializedName("cabinetLockPlate")
    String cabinetLockPlate;
    @SerializedName("circuitNumber")
    String circuitNumber;
    @SerializedName("cabinetNumber")
    String cabinetNumber;

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
