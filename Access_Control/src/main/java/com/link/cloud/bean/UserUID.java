package com.link.cloud.bean;

/**
 * Created by Administrator on 2017/8/3.
 */

public class UserUID extends ResultResponse{
    String memberUID;
    String coachUID;
    String checkUID;

    public void setMemberUID(String memberUID) {
        this.memberUID = memberUID;
    }

    public String getMemberUID() {
        return memberUID;
    }

    public void setCoachUID(String coachUID) {
        this.coachUID = coachUID;
    }

    public String getCoachUID() {
        return coachUID;
    }

    public void setCheckUID(String checkUID) {
        this.checkUID = checkUID;
    }

    public String getCheckUID() {
        return checkUID;
    }

    @Override
    public String toString() {
        return "UserUID{"+
                ",memberUID:"+'\''+
                ",coachUID:"+'\''+
                ",checkUID:"+'\''+"}";
    }
}
