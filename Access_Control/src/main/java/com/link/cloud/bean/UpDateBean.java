package com.link.cloud.bean;

/**
 * Created by 49488 on 2018/7/12.
 */

public class UpDateBean {

    /**
     * data : {"package_remark":"1111","package_path":"http://devicepackage.oss-cn-shenzhen.aliyuncs.com/20180428/index.html","package_name":"v3.0.1","package_state":"1","package_id":1,"package_ctime":"2018-04-28 16:28:47","package_version":30001,"device_type_id":1}
     * msg : 请求成功
     * status : 0
     */

    private DataBean data;
    private String msg;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * package_remark : 1111
         * package_path : http://devicepackage.oss-cn-shenzhen.aliyuncs.com/20180428/index.html
         * package_name : v3.0.1
         * package_state : 1
         * package_id : 1
         * package_ctime : 2018-04-28 16:28:47
         * package_version : 30001
         * device_type_id : 1
         */

        private String package_remark;
        private String package_path;
        private String package_name;
        private String package_state;
        private int package_id;
        private String package_ctime;
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

        public String getPackage_state() {
            return package_state;
        }

        public void setPackage_state(String package_state) {
            this.package_state = package_state;
        }

        public int getPackage_id() {
            return package_id;
        }

        public void setPackage_id(int package_id) {
            this.package_id = package_id;
        }

        public String getPackage_ctime() {
            return package_ctime;
        }

        public void setPackage_ctime(String package_ctime) {
            this.package_ctime = package_ctime;
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
}
