package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/11.
 */

public class UpdateMessage extends ResultResponse {
    @SerializedName("fileName")
    String fileName;
    @SerializedName("version")
    String version;
    @SerializedName("url")
    String url;
    @SerializedName("remark")
    String remark;
    @SerializedName("createTime")
    String createTime;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "UpdateMessage{" +
                "fileName='" + fileName + '\'' +
                ", version='" + version + '\'' +
                ", url='" + url + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime='" + createTime+
                '}';
    }
}
