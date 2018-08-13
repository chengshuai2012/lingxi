package com.link.cloud.utils;



import android.Manifest;

import android.app.Activity;

import android.content.pm.PackageManager;

import android.support.v4.app.ActivityCompat;



/**

 *  安卓7.0及以上设备需要动态的文件访问授权，否则无法存取数据，安卓7.0以下设备可去掉此动态授权模块；

 */

public class PermissionUtils {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {

            Manifest.permission.READ_EXTERNAL_STORAGE,

            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };



    /**

     * Checks if the app has permission to write to device storage

     * If the app does not has permission then the user will be prompted to

     * grant permissions

     */

    public static void verifyStoragePermissions(Activity activity) {

        int permission= ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission!=PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);

        }

    }

}