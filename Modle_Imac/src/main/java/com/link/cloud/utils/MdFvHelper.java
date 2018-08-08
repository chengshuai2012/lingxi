package com.link.cloud.utils;

import android.util.Log;

import md.com.sdk.MicroFingerVein;

/**
 *  定义一个辅助类处理操作细节，提高抓图及质量评估成功率；
 */
public class MdFvHelper {
    private final static String TAG=MdFvHelper.class.getSimpleName()+"_DEBUG";

    /**
     *  连续尝试抓取最多tryTimes张图，首次遇到得分大于指定倍数质量评估阈值的优质图时立即返回，使用读microFingerVein.fImageEnergy值的方式获取质评得分；</BR>
     */
    public static byte[] tryGetFirstBestImg(MicroFingerVein microFingerVein, int deviceIndex, final int tryTimes){
        if(microFingerVein==null) {
            Log.e(TAG,"tryGetFirstBestImg() microFingerVein is null.");
            return null;
        }
        long startTimeMillis=System.currentTimeMillis();
        float decentScore=microFingerVein.fEnergyThreshold*1.5f;//大于此值认为是质评优质图；
        int i;
        byte[] img=null;
        float quaScore=0f;
        for(i=0;i<tryTimes;i++){
            byte[] imgTmp=microFingerVein.fvdev_grab(deviceIndex);
            if(imgTmp!=null&& MicroFingerVein.fv_quality(imgTmp)==0){
                float quaScoreTmp=microFingerVein.fImageEnergy;
                if(quaScoreTmp>quaScore){
                    quaScore=quaScoreTmp;
                    img=imgTmp;
                }
            }
            if(quaScore>=decentScore) break;//遇到质评优质图立即返回
        }
        if(i>=tryTimes) i=tryTimes-1;
        Log.e(TAG,"constantly grab img "+(i+1)+"/"+tryTimes+" times,takeTimeMillis="
                +(System.currentTimeMillis()-startTimeMillis) +",highScore="+quaScore);
        return img;
    }

    /**
     *  连续尝试抓取最多tryTimes张图，首次遇到得分大于指定倍数质量评估阈值的优质图时立即返回,使用新接口fvQualityEx()直接获取质评得分；</BR>
     */
    public static byte[] tryGetFirstBestImgNew(MicroFingerVein microFingerVein, int deviceIndex, final int tryTimes){
        long startTimeMillis=System.currentTimeMillis();
        float decentScore=microFingerVein.fEnergyThreshold*1.5f;//大于此值认为是质评优质图；
        int i;
        byte[] img=null;
        float quaScore=0f;
        float[] quaScoreTmp={0f,0f,0f,0f};
        for(i=0;i<tryTimes;i++){
            byte[] imgTmp=microFingerVein.fvdev_grab(deviceIndex);
            if(imgTmp!=null){
                MicroFingerVein.fv_QualityEx(imgTmp,quaScoreTmp);
                if(quaScoreTmp[1]>quaScore){
                    quaScore=quaScoreTmp[1];
                    img=imgTmp;
                }
            }
            if(quaScore>=decentScore) break;//遇到质评优质图立即返回
        }
        if(i>=tryTimes) i=tryTimes-1;
        Log.e(TAG,"constantly grab img "+(i+1)+"/"+tryTimes+" times,takeTimeMillis="
                +(System.currentTimeMillis()-startTimeMillis) +",highScore="+quaScore);
        return img;
    }
}
