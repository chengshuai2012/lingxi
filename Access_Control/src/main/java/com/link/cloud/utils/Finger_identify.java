package com.link.cloud.utils;
import android.content.SharedPreferences;
import com.link.cloud.activity.LockActivity;
import com.link.cloud.bean.Person;
import com.orhanobut.logger.Logger;
import org.apache.commons.lang.StringUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;
import md.com.sdk.MicroFingerVein;

import static com.alibaba.sdk.android.ams.common.util.HexUtil.bytesToHexString;
import static com.alibaba.sdk.android.ams.common.util.HexUtil.hexStringToByte;
/**
 * Created by 30541 on 2018/6/20.
 */
public class Finger_identify {
    final static float IDENTIFY_SCORE_THRESHOLD=0.63f;

    public static String Finger_identify (LockActivity activty, byte[] img,Realm realm){
        int[]pos=new int[1];
        float[]score=new float[1];
        List<Person> people =realm.copyToRealm(realm.where(Person.class).findAll());
        String [] uidss= new String[people.size()];
        StringBuilder builder = new StringBuilder();
        for(int x=0;x<people.size();x++){
            builder.append(people.get(x).getFeature());
            uidss[x]=people.get(x).getUid();
        }
        realm.close();
        people=null;
        byte[] allFeaturesBytes=hexStringToByte(builder.toString());
        boolean identifyResult= MicroFingerVein.fv_index(allFeaturesBytes,uidss.length,img,pos,score);//比对是
        SharedPreferences userinfo=activty.getSharedPreferences("user_info",0);
        String deviceId=userinfo.getString("deviceId","");
        identifyResult=identifyResult&&score[0]>IDENTIFY_SCORE_THRESHOLD;//得分是否达标
        DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTimeformat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String strBeginDate = dateTimeformat.format(new Date());

            if (identifyResult) {
                String featureName = uidss[pos[0]];
                Logger.e("SignActivity"+"pos="+pos+"score="+score);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        activty.sendLogMessageTastContract.sendLog(deviceId,featureName,StringUtils.join(uidss,","),bytesToHexString(img),strBeginDate,score[0]+"","验证成功");
                    }
                }).start();

                return featureName;
            }else {


                return null;
            }
        }

}
