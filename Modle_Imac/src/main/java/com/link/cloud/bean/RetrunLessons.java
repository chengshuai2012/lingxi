package com.link.cloud.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 30541 on 2018/3/13.
 */

public class RetrunLessons extends ResultResponse {

    /**
     * data : {"lessonInfo":[{"lessonName":"私教课","lessonId":"643914","cardInfo":[{"cardNo":"2","cardDate":"2018-09-30","cardName":"瑜伽","cardTimes":99,"cardType":"6"}],"lessonDate":"2018-08-15 18:00:00"},{"lessonName":"私教课","lessonId":"643913","cardInfo":[{"cardNo":"2","cardDate":"2018-09-30","cardName":"瑜伽","cardTimes":99,"cardType":"6"}],"lessonDate":"2018-08-15 19:30:00"},{"lessonName":"私教课","lessonId":"643912","cardInfo":[{"cardNo":"2","cardDate":"2018-09-30","cardName":"瑜伽","cardTimes":99,"cardType":"6"}],"lessonDate":"2018-08-15 16:00:00"}],"coach":"测试教练","memberImg":"http://thirdwx.qlogo.cn/mmopen/vi_32/O58rfVDb4iclfXJEb4pr7cK1JtuKlzmibVsHeYmV15K5XPibbpwyYlD0M9Gkd2gHTYZ4ia8SBh50hCDyjfiaOjWTIuA/132","coachImg":"http://thirdwx.qlogo.cn/mmopen/vi_32/vWFt70ADeBb5VhWV34Opcq8QZRFIrNsIFBHXcb4BMfhguxKbTWZmxnNSPibh51OGic8vapdLcpzYC1bPKdbEuuMA/132","memberName":"杨寇"}
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
         * lessonInfo : [{"lessonName":"私教课","lessonId":"643914","cardInfo":[{"cardNo":"2","cardDate":"2018-09-30","cardName":"瑜伽","cardTimes":99,"cardType":"6"}],"lessonDate":"2018-08-15 18:00:00"},{"lessonName":"私教课","lessonId":"643913","cardInfo":[{"cardNo":"2","cardDate":"2018-09-30","cardName":"瑜伽","cardTimes":99,"cardType":"6"}],"lessonDate":"2018-08-15 19:30:00"},{"lessonName":"私教课","lessonId":"643912","cardInfo":[{"cardNo":"2","cardDate":"2018-09-30","cardName":"瑜伽","cardTimes":99,"cardType":"6"}],"lessonDate":"2018-08-15 16:00:00"}]
         * coach : 测试教练
         * memberImg : http://thirdwx.qlogo.cn/mmopen/vi_32/O58rfVDb4iclfXJEb4pr7cK1JtuKlzmibVsHeYmV15K5XPibbpwyYlD0M9Gkd2gHTYZ4ia8SBh50hCDyjfiaOjWTIuA/132
         * coachImg : http://thirdwx.qlogo.cn/mmopen/vi_32/vWFt70ADeBb5VhWV34Opcq8QZRFIrNsIFBHXcb4BMfhguxKbTWZmxnNSPibh51OGic8vapdLcpzYC1bPKdbEuuMA/132
         * memberName : 杨寇
         */

        private String coach;
        private String memberImg;
        private String coachImg;
        private String memberName;
        private ArrayList<LessonInfoBean> lessonInfo;

        public String getCoach() {
            return coach;
        }

        public void setCoach(String coach) {
            this.coach = coach;
        }

        public String getMemberImg() {
            return memberImg;
        }

        public void setMemberImg(String memberImg) {
            this.memberImg = memberImg;
        }

        public String getCoachImg() {
            return coachImg;
        }

        public void setCoachImg(String coachImg) {
            this.coachImg = coachImg;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public ArrayList<LessonInfoBean> getLessonInfo() {
            return lessonInfo;
        }

        public void setLessonInfo(ArrayList<LessonInfoBean> lessonInfo) {
            this.lessonInfo = lessonInfo;
        }

        public static class LessonInfoBean {
            /**
             * lessonName : 私教课
             * lessonId : 643914
             * cardInfo : [{"cardNo":"2","cardDate":"2018-09-30","cardName":"瑜伽","cardTimes":99,"cardType":"6"}]
             * lessonDate : 2018-08-15 18:00:00
             */

            private String lessonName;
            private String lessonId;
            private String lessonDate;
            private List<CardInfoBean> cardInfo;

            public String getLessonName() {
                return lessonName;
            }

            public void setLessonName(String lessonName) {
                this.lessonName = lessonName;
            }

            public String getLessonId() {
                return lessonId;
            }

            public void setLessonId(String lessonId) {
                this.lessonId = lessonId;
            }

            public String getLessonDate() {
                return lessonDate;
            }

            public void setLessonDate(String lessonDate) {
                this.lessonDate = lessonDate;
            }

            public List<CardInfoBean> getCardInfo() {
                return cardInfo;
            }

            public void setCardInfo(List<CardInfoBean> cardInfo) {
                this.cardInfo = cardInfo;
            }

            public static class CardInfoBean {
                /**
                 * cardNo : 2
                 * cardDate : 2018-09-30
                 * cardName : 瑜伽
                 * cardTimes : 99
                 * cardType : 6
                 */

                private String cardNo;
                private String cardDate;
                private String cardName;
                private int cardTimes;
                private String cardType;

                public String getCardNo() {
                    return cardNo;
                }

                public void setCardNo(String cardNo) {
                    this.cardNo = cardNo;
                }

                public String getCardDate() {
                    return cardDate;
                }

                public void setCardDate(String cardDate) {
                    this.cardDate = cardDate;
                }

                public String getCardName() {
                    return cardName;
                }

                public void setCardName(String cardName) {
                    this.cardName = cardName;
                }

                public int getCardTimes() {
                    return cardTimes;
                }

                public void setCardTimes(int cardTimes) {
                    this.cardTimes = cardTimes;
                }

                public String getCardType() {
                    return cardType;
                }

                public void setCardType(String cardType) {
                    this.cardType = cardType;
                }
            }
        }
    }
}
