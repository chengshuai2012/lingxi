package com.link.cloud.bean;


import java.util.List;

public class FaceBindBean {


    /**
     * data : {"memberCardInfo":[{"cardName":"一年卡","beginTime":"2018-07-11","endTime":"2019-07-11","cardNumber":"1531296023540"}],"userInfo":{"sex":0,"phone":"17673467282","name":"测试会员","img":"http://niulang-v2.oss-cn-beijing.aliyuncs.com/2018-07-11/gym5b45b92df36e31531296045.png","userType":1}}
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
         * memberCardInfo : [{"cardName":"一年卡","beginTime":"2018-07-11","endTime":"2019-07-11","cardNumber":"1531296023540"}]
         * userInfo : {"sex":0,"phone":"17673467282","name":"测试会员","img":"http://niulang-v2.oss-cn-beijing.aliyuncs.com/2018-07-11/gym5b45b92df36e31531296045.png","userType":1}
         */

        private UserInfoBean userInfo;
        private List<MemberCardInfoBean> memberCardInfo;

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public List<MemberCardInfoBean> getMemberCardInfo() {
            return memberCardInfo;
        }

        public void setMemberCardInfo(List<MemberCardInfoBean> memberCardInfo) {
            this.memberCardInfo = memberCardInfo;
        }

        public static class UserInfoBean {
            /**
             * sex : 0
             * phone : 17673467282
             * name : 测试会员
             * img : http://niulang-v2.oss-cn-beijing.aliyuncs.com/2018-07-11/gym5b45b92df36e31531296045.png
             * userType : 1
             */

            private int sex;
            private String phone;
            private String name;
            private String img;
            private int userType;

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }
        }

        public static class MemberCardInfoBean {
            /**
             * cardName : 一年卡
             * beginTime : 2018-07-11
             * endTime : 2019-07-11
             * cardNumber : 1531296023540
             */

            private String cardName;
            private String beginTime;
            private String endTime;
            private String cardNumber;

            public String getCardName() {
                return cardName;
            }

            public void setCardName(String cardName) {
                this.cardName = cardName;
            }

            public String getBeginTime() {
                return beginTime;
            }

            public void setBeginTime(String beginTime) {
                this.beginTime = beginTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getCardNumber() {
                return cardNumber;
            }

            public void setCardNumber(String cardNumber) {
                this.cardNumber = cardNumber;
            }
        }
    }
}
