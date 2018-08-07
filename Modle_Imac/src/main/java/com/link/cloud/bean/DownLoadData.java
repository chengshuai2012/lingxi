package com.link.cloud.bean;

import com.link.cloud.greendaodemo.Person;

import java.util.List;

/**
 * Created by OFX002 on 2018/7/19.
 */

public class DownLoadData {

    /**
     * data : [{"uid":"ncmchbv9n58","shopId":"0xj13ti_0000zd","userName":"123456","appid":"0xj13ti","feature":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","fingerId":6,"deviceId":"pmljt8z"},{"uid":"9e58mq89n74","shopId":"0xj13ti_0000zd","userName":"君君呀","appid":"0xj13ti","feature":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","fingerId":16,"deviceId":"pmljt8z"}]
     * msg : 请求成功
     * status : 0
     */

    private String msg;
    private int status;
    private List<Person> data;

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

    public List<Person> getData() {
        return data;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : ncmchbv9n58
         * shopId : 0xj13ti_0000zd
         * userName : 123456
         * appid : 0xj13ti
         * feature : aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
         * fingerId : 6
         * deviceId : pmljt8z
         */

        private String uid;
        private String shopId;
        private String userName;
        private String appid;
        private String feature;
        private int fingerId;
        private String deviceId;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public int getFingerId() {
            return fingerId;
        }

        public void setFingerId(int fingerId) {
            this.fingerId = fingerId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }
}
