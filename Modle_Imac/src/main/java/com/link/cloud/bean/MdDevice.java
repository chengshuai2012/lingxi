package com.link.cloud.bean;

import java.io.Serializable;

public class MdDevice implements Serializable{
    private String deviceName;
    private int deviceNo;
    private int deviceIndex;

    public MdDevice(){
        this.deviceName="MdDevice";
        this.deviceNo=0;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName=deviceName;
    }
    public String getDeviceName(){
        return deviceName;
    }

    public void setDeviceNo(int deviceNo){
        this.deviceNo=deviceNo;
    }
    public int getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceIndex(int deviceIndex) {
        this.deviceIndex=deviceIndex;
    }
    public int getDeviceIndex(){
        return this.deviceIndex;
    }
}
