package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/7/31.
 */

public class LessonResponse extends ResultResponse{
    @SerializedName("memberName")
    public String membername;
    @SerializedName("memberTel")
    public String memberphone;
    @SerializedName("coach")
    public String coach;
//    @SerializedName("lessonName")
//    public String lessonName;
//    @SerializedName("lessonDate")
//    public String lessonDate;
    @SerializedName("lessonInfo")
    public LessonMessage[] lessonInfo;

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getCoach() {
        return coach;
    }

//    public void setLessonName(String lessonName) {
//        this.lessonName = lessonName;
//    }
//
//    public String getLessonName() {
//        return lessonName;
//    }
//
//    public void setLessonDate(String lessonDate) {
//        this.lessonDate = lessonDate;
//    }
//
//    public String getLessonDate() {
//        return lessonDate;
//    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getMembername() {
        return membername;
    }

    public void setMemberphone(String memberphone) {
        this.memberphone = memberphone;
    }

    public String getMemberphone() {
        return memberphone;
    }

    public void setLessonInfo(LessonMessage[] lessonInfo) {
        this.lessonInfo = lessonInfo;
    }

    public LessonMessage[] getLessonInfo() {
        return lessonInfo;
    }

    @Override
    public String toString() {
        return "LessonResponse{"+
                "status:"+status+'\''+
//                ",lessonName:"+lessonName+'\''+
//                ",lessonDate:"+lessonDate+'\''+
                ",lessonInfo"+lessonInfo+'\''+
                ",coach:"+coach+'\''+
                ",memberName:"+membername+'\''+
                ",memberphone"+memberphone+'}';
    }
}
