package com.link.cloud.bean;

/**
 * Created by 49488 on 2018/8/2.
 */

public class PushUpDateBean {

    /**
     * package_remark : 优化了验证速度
     * package_path : http://devicepackage.oss-cn-shenzhen.aliyuncs.com/20180802/智能一体机2.2.9.4.apk
     * package_name : 智能一体机2.2.9.4
     * package_ctime : 2018-08-02 12:00:00
     * messageId : 20180802155151-1
     * type : 4
     * package_version : 229
     * device_type_id : 1
     */

    private String package_remark;
    private String package_path;
    private String package_name;
    private String package_ctime;
    private String messageId;
    private String type;
    private int package_version;
    private int device_type_id;

    public String getPackage_remark() {
        return package_remark;
    }

    public void setPackage_remark(String package_remark) {
        this.package_remark = package_remark;
    }

    public String getPackage_path() {
        return package_path;
    }

    public void setPackage_path(String package_path) {
        this.package_path = package_path;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_ctime() {
        return package_ctime;
    }

    public void setPackage_ctime(String package_ctime) {
        this.package_ctime = package_ctime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPackage_version() {
        return package_version;
    }

    public void setPackage_version(int package_version) {
        this.package_version = package_version;
    }

    public int getDevice_type_id() {
        return device_type_id;
    }

    public void setDevice_type_id(int device_type_id) {
        this.device_type_id = device_type_id;
    }
}
