package com.link.cloud.utils;
import android.content.SharedPreferences;
import android.util.Log;

import com.link.cloud.BaseApplication;
import com.link.cloud.activity.LockActivity;
import com.link.cloud.bean.Person;
import com.orhanobut.logger.Logger;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import md.com.sdk.MicroFingerVein;

import static com.alibaba.sdk.android.ams.common.util.HexUtil.hexStringToByte;
/**
 * Created by 30541 on 2018/6/20.
 */
public class Finger_identify {
    final static float IDENTIFY_SCORE_THRESHOLD=0.60f;
    private static List<Person> people = new ArrayList<>();
    public static String TAG = "Finger_identify";
    public static String Finger_identify (LockActivity activty, byte[] img){
        int[]pos=new int[1];
        float[]score=new float[1];
        boolean identifyResult=false;
        if(((BaseApplication) activty.getApplicationContext().getApplicationContext()).getPerson().size()!=people.size()){
            people.clear();
            people.addAll(((BaseApplication) activty.getApplicationContext().getApplicationContext()).getPerson());
        }
        String [] uidss= new String[people.size()];
        Log.e(TAG, "identifyNewImg: "+uidss.length );
        StringBuilder builder = new StringBuilder();
        int y =0;
        while (y< people.size()/1000+1&&!identifyResult){
            if(y< people.size()/1000){
                for(int x=y*1000;x<(y+1)*1000;x++){
                    builder.append(people.get(x).getFeature());
                    uidss[x]= people.get(x).getUid();

                }
            }else {
                for(int x = y*1000; x< people.size(); x++){
                    builder.append(people.get(x).getFeature());
                    uidss[x]= people.get(x).getUid();

                }
            }

            byte[] allFeaturesBytes=hexStringToByte(builder.toString());
            builder.delete(0,builder.length());
            //比对是否通过
            identifyResult = MicroFingerVein.fv_index(allFeaturesBytes,allFeaturesBytes.length/3352,img,pos,score);
            identifyResult = identifyResult &&score[0]>IDENTIFY_SCORE_THRESHOLD;//得分是否达标
            y++;
        }
        if(identifyResult){//比对通过且得分达标时打印此手指绑定的用户名
            String featureName = uidss[(y-1)*1000+pos[0]];
            return  featureName;
        }else {
            if(y== people.size()/1000+1){
                return null;
            }

        }
        return null;
    }




}
