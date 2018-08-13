package com.link.cloud.bean;

import io.realm.RealmObject;

/**
 * Created by choan on 2018/8/4.
 */

public class SignUser extends RealmObject{
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
