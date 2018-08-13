package com.link.cloud.activity;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.sdk.android.push.AliyunMessageIntentService;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.link.cloud.BaseApplication;
import com.link.cloud.R;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.bean.ResultHeartBeat;
import com.link.cloud.contract.DeviceHeartBeatContract;
import com.link.cloud.contract.SyncUserFeature;
import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import butterknife.Bind;
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
    @Bind(R.id.edit_code)
    EditText edit_code;
    boolean ret=false;
    static LockActivity activity=null;
    private ConnectivityManager connectivityManager;//用于判断是否有网络
    private Toast mToast=null;
    private SyncUserFeature syncUserFeature;
    private MicroFingerVein microFingerVein;
    DeviceHeartBeatContract deviceHeartBeatContract;
    public static final String ACTION_NET = "com.link.cloud.noNet";
    String getName="deviceId.text";
    @Override
    public void onCreate() {
        super.onCreate();
        microFingerVein=MicroFingerVein.getInstance(this);
        deviceHeartBeatContract=new DeviceHeartBeatContract();
        deviceHeartBeatContract.attachView(this);
        Logger.e("WorkService"+"==========");
    }
    public static void setActactivity(LockActivity actactivity) {
        activity = actactivity;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new TimeThread().start();
        return Service.START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        new TimeThread().start();
        return myBinder;
    }
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void network(){
        connectivityManager =(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
        NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
        if (info == null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）

            showPromptToast(getResources().getString(R.string.network_error));
            final Intent intent = new Intent();
            intent.setAction(ACTION_NET);
            intent.putExtra("Network","1");
            sendBroadcast(intent);
//            Toast.makeText(WorkService.this, "检查网络连接是否打开", Toast.LENGTH_SHORT).show();
        } else { //当前有已激活的网络连接
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
    public class TimeThread extends Thread {
        Boolean isfirst=true;
        @Override
        public void run() {
            do {
                if(isfirst){
                        isfirst=false;
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                }
//                try {
//                    Thread.sleep(180000);
//                    String deviceId=FileUtils.loadDataFromFile(activity,getName);
//                    if (!"".equals(deviceId)) {
//                        deviceHeartBeatContract.deviceUpgrade(deviceId);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            } while (true);
        }
        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        mHandler.removeMessages(1);
                        network();
                        final Intent intent = new Intent();
                        intent.setAction(LockActivity.ACTION_UPDATEUI);
                        intent.putExtra("timeStr",getTime());
//                        intent.setAction(LockActivity.ACTION_UPDATE);
//                        intent.putExtra("timeData",getData());
                        sendBroadcast(intent);
                        mHandler.sendEmptyMessageDelayed(1,1000);
                        break;
                    default:
                        break;
                }
            }
        };
        //获得当前年月日时分秒星期
        public String getData(){
            String timeStr=null;
            String mMonth=null;
            String mDay=null;
            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
            if ((c.get(Calendar.MONTH) + 1)<10){
                mMonth = "0"+String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            }else {
                mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            }
            if(c.get(Calendar.DAY_OF_MONTH)<10){
                mDay = "0"+String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
            }else {
                mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
            }
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
            return mYear+"-"+mMonth + "-" + mDay;
        }
        public String getTime(){
            String timeStr=null;
            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
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
//            Logger.e("Workservice"+checknum(mtime)+":"+checknum(mMinute)+":"+checknum(seconds));
            return checknum(mtime)+":"+checknum(mMinute)+":"+checknum(seconds);
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
    public void deviceHearBeat(ResultHeartBeat resultResponse) {
    }
//
//    @Override
//    public void syncSignUserSuccess(DownLoadData downLoadData) {
//        Cursor cursor=null;
//        String sql;
//        sql = "select UID from PERSON";
//        String [] Uids=new String[cursor.getCount()];
//        int i=0;
//        String [] downUid=new String[downLoadData.getDown_userInfo().length];
//        cursor = BaseApplication.getInstances().getDaoSession().getDatabase().rawQuery(sql,null);
//        while (cursor.moveToNext()){
//            Uids[i]=cursor.getString(cursor.getColumnIndex("UID"));
//            i++;
//        }
////        SigePersonDao sigePersonDao=BaseApplication.getInstances().getDaoSession().getSigePersonDao();
//        if (downLoadData.getDown_userInfo().length>Uids.length){
//            for (int j=0;j<downLoadData.getDown_userInfo().length;j++){
////                SignPerson signPerson =new SignPerson();
////                signPerson.setId((long) j);
////                signPerson.setPos(j+"");
////                signPerson.setUserid(downLoadData.getDown_userInfo()[j].getUid());
////                sigePersonDao.insert(signPerson);
//            }
//        }
//    }
//    @Override
//    public void syncUserSuccess(DownLoadData resultResponse) {
//        PersonDao personDao = BaseApplication.getInstances().getDaoSession().getPersonDao();
//        personDao.deleteAll();
//        Person person = new Person();
//        if (resultResponse.getDown_userInfo().length >= 0) {
//            for (int i = 0; i < resultResponse.getDown_userInfo().length; i++) {
//                person.setId((long)i+1);
//                person.setUid(resultResponse.getDown_userInfo()[i].getUid());
//                person.setNumber(resultResponse.getDown_userInfo()[i].getUserName());
//                person.setPos(i+"");
//                person.setFingermodel(resultResponse.getDown_userInfo()[i].getFeature());
//                personDao.insert(person);
//            }
//        }
//        showPromptToast("同步成功");
//    }
    @Override
    public void onPermissionError(ApiException e) {
    }
    @Override
    public void onError(ApiException e) {
    }
    @Override
    public void onResultError(ApiException e) {
    }
    private final static String TAG=WorkService.class.getSimpleName()+"_DEBUG";
    private final static String ACTION_USB_PERMISSION = "com.android.USB_PERMISSION";
    private MyBinder myBinder=new MyBinder();
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MicroFingerVein.USB_HAS_REQUST_PERMISSION: {//请求usb授权；
//                    Log.e(TAG,"usb has request permission.");
                    UsbDevice usbDevice=(UsbDevice)msg.obj;
                    UsbManager mManager=(UsbManager)getSystemService(Context.USB_SERVICE);
                    PendingIntent mPermissionIntent = PendingIntent.getBroadcast(getApplicationContext(),0, new Intent(ACTION_USB_PERMISSION), 0);
                    if(mManager==null){
                        mManager=(UsbManager)getSystemService(Context.USB_SERVICE);
                    }
                    mManager.requestPermission(usbDevice,mPermissionIntent);
                    break;
                }
                case MicroFingerVein.USB_CONNECT_SUCESS: {//打印usb节点信息；
//                    Log.e(TAG,"get usb connect info success.");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        UsbDevice usbDevice = (UsbDevice) msg.obj;

                        if(WorkService.this.usbMsgCallback!=null){
                            usbMsgCallback.onUsbConnSuccess(usbDevice.getManufacturerName(),usbDevice.getDeviceName());
                        }
                    }
                    break;
                }
                case MicroFingerVein.USB_DISCONNECT: {//usb 连接已断开；
//                    Log.e(TAG,"usb disconnected.");
                    if(WorkService.this.usbMsgCallback!=null){
                        usbMsgCallback.onUsbDisconnect();
                    }
                    break;
                }
                case MicroFingerVein.UsbDeviceConnection: {//接收device连接器对象；
//                    Log.e(TAG, "usb device connection OK.");
                    if (msg.obj!=null) {
                        if(WorkService.this.usbMsgCallback!=null){
                            usbMsgCallback.onUsbDeviceConnection((UsbDeviceConnection)msg.obj);
                        }
                    }
                    break;
                }
                default: {
                    Log.e(TAG, "undefined msg!(what=" + msg.what + ")");
                    break;
                }
            }
            return false;
        }
    });
    private UsbMsgCallback usbMsgCallback;
    public interface UsbMsgCallback{
        /***
         *  when find a new usb device;
         *  @param usbManufacturerName manufacturer name of the new usb device;
         *  @param usbDeviceName device name of the new usb device;
         */
        void onUsbConnSuccess(String usbManufacturerName, String usbDeviceName);
        /**
         *  when the md usb device disconnected;
         */
        void onUsbDisconnect();
        /**
         *  when connect md device success;
         */
        void onUsbDeviceConnection(UsbDeviceConnection usbDevConn);
    }


    public class MyBinder extends Binder {
        /**
         *  return a MicroFingerVein object for operate md device;
         */
        public MicroFingerVein getMicroFingerVeinInstance(){
            return WorkService.this.microFingerVein;
        }
        /**
         *  set a UsbMsgCallback object for the service to callback custom operating of usb msg;
         */
        public void setOnUsbMsgCallback(UsbMsgCallback usbMsgCallback){
            WorkService.this.usbMsgCallback=usbMsgCallback;
        }
    }
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
//        BaseApplication.setConsoleText("收到一条推送通知 ： " + title + ", summary:" + summary);
    }
    /**
     * 推送消息的回调方法
     * @param context
     * @param cPushMessage
     */
    @Override
    protected void onMessage(Context context, CPushMessage cPushMessage) {
        Log.i(REC_TAG,"收到一条推送消息 ： " + cPushMessage.getTitle()+cPushMessage.getTraceInfo()+", content:" + cPushMessage.getContent());
        BaseApplication.setConsoleText(cPushMessage.getContent());
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