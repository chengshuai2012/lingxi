package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/3/8.
 */

public class Lockdata extends ResultResponse {
    @SerializedName("data")
    User_Lessage_Lock lockdata;
    public User_Lessage_Lock getLockdata() {
        return lockdata;
    }
    public void setLockdata(User_Lessage_Lock lockdata) {
        this.lockdata = lockdata;
    }
}
