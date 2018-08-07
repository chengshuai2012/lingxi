package com.link.cloud.bean;

/**
 * Created by choan on 2018/7/17.
 */

public class Code_Message extends ResultResponse{

    /**
     * data : {"uid":"gn1lrhmr7me","numberValue":"15523887372","name":"小霸王大吉大利今晚吃鸡","numberType":1,"userType":0}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : gn1lrhmr7me
         * numberValue : 15523887372
         * name : 小霸王大吉大利今晚吃鸡
         * numberType : 1
         * userType : 0
         */

        private String uid;
        private String numberValue;
        private String name;
        private int numberType;
        private int userType;

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

        public int getNumberType() {
            return numberType;
        }

        public void setNumberType(int numberType) {
            this.numberType = numberType;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }
    }
}
