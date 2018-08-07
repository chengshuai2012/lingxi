package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/6/25.
 */

public class ResultHeartBeat{
    @SerializedName("data")
    Resultdata resultdata;

    public void setResultdata(Resultdata resultdata) {
        this.resultdata = resultdata;
    }

    public Resultdata getResultdata() {
        return resultdata;
    }


    class Resultdata extends ResultResponse{
        @SerializedName("userCount")
        String count;

        public void setCount(String count) {
            this.count = count;
        }

        public String getCount() {
            return count;
        }
    }
}
