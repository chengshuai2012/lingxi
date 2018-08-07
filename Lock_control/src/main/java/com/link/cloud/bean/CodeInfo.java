package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/8/1.
 */

public class CodeInfo extends  ResultResponse{
    @SerializedName("codeInfo")
    public String codeInfo;

    public void setCodeInfo(String codeInfo) {
        this.codeInfo = codeInfo;
    }

    @Override
    public String getMsg() {
        return super.getMsg();
    }

    @Override
    public String toString() {
        return "SignedCodeInfo{"+
                "status:"+status+'\''+
                ",codeInfo:"+codeInfo+'}';
    }
}
