package com.link.cloud.bean;

/**
 * Created by Administrator on 2017/9/11.
 */

public class UpdateMessage extends ResultResponse {
    String fileName;
    String version;
    String url;
    String remark;
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
