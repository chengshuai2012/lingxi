package com.link.cloud.bean;

/**
 * Created by choan on 2018/7/21.
 */

public class Code_Message {

    /**
     * data : {"uid":"gn1lrhmr7me","numberValue":"15523887372","name":"小霸王大吉大利今晚吃鸡","cabinetNumber":"6","numberType":1}
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
         * uid : gn1lrhmr7me
         * numberValue : 15523887372
         * name : 小霸王大吉大利今晚吃鸡
         * cabinetNumber : 6
         * numberType : 1
         */

        private String uid;
        private String numberValue;
        private String name;
        private String cabinetNumber;
        private int numberType;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNumberValue() {
            return numberValue;
        }

        public void setNumberValue(String numberValue) {
            this.numberValue = numberValue;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCabinetNumber() {
            return cabinetNumber;
        }

        public void setCabinetNumber(String cabinetNumber) {
            this.cabinetNumber = cabinetNumber;
        }

        public int getNumberType() {
            return numberType;
        }

        public void setNumberType(int numberType) {
            this.numberType = numberType;
        }
    }
}
