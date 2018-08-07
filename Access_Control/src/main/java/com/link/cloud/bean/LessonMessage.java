package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/20.
 */

public class LessonMessage extends ResultResponse{
    @SerializedName("lessonId")
    public String lessonId;
    @SerializedName("lessonName")
    public String lessonName;
    @SerializedName("lessonDate")
    public String lessonDate;

    public ArrayList<CardInfo> cardInfos;
    public void setLessonDate(String lessonDate) {
        this.lessonDate = lessonDate;
    }

    public String getLessonDate() {
        return lessonDate;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getLessonName() {
        return lessonName;
    }

    @Override
    public String toString() {
        return "LessonMessage{" +
                "lessonId='" + lessonId + '\'' +
                ", lessonName='" + lessonName + '\'' +
                ", lessonDate='" + lessonDate + '\'' +
                '}';
    }
}
