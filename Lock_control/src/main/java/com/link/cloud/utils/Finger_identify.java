package com.link.cloud.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.link.cloud.BaseApplication;
import com.link.cloud.activity.LockActivity;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.Person;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.bean.SignUser;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.orhanobut.logger.Logger;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.alibaba.sdk.android.ams.common.util.HexUtil.bytesToHexString;
import static com.alibaba.sdk.android.ams.common.util.HexUtil.hexStringToByte;
/**
 * Created by 30541 on 2018/6/20.
 */
public class Finger_identify {
   final static float IDENTIFY_SCORE_THRESHOLD=0.63f;
    private byte[] nFeatuer;

    public static String Finger_identify (LockActivity activty, byte[] img){
       int[]pos=new int[1];
       float[]score=new float[1];
        boolean identifyResult=false;
        byte[]  nFeatuer;
       RealmResults<SignUser> uid = Realm.getDefaultInstance().where(SignUser.class).findAll();
       String [] uids= new String[uid.size()];
       for(int x=0;x<uid.size();x++){
           uids[x]=uid.get(x).getUid();
       }
        List<Person> persons= new ArrayList<>();
       if(uids.length!=0){
          persons= Realm.getDefaultInstance().where(Person.class).in("uid", uids).findAll();
       }

       byte[][] feature=new byte[persons.size()][];
       String [] Uids=new String[persons.size()];
       Logger.e("finger_identify"+"cursor.getCount()"+persons.size());
       for(int x=0;x<persons.size();x++){
           feature[x]=hexStringToByte(persons.get(x).getFeature());
           Uids[x]=persons.get(x).getUid();
       }
       int len = 0;
       // 计算一维数组长度
       if(feature.length>0) {
           for (byte[] element : feature) {
               len += element.length;
           }
           // 复制元素
            nFeatuer = new byte[len];
           int index = 0;
           for (byte[] element : feature) {
               for (byte element2 : element) {
                   nFeatuer[index++] = element2;
               }
           }
            identifyResult = activty.microFingerVein.fv_index(nFeatuer, nFeatuer.length / 3352, img, pos, score);//比对是否通过
           identifyResult = identifyResult && score[0] > IDENTIFY_SCORE_THRESHOLD;//得分是否达标
           if (score[0]<IDENTIFY_SCORE_THRESHOLD){
               List<Person> personss= Realm.getDefaultInstance().where(Person.class).findAll();
               feature=new byte[personss.size()][];
               Uids=new String[personss.size()];
               Logger.e("finger_identify"+"cursor.getCount()"+persons.size());
               for(int x=0;x<personss.size();x++){
                   feature[x]=hexStringToByte(personss.get(x).getFeature());
                   Uids[x]=personss.get(x).getUid();
               }

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
           }else {
               feature=null;
               nFeatuer=null;
           }
           DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           SharedPreferences userinfo=activty.getSharedPreferences("user_info",0);
           String deviceId=userinfo.getString("deviceId","");
           String Uid = Uids[pos[0]];
           String uidss= StringUtils.join(Uids,",");
           String strBeginDate = dateTimeformat.format(new Date());
           Logger.e(Uid+">>>>>>>>>>>>");
           if (identifyResult) {
               Logger.e("SignActivity"+"pos="+pos[0]+"score="+score[0]+"strBeginDate:"+strBeginDate);
                       activty.sendLogMessageTastContract.sendLog(deviceId,Uid,uidss,bytesToHexString(img),strBeginDate,score[0]+"","验证成功");
               feature=null;
               nFeatuer=null;
               return Uid;
           }else {
                   activty.sendLogMessageTastContract.sendLog(deviceId,null,uidss,bytesToHexString(img),strBeginDate,score[0]+"","验证失败");
               feature=null;
               nFeatuer=null;
               return null;
           }
       }else {
           List<Person> personss= Realm.getDefaultInstance().where(Person.class).findAll();
           feature=new byte[personss.size()][];
           Uids=new String[personss.size()];
           Logger.e("finger_identify"+"cursor.getCount()"+persons.size());
           for(int x=0;x<personss.size();x++){
               feature[x]=hexStringToByte(personss.get(x).getFeature());
               Uids[x]=personss.get(x).getUid();
           }

           len = 0;
           // 计算一维数组长度
           if(feature.length>0) {
               for (byte[] element : feature) {
                   len += element.length;
               }

               // 复制元素
               nFeatuer = new byte[len];
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
       }
       DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       SharedPreferences userinfo=activty.getSharedPreferences("user_info",0);
       String deviceId=userinfo.getString("deviceId","");
        String Uid=null;
        if(Uids.length>0){
           Uid = Uids[pos[0]];
       }
       String uidss= StringUtils.join(Uids,",");
       String strBeginDate = dateTimeformat.format(new Date());
       if (identifyResult) {
           Logger.e("SignActivity"+"pos="+pos+"score="+score[0]+"strBeginDate:"+strBeginDate+">>>>>>>>>>>>");
           activty.sendLogMessageTastContract.sendLog(deviceId,Uid,uidss,bytesToHexString(img),strBeginDate,score[0]+"","验证成功");
           feature=null;
           nFeatuer=null;
           return Uid;
       }else {
           activty.sendLogMessageTastContract.sendLog(deviceId,null,uidss,bytesToHexString(img),strBeginDate,score[0]+"","验证失败");
           feature=null;
           nFeatuer=null;
           return null;
       }

   }
}
