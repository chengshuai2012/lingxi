package com.link.cloud.base;

import android.content.Context;

import com.link.cloud.BaseApplication;

/**
 * Created by Shaozy on 2016/8/10.
 */
public class DeviceApi {
    private static DeviceApi ourInstance;
    private static Context context = BaseApplication.getInstance().getApplicationContext();
    public static DeviceApi getInstance() {
        if (ourInstance == null) ourInstance = new DeviceApi();
        return ourInstance;
    }
    private DeviceApi() {
    }

}
