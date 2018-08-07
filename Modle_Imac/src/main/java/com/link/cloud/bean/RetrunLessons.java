package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/3/13.
 */

public class RetrunLessons extends ResultResponse {
    @SerializedName("data")
    LessonResponse lessonResponse;

    public LessonResponse getLessonResponse() {
        return lessonResponse;
    }

    public void setLessonResponse(LessonResponse lessonResponse) {
        this.lessonResponse = lessonResponse;
    }
}
