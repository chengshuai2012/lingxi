package com.link.cloud.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.link.cloud.BaseApplication;
import com.link.cloud.activity.LockActivity;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.greendao.gen.PersonDao;
import com.link.cloud.greendaodemo.Person;
import com.orhanobut.logger.Logger;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
        HashMap<String,byte[]> map = new HashMap<>();
        List<Person> people = BaseApplication.getInstances().getDaoSession().getPersonDao().loadAll();
        for (int x=0;x<people.size();x++){
           map.put(people.get(x).getUid(),hexStringToByte(people.get(x).getFeature()));
        }

        byte[] allFeaturesBytes=new byte[0];
        List<byte[]> allFeatureList=new ArrayList<byte[]>(map.values());
        for(byte[] feature:allFeatureList){
            allFeaturesBytes= CommonUtils.byteMerger(allFeaturesBytes,feature);
        }
        SharedPreferences userinfo=activty.getSharedPreferences("user_info",0);
        String deviceId=userinfo.getString("deviceId","");
        boolean identifyResult= MicroFingerVein.fv_index(allFeaturesBytes,allFeatureList.size(),img,pos,score);//比对是否通过
        identifyResult=identifyResult&&score[0]>IDENTIFY_SCORE_THRESHOLD;//得分是否达标
        DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTimeformat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String strBeginDate = dateTimeformat.format(new Date());
            if (identifyResult) {
                String featureName = (String) map.keySet().toArray()[pos[0]];
                Logger.e("SignActivity"+"pos="+pos+"score="+score);
                activty.sendLogMessageTastContract.sendLog(deviceId,featureName,StringUtils.join(map.keySet().toArray()),bytesToHexString(img),strBeginDate,score[0]+"","验证成功");
                return featureName;
            }else {
                activty.sendLogMessageTastContract.sendLog(deviceId,null,StringUtils.join(map.keySet().toArray()),bytesToHexString(img),strBeginDate,score[0]+"","验证失败");
                return null;
            }
        }

}
