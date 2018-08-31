
package com.link.cloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
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
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.link.cloud.activity.NewMainActivity;
import com.link.cloud.activity.WelcomeActivity;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.BindFaceMes;
import com.link.cloud.bean.DeviceData;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.bean.PagesInfoBean;
import com.link.cloud.bean.PushMessage;
import com.link.cloud.bean.PushUpDateBean;
import com.link.cloud.bean.SyncFeaturesPage;
import com.link.cloud.bean.SyncUserFace;
import com.link.cloud.bean.UpDateBean;
import com.link.cloud.component.TimeService;
import com.link.cloud.constant.Constant;
import com.link.cloud.contract.DownloadFeature;
import com.link.cloud.contract.GetDeviceIDContract;
import com.link.cloud.contract.SyncUserFeature;
import com.link.cloud.greendao.gen.DaoMaster;
import com.link.cloud.greendao.gen.DaoSession;
import com.link.cloud.greendao.gen.PersonDao;
import com.link.cloud.greendaodemo.HMROpenHelper;
import com.link.cloud.greendaodemo.Person;
import com.link.cloud.utils.DownLoad;
import com.link.cloud.utils.DownloadUtils;
import com.link.cloud.utils.FaceDB;
import com.link.cloud.utils.FileUtils;
import com.link.cloud.utils.Utils;
import com.link.cloud.utils.VenueUtils;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description：BaseApplication
 * Created by Shaozy on 2016/8/10.
 */
public class BaseApplication extends MultiDexApplication  implements GetDeviceIDContract.VersoinUpdate,DownloadFeature.download,SyncUserFeature.syncUser{
    public static boolean DEBUG = false;
    private static BaseApplication ourInstance = new BaseApplication();
    public boolean log = true;
    public boolean flag;
    public Gson gson;
    private static Context context;
    public static final long ONE_KB = 1024L;
    public static final long ONE_MB = ONE_KB * 1024L;
    public static final long CACHE_DATA_MAX_SIZE = ONE_MB * 3L;
    private static final String TAG = "BaseApplication";
    private static NewMainActivity mainActivity = null;
    public static BaseApplication getInstance() {
        return ourInstance;
    }
    PersonDao personDao;
    String deviceTargetValue;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    public static String deviceID;
    static SyncUserFeature syncUserFeature;
    GetDeviceIDContract presenter;
    public static BaseApplication instances;
    static DownloadFeature downloadFeature;
    static boolean ret = false;
    public FaceDB mFaceDB;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public static BaseApplication getInstances() {
        return instances;
    }
    public static VenueUtils venueUtils;
    public  static VenueUtils getVenueUtils(){
        synchronized (VenueUtils.class) {
            if (venueUtils==null){
                return new VenueUtils();
            }
            return venueUtils;
        }
    }
    private List<Person> people = new ArrayList<>();
    public List<Person> getPerson(){
        return people;
    }
    public void setRet(boolean ret) {
        this.ret = ret;
    }
    public int count =0;
    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler.getInstance().init(this);
//        LogcatHelper.getInstance(this).start();
        CrashReport.initCrashReport(getApplicationContext(), "62ab7bf668", true);
        setDatabase();
        instances = this;
        ourInstance = this;
        context=getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
        mFaceDB = new FaceDB(Environment.getExternalStorageDirectory().getAbsolutePath() + "/faceFile");
        handler.sendEmptyMessage(1);
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
        Intent intent = new Intent(getApplicationContext(), TimeService.class);
        startService(intent);
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            DEBUG = appInfo.metaData.getBoolean("DEBUG");
        } catch (Exception e) {
            DEBUG = false;
        }

        Logger.init("S1 Vip Manages")               // default tag : PRETTYLOGGER or use just init()
                .hideThreadInfo();             // default it is shown
        this.initGson();
        this.initReservoir();
        this.initCCPRestSms();
        TTSIf();
        syncUserFeature=new SyncUserFeature();
        syncUserFeature.attachView(this);
        presenter=new GetDeviceIDContract();
        presenter.attachView(this);
        downloadFeature=new DownloadFeature();
        downloadFeature.attachView(this);
        initCloudChannel(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                people.addAll(getDaoSession().getPersonDao().loadAll());
            }
        }).start();

        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }
    void TTSIf(){
        // 应用程序入口处调用,避免手机内存过小,杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用“,”分隔。
        // 设置你申请的应用appid
        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
        StringBuffer param = new StringBuffer();
        param.append("appid="+getString(R.string.app_id));
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(BaseApplication.this, param.toString());
    }
    public static Context getContext() {
        return context;
    }
    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
