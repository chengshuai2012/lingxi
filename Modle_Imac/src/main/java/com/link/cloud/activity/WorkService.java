package com.link.cloud.activity;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.push.AliyunMessageIntentService;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.link.cloud.BaseApplication;
import com.link.cloud.R;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.ResultHeartBeat;
import com.link.cloud.contract.BindTaskContract;
import com.link.cloud.contract.DeviceHeartBeatContract;
import com.link.cloud.greendao.gen.PersonDao;
import com.link.cloud.utils.FileUtils;
import com.link.cloud.view.ProgressHUD;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import md.com.sdk.MicroFingerVein;
import static com.link.cloud.component.MyMessageReceiver.REC_TAG;
/**
 * Created by Administrator on 2017/8/16.
 */
/**
 * Created by liyazhou on 17/8/22.
 * 为避免推送广播被系统拦截的小概率事件,我们推荐用户通过IntentService处理消息互调,接入步骤:
 * 1. 创建IntentService并继承AliyunMessageIntentService
 * 2. 覆写相关方法,并在Manifest的注册该Service
 * 3. 调用接口CloudPushService.setPushIntentService
 * 详细用户可参考:https://help.aliyun.com/document_detail/30066.html#h2-2-messagereceiver-aliyunmessageintentservice
 */
public class WorkService extends AliyunMessageIntentService implements DeviceHeartBeatContract.Devicehearbeat{
    BindTaskContract bindTaskContract;
    Handler handler=new Handler();
    private ConnectivityManager connectivityManager;//用于判断是否有网络
    private Toast mToast=null;
    static boolean ret=false;
    int[] state =new int[1];
    private volatile boolean bRun=true;
    public static MicroFingerVein microFingerVein;
    static Application application;
    private static UsbManager mUsbManager;
    static Activity activity=null;
    DeviceHeartBeatContract deviceHeartBeatContract;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    @Override
    public void onCreate() {
        super.onCreate();
        deviceHeartBeatContract=new DeviceHeartBeatContract();
        deviceHeartBeatContract.attachView(this);
        Logger.e("WorkService"+"onCreate");

//        reflectActivity();
    }

    public static void setActactivity(Activity actactivity) {
        activity = actactivity;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new TimeThread().start();
        new HeartBeatThread().start();
        return Service.START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
    @Override
    public void onDestroy() {
        microFingerVein.close();
        super.onDestroy();
    }
    private void network(){
        connectivityManager =(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
        NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
        if (info == null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
            showPromptToast("网络已断开");
//            Toast.makeText(WorkService.this, "检查网络连接是否打开", Toast.LENGTH_SHORT).show();
        } else {              //当前有已激活的网络连接
        }
    }
    public void showPromptToast(String promptWord) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), promptWord,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(promptWord);
        }
        mToast.show();
    }
//    int cnt;
//    NewMainActivity mainActivity;
    private void checkMD(){
//        SharedPreferences user_ret1=getSharedPreferences("user_info",0);
//        ret=user_ret1.getBoolean("ret_service",false);
        if (activity!=null&&ret!=true) {
            microFingerVein = MicroFingerVein.getInstance(activity);
            int devicecount=microFingerVein.fvdev_get_count();
            if (devicecount!=0) {
                ret = microFingerVein.fvdev_open();
//                SharedPreferences user_ret=getSharedPreferences("user_info",0);
//                user_ret.edit().putBoolean("ret_service",ret);
//                Logger.e("WorkService"+"devicecount"+devicecount + "=====================" + ret);
            }
        }
    }
    Boolean isfirst=true;
    public class HeartBeatThread extends Thread{
        @Override
        public void run() {
            do {
//                try {
//                    Thread.sleep(180000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if (!"".equals(FileUtils.loadDataFromFile(activity,"deviceId.text"))) {
//                    deviceHeartBeatContract.deviceUpgrade(FileUtils.loadDataFromFile(activity,"deviceId.text"));
//                }
            }while (true);

        }
    }
    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                checkMD();
                if(isfirst){
                    isfirst=false;
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 2;
                mHandler.sendMessage(msg);
                }
            } while (true);
        }
        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        network();
                        final Intent intent = new Intent();
                        intent.setAction(NewMainActivity.ACTION_UPDATEUI);
                        intent.putExtra("timethisStr",getthisTime());
                        intent.putExtra("timeStr",getTime());
                        intent.putExtra("timeData",getData());
                        sendBroadcast(intent);
                        mHandler.sendEmptyMessageDelayed(1,1000);
                        break;
                    case MicroFingerVein.USB_HAS_REQUST_PERMISSION:
                    {
                        UsbDevice  usbDevice=(UsbDevice) msg.obj;
                        UsbManager mManager=(UsbManager)getSystemService(Context.USB_SERVICE);
                        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(activity, 0, new Intent(ACTION_USB_PERMISSION), 0);
                        if(mManager == null)
                        {
                            mManager=(UsbManager)getSystemService(Context.USB_SERVICE);
                            IntentFilter filter = new IntentFilter();
                        }
                        mManager.requestPermission(usbDevice,mPermissionIntent);
                    }
                    break;
                    case MicroFingerVein.USB_CONNECT_SUCESS: {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            UsbDevice  usbDevice=(UsbDevice) msg.obj;
                            Logger.e(usbDevice.getManufacturerName()+"  节点："+usbDevice.getDeviceName());
                        }
                    }
                    break;
                    case MicroFingerVein.USB_DISCONNECT:{
//                        ret=false;
//                        deviceTouchState=2;
//                        handler.obtainMessage(MSG_SHOW_LOG,"device disconnected.");
//                        bopen=false;
//                        bt_model.setText("开始建模");
//                        bt_identify.setText("开始认证");
                    }
                    break;
                    case MicroFingerVein.UsbDeviceConnection: {
//                        ret=false;
//                        handler.obtainMessage(MSG_SHOW_LOG,"UsbDeviceConnection.");
                        //----------------------------------------------
                        if(msg.obj!=null) {
                            UsbDeviceConnection usbDevConn=(UsbDeviceConnection)msg.obj;
                        }
                        //----------------------------------------------
                        //if(msg.obj!=null) {
                        //   microFingerVein.close();
                        //}
                        //----------------------------------------------
                    }
                    break;
                }
            }
        };

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
        public String getthisTime(){
            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            int mtime=c.get(Calendar.HOUR_OF_DAY);
            int mHour = c.get(Calendar.HOUR);//时
            int mMinute = c.get(Calendar.MINUTE);//分
            int seconds=c.get(Calendar.SECOND);
            return checknum(mtime)+":"+checknum(mMinute)+":"+checknum(seconds);
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
    }

    @Override
    public void onError(ApiException e) {

    }

    @Override
    public void onPermissionError(ApiException e) {

    }

    @Override
    public void onResultError(ApiException e) {

    }

    @Override
    public void deviceHearBeat(ResultHeartBeat deviceHeartBeat) {

    }
    //    private PersonDao personDao;
