package com.link.cloud.bean;

import java.util.List;

/**
 * Created by 49488 on 2018/7/31.
 */

public class SyncUserFace {

    /**
     * data : [{"uid":"w5r4fa4pw5h","faceUrl":"http://devicepackage.oss-cn-shenzhen.aliyuncs.com/2018072921303615966666666.jpg","faceId":4124},{"uid":"336a62msnqq","faceUrl":"http://devicepackage.oss-cn-shenzhen.aliyuncs.com/2018073010053317665222565.data","faceId":4128}]
     * msg : 请求成功
     * status : 0
     */

    private String msg;
    private int status;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : w5r4fa4pw5h
         * faceUrl : http://devicepackage.oss-cn-shenzhen.aliyuncs.com/2018072921303615966666666.jpg
         * faceId : 4124
         */

        private String uid;
        private String faceUrl;
        private int faceId;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getFaceUrl() {
            return faceUrl;
        }

        public void setFaceUrl(String faceUrl) {
            this.faceUrl = faceUrl;
        }

        public int getFaceId() {
            return faceId;
        }

        public void setFaceId(int faceId) {
            this.faceId = faceId;
        }
    }
}
