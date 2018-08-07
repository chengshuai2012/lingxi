package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/3/7.
 */

public class Memberdata extends ResultResponse {
    @SerializedName("userInfo")
    UserInfo userInfo;
    @SerializedName("memberCardInfo")
    MemberCard memberCard;

    public void setMemberCard(MemberCard memberCard) {
        this.memberCard = memberCard;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public MemberCard getMemberCard() {
        return memberCard;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