//        DaoMaster.DevOpenHelper mHelpter = new DaoMaster.DevOpenHelper(this,"notes-db");
        HMROpenHelper mHelpter = new HMROpenHelper(this, "notes-db", null);//为数据库升级封装过的使用方式
        db = mHelpter.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public boolean isRet() {
        return ret;
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
    //     */
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
     *
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
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                deviceTargetValue = Utils.getMD5(getMac());
                presenter.getDeviceID(deviceTargetValue,1);
                Logger.e(TAG + "init cloudchannel success" +"   deviceTargetValue:" + deviceTargetValue);
//                        }
//                        Log.i(TAG, "init cloudchannel success" + deviceTargetValue);
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.e(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
//                setConsoleText("init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
//        MiPushRegister.register(applicationContext, "XIAOMI_ID", "XIAOMI_KEY"); // 初始化小米辅助推送
//        HuaWeiRegister.register(applicationContext); // 接入华为辅助推送
//        GcmRegister.register(applicationContext, "send_id", "application_id"); // 接入FCM/GCM初始化推送
    }
    /**
     * 添加推送账户
     */
    private void devicebindAccount (final Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.bindAccount(Utils.getMD5(getMac()), new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                deviceTargetValue = Utils.getMD5(getMac());
                presenter.getDeviceID(deviceTargetValue,1);
                Logger.e(TAG + "init cloudchannel success" + "   deviceTargetValue:" + deviceTargetValue);
            }
            @Override
            public void onFailed(String s, String s1) {
                Log.e(TAG, "init cloudchannel failed -- errorcode:" + s + " -- errorMessage:" + s1);
            }
        });
    }
    public static String getMac(){
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig","HWaddr");
        //如果返回的result == null，则说明网络不可取
        if(result==null){
            return "网络出错，请检查网络";
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
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.removeCallbacksAndMessages(null);
            switch (msg.what){
                case 0:

                    String s = FileUtils.loadDataFromFile(getContext(), "deviceId.text");
                    connectivityManager =(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                    NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                    if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                        try {
                            downloadFeature.downloadNotReceiver(s);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(getContext(), "网络已断开，请查看网络", Toast.LENGTH_LONG).show();
                    }
                    handler.sendEmptyMessageDelayed(0,30*1000);
                    break;
                case 1:
                    handler.removeMessages(1);
                    if(time==null){
                        time=new Intent();
                    }
                    time.setAction(NewMainActivity.ACTION_UPDATEUI);
                    time.putExtra("timethisStr",getthisTime());
                    time.putExtra("timeStr",getTime());
                    time.putExtra("timeData",getData());
                    sendBroadcast(time);
                    handler.sendEmptyMessageDelayed(1,1000);
                    break;
            }

        }
    };
    public String getthisTime(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int mtime=c.get(Calendar.HOUR_OF_DAY);
        int mHour = c.get(Calendar.HOUR);//时
        int mMinute = c.get(Calendar.MINUTE);//分
        int seconds=c.get(Calendar.SECOND);
        return checknum(mtime)+":"+checknum(mMinute)+":"+checknum(seconds);
    }
    Intent time;
    //获得当前年月日时分秒星期
    public String getData(){
        String timeStr=null;
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        int mtime=c.get(Calendar.HOUR_OF_DAY);
        int mHour = c.get(Calendar.HOUR);//时
        int mMinute = c.get(Calendar.MINUTE);//分
        int seconds=c.get(Calendar.SECOND);
        if (mtime>=0&&mtime<=5){
            timeStr="凌晨";
        }else if (mtime>5&&mtime<8){
            timeStr="早晨";
        }else if(mtime>8&&mtime<12){
            timeStr="上午";
        }else if(mtime>=12&&mtime<14){
            timeStr="中午";
        }else if(mtime>=14&&mtime<18){
            timeStr="下午";
        }else if(mtime>=18&&mtime<19){
            timeStr="傍晚";
        }else if(mtime>=19&&mtime<=22){
            timeStr="晚上";
        }else if(mtime>22){
            timeStr="深夜";
        }
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return mMonth + "月" + mDay+"日"+"|"+"星期"+mWay;
    }
    public String getTime(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int mtime=c.get(Calendar.HOUR_OF_DAY);
        int mHour = c.get(Calendar.HOUR);//时
        int mMinute = c.get(Calendar.MINUTE);//分
        int seconds=c.get(Calendar.SECOND);
        return checknum(mHour)+":"+checknum(mMinute);
    }
    private String checknum(int num){
        String strnum=null;
        if (num<10){
            strnum="0"+num;
        }else {
            strnum=num+"";
        }
        return strnum;
    }
    public static final String COUNT_CHANGE = "change_count";
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
    public static void setMainActivity(NewMainActivity activity) {
        mainActivity = activity;
    }
    @Override
    public void onResultError(ApiException e) {
        Logger.e("onError======="+e.getDisplayMessage());
        if(downLoadListner!=null){
            downLoadListner.finish();
        }
    }
    @Override
    public void onError(ApiException e) {
        Logger.e("onError======="+e.getDisplayMessage());
        if(downLoadListner!=null){
            downLoadListner.finish();
        }

    }
    @Override
    public void onPermissionError(ApiException e) {
        Logger.e("onError======="+e.getDisplayMessage());
        if(downLoadListner!=null){
            downLoadListner.finish();
        }

    }
    @Override
    public void syncUserSuccess(DownLoadData resultResponse) {

        personDao=BaseApplication.getInstances().getDaoSession().getPersonDao();
        List<Person> list=BaseApplication.getInstances().getDaoSession().getPersonDao().loadAll();
        if (resultResponse.getData().size()!=list.size()) {
            if (resultResponse.getData().size() > 0) {
                personDao = BaseApplication.getInstances().getDaoSession().getPersonDao();
                personDao.deleteAll();
                personDao.insertInTx(resultResponse.getData());
            }
        }
        if(downLoadListner!=null){
            downLoadListner.finish();
        }
        List<Person>personList=personDao.loadAll();
        connectivityManager =(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
        NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
        if (info != null) { //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）

        }
        Logger.e("=====================数据同步"+personList.size());
    }
    @Override
    public void downloadNotReceiver(DownLoadData resultResponse) {
        PersonDao personDao=BaseApplication.getInstances().getDaoSession().getPersonDao();
        if(resultResponse.getData().size()>0){
            people.addAll(resultResponse.getData());
            for(int x=0;x<resultResponse.getData().size();x++){
                Person person = new Person();
                person.setFeature(resultResponse.getData().get(x).getFeature());
                person.setUid(resultResponse.getData().get(x).getUid());
                person.setUserType(resultResponse.getData().get(x).getUserType());
                person.setFingerId(resultResponse.getData().get(x).getFingerId());
                personDao.insert(person);
            }

        }
    }
    public downloafinish downLoadListner;
    public void setDownLoadListner(downloafinish downLoadListner){
        this.downLoadListner=downLoadListner;
    }
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
    @Override
    public void downloadApK(UpDateBean resultResponse) {
        int version = getVersion(getApplicationContext());
        Log.e(TAG, "downloadApK: "+version);
        if(version<resultResponse.getData().getPackage_version()){
            Log.e(TAG, "downloadApK: "+version);
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
            Toast.makeText(this, "通知栏下载中", Toast.LENGTH_SHORT).show();
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
    ArrayList<Person> SyncFeaturesPages = new ArrayList<>();
    int totalPage=0,currentPage=1,downloadPage=0;
    @Override
    public void getPagesInfo(PagesInfoBean resultResponse) {
        totalPage = resultResponse.getData().getPageCount();
        if(totalPage==0){
            downLoadListner.finish();
        }
        ExecutorService service = Executors.newFixedThreadPool(1);
        for(int x =0 ;x<resultResponse.getData().getPageCount();x++){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    downloadFeature.syncUserFeaturePages(FileUtils.loadDataFromFile(getContext(), "deviceId.text"), currentPage++);
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
                PersonDao personDao = BaseApplication.getInstances().getDaoSession().getPersonDao();
                personDao.insertInTx(SyncFeaturesPages);
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
    @Override
    public void downloadSuccess(DownLoadData resultResponse) {
        PersonDao personDao=BaseApplication.getInstances().getDaoSession().getPersonDao();
        if(resultResponse.getData().size()>0){
            people.addAll(resultResponse.getData());
            for(int x=0;x<resultResponse.getData().size();x++){
                Person person = new Person();
                person.setFeature(resultResponse.getData().get(x).getFeature());
                person.setUid(resultResponse.getData().get(x).getUid());
                person.setUserType(resultResponse.getData().get(x).getUserType());
                person.setFingerId(resultResponse.getData().get(x).getFingerId());
                personDao.insert(person);
            }

        }
    }
    class ResultData<T>{
        T data;
        String msg;
    }
    ConnectivityManager connectivityManager;
    @Override
    public void getDeviceSuccess(DeviceData deviceData) {
        downloadFeature.appUpdateInfo(deviceData.getDeviceData().getDeviceId());
        Logger.e("BaseApplication+devicedate"+deviceData.getDeviceData().getDeviceId()+"numberType"+deviceData.getDeviceData().getNumberType());
        SharedPreferences userInfo = getSharedPreferences("user_info",0);
        if (!"".equals(deviceData.getDeviceData().getDeviceId())){

            userInfo.edit().putString("deviceId", deviceData.getDeviceData().getDeviceId()).commit();

            if(android.hardware.Camera.getNumberOfCameras()!=0){

                downloadFeature.syncUserFacePages(deviceData.getDeviceData().getDeviceId());
            }
            // userInfo.edit().putString("deviceId", "1000UVL4LKR").commit();
        }
        userInfo.edit().putInt("numberType",deviceData.getDeviceData().getNumberType()).commit();
        FileUtils.saveDataToFile(getContext(),deviceData.getDeviceData().getDeviceId(),"deviceId.text");
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.bindAccount(deviceData.getDeviceData().getDeviceId(), new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                connectivityManager =(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                    PersonDao personDao= getDaoSession().getPersonDao();
                    List<Person>list=personDao.loadAll();
                    if (list.size()==0) {
//                            syncUserFeature.syncUser(deviceData.getDeviceData().getDeviceId());
                        downloadFeature.getPagesInfo(deviceData.getDeviceData().getDeviceId());
                        if (downLoadListner != null) {
                            downLoadListner.start();
                        }

                    }else {


                    }
                    if(deviceData.getDeviceData().getDeviceId()!=null) {
                        handler.sendEmptyMessageDelayed(0, 1000);

                    }
                }else {

                    Toast.makeText(getContext(),getResources().getString(R.string.please_check_net),Toast.LENGTH_LONG).show();
                }
                Logger.e(TAG + "init cloudchannel bindAccount" +"deviceTargetValue:" + deviceData.getDeviceData().getDeviceId());
            }
            @Override
            public void onFailed(String s, String s1) {

            }
        });
    }
    public static void setConsoleText(String text) {
        if (mainActivity != null && text != null) {
            mainActivity.todialog(text);
        }
        pushMessage=toJsonArray(text);
        messageId=pushMessage.getMessageId();
        appid=pushMessage.getAppid();
        shopId=pushMessage.getShopId();
        uid=pushMessage.getUid();
        if ("1".equals(pushMessage.getType())){
            downloadFeature.download(messageId,appid,shopId,FileUtils.loadDataFromFile(getContext(),"deviceId.text"),uid);
//            syncUserFeature.syncUser(FileUtils.loadDataFromFile(getContext(),"deviceId.text"));
        }
        if("10".equals(pushMessage.getType())&& android.hardware.Camera.getNumberOfCameras()!=0){
            Gson gson = new Gson();
            BindFaceMes bindFaceMes = gson.fromJson(text, BindFaceMes.class);
            DownLoad.download(bindFaceMes.getFaceUrl(),bindFaceMes.getUid());
        }
        if("4".equals(pushMessage.getType())){
            Gson gson = new Gson();
            PushUpDateBean pushUpDateBean = gson.fromJson(text, PushUpDateBean.class);
            int device_type_id = pushUpDateBean.getDevice_type_id();
            if(device_type_id==1){
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
    private static String  appid,shopId,uid,messageId;
    static JSONObject  object;
    private static PushMessage pushMessage;
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