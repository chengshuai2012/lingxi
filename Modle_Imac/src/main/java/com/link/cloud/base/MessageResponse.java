package com.link.cloud.base;

import com.google.gson.annotations.SerializedName;
import com.link.cloud.bean.ResultResponse;

/**
 * Created by Administrator on 2017/9/28.
 */

public class MessageResponse extends ResultResponse {
    @SerializedName("appid")
    public String appid;
    @SerializedName("eliminateId")
    public String eliminateId;
    @SerializedName("deviceID")
    public String deviceID;
    @SerializedName("memberId")
    public String memberid;
    @SerializedName("memberName")
    public String memberName;
    @SerializedName("coachId")
    public String coachid;
    @SerializedName("coachName")
    public String coachName;
    @SerializedName("lessonName")
    public String lessonName;
    @SerializedName("lessonDate")
    public String lessonDate;
    @SerializedName("memberTel")
    public String memberTel;
    @SerializedName("sendTime")
    public String sendTime;
//    @SerializedName("clerkId")
//    public String clerkId;
//    @SerializedName("clerkName")
//    public String clerkName;

    public void setEliminateId(String eliminateId) {
        this.eliminateId = eliminateId;
    }
    public String getEliminateId() {
        return eliminateId;
    }
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppid() {
        return appid;
    }

    public void setCoachid(String coachid) {
        this.coachid = coachid;
    }

    public void setMemberTel(String memberTel) {
        this.memberTel = memberTel;
    }

    public String getMemberTel() {
        return memberTel;
    }

    public String getCoachid() {
        return coachid;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setLessonDate(String lessonDate) {
        this.lessonDate = lessonDate;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getLessonDate() {
        return lessonDate;
    }

    public String getLessonName() {
        return lessonName;
    }

    public String getMemberid() {
        return memberid;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendTime() {
        return sendTime;
    }

//    public String getClerkId() {
//        return clerkId;
//    }
//
//    public String getClerkName() {
//        return clerkName;
//    }
//
//    public void setClerkId(String clerkId) {
//        this.clerkId = clerkId;
//    }
//
//    public void setClerkName(String clerkName) {
//        this.clerkName = clerkName;
//    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "appid='" + appid + '\'' +
                ", deviceID='" + deviceID + '\'' +
                ", memberId='" + memberid + '\'' +
                ", eliminateId='" + eliminateId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", memberTel='" + memberTel + '\'' +
                ", coachId='" + coachid + '\'' +
                ", coachName='" + coachName + '\'' +
//                ", clerkId='" + clerkId + '\'' +
//                ", clerkName='" + clerkName + '\'' +
                ", lessonName='" + lessonName + '\'' +
                ", lessonDate='" + lessonDate + '\'' +
                ",  sendTme=" +sendTime+'\''+
                '}';
    }
}