//    byte[] featuer = null;
//    int i=0;
//    void  executeSql() {
//        personDao = BaseApplication.getInstances().getDaoSession().getPersonDao();
//        personDao.loadAll();
//        String sql = "select FINGERMODEL from PERSON" ;
//        Cursor cursor = BaseApplication.getInstances().getDaoSession().getDatabase().rawQuery(sql,null);
//        byte[][] feature=new byte[cursor.getCount()][];
//        while (cursor.moveToNext()){
////            Logger.e("SigeActivity----no---");
//            int nameColumnIndex = cursor.getColumnIndex("FINGERMODEL");
//            String strValue=cursor.getString(nameColumnIndex);
////            Logger.e("SigeActivity-------"+strValue);
//            feature[i]=hexStringToByte(strValue);
//            if (cursor.getCount()>1) {
//                i++;
//            }
//        }
//        int len = 0;
//        // 计算一维数组长度
//        for (byte[] element : feature) {
//            len += element.length;
//        }
//        // 复制元素
//        featuer = new byte[len];
//        int index = 0;
//        for (byte[] element : feature) {
//            for (byte element2 : element) {
//                featuer[index++] = element2;
//            }
//        }
//    }
//    /**
//     * 字节数组转换为十六进制字符串
//     *
//     * @param b
//     *            byte[] 需要转换的字节数组
//     * @return String 十六进制字符串
//     */
//    public static final String byte2hex(byte b[]) {
//        if (b == null) {
//            throw new IllegalArgumentException(
//                    "Argument b ( byte array ) is null! ");
//        }
//        String hs = "";
//        String stmp = "";
//        for (int n = 0; n < b.length; n++) {
//            stmp = Integer.toHexString(b[n] & 0xff);
//            if (stmp.length() == 1) {
//                hs = hs + "0" + stmp;
//            } else {
//                hs = hs + stmp;
//            }
//        }
//        return hs.toUpperCase();
//    }
//    /**
//     * 把16进制字符串转换成字节数组
//     * @param hex
//     * @return byte[]
//     */
//    public static byte[] hexStringToByte(String hex) {
//        int len = (hex.length() / 2);
//        byte[] result = new byte[len];
//        char[] achar = hex.toCharArray();
//        for (int i = 0; i < len; i++) {
//            int pos = i * 2;
//            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
//        }
//        return result;
//    }
//    private static int toByte(char c) {
//        byte b = (byte) "0123456789ABCDEF".indexOf(c);
//        return b;
//    }

    /**
     * 推送通知的回调方法
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    protected void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        Log.i(REC_TAG,"收到一条推送通知 ： " + title + ", summary:" + summary);
        BaseApplication.setConsoleText("收到一条推送通知 ： " + title + ", summary:" + summary);
    }
    /**
     * 推送消息的回调方法
     * @param context
     * @param cPushMessage
     */
    @Override
    protected void onMessage(Context context, CPushMessage cPushMessage) {
        Log.i(REC_TAG,"收到一条推送消息 ： " + cPushMessage.getTitle()+cPushMessage.getTraceInfo()+", content:" + cPushMessage.getContent());
        BaseApplication.setConsoleText( cPushMessage.getContent());
    }
    /**
     * 从通知栏打开通知的扩展处理
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    protected void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.i(REC_TAG,"onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
        BaseApplication.setConsoleText("onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
    }
    /**
     * 无动作通知点击回调。当在后台或阿里云控制台指定的通知动作为无逻辑跳转时,通知点击回调为onNotificationClickedWithNoAction而不是onNotificationOpened
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.i(REC_TAG,"onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
        BaseApplication.setConsoleText("onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
    }
    /**
     * 通知删除回调
     * @param context
     * @param messageId
     */
    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.i(REC_TAG, "onNotificationRemoved ： " + messageId);
        BaseApplication.setConsoleText("onNotificationRemoved ： " + messageId);
    }
    /**
     * 应用处于前台时通知到达回调。注意:该方法仅对自定义样式通知有效,相关详情请参考https://help.aliyun.com/document_detail/30066.html#h3-3-4-basiccustompushnotification-api
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     * @param openType
     * @param openActivity
     * @param openUrl
     */
    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.i(REC_TAG,"onNotificationReceivedInApp ： " + " : " + title + " : " + summary + "  " + extraMap + " : " + openType + " : " + openActivity + " : " + openUrl);
        BaseApplication.setConsoleText("onNotificationReceivedInApp ： " + " : " + title + " : " + summary);
    }

}