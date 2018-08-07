package com.link.cloud.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import com.link.cloud.BaseApplication;
import com.link.cloud.activity.LockActivity;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.orhanobut.logger.Logger;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.alibaba.sdk.android.ams.common.util.HexUtil.bytesToHexString;
import static com.alibaba.sdk.android.ams.common.util.HexUtil.hexStringToByte;
/**
 * Created by 30541 on 2018/6/20.
 */
public class Finger_identify {
    final static float IDENTIFY_SCORE_THRESHOLD=0.63f;
    public static String Finger_identify (LockActivity activty, byte[] img){
        SendLogMessageTastContract sendLogMessageTastContract;
        int[]pos=new int[1];
        float[]score=new float[1];
        int i=0;
        Cursor cursor;
        String sql;
        sql = "select UID,FEATURE from PERSON where UID in(select Uid from SIGN_USER)" ;
        cursor = BaseApplication.getInstances().getDaoSession().getDatabase().rawQuery(sql,null);
        byte[][] feature=new byte[cursor.getCount()][];
        String [] Uids=new String[cursor.getCount()];
        Logger.e("finger_identify"+"cursor.getCount()"+cursor.getCount());
        while (cursor.moveToNext()){
            int nameColumnIndex = cursor.getColumnIndex("FEATURE");
            String strValue=cursor.getString(nameColumnIndex);
            feature[i]=hexStringToByte(strValue);
            Uids[i]=cursor.getString(cursor.getColumnIndex("UID"));
            i++;
        }
        cursor.close();
        int len = 0;
        // 计算一维数组长度
        if(feature.length>0) {
            for (byte[] element : feature) {
                len += element.length;
            }
            // 复制元素
            byte[]  nFeatuer = new byte[len];
            int index = 0;
            for (byte[] element : feature) {
                for (byte element2 : element) {
                    nFeatuer[index++] = element2;
                }
            }
            boolean  identifyResult = activty.microFingerVein.fv_index(nFeatuer, nFeatuer.length / 3352, img, pos, score);//比对是否通过
            identifyResult = identifyResult && score[0] > IDENTIFY_SCORE_THRESHOLD;//得分是否达标
            if (score[0]<IDENTIFY_SCORE_THRESHOLD){
                i=0;
                Cursor cursor1;
                sql="select UID,FEATURE from  PERSON";
                cursor1 = BaseApplication.getInstances().getDaoSession().getDatabase().rawQuery(sql,null);
                Logger.e("finger_identify"+"cursor1.getCount()"+cursor1.getCount());
                feature=new byte[cursor1.getCount()][];
                Uids=new String[cursor1.getCount()];
                while (cursor1.moveToNext()){
                    int nameColumnIndex = cursor1.getColumnIndex("FEATURE");
                    String strValue=cursor1.getString(nameColumnIndex);
                    feature[i]=hexStringToByte(strValue);
                    Uids[i]=cursor1.getString(cursor1.getColumnIndex("UID"));
                    i++;
                }
                cursor1.close();
                len = 0;
                // 计算一维数组长度
                if(feature.length>0) {
                    for (byte[] element : feature) {
                        len += element.length;
                    }
                    // 复制元素
                    nFeatuer = new byte[len];
                    index = 0;
                    for (byte[] element : feature) {
                        for (byte element2 : element) {
                            nFeatuer[index++] = element2;
                        }
                    }
                    Logger.e("finger_identify"+"nFeatuer.length"+nFeatuer.length);
                    identifyResult = activty.microFingerVein.fv_index(nFeatuer, nFeatuer.length / 3352, img, pos, score);//比对是否通过
                    identifyResult = identifyResult && score[0] > IDENTIFY_SCORE_THRESHOLD;//得分是否达标
                    Logger.e("finger_identify"+"pos"+pos[0]+"score"+score[0]);
                }
            }
            DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SharedPreferences userinfo=activty.getSharedPreferences("user_info",0);
            String deviceId=userinfo.getString("deviceId","");
            String Uid = Uids[pos[0]];
            String uids= StringUtils.join(Uids,",");
            String strBeginDate = dateTimeformat.format(new Date());
            if (identifyResult) {
                Logger.e("SignActivity"+"pos="+pos+"score="+score+"strBeginDate:"+strBeginDate);
                activty.sendLogMessageTastContract.sendLog(deviceId,Uid,uids,bytesToHexString(img),strBeginDate,score[0]+"","验证成功");
                return Uid;
            }else {
                activty.sendLogMessageTastContract.sendLog(deviceId,null,uids,bytesToHexString(img),strBeginDate,score[0]+"","验证失败");
                return null;
            }
        }else {
            boolean identifyResult=false;
            i=0;
            Cursor cursor1;
            sql="select UID,FEATURE from  PERSON";
            cursor1 = BaseApplication.getInstances().getDaoSession().getDatabase().rawQuery(sql,null);
            Logger.e("finger_identify"+"cursor1.getCount()"+cursor1.getCount());
            feature=new byte[cursor1.getCount()][];
            Uids=new String[cursor1.getCount()];
            while (cursor1.moveToNext()){
                int nameColumnIndex = cursor1.getColumnIndex("FEATURE");
                String strValue=cursor1.getString(nameColumnIndex);
                feature[i]=hexStringToByte(strValue);
                Uids[i]=cursor1.getString(cursor1.getColumnIndex("UID"));
                i++;
            }
            cursor1.close();
            len = 0;
            // 计算一维数组长度
            if(feature.length>0) {
                for (byte[] element : feature) {
                    len += element.length;
                }
                // 复制元素
                byte[] nFeatuer = new byte[len];
                int index = 0;
                for (byte[] element : feature) {
                    for (byte element2 : element) {
                        nFeatuer[index++] = element2;
                    }
                }
                Logger.e("finger_identify"+"nFeatuer.length"+nFeatuer.length);
                identifyResult = activty.microFingerVein.fv_index(nFeatuer, nFeatuer.length / 3352, img, pos, score);//比对是否通过
                identifyResult = identifyResult && score[0] > IDENTIFY_SCORE_THRESHOLD;//得分是否达标
                Logger.e("finger_identify"+"pos"+pos[0]+"score"+score[0]);
            }
            DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SharedPreferences userinfo=activty.getSharedPreferences("user_info",0);
            String deviceId=userinfo.getString("deviceId","");
            String Uid = Uids[pos[0]];
            String uids= StringUtils.join(Uids,",");
            String strBeginDate = dateTimeformat.format(new Date());
            if (identifyResult) {

                Logger.e("SignActivity"+"pos="+pos+"score="+score);
                activty.sendLogMessageTastContract.sendLog(deviceId,Uid,uids,bytesToHexString(img),strBeginDate,score[0]+"","验证成功");
                return Uid;
            }else {
                activty.sendLogMessageTastContract.sendLog(deviceId,null,uids,bytesToHexString(img),strBeginDate,score[0]+"","验证失败");
                return null;
            }
        }
    }
}
