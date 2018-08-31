
package com.link.cloud;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.link.cloud.activity.LockActivity;
import com.link.cloud.activity.WelcomeActivity;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.BindFaceMes;
import com.link.cloud.bean.DeviceData;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.bean.PagesInfoBean;
import com.link.cloud.bean.Person;
import com.link.cloud.bean.PushMessage;
import com.link.cloud.bean.PushUpDateBean;
import com.link.cloud.bean.SignUser;
import com.link.cloud.bean.Sign_data;
import com.link.cloud.bean.SyncFeaturesPage;
import com.link.cloud.bean.SyncUserFace;
import com.link.cloud.bean.UpDateBean;
import com.link.cloud.component.TimeService;
import com.link.cloud.constant.Constant;
import com.link.cloud.contract.DownloadFeature;
import com.link.cloud.contract.GetDeviceIDContract;
import com.link.cloud.contract.SyncUserFeature;
import com.link.cloud.utils.DownLoad;
import com.link.cloud.utils.DownloadUtils;
import com.link.cloud.utils.FaceDB;
import com.link.cloud.utils.FileUtils;
import com.link.cloud.utils.Utils;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
/**
 * Description：BaseApplication
 * Created by Shaozy on 2016/8/10.
 */
public class BaseApplication extends MultiDexApplication  implements GetDeviceIDContract.VersoinUpdate,DownloadFeature.download,SyncUserFeature.syncUser{
    public static boolean DEBUG = false;
    private static BaseApplication ourInstance = new BaseApplication();
    public boolean log = true;
    public boolean flag;
    public  static String messgetext;
    public Gson gson;
    private static Context context;
    public static final long ONE_KB = 1024L;
    public static final long ONE_MB = ONE_KB * 1024L;
    public static final long CACHE_DATA_MAX_SIZE = ONE_MB * 3L;
    private static final String TAG = "BaseApplication";
//    static String string;static int type;
   static LockActivity mainActivity=null;
    private Realm realm;

    public static BaseApplication getInstance() {
        return ourInstance;
    }
    String deviceTargetValue;
    private SQLiteDatabase db;
    GetDeviceIDContract presenter;
    public static BaseApplication instances;
    private static LockActivity mainAcivity;
   static DownloadFeature feature;
   static SyncUserFeature syncUserFeature;

    public FaceDB mFaceDB;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static BaseApplication getInstances() {
        return instances;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "62ab7bf668", true);
        //        CrashHandler.getInstance().init(this);
        //        LogcatHelper.getInstance(this).start();
        feature=new DownloadFeature();
        feature.attachView(this);
        syncUserFeature=new SyncUserFeature();
        syncUserFeature.attachView(this);
        instances = this;
        ourInstance = this;
        mFaceDB = new FaceDB(Environment.getExternalStorageDirectory().getAbsolutePath() + "/faceFile");
         context=getApplicationContext();
        Realm.init(this);
//自定义配置
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("myRealm.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
            }
            @Override
            public void onActivityStarted(Activity activity) {
                count++;
                Log.e("onActivityStarted: ",count+"" );
                Intent countIntent = new Intent(COUNT_CHANGE);
                countIntent.putExtra("count",count);
                sendBroadcast(countIntent);
            }
            @Override
            public void onActivityResumed(Activity activity) {
                TestActivityManager.getInstance().setCurrentActivity(activity);
            }
            @Override
            public void onActivityPaused(Activity activity) {
            }
            @Override
            public void onActivityStopped(Activity activity) {

            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
                count--;
                Intent countIntent = new Intent(COUNT_CHANGE);
                countIntent.putExtra("count",count);
                sendBroadcast(countIntent);
            }
        });
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            DEBUG = appInfo.metaData.getBoolean("DEBUG");
        } catch (Exception e) {
            DEBUG = false;
        }
        Intent intent = new Intent(getApplicationContext(), TimeService.class);
        startService(intent);
        ifspeaking();
        realm = Realm.getDefaultInstance();
        RealmResults<Person> allAsync = realm.where(Person.class).findAllAsync();
        allAsync.addChangeListener(new RealmChangeListener<RealmResults<Person>>() {
            @Override
            public void onChange(RealmResults<Person> peoples) {
                people.addAll(realm.copyFromRealm(peoples));
            }
        });
        this.initGson();
        this.initReservoir();
        this.initCCPRestSms();
        presenter=new GetDeviceIDContract();
        presenter.attachView(this);
        initCloudChannel(this);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

    }
    private List<Person> people = new ArrayList<>();
    public List<Person> getPerson(){
        return people;
    }
    public int count =0;
    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            Throwable cause = ex.getCause();
            StringBuilder builder = new StringBuilder();
            builder.append(ex.getCause().toString()+"\r\n");
            for(int x=0;x<cause.getStackTrace().length;x++){
                builder.append("FileName:"+cause.getStackTrace()[x].getFileName()+">>>>Method:"+cause.getStackTrace()[x].getMethodName()+">>>>FileLine:"+cause.getStackTrace()[x].getLineNumber()+"\r\n");
            }

            Logger.e(builder.toString());
            restartApp();
        }
    };
    public void restartApp() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }
    /**
     * 参数设置
     * @return
     */
