package com.link.cloud.model.impl;

import com.link.cloud.model.IDeviceHelper;

/**
 * Created by Shaozy on 2016/8/10.
 */
public class DeviceHelper implements IDeviceHelper {

    private static final DeviceHelper ourInstance = new DeviceHelper();


    public static DeviceHelper getInstance() {
        return ourInstance;
    }


    private DeviceHelper() {
    }


}
