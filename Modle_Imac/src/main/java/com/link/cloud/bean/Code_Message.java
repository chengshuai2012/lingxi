package com.link.cloud.bean;

import java.util.List;

/**
 * Created by choan on 2018/7/26.
 */

public class Code_Message {
    /**
     * data : {"userInfo":{"uid":"","sex":0,"phone":"18610933991","name":"秦永波","img":"https://imagecdn.rocketbird.cn/test/image/9b24661e4001798aafbc9b638e3b8547.png@184w_184h_90q_1pr","userType":0},"memberCardInfo":[{"cardName":"会员卡一","cardNumber":"XXXXXXX","beginTime":"2018-01-01","endTime":"2018-12-01"},{"cardName":"会员卡二","cardNumber":"XXXXXXX","beginTime":"2018-01-01","endTime":"2018-12-01"}]}
     * type : 请求成功
     * status : 0
     */
    private DataBean data;
    private String type;
    private int status;
    public DataBean getData() {
        return data;
    }
    public void setData(DataBean data) {
        this.data = data;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public static class DataBean {
        /**
         * userInfo : {"uid":"","sex":0,"phone":"18610933991","name":"秦永波","img":"https://imagecdn.rocketbird.cn/test/image/9b24661e4001798aafbc9b638e3b8547.png@184w_184h_90q_1pr","userType":0}
         * memberCardInfo : [{"cardName":"会员卡一","cardNumber":"XXXXXXX","beginTime":"2018-01-01","endTime":"2018-12-01"},{"cardName":"会员卡二","cardNumber":"XXXXXXX","beginTime":"2018-01-01","endTime":"2018-12-01"}]
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
             * uid :
             * sex : 0
             * phone : 18610933991
             * name : 秦永波
             * img : https://imagecdn.rocketbird.cn/test/image/9b24661e4001798aafbc9b638e3b8547.png@184w_184h_90q_1pr
             * userType : 0
             */

            private String uid;
            private int sex;
            private String phone;
            private String name;
            private String img;
            private int userType;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

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
             * cardName : 会员卡一
             * cardNumber : XXXXXXX
             * beginTime : 2018-01-01
             * endTime : 2018-12-01
             */

            private String cardName;
            private String cardNumber;
            private String beginTime;
            private String endTime;

            public String getCardName() {
                return cardName;
            }

            public void setCardName(String cardName) {
                this.cardName = cardName;
            }

            public String getCardNumber() {
                return cardNumber;
            }

            public void setCardNumber(String cardNumber) {
                this.cardNumber = cardNumber;
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
        }
    }
}