//    private void setParam(){
//        // 清空参数
//        mTts.setParameter(SpeechConstant.PARAMS, null);
//        //设置使用本地引擎
//        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
//        //设置发音人资源路径
//        mTts.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath());
//        //设置发音人
//        mTts.setParameter(SpeechConstant.VOICE_NAME,voicerLocal);
//        //设置合成语速
//        mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
//        //设置合成音调
//        mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
//        //设置合成音量
//        mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
//        //设置播放器音频流类型
//        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
//
//        // 设置播放合成音频打断音乐播放，默认为true
//        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
//        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
//    }
//    //获取发音人资源路径
//    private String getResourcePath(){
//        StringBuffer tempBuffer = new StringBuffer();
//        //合成通用资源
//        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
//        tempBuffer.append(";");
//        //发音人资源
//        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/"+LockActivity.voicerLocal+".jet"));
//        return tempBuffer.toString();
//    }
    boolean ret=false;
//    private void checkMD(){
//        if (mainAcivity!=null&&ret!=true) {
//            microFingerVein = MicroFingerVein.getInstance(mainAcivity);
//            int devicecount=microFingerVein.fvdev_get_count();
//            if (devicecount!=0) {
//                ret = microFingerVein.fvdev_open();
////               Logger.e("WorkService1"+"devicecount"+devicecount + "=====================" + ret);
//            }
//        }
//    }
    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
               mainAcivity.showTip (getResources().getString(R.string.mTts_stating_error)+code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    void ifspeaking(){
        StringBuffer param = new StringBuffer();
        param.append("appid="+getString(R.string.app_id));
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(BaseApplication.this, param.toString());
    }
    public String getMac(){
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig","HWaddr");
        if(result==null){
            return getResources().getString(R.string.network_error);
        }
        //对该行数据进行解析
        //例如：eth0      Link encap:Ethernet  HWaddr 00:16:E8:3E:DF:67
        if(result.length()>0 && result.contains("HWaddr")==true){
            Mac = result.substring(result.indexOf("HWaddr")+6, result.length()-1);
            Log.i("test","Mac:"+Mac+" Mac.length: "+Mac.length());
            result = Mac;
            Log.i("test",result+" result.length: "+result.length());
        }
        return result;
    }
    private static String callCmd(String cmd,String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            //执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine ()) != null && line.contains(filter)== false) {
                //result += line;
                Log.i("test","line: "+line);
            }
            result = line;
            Log.i("test","result: "+result);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static Context getContext() {
        return context;
    }

    @Override
    public void downloadApK(UpDateBean resultResponse) {
        int version = getVersion(getApplicationContext());
        if(version<resultResponse.getData().getPackage_version()){
            downLoadApk(resultResponse.getData().getPackage_path());
        }
        Logger.e(resultResponse.getMsg()+resultResponse.getData().getPackage_path());
    }

    @Override
    public void syncUserFacePagesSuccess(SyncUserFace resultResponse) {
        ExecutorService service = Executors.newFixedThreadPool(1);
        for(int x =0;x<resultResponse.getData().size();x++){

            int finalX = x;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    DownLoad.download(resultResponse.getData().get(finalX).getFaceUrl(),resultResponse.getData().get(finalX).getUid());
                }
            };
            service.execute(runnable);
        }
    }

    private void downLoadApk(String downloadurl) {
        // 判断当前用户是否有sd卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "lingxi.apk");
            if (file.exists()) {
                file.delete();
            }
            Toast.makeText(this, getResources().getString(R.string.notice_stating), Toast.LENGTH_SHORT).show();
            DownloadUtils utils = new DownloadUtils(this);
            utils.downloadAPK(downloadurl, "lingxi.apk");
            Logger.e(file.getAbsolutePath());

        }
    }
    private static int getVersion(Context context)// 获取版本号
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }
    @Override
    public void downloadSuccess(DownLoadData resultResponse) {
        RealmResults<Person> uid = realm.where(Person.class).equalTo("uid", resultResponse.getData().get(0).getUid()).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(int x=0;x<uid.size();x++){
                    uid.deleteAllFromRealm();
                }
            }
        });
        people.addAll(resultResponse.getData());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(int x= 0;x<resultResponse.getData().size();x++){
                    realm.copyToRealm(resultResponse.getData().get(x));
                }
            }
        });

    }
    @Override
    public void downloadNotReceiver(DownLoadData resultResponse) {
        if(resultResponse.getData().size()>0){
            people.addAll(resultResponse.getData());
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(int x= 0;x<resultResponse.getData().size();x++){
                    realm.copyToRealm(resultResponse.getData().get(x));
                }
            }
        });
    }
    public SQLiteDatabase getDb() {
        return db;
    }
    private void initGson() {
        this.gson = new GsonBuilder().setDateFormat(Constant.BASE_DATA_FORMAT).create();
    }

    private void initReservoir() {
        try {
            Reservoir.init(this, CACHE_DATA_MAX_SIZE, this.gson);
        } catch (Exception e) {
            //failure
            e.printStackTrace();
            Logger.e("initReservoir failure :" + e);
        }
    }
    private void initCCPRestSms() {
        /*restAPI = new CCPRestSmsSDK();
        /*//******************************注释*********************************************
         /*//*初始化服务器地址和端口                                                       *
        /*//*沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
        /*//*生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883");       *
        /*//*******************************************************************************
         restAPI.init("app.cloopen.com", "8883");
         /*//******************************注释*********************************************
         /*//*初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN     *
        /*//*ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
        /*//*参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。                   *
        /*//*******************************************************************************
         restAPI.setAccount(getString(R.string.ACCOUNT_SID), getString(R.string.AUTH_TOKEN));
         /*//******************************注释*********************************************
         /*//*初始化应用ID                                                                 *
        /*//*测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID     *
        /*//*应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
        /*//*******************************************************************************
         restAPI.setAppId(getString(R.string.APPID));
         */
    }
    /**
     * 获取log日志根目录
     *
     * @return
     */
    public static String getLogDir() {
        return getDiskCacheDir(Utils.getMetaData(Constant.CHANNEL_NAME));
    }
    /**
     * 获取相关功能业务目录
     *
     * @return 文件缓存路径
     */
    public static String getDiskCacheDir(String dirName) {
        String dir = String.format("%s/%s/", getDiskCacheRootDir(), dirName);
        File file = new File(dir);
        if (!file.exists()) {
            boolean isSuccess = file.mkdirs();
            if (isSuccess) {
                Logger.e(dir + " mkdirs success");
            }
        }
        return file.getPath();
    }
    /**
     * 获取app的根目录
     *
     * @return 文件缓存根路径
//   */
    public static String getDiskCacheRootDir() {
        File diskRootFile;
        if (existsSdcard()) {
            diskRootFile = BaseApplication.getInstance().getExternalCacheDir();
        } else {
            diskRootFile = BaseApplication.getInstance().getCacheDir();
        }
        String cachePath;
        if (diskRootFile != null) {
            cachePath = diskRootFile.getPath();
        } else {
            throw new IllegalArgumentException("disk is invalid");
        }
        return cachePath;
    }
    /**
     * 判断外置sdcard是否可以正常使用
     * @return
     */
    public static Boolean existsSdcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable();
    }
    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(final Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        String id =pushService.getDeviceId();
        Logger.e(""+id);
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        deviceTargetValue = Utils.getMD5(getMac());
                        presenter.getDeviceID(deviceTargetValue,2);
                    }
                }).start();
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.e(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }
    @Override
    public void onResultError(ApiException e) {
        onError(e);
    }
    @Override
    public void onError(ApiException e) {
        if (downLoadListner!=null) {
            downLoadListner.finish();
        }
    }
    @Override
    public void onPermissionError(ApiException e) {
        onError(e);
    }
    @Override
    public void syncUserSuccess(DownLoadData resultResponse) {
        List<Person> data = resultResponse.getData();
        RealmResults<Person> all = realm.where(Person.class).findAll();
        people.addAll(resultResponse.getData());
        Logger.e(">>>>>>>"+all.size());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                all.deleteAllFromRealm();
            }
        });
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int x = 0; x < data.size(); x++) {
                    realm.copyToRealm(data.get(x));
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Logger.e(">>>>>>>onSuccess");
                if(downLoadListner!=null){
                    downLoadListner.finish();
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable throwable) {
                Logger.e(">>>>>>>onError");
                if(downLoadListner!=null){
                    downLoadListner.finish();
                }
            }
        });
