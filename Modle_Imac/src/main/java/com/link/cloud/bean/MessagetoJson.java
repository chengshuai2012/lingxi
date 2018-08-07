package com.link.cloud.bean;

import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by 30541 on 2018/3/6.
 */

public class MessagetoJson extends ResultResponse {
    String jsonObject;

    public void setJsonObject(String jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getJsonObject() {
        return jsonObject;
    }
}
