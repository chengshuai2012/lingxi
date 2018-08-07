package com.hotelmanager.xzy.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2017/6/5.
 */

public class HotelUtil {

    private static final String TAG="HOTELMANAGER";
    private Handler mHandler;
    public HotelUtil(Handler mHandler){
        this.mHandler=mHandler;
    }


    //开门返回结果
    public void openDoorSuccess(byte[] data){

        switch (data[2]){
            case (byte) 0xA4:
                //开单门
               openOneDoorResult(data[4]);

                break;

            case (byte) 0xA5:
                //开多门

                break;

            case (byte) 0xA6:

                break;

            case (byte) 0xA7:
                break;

            case (byte) 0xA8:
            	closeDoorResultToServer(data);

                break;

        }

    }

    private String convertNumbersToStrings(int i){
        switch (i){
            case 0:
                return "001";
            case 1:
                return "002";
            case 2:
                return "003";
            case 3:
                return "004";
            case 4:
                return "005";
            case 5:
                return "006";
            case 6:
                return "007";
            case 7:
                return "008";


        }
        return null;
    }
    private void closeDoorResultToServer(byte[] data) {
    	ArrayList<String> list =new ArrayList<String>();
       if (data[3]==0x01){
           list.add("009");
       }else if(data[3]==0x02){
           list.add("010");
       }else if(data[3]==0x03){
           list.add("009");
           list.add("010");
       }
       byte[] door_tem={0x01,0x02,0x04,0x08,0x10,0x20,0x40, (byte)0x80};
       for (int i=0;i<door_tem.length;i++){
    
             if ((door_tem[i]&data[4])!=0){
            	 
                 list.add(convertNumbersToStrings(i));
             }
       }
       
       Message msg= Message.obtain();
       Bundle bundle=msg.getData();
       bundle.putStringArrayList("no1",list);
       msg.what=0;
       msg.setData(bundle);
       mHandler.sendMessage(msg);
       
   }
    
    private void closeDoor(byte[] data) {
        switch (data[3]){

            case 0x01:
            String mm="1";
                sendMessege(0,mm);

                break;
            case 0x02:
                String mm1="2";
                sendMessege(0,mm1);
                break;
            case 0x03:
                String mm3="3";
                sendMessege(0,mm3);
                break;
            case 0x04:
                sendMessege(0,"4");
                break;
            case 0x05:
                sendMessege(0,"5");
                break;
            case 0x06:
                sendMessege(0,"6");
                break;
            case 0x07:
                sendMessege(0,"7");
                break;
            case 0x08:
                sendMessege(0,"8");
                break;
            case 0x09:
                sendMessege(0,"9");
                break;
            case (byte) 0x0A:
                Log.e(TAG, "closeDoor: byte[10]====" );
                sendMessege(0,"10");
                break;
        }
    }

    private void sendMessege(int i,String mm) {
        Log.e(TAG, "sendMessege: =="+mm );
        if (mHandler!=null){
        Message message= Message.obtain();
        message.what=i;
        message.obj=mm;
        mHandler.sendMessage(message);
        }
    }

    private void openOneDoorResult(byte b) {
        switch (b){
            case 0x01:
                sendMessege(1,"1");
                break;
            case 0x02:
                sendMessege(1,"2");
                break;
            case 0x03:
                sendMessege(1,"3");
                break;
            case 0x04:
                sendMessege(1,"4");
                break;
            case 0x05:
                sendMessege(1,"5");
                break;
            case 0x06:
                sendMessege(1,"6");
                break;
            case 0x07:
                sendMessege(1,"7");
                break;
            case 0x08:
                sendMessege(1,"8");
                break;
            case 0x09:
                sendMessege(1,"9");
                break;
            case (byte) 0x0A:
                sendMessege(1,"10");
                break;

        }
    }



private String getSwcode(String num){
   /* Calendar c = Calendar.getInstance();
   int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);*/

    SimpleDateFormat formatter    =   new SimpleDateFormat("yyyyMMdd");
    Date curDate  =   new Date(System.currentTimeMillis());//获取当前时间
    String str    =    formatter.format(curDate);
    Log.i(TAG, "getSwcode: str== "+str);
    return "sw"+str+num;
}
}
