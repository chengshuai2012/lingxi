package com.link.cloud.bean;


import io.realm.Realm;
import io.realm.RealmObject;


public class CabinetRecord extends RealmObject{
    String memberName;
    String phoneNum;
    String cabinetStating;
    String isUsed;
    String exist;
    String opentime;
    String cabinetNumber;

    public CabinetRecord() {
    }
    public void setCabinetNumber(String cabinetNumber) {
        this.cabinetNumber = cabinetNumber;
    }

    public void setCabinetStating(String cabinetStating) {
        this.cabinetStating = cabinetStating;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCabinetNumber() {
        return cabinetNumber;
    }

    public String getCabinetStating() {
        return cabinetStating;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getOpentime() {
        return opentime;
    }

    public String getPhoneNum() {
        return phoneNum;
    }
    public String getExist() {
        return this.exist;
    }
    public void setExist(String exist) {
        this.exist = exist;
    }
    public String getIsUsed() {
        return this.isUsed;
    }
    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }
}
