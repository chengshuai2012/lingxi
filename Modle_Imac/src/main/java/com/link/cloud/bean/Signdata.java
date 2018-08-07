package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;
import com.link.cloud.bean.MemberCard;
import com.link.cloud.bean.ResultResponse;
import com.link.cloud.bean.UserInfo;

/**
 * Created by 30541 on 2018/3/12.
 */

public class Signdata extends ResultResponse {
    @SerializedName("userInfo")
    UserInfo userInfo;
    @SerializedName("memberCardInfo")
    MemberCard memberCard;

    public MemberCard getMemberCard() {
        return memberCard;
    }

    public void setMemberCard(MemberCard memberCard) {
        this.memberCard = memberCard;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