//        TTSUtils.getInstance().speak("初始化成功");
        NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
        if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）

        }else {
            Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }
        if(downLoadListner!=null){
            downLoadListner.finish();
        }
    }

    ArrayList<Person> SyncFeaturesPages = new ArrayList<>();
    int totalPage=0,currentPage=1,downloadPage=0;
    @Override
    public void getPagesInfo(PagesInfoBean resultResponse) {
        totalPage = resultResponse.getData().getPageCount();
        ExecutorService service = Executors.newFixedThreadPool(1);
        for(int x =0 ;x<resultResponse.getData().getPageCount();x++){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    feature.syncUserFeaturePages(FileUtils.loadDataFromFile(getContext(), "deviceId.text"), currentPage++);
                }
            };
            service.execute(runnable);
        }

    }

    @Override
    public void syncUserFeaturePagesSuccess(SyncFeaturesPage resultResponse) {
        if (resultResponse.getData().size()>0) {
            downloadPage++;
            Logger.e(resultResponse.getData().size() + getResources().getString(R.string.syn_data)+"current");
            SyncFeaturesPages.addAll(resultResponse.getData());
            Logger.e(SyncFeaturesPages.size() + getResources().getString(R.string.syn_data)+"total");
            if (downloadPage == totalPage) {
                people.addAll(SyncFeaturesPages);
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for(int x= 0;x<SyncFeaturesPages.size();x++){
                            realm.copyToRealm(SyncFeaturesPages.get(x));
                        }
                    }
                });
                Logger.e(SyncFeaturesPages.size() + getResources().getString(R.string.syn_data));
                NetworkInfo info = connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）

                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.syn_data), Toast.LENGTH_LONG).show();
                }
                if (downLoadListner != null) {
                    downLoadListner.finish();
                }
            }
        }else {
            if (downLoadListner != null) {
                downLoadListner.finish();
            }
        }
    }
    public static final String COUNT_CHANGE = "change_count";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Logger.e("downloadNotReceiver>>>>>>>>>>>>>>>>>>>>>>>>");
            handler.removeCallbacksAndMessages(null);
            String s = FileUtils.loadDataFromFile(getContext(), "deviceId.text");
            connectivityManager =(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
            NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
            if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                    feature.downloadNotReceiver(s);
            }else {
                Toast.makeText(getContext(), getResources().getString(R.string.syn_data), Toast.LENGTH_LONG).show();
            }
            handler.sendEmptyMessageDelayed(0,30*1000);
        }
    };
    public downloafinish downLoadListner;
    public void setDownLoadListner(downloafinish downLoadListner){
        this.downLoadListner=downLoadListner;
   }

    @Override
    public void syncSignUserSuccess(Sign_data downLoadData) {
        List<SignUser> data = downLoadData.getData();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(int x= 0;x<data.size();x++){
                    realm.copyToRealm(data.get(x));
                }
            }
        });
    }
