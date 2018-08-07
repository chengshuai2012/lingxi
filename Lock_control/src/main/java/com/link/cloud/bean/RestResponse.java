package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by rj1 on 2016/8/26.
 */
public class RestResponse implements Serializable {
    @SerializedName("statusCode")
    public String statusCode;

    @SerializedName("templateSMS")
    public HashMap<String, String> templateSMS;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public HashMap getTemplateSMS() {
        return templateSMS;
    }

    public void setTemplateSMS(HashMap templateSMS) {
        this.templateSMS = templateSMS;
    }
}
