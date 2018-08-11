package com.link.cloud.utils;
import android.content.SharedPreferences;

import com.link.cloud.BaseApplication;
import com.link.cloud.activity.LockActivity;
import com.link.cloud.greendaodemo.Person;
import com.orhanobut.logger.Logger;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import md.com.sdk.MicroFingerVein;

import static com.alibaba.sdk.android.ams.common.util.HexUtil.bytesToHexString;
import static com.alibaba.sdk.android.ams.common.util.HexUtil.hexStringToByte;
/**
 * Created by 30541 on 2018/6/20.
 */
public class Finger_identify {
    final static float IDENTIFY_SCORE_THRESHOLD=0.63f;
    public static String Finger_identify (LockActivity activty, byte[] img){
        int[]pos=new int[1];
        float[]score=new float[1];
        List<Person> people = BaseApplication.getInstances().getDaoSession().getPersonDao().loadAll();
        String [] uidss= new String[people.size()];
        StringBuilder builder = new StringBuilder();
        for(int x=0;x<people.size();x++){
            builder.append(people.get(x).getFeature());
            uidss[x]=people.get(x).getUid();
        }
        byte[] allFeaturesBytes=hexStringToByte(builder.toString());
        SharedPreferences userinfo=activty.getSharedPreferences("user_info",0);
        String deviceId=userinfo.getString("deviceId","");
        boolean identifyResult= MicroFingerVein.fv_index(allFeaturesBytes,people.size(),img,pos,score);//比对是否通过
        identifyResult=identifyResult&&score[0]>IDENTIFY_SCORE_THRESHOLD;//得分是否达标
        DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTimeformat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String strBeginDate = dateTimeformat.format(new Date());
            if (identifyResult) {
                String featureName = uidss[pos[0]];
                Logger.e("SignActivity"+"pos="+pos+"score="+score);
                activty.sendLogMessageTastContract.sendLog(deviceId,featureName,StringUtils.join(uidss,","),bytesToHexString(img),strBeginDate,score[0]+"","验证成功");
                return featureName;
            }else {
                activty.sendLogMessageTastContract.sendLog(deviceId,null,StringUtils.join(uidss,","),bytesToHexString(img),strBeginDate,score[0]+"","验证失败");
                return null;
            }
        }

}
