package com.link.cloud.utils;

import android.content.Context;
import android.util.Log;

import com.link.cloud.BaseApplication;
import com.link.cloud.component.MdUsbService;
import com.link.cloud.greendaodemo.Person;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import md.com.sdk.MicroFingerVein;

import static com.alibaba.sdk.android.ams.common.util.HexUtil.bytesToHexString;
import static com.alibaba.sdk.android.ams.common.util.HexUtil.hexStringToByte;

/**
 * Created by 49488 on 2018/7/27.
 */

public class VenueUtils {

    private boolean identifyResult;
    private ExecutorService service;

    public interface VenueCallBack{
        void VeuenMsg(int state, String data, String uids, String feature, String score,String userTpye);
        void ModelMsg(int state, ModelImgMng modelImgMng, String feature);
    }
    private  String TAG ="VENUEUTILS";
    private  boolean bOpen;//设备是否打开
    private  volatile boolean bRun=true;//是否运行工作线程
    private  volatile boolean bWorkIdentify;//是否是认证
    private volatile boolean bWorkModel;//是否是建模
    private  int deviceTouchState=1;
    private  Thread mdWorkThread;//进行建模或认证的全局工作线程
    MdUsbService.MyBinder mdDeviceBinder;
    Context context;
    VenueCallBack callBack;
    public  void initVenue(MdUsbService.MyBinder mdDeviceBinder, Context context, VenueCallBack callBack, Boolean bWorkIdentify, Boolean bWorkModel){
        this.mdDeviceBinder=mdDeviceBinder;
        this.context=context;
        this.callBack=callBack;
        this.bWorkModel=bWorkModel;
        this.bWorkIdentify=bWorkIdentify;
        startIdenty();
    }
    public  void startIdenty(){
        bRun=true;
        bOpen=false;
        deviceTouchState=1;
        if(service==null){
            service = Executors.newFixedThreadPool(1);
        }
        service.execute(runnable);
    }
    public  void StopIdenty(){
        bOpen=false;
        bRun=false;
        deviceTouchState=2;
    }
    public boolean getRun(){
        return bRun;
    }
    private Runnable runnable = new Runnable() {
        private int state;
        private int[] pos=new int[1];
        private float[] score=new float[1];
        private boolean ret;
        private ModelImgMng modelImgMng=new ModelImgMng();
        private int[] tipTimes={0,0};//后两次次建模时用了不同手指或提取特征识别时，最多重复提醒限制3次
        private int lastTouchState=0;//记录上一次的触摸状态
        private int modOkProgress=0;
        @Override
        public void run() {
            while(getRun()){
                if(!bOpen){
                    deviceTouchState=2;
                    if(mdDeviceBinder.getDeviceCount()<=0){//无设备连接
                        try {
                            Thread.sleep(150L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    modOkProgress=0;
                    modelImgMng.reset();
                    bOpen=mdDeviceBinder.openDevice(0);//开启指定索引的设备
                    if(bOpen){
                        Log.e(TAG,"open device success");
                        callBack.VeuenMsg(0,"","","","","");
                    } else{
                        Log.e(TAG,"open device failed,stop identifying and modeling.");

                    }
                    continue;
                }
                //设备连接正常则获取设备状态，进入正常建模或认证流程
                state=mdDeviceBinder.getDeviceTouchState(0);


                if(state!=3){
                    if(lastTouchState!=0){
                        mdDeviceBinder.setDeviceLed(0, MdUsbService.getFvColorRED(),true);
                    }
                    lastTouchState=0;
                }
                if(state==3){//返回值state=3表检测到了双Touch触摸,返回1表示仅指腹触碰，返回2表示仅指尖触碰，返回0表示未检测到触碰
                    lastTouchState=3;
                    deviceTouchState=0;
                    mdDeviceBinder.setDeviceLed(0,MdUsbService.getFvColorGREEN(),false);
                    if(!bWorkIdentify&&!bWorkModel){
                        Log.e(TAG,"no identify or model task,try sleep a moment.");
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    //-------------------------------------------------------------------------------------------------//抓图与质量评估
                    callBack.ModelMsg(1,null,"");
                    Log.e(TAG,"try grab image.");//3种示例抓图方式，按需选择；
                    byte[] img=mdDeviceBinder.tryGrabImg(0);//optional way 3
                    if(img==null){
                        Log.e(TAG,"get img failed,please try again");
                        continue;
                    }
                    //---------------------------//show visible decoded img(optional function)
                    if(bWorkModel){//只对建模图片做质量评估，不需要对认证图片做质量评估；
                        float[] quaScore={0f,0f,0f,0f};
                        int quaRtn=MdUsbService.qualityImgEx(img,quaScore);
                        String oneResult= ("quality return=" + quaRtn) + ",result=" + quaScore[0] + ",score=" + quaScore[1] + ",fLeakRatio=" + quaScore[2] + ",fPress=" + quaScore[3];
                        Log.e(TAG,oneResult);
                        int quality=(int)quaScore[0];
                        if(quality!=0){
                            callBack.VeuenMsg(9,"取图失败请抬高手指,重试","","","","");
                            Log.e(TAG,"取图失败请抬高手指,重试");
                            continue;
                        }
                    }
                    //-------------------------------------------------------------------------------------------------//抓图与质量评估
                    byte[] feature=MdUsbService.extractImgModel(img,null,null);
                    if(feature==null) {
                        callBack.VeuenMsg(9,"取图失败请抬高手指,重试","","","","");
                        Log.e(TAG,"extractImgModel get feature from img fail,retry soon");
                    } else {
                        if(bWorkIdentify) {//认证
                            tipTimes[0]=0;
                            tipTimes[1]=0;
                            modelImgMng.reset();
                            modOkProgress=0;
                            Log.e(TAG,"identify success(pos="+pos[0]+")");
                            if(identifyNewImg(img,pos,score)){//比对及判断得分放到identifyNewImg()内实现
                                Log.e(TAG,"identify success(pos="+pos[0]+")");

                            }else{
                                Log.e("identify fail,","pos="+pos[0]);

                                //----------------------------------------------------------
                                //if(isMdDebugOpen&&!featureSpUtils.isFeatureDbEmpty()){//数据库非空+debug模式开启+认证失败时才打印
                                //    String tips="DEBUG:本次认证图片及状态日志已保存到目录：\n"+MdDebugger.debugIdentifySrcByTimeMillis(false,pos[0],score[0],img);
                                //    Log.e(TAG,tips);
                                //    handler.obtainMessage(MSG_SHOW_LOG,tips).sendToTarget();
                                //}
                                //----------------------------------------------------------
                            }
                            while(state==3&&bRun){
                                deviceTouchState=0;
                                state=mdDeviceBinder.getDeviceTouchState(0);
                                if(!bOpen){//等待手指拿开的中途设备断开了
                                    Log.e(TAG,"device disconnected when identifying is waiting for finger moving away");

                                }
                            }
                            continue;
                        }else if (bWorkModel){//建模
                            modOkProgress++;
                             if(modOkProgress==1) {//first model
                                tipTimes[0]=0;
                                tipTimes[1]=0;
                                modelImgMng.setImg1(img);
                                modelImgMng.setFeature1(feature);
                                Log.e(TAG,"first model ok");
                                callBack.ModelMsg(2,null,"");
                            }else if(modOkProgress==2){//second model
                                ret=MdUsbService.fvSearchFeature(modelImgMng.getFeature1(),1,img,pos,score);
                                if(ret && score[0]>MODEL_SCORE_THRESHOLD) {
                                    feature=MdUsbService.extractImgModel(img,null,null);//无须传入第一张图片，第三次混合特征值时才同时传入3张图；
                                    if(feature != null) {
                                        tipTimes[0]=0;
                                        tipTimes[1]=0;
                                        modelImgMng.setImg2(img);
                                        modelImgMng.setFeature2(feature);
                                        Log.e(TAG,"second model ok");
                                        callBack.ModelMsg(2,null,"");
                                    }else {//第二次建模从图片中取特征值无效
                                        modOkProgress=1;
                                        if(++tipTimes[0]<=3){
                                            Log.e(TAG,"get feature from img failed when try second modeling");
                                            callBack.ModelMsg(3,null,"");
                                        }else {//连续超过3次放了不同手指则忽略此次建模重来
                                            modOkProgress=0;
                                            modelImgMng.reset();
                                            Log.e(TAG,"put different finger more than 3 times,this modeling is ignored,a new modeling start.");
                                            callBack.ModelMsg(3,null,"");
                                        }
                                    }
                                }else{
                                    modOkProgress=1;
                                    if(++tipTimes[0]<=3){
                                        Log.e(TAG,"high difference to last model,seems to has used different finger,please retry for second model.");
                                        callBack.ModelMsg(3,null,"");
                                    }else {//连续超过3次放了不同手指则忽略此次建模重来
                                        modOkProgress=0;
                                        modelImgMng.reset();
                                        Log.e(TAG,"put different finger more than 3 times,this modeling is ignored,a new modeling start.");
                                        callBack.ModelMsg(3,null,"");
                                    }
                                }
                            }else if(modOkProgress==3){//third model
                                ret=MdUsbService.fvSearchFeature(modelImgMng.getFeature2(),1,img,pos,score);
                                if (ret && score[0]>MODEL_SCORE_THRESHOLD) {
                                    feature=MdUsbService.extractImgModel(modelImgMng.getImg1(),modelImgMng.getImg2(),img);
                                    if(feature!=null) {//成功生成一个3次建模并融合的融合特征数组
                                        tipTimes[0]=0;
                                        tipTimes[1]=0;
                                        modelImgMng.setImg3(img);
                                        modelImgMng.setFeature3(feature);
                                        callBack.ModelMsg(0,modelImgMng,bytesToHexString(feature));
                                        callBack.ModelMsg(5,null,"");
                                        modOkProgress=0;
                                        //----------------------------------------------------------
                                        //if(isMdDebugOpen) {//保存3次建模后的3张图片用于分析异常情况;
                                        //    String tips="DEBUG:本次建模图片及日志已经保存到:\n"+MdDebugger.debugModelSrcByTimeMillis(modelImgMng.getImg1(),modelImgMng.getImg2(),modelImgMng.getImg3());
                                        //    Log.e(TAG,tips);
                                        //    handler.obtainMessage(MSG_SHOW_LOG,tips).sendToTarget();
                                        //}
                                    }else {//第三次建模从图片中取特征值无效
                                        modOkProgress=2;
                                        if(++tipTimes[1]<=3) {
                                            Log.e(TAG,"extract feature get third feature fail.(isAllImgDataOk="+modelImgMng.isAllImgDataOk()+")");
                                            callBack.ModelMsg(2,null,"");
                                        }
                                    }
                                } else {
                                    modOkProgress=2;
                                    if(++tipTimes[1]<=3) {
                                        callBack.ModelMsg(3,null,"");
                                    }else {//连续超过3次放了不同手指则忽略此次建模重来
                                        modOkProgress=0;
                                        modelImgMng.reset();
                                        callBack.ModelMsg(3,null,"");
                                    }
                                }
                            }else {
                                modOkProgress=0;
                                modelImgMng.reset();
                            }
                        }
                    }
                    while(state==3&&bRun){//等待手指移开
                        deviceTouchState=0;
                        state=mdDeviceBinder.getDeviceTouchState(0);
                        if(!bOpen){//等待手指拿开的中途设备断开了
                            Log.e(TAG,"device disconnected when identifying is waiting for finger moving away");

                        }
                    }
                }else if(state==1||state==2){

                    if(bOpen) {
                        deviceTouchState=1;
                    }
                } else if(state==0){//触摸state==0时，表无触摸状态

                    if(bOpen) {
                        deviceTouchState=1;
                    }
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (bOpen){
                mdDeviceBinder.closeDevice(0);
                bOpen=false;
            }
        }


    };

    private List<Person> people = new ArrayList<>();
    private boolean identifyNewImg(final byte[] img,int[] pos,float[] score) {
        identifyResult=false;
        people.clear();
        people.addAll(((BaseApplication) context.getApplicationContext().getApplicationContext()).getPerson());
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
            Log.e(TAG, "allFeaturesBytes: "+allFeaturesBytes.length);
            //比对是否通过
            identifyResult = MicroFingerVein.fv_index(allFeaturesBytes,allFeaturesBytes.length/3352,img,pos,score);
            Log.e(TAG, "identifyResult: "+ identifyResult);
            identifyResult = identifyResult &&score[0]>IDENTIFY_SCORE_THRESHOLD;//得分是否达标
            Log.e(TAG, "identifyResult: "+ identifyResult);

            y++;
        }
        String uids =  StringUtils.join(uidss,",")+"";
        if(identifyResult){//比对通过且得分达标时打印此手指绑定的用户名
            String featureName = uidss[(y-1)*1000+pos[0]];
            String userType = people.get((y - 1) * 1000 + pos[0]).getUserType();
            Log.e(TAG, featureName+uids);
            callBack.VeuenMsg(1,featureName,uids,bytesToHexString(img),score[0]+"",userType);
        }else {
            if(y== people.size()/1000+1){
                callBack.VeuenMsg(2,"",uids,bytesToHexString(img),score[0]+"","");
            }

        }
        return identifyResult;
    }
    private final static float IDENTIFY_SCORE_THRESHOLD=0.63f;//认证通过的得分阈值，超过此得分才认为认证通过；
    private final static float MODEL_SCORE_THRESHOLD=0.4f;//

}