ConnectivityManager connectivityManager;
    @Override
    public void getDeviceSuccess(DeviceData deviceData) {
        feature.appUpdateInfo(deviceData.getDeviceData().getDeviceId());
       Logger.e("BaseApplication+devicedate"+deviceData.getDeviceData().getDeviceId()+"numberType"+deviceData.getDeviceData().getNumberType());
            SharedPreferences userInfo = getSharedPreferences("user_info",MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor=userInfo.edit();
            editor.putString("deviceId",deviceData.getDeviceData().getDeviceId() );
            if(Camera.getNumberOfCameras()!=0){
                feature.syncUserFacePages(deviceData.getDeviceData().getDeviceId());
            }
            editor.putInt("numberType", deviceData.getDeviceData().getNumberType());
            editor.commit();
            if (!"".equals(deviceData.getDeviceData().getDeviceId())) {
                FileUtils.saveDataToFile(getContext(), deviceData.getDeviceData().getDeviceId(), "deviceId.text");
            }
            final CloudPushService pushService = PushServiceFactory.getCloudPushService();
            pushService.bindAccount(deviceData.getDeviceData().getDeviceId(), new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    connectivityManager =(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                    NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                    if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                    String  deviceID= FileUtils.loadDataFromFile(getContext(),"deviceId.text");
                    if (downLoadListner!=null) {
                        downLoadListner.start();
                    }
                        long count = Realm.getDefaultInstance().where(Person.class).count();
                    if (count==0) {
                        syncUserFeature.syncSign(deviceID);
                        syncUserFeature.syncUser(deviceID);
//                        feature.getPagesInfo(deviceID);
                        handler.sendEmptyMessageDelayed(0,1000);
                        //                        syncUserFeature.syncUser(deviceID);
                    }else {
                    }
                    Logger.e(TAG + "init cloudchannel bindAccount" +"deviceTargetValue:" + deviceData.getDeviceData().getDeviceId());
                    }else {
                        Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailed(String s, String s1) {
                }
            });
        }
    public static void setMainActivity(LockActivity activity) {
        mainActivity = activity;
    }
    static PushMessage pushMessage;
    public  static  Realm realm1;
    public static void setConsoleText(String text) {
        if(realm1==null){
            realm1 =  Realm.getDefaultInstance();
        }
        Logger.e("BaseApplication setConsoleText===================="+text);
        pushMessage=toJsonArray(text);
        String deviceId=FileUtils.loadDataFromFile(getContext(),"deviceId.text");
        if ("1".equals(pushMessage.getType())) {
            feature.download(pushMessage.getMessageId(), pushMessage.getAppid(), pushMessage.getShopId(), FileUtils.loadDataFromFile(getContext(), "deviceId.text"), pushMessage.getUid());
        } else if ("9".equals(pushMessage.getType())) {
            realm1.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    SignUser signUser = new SignUser();
                    signUser.setUid(pushMessage.getUid());
                    realm.copyToRealm(signUser);
                }
            });
        }else if("10".equals(pushMessage.getType())){
            if(Camera.getNumberOfCameras()!=0){
                Gson gson = new Gson();
                BindFaceMes bindFaceMes = gson.fromJson(text, BindFaceMes.class);
                DownLoad.download(bindFaceMes.getFaceUrl(),bindFaceMes.getUid());
            }
        }
        if("4".equals(pushMessage.getType())){
            Gson gson = new Gson();
            PushUpDateBean pushUpDateBean = gson.fromJson(text, PushUpDateBean.class);
            int device_type_id = pushUpDateBean.getDevice_type_id();
            if(device_type_id==4){
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "lingxi.apk");
                    if (file.exists()) {
                        file.delete();
                    }
                    Toast.makeText(getContext(), "通知栏下载中", Toast.LENGTH_SHORT).show();
                    DownloadUtils utils = new DownloadUtils(getContext());
                    utils.downloadAPK(pushUpDateBean.getPackage_path(), "lingxi.apk");
                    Logger.e(file.getAbsolutePath());
                }
            }
        }
    }
    public static PushMessage toJsonArray(String json) {
        try {
            pushMessage = new PushMessage();
            JSONObject object=new JSONObject(json);
            pushMessage.setType(object.getString("type"));
            pushMessage.setAppid(object.getString("appid"));
            pushMessage.setShopId(object.getString("shopId"));
            pushMessage.setUid(object.getString("uid"));
            pushMessage.setSendTime(object.getString("sendTime"));
            pushMessage.setMessageId(object.getString("messageId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pushMessage;
    }
    public interface downloafinish{
        void finish();
        void start();
    }

}
