package com.link.cloud.greendaodemo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 30541 on 2018/6/30.
 */
@Entity
public class DeviceId {
    @Id
    Long Id;
    String deviceid;

    @Generated(hash = 913719801)
    public DeviceId(Long Id, String deviceid) {
        this.Id = Id;
        this.deviceid = deviceid;
    }

    @Generated(hash = 1942739866)
    public DeviceId() {
    }

    public Long getId() {
        return Id;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
}
