package com.link.cloud.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.arcsoft.ageestimation.ASAE_FSDKEngine;
import com.arcsoft.ageestimation.ASAE_FSDKError;
import com.arcsoft.ageestimation.ASAE_FSDKVersion;
import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKMatching;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.arcsoft.facetracking.AFT_FSDKEngine;
import com.arcsoft.facetracking.AFT_FSDKError;
import com.arcsoft.facetracking.AFT_FSDKFace;
import com.arcsoft.facetracking.AFT_FSDKVersion;
import com.arcsoft.genderestimation.ASGE_FSDKEngine;
import com.arcsoft.genderestimation.ASGE_FSDKError;
import com.arcsoft.genderestimation.ASGE_FSDKVersion;
import com.guo.android_extend.java.AbsLoop;
import com.guo.android_extend.tools.CameraHelper;
import com.guo.android_extend.widget.CameraFrameData;
import com.guo.android_extend.widget.CameraGLSurfaceView;
import com.guo.android_extend.widget.CameraSurfaceView;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.link.cloud.BaseApplication;
import com.link.cloud.R;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.bean.Lockdata;
import com.link.cloud.bean.PagesInfoBean;
import com.link.cloud.bean.Person;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.bean.SignUser;
import com.link.cloud.bean.Sign_data;
import com.link.cloud.bean.SyncFeaturesPage;
import com.link.cloud.bean.SyncUserFace;
import com.link.cloud.bean.UpDateBean;
import com.link.cloud.constant.Constant;
import com.link.cloud.contract.DownloadFeature;
import com.link.cloud.contract.IsopenCabinet;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.contract.SyncUserFeature;
import com.link.cloud.core.BaseAppCompatActivity;
import com.link.cloud.gpiotest.Gpio;
import com.link.cloud.message.MessageEvent;
import com.link.cloud.model.MdFvHelper;
import com.link.cloud.setting.TtsSettings;
import com.link.cloud.utils.APKVersionCodeUtils;
import com.link.cloud.utils.FaceDB;
import com.link.cloud.utils.FileUtils;
import com.link.cloud.utils.Finger_identify;
import com.link.cloud.utils.ToastUtils;
import com.link.cloud.utils.Utils;
import com.link.cloud.view.ExitAlertDialog;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import md.com.sdk.MicroFingerVein;

/**
 * Created by 30541 on 2018/3/12.
 */
public class LockActivity extends BaseAppCompatActivity implements IsopenCabinet.isopen,SyncUserFeature.syncUser,SendLogMessageTastContract.sendLog,DownloadFeature.download,CameraSurfaceView.OnCameraListener, View.OnTouchListener, Camera.AutoFocusCallback{
    @Bind(R.id.head_text_01)
    TextView head_text_01;
    @Bind(R.id.head_text_02)
    TextView head_text_02;
    @Bind(R.id.head_text_03)
    TextView head_text_03;
    @Bind(R.id.button02)
    Button button02;
    @Bind(R.id.button1)
    Button button1;
    @Bind(R.id.button2)
    Button button2;
    @Bind(R.id.button3)
    Button button3;
    @Bind(R.id.button4)
    Button button4;
    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.text_error)
    TextView text_error;
    IsopenCabinet isopenCabinet;
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    int state =0;
    boolean ret = false;
    SyncUserFeature syncUserFeature;
    String deviceId;
    public MesReceiver mesReceiver;
    private final static int MSG_SHOW_LOG=0;
    BaseApplication baseApplication;
    public  MicroFingerVein microFingerVein;
    String gpiostr;
    String userUid;
    public SendLogMessageTastContract sendLogMessageTastContract;
    public static final String ACTION_UPDATEUI = "com.link.cloud.dataTime";
    private static String ACTION_USB_PERMISSION = "com.android.USB_PERMISSION";
    private final static float IDENTIFY_SCORE_THRESHOLD=0.63f;//认证通过的得分阈值，超过此得分才认为认证通过；
    SharedPreferences userinfo;
    String gpiotext="";
    String TAG="LockActivity";
    private UsbDeviceConnection usbDevConn;
    ExitAlertDialog exitAlertDialog;
    // 语音合成对象
    public SpeechSynthesizer mTts;
    // 默认本地发音人
    public static String voicerLocal="xiaoyan";
    // 本地发音人列表
    private String[] localVoicersEntries;
    private String[] localVoicersValue ;
    // 云端/本地选择按钮
    private RadioGroup mRadioGroup;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    DownloadFeature downloadFeature;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    private Realm realm;
    private RealmResults<Person> all;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        baseApplication=(BaseApplication)getApplication();
        EventBus.getDefault().register(this);
        // 初始化合成对象
        Log.e(TAG, Camera.getNumberOfCameras()+">>>>>>>>>>>>>>>>");
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        sendLogMessageTastContract=new SendLogMessageTastContract();
        sendLogMessageTastContract.attachView(this);
//        TTSUtils.getInstance().init(this);
        exitAlertDialog=new ExitAlertDialog(this);
        exitAlertDialog.setCanceledOnTouchOutside(false);
        exitAlertDialog.setCancelable(false);
        BaseApplication.setMainActivity(this);
        WorkService.setActactivity(this);
        downloadFeature=new DownloadFeature();
        downloadFeature.attachView(this);
        setupExtra();
        sendLogMessageTastContract=new SendLogMessageTastContract();
        sendLogMessageTastContract.attachView(this);
        setParam();
        mEngineType =  SpeechConstant.TYPE_LOCAL;
        mTts.startSpeaking("初始化成功", mTtsListener);
        realm = Realm.getDefaultInstance();
        RealmResults<Person> allAsync = realm.where(Person.class).findAll();
        arrayList.addAll(realm.copyFromRealm(allAsync));

    }

    ArrayList <Person>arrayList = new ArrayList();

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip(getResources().getString(R.string.mTts_stating_error)+code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };
    /**
     * 参数设置
     * @return
     */
    public  void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
            //设置使用本地引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath());
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME,voicerLocal);
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));

        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }
    //获取发音人资源路径
    private String getResourcePath(){
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/"+LockActivity.voicerLocal+".jet"));
        return tempBuffer.toString();
    }
    public void showTip(final String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }
    private void setupExtra() {
        Intent intent=new Intent(this,WorkService.class);
        if(!bindService(intent,mdSrvConn, Service.BIND_AUTO_CREATE)){
            handler.removeCallbacksAndMessages(null);
            finish();
        }
    }
    private ServiceConnection mdSrvConn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WorkService.MyBinder myBinder=(WorkService.MyBinder)service;
            if(myBinder!=null){
                microFingerVein=myBinder.getMicroFingerVeinInstance();
                myBinder.setOnUsbMsgCallback(mdUsbMsgCallback);
                startupParam();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    EditText code_mumber;
    @Override
    protected void initData() {
        TextView textView=findView(R.id.versionName);
        textView.setText( APKVersionCodeUtils.getVerName(this));
        code_mumber=(EditText) findViewById(R.id.code_mumber1);
        code_mumber.setFocusable(true);
        code_mumber.setCursorVisible(true);
        code_mumber.setFocusableInTouchMode(true);
        code_mumber.requestFocus();
        /**
          * EditText编辑框内容发生变化时的监听回调
          */
        code_mumber.addTextChangedListener(new EditTextChangeListener());
    }
    public class EditTextChangeListener implements TextWatcher {
        long lastTime;
        /**
         * 编辑框的内容发生改变之前的回调方法
         */
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            Logger.e("MyEditTextChangeListener"+"beforeTextChanged---" + charSequence.toString());
        }
        /**
         * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入
         * 我们可以在这里实时地 通过搜索匹配用户的输入
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            Logger.e("MyEditTextChangeListener"+"onTextChanged---" + charSequence.toString());
        }
        /**
         * 编辑框的内容改变以后,用户没有继续输入时 的回调方法
         */
        @Override
        public void afterTextChanged(Editable editable) {
            String str=code_mumber.getText().toString();
//            Logger.e("MyEditTextChangeListener"+ "afterTextChanged---"+code_mumber.getText().toString());
            if (str.contains("\n")) {
                    if(System.currentTimeMillis()-lastTime<1500){
                        code_mumber.setText("");
                        return;
                    }
                    lastTime=System.currentTimeMillis();
                    userinfo = getSharedPreferences("user_info", MODE_MULTI_PROCESS);
                    deviceId = userinfo.getString("deviceId", "");
                    connectivityManager =(ConnectivityManager)LockActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                    NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                    if (info != null) {
                        //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）

                        isopenCabinet.memberCode(deviceId, code_mumber.getText().toString());
                 }else {
                        mTts.startSpeaking(getResources().getString(R.string.network_error),mTtsListener);
                    }
                    code_mumber.setText("");
            }
        }
    }
    boolean bopen=false;
    boolean bRun=false;
    private Thread mdWorkThread=null;
    private void startupParam() {
        Logger.e("LockActivity"+"====startupParam===");
        mdWorkThread=null;
            bRun = true;
            mdWorkThread = new Thread(runnable);
            mdWorkThread.start();
    }
    boolean istext=false;
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            boolean ret = false;
            int[] tipTimes = {0, 0};//后两次次建模时用了不同手指，重复提醒限制3次
            int modOkProgress = 0;
            while (bRun) {
                if(!bopen&&microFingerVein!=null) {
                    int cnt = microFingerVein.fvdev_get_count();
                    if(cnt == 0) continue;
                    bopen = microFingerVein.fvdev_open();//开启指定索引的设备
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                state = microFingerVein.fvdev_get_state();
                //设备连接正常则进入正常建模或认证流程
                if (state != 0&&istext==false) {
                    long lasttime=System.currentTimeMillis();
                    Logger.e("FirstFragment===========state" + state);
                    if (state == 1 || state == 2) {
                        continue;
                    } else if (state == 3) {

                    }

                    byte[] img= MdFvHelper.tryGetFirstBestImg(microFingerVein,0,5);
                    Logger.e("FirstFragment===========img" + img);
                    if (img == null) {
                        continue;
                    }

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = Message.obtain();
                    message.obj = img;
                    message.what=11;
                   handler.sendMessage(message);

                    bRun=false;
                }
                else {
                    istext=false;
                    if (handler != null) {
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                }
            }
        }
    };
    int isopen=0;
    long starttime,lasttime;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(MessageEvent event){
//        Logger.e("FirstFragment"+"========messageEventBus+type="+event.type+"isopen=="+isopen);
        if (event.type==1&&isopen<1) {
            userinfo=getSharedPreferences("user_info",MODE_MULTI_PROCESS);
            deviceId=userinfo.getString("deviceId","");
            String gpio=userinfo.getString("gpiotext",null);
            if (gpio==null){
                userinfo.edit().putString("gpiotext","1067").commit();
            }
            gpiotext=userinfo.getString(gpiotext,"");
            Gpio.gpioInt(gpiotext);
            Gpio.set(gpiotext,48);
            text_error.setText(R.string.check_successful);
             starttime=System.currentTimeMillis();
                connectivityManager =(ConnectivityManager)LockActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                    if (starttime-lasttime>500) {
                        isopenCabinet.isopen(deviceId, userUid, "vein");
                    lasttime = System.currentTimeMillis();
                    }
                }else {
                    mTts.startSpeaking(getResources().getString(R.string.network_error),mTtsListener);
                }

        }else if (event.type==0&&isopen<1){
            isopen=0;
            if(istext){
//                TTSUtils.getInstance().speak("验证失败");
            }
            mTts.startSpeaking(getResources().getString(R.string.check_failed),mTtsListener);
            if(handler!=null){
                handler.sendEmptyMessageDelayed(10,1000);
            }
            text_error.setText(R.string.check_failed);
        }
        isopen++;
    }
    String  pwdmodel ="1";
    private class ExitAlertDialog1 extends Dialog implements View.OnClickListener {
        private Context mContext;
        private EditText etPwd;
        private Button btCancel;
        private Button btConfirm;
        private TextView texttilt;
        public ExitAlertDialog1(Context context, int theme) {
            super(context, theme);
            mContext = context;
            initDialog();
        }
        public ExitAlertDialog1(Context context) {
            super(context, R.style.customer_dialog);
            mContext = context;
            initDialog();
        }
        private void initDialog() {
            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_exit_confirm, null);
            setContentView(view);
            btCancel = (Button) view.findViewById(R.id.btCancel);
            btConfirm = (Button) view.findViewById(R.id.btConfirm);
            etPwd = (EditText) view.findViewById(R.id.deviceCode);
            texttilt=(TextView)view.findViewById(R.id.text_title);
            btCancel.setOnClickListener(this);
            btConfirm.setOnClickListener(this);
        }
        @Override
        public void show() {
            etPwd.setText("");
            if (pwdmodel=="1"){
            }else if (pwdmodel=="2"){
                texttilt.setText(R.string.chang_pwd);
                etPwd.setHint(getResources().getString(R.string.put_new_pwd));
            }
            super.show();
        }
        String devicepwd;
        SharedPreferences userInfo;
        Intent intent;
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btCancel:
                    this.dismiss();
                    break;
                case R.id.btConfirm:
                    etPwd.setInputType(InputType.TYPE_NULL);
                    if(pwdmodel.equals("1")){
                        String pwd = etPwd.getText().toString().trim();
                        if (Utils.isEmpty(pwd)) {
                            ToastUtils.show(mContext, getResources().getString(R.string.put_pwd), ToastUtils.LENGTH_SHORT);
                            return;
                        }
                        String repwd;
                        try {
                            repwd = Reservoir.get(Constant.KEY_PASSWORD, String.class);
                        } catch (Exception e) {
                            userInfo=getSharedPreferences("user_info",0);
                            repwd = userInfo.getString("devicepwd","0");
                        }
                        if (!pwd.equals(repwd)) {
                            ToastUtils.show(mContext, getResources().getString(R.string.error_password), ToastUtils.LENGTH_SHORT);
                            return;
                        }else {
                            if(Camera.getNumberOfCameras()!=0){
                                userInfo = getSharedPreferences("user_info", 0);
                                userInfo.edit().putString("devicepwd", pwd).commit();
                                mGLSurfaceView.setVisibility(View.INVISIBLE);
                                setting_ll.setVisibility(View.VISIBLE);
                            }
                            this.dismiss();
                        }
                    }else if (pwdmodel.equals("2")){
                        userInfo=getSharedPreferences("user_info",0);
                        String pwd = etPwd.getText().toString().trim();
                        if (userInfo.getString("devicepwd","").toString().trim()==pwd) {
                            ToastUtils.show(mContext, getResources().getString(R.string.same_pwd), ToastUtils.LENGTH_SHORT);
                        }else {
                            userInfo.edit().putString("devicepwd",pwd).commit();
                            ToastUtils.show(mContext, getResources().getString(R.string.chang_pwd_successful), ToastUtils.LENGTH_SHORT);
                        }
                    }
                    break;
            }
        }
    }
    ExitAlertDialog1 exitAlertDialog1;
    //认证一个手指模板,当比对成功且得分大于自定义认证阈值时返回true，否则返回false;
    LinearLayout setting_ll;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        if(Camera.getNumberOfCameras()==2){
            mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        if(Camera.getNumberOfCameras()==1){
            mCameraID =  Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        mCameraRotate = 0;
        mCameraMirror = false;
        mWidth = 640;
        mHeight = 480;
        mFormat = ImageFormat.NV21;
        SharedPreferences userInfo=getSharedPreferences("user_info",0);
        String repwd = userInfo.getString("devicepwd","");
        if(TextUtils.isEmpty(repwd)){
            userInfo.edit().putString("devicepwd","666666").commit();
        }
        mGLSurfaceView = (CameraGLSurfaceView) findViewById(R.id.glsurfaceView);
        mGLSurfaceView.setOnTouchListener(LockActivity.this);
        mSurfaceView = (CameraSurfaceView) findViewById(R.id.surfaceView);
        setting_ll = (LinearLayout) findViewById(R.id.setting_ll);
        mSurfaceView.setOnCameraListener(LockActivity.this);
        findViewById(R.id.versionName).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                pwdmodel="1";
                exitAlertDialog1=new ExitAlertDialog1(LockActivity.this);
                exitAlertDialog1.show();
                return true;
            }
        });
        if(Camera.getNumberOfCameras()!=0){
            mSurfaceView.setupGLSurafceView(mGLSurfaceView, true, mCameraMirror, mCameraRotate);
            mSurfaceView.debug_print_fps(true, false);
            AFT_FSDKError err = engine.AFT_FSDK_InitialFaceEngine(FaceDB.appid, FaceDB.ft_key, AFT_FSDKEngine.AFT_OPF_0_HIGHER_EXT, 16, 5);
            Log.d(TAG, "AFT_FSDK_InitialFaceEngine =" + err.getCode());
            err = engine.AFT_FSDK_GetVersion(version);
            Log.d(TAG, "AFT_FSDK_GetVersion:" + version.toString() + "," + err.getCode());

            ASAE_FSDKError error = mAgeEngine.ASAE_FSDK_InitAgeEngine(FaceDB.appid, FaceDB.ag_key);
            Log.d(TAG, "ASAE_FSDK_InitAgeEngine =" + error.getCode());
            error = mAgeEngine.ASAE_FSDK_GetVersion(mAgeVersion);
            Log.d(TAG, "ASAE_FSDK_GetVersion:" + mAgeVersion.toString() + "," + error.getCode());

            ASGE_FSDKError error1 = mGenderEngine.ASGE_FSDK_InitgGenderEngine(FaceDB.appid, FaceDB.sx_key);
            Log.d(TAG, "ASGE_FSDK_InitgGenderEngine =" + error1.getCode());
            error1 = mGenderEngine.ASGE_FSDK_GetVersion(mGenderVersion);
            Log.d(TAG, "ASGE_FSDK_GetVersion:" + mGenderVersion.toString() + "," + error1.getCode());
            ((BaseApplication) getApplicationContext().getApplicationContext()).mFaceDB.loadFaces();
            mFRAbsLoop = new FRAbsLoop();
            mFRAbsLoop.start();
        }else {
            mGLSurfaceView.setVisibility(View.INVISIBLE);
            setting_ll.setVisibility(View.VISIBLE);
        }


    }
    @Override
    protected void initListeners() {
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
    @Override
    protected void initToolbar(Bundle savedInstanceState) {
    }
    int openType;
    long start=0,end=0;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    text_error.setText(R.string.finger_right);
                    break;
                case 1:
//                    userinfo=getSharedPreferences("user_info",MODE_MULTI_PROCESS);
//                    String gpio=userinfo.getString("gpiotext",null);
//                    deviceId=userinfo.getString("deviceId","");
//                    if (gpio==null){
//                        userinfo.edit().putString("gpiotext","1067").commit();
//                    }
//                    gpiotext=userinfo.getString(gpiotext,"");
//                    Gpio.gpioInt(gpiotext);
//                    Gpio.set(gpiotext,48);
                    text_error.setText(R.string.check_successful);

                    connectivityManager =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                    NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                    if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                         start=System.currentTimeMillis();
                        if (start-end>2000) {
                            isopenCabinet.isopen(deviceId, userUid, "vein");
                            end=System.currentTimeMillis();
                        }
                    }else {
                        mTts.startSpeaking(getResources().getString(R.string.network_error),mTtsListener);
                    }
                    break;
                case 7:
//                    TTSUtils.getInstance().speak("验证失败");
                    mTts.startSpeaking(getResources().getString(R.string.check_failed), mTtsListener);
                    text_error.setText(R.string.check_failed);
                    break;
                case 8:
                    text_error.setText(R.string.move_finger);
                    break;
                case MicroFingerVein.USB_HAS_REQUST_PERMISSION:
                {
                    UsbDevice usbDevice=(UsbDevice) msg.obj;
                    UsbManager mManager=(UsbManager)getSystemService(Context.USB_SERVICE);
                    PendingIntent mPermissionIntent = PendingIntent.getBroadcast(LockActivity.this, 0, new Intent(ACTION_USB_PERMISSION), 0);
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
                        UsbDevice usbDevice=(UsbDevice) msg.obj;
//                        Logger.e(usbDevice.getManufacturerName()+"  节点："+usbDevice.getDeviceName());
                    }
                }
                break;
                case MicroFingerVein.USB_DISCONNECT:{
                    Logger.e("NewMAinActivity=========="+ret);
                }
                break;
                case MicroFingerVein.UsbDeviceConnection: {
                    if(msg.obj!=null) {
                        UsbDeviceConnection usbDevConn=(UsbDeviceConnection)msg.obj;
                        if(LockActivity.this.isFinishing()||LockActivity.this.isDestroyed()) {
                            //修复bug:启动activity几十毫秒内用户快速关闭activity，此时尚未收到usbDeviceConnection对象导致usb不能正常关闭
                            usbDevConn.close();
                        }
                    }
                }
                break;
                case 10:
                    if(bRun==false){
                        removeMessages(10);
                        bRun=true;
                        mdWorkThread.start();

                    }
                    break;
                case 11:
                    byte []img = (byte[]) msg.obj;

                    userUid=Finger_identify.Finger_identify(LockActivity.this, img);
                    isopen=0;
                    istext=true;
                    if (userUid!=null){

                        EventBus.getDefault().post(new MessageEvent(1,getResources().getString(R.string.check_successful)));
                    }else {

                        EventBus.getDefault().post(new MessageEvent(0,getResources().getString(R.string.check_failed)));

                    }



                    break;

            }
        }
    };
    ConnectivityManager connectivityManager;
    @OnClick({ R.id.button02, R.id.button1, R.id.button2, R.id.button3, R.id.button4,R.id.button5,R.id.button6, R.id.button7,R.id.head_text_02})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button02:
                userinfo=getSharedPreferences("user_info",0);
                userinfo.edit().putString("gpiotext","1067").commit();
                gpiostr=userinfo.getString("gpiotext","");
                Gpio.gpioInt(gpiostr);
                Toast.makeText(LockActivity.this, getResources().getString(R.string.configure_io), Toast.LENGTH_SHORT).show();
                break;
            case R.id.button1:
                Gpio.set(gpiostr, 49);
                Toast.makeText(LockActivity.this, getResources().getString(R.string.set_hight), Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                Gpio.set(gpiostr, 48);
                Toast.makeText(LockActivity.this, getResources().getString(R.string.set_low), Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                textView1.setText(Gpio.get(gpiostr)+"");
                Toast.makeText(LockActivity.this, getResources().getString(R.string.set_stating), Toast.LENGTH_SHORT).show();
                break;
            case R.id.button4:
                deviceId=userinfo.getString("deviceId","");
                textView2.setText(deviceId);
                break;
            case R.id.head_text_02:
                connectivityManager =(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                    exitAlertDialog.show();
                    deviceId=userinfo.getString("deviceId","");
//                    downloadFeature.getPagesInfo(deviceId);
                    syncUserFeature.syncUser(deviceId);
                    syncUserFeature.syncSign(deviceId);
                }else {
                    mTts.startSpeaking(getResources().getString(R.string.network_error),mTtsListener);
                    Toast.makeText(this, getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.button5:
                bRun=false;
                try {
                    mdWorkThread.join(200);
                    mdWorkThread=null;
                    System.gc();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
                case R.id.button6:
                    if(Camera.getNumberOfCameras()!=0){
                        mGLSurfaceView.setVisibility(View.VISIBLE);
                        setting_ll.setVisibility(View.INVISIBLE);
                    }

                break;
                case R.id.button7:
                    pwdmodel="2";
                    exitAlertDialog1=new ExitAlertDialog1(LockActivity.this);
                    exitAlertDialog1.show();
                break;
            default:
                break;
        }
    }
//    public static String desEncrypt(String datacode) throws Exception {
//        try
//        {
//            String data = "LU8wzgej7Uzw2EGHRJuTT62zQ9kuyVCg4z0S1vg/1VR3cQdilIgnsAYouHksGcDl";
//            String key = "rocketbird!@sjs!";
//            String iv = "kiPqmEVXtZrgaVkf";
//            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(datacode);
//            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
//            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
//            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
//            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
//            byte[] original = cipher.doFinal(encrypted1);
//            String originalString = new String(original);
//            return originalString;
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @Override
    public void qrCodeSuccess(Code_Message code_message) {
//        TTSUtils.getInstance().speak("验证成功");
        mTts.startSpeaking(getResources().getString(R.string.successful_open),mTtsListener);
        SharedPreferences sharedPreferences=getSharedPreferences("user_info",0);
        gpiostr=sharedPreferences.getString("gpiotext","");
        Logger.e("LockAcitvity"+"==========="+gpiostr);
        try {
            Gpio.gpioInt(gpiostr);
            Thread.sleep(400);
            Gpio.set(gpiostr,48);
//            TTSUtils.getInstance().speak("门已开");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Gpio.set(gpiostr,49);
//        if(bRun==false){
//            bRun=true;
//            mdWorkThread.start();
//        }

//        if(handler!=null){
//            handler.sendEmptyMessageDelayed(10,1000);
//        }

    }

    @Override
    public void syncSignUserSuccess(Sign_data downLoadData) {
        List<SignUser> data = downLoadData.getData();
        RealmResults<SignUser> all = realm.where(SignUser.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                all.deleteAllFromRealm();
            }
        });
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(int x= 0;x<data.size();x++){
                    realm.copyToRealm(data.get(x));
                }
            }
        });

    }
    @Override
    public void syncUserSuccess(DownLoadData resultResponse) {
        List<Person> data = resultResponse.getData();
        if(all==null){
            all = realm.where(Person.class).findAll();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                all.deleteAllFromRealm();
            }
        });
        ((BaseApplication) getApplicationContext().getApplicationContext()).getPerson().clear();
        ((BaseApplication) getApplicationContext().getApplicationContext()).getPerson().addAll(data);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int x = 0; x < data.size(); x++) {
                    realm.copyToRealm(data.get(x));
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(LockActivity.this, getResources().getString(R.string.syn_data), Toast.LENGTH_SHORT).show();
                mTts.startSpeaking(getResources().getString(R.string.syn_data),mTtsListener);
                data.clear();
                System.gc();
                exitAlertDialog.dismiss();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(LockActivity.this, getResources().getString(R.string.syn_error), Toast.LENGTH_SHORT).show();
                mTts.startSpeaking(getResources().getString(R.string.syn_error),mTtsListener);
                data.clear();
                System.gc();
                exitAlertDialog.dismiss();
            }
        });



    }

    @Override
    public void downloadNotReceiver(DownLoadData resultResponse) {

    }

    @Override
    public void downloadApK(UpDateBean resultResponse) {

    }

    @Override
    public void syncUserFacePagesSuccess(SyncUserFace resultResponse) {

    }

    @Override
    public void downloadSuccess(DownLoadData resultResponse) {

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
                    downloadFeature.syncUserFeaturePages(FileUtils.loadDataFromFile(LockActivity.this, "deviceId.text"), currentPage++);
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
                    downloadFeature.appUpdateInfo(FileUtils.loadDataFromFile(this, "deviceId.text"));
                } else {
                    Toast.makeText(this, getResources().getString(R.string.syn_data), Toast.LENGTH_LONG).show();
                }
            }

        }}
    @Override
    protected void onResume() {
        Logger.e("resume");
        userinfo=getSharedPreferences("user_info",MODE_MULTI_PROCESS);
        String gpio=userinfo.getString("gpiotext",null);
        deviceId=userinfo.getString("deviceId","");
        if (gpio==null){
            userinfo.edit().putString("gpiotext","1067").commit();
        }
        gpiotext=userinfo.getString(gpiotext,"");
        Gpio.gpioInt(gpiotext);
        Gpio.set(gpiotext,48);

        super.onResume();


    }
    @Override
    protected void onStart() {
        super.onStart();
        mesReceiver = new MesReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LockActivity.ACTION_UPDATEUI);
        registerReceiver(mesReceiver, intentFilter);
        isopenCabinet=new IsopenCabinet();
        isopenCabinet.attachView(this);
        syncUserFeature=new SyncUserFeature();
        syncUserFeature.attachView(this);

    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    public void isopenSuccess(Lockdata resultResponse) {
        mTts.startSpeaking(getResources().getString(R.string.successful_open), mTtsListener);
        SharedPreferences sharedPreferences=getSharedPreferences("user_info",0);
        gpiostr=sharedPreferences.getString("gpiotext","");
        Logger.e("LockAcitvity"+"==========="+gpiostr);
        try {
            Gpio.gpioInt(gpiostr);
            Thread.sleep(400);
            Gpio.set(gpiostr,48);
//            TTSUtils.getInstance().speak("门已开");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Gpio.set(gpiostr,49);
      if(handler!=null){
          handler.sendEmptyMessageDelayed(10,1000);
      }
    }

    @Override
    public void onError(ApiException e) {
        String reg = "[^\u4e00-\u9fa5]";
        String syt=e.getMessage().replaceAll(reg, "");
        Logger.e("BindActivity"+syt);
        mTts.startSpeaking(syt,mTtsListener);
        if(handler!=null){
            handler.sendEmptyMessageDelayed(10,1000);
        }
    }
    /**
     * 合成回调监听。
     */
    public SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
        }
        @Override
        public void onSpeakPaused() {
        }
        @Override
        public void onSpeakResumed() {
        }
        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
        }
        @Override
        public void onCompleted(SpeechError speechError) {
        }
        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
        }
    };
    @Override
    public void sendLogSuccess(RestResponse resultResponse) {
    }
    private WorkService.UsbMsgCallback mdUsbMsgCallback=new WorkService.UsbMsgCallback(){
        @Override
        public void onUsbConnSuccess(String usbManufacturerName, String usbDeviceName) {
//            String newUsbInfo="USB厂商："+usbManufacturerName+"  \nUSB节点："+usbDeviceName;
//            handler.obtainMessage(MSG_SHOW_LOG,newUsbInfo).sendToTarget();
        }
        @Override
        public void onUsbDisconnect() {
            handler.obtainMessage(MSG_SHOW_LOG,"usb disconnected.").sendToTarget();
            if(microFingerVein!=null) {
                microFingerVein.close();
            }
            bopen=false;
        }
        @Override
        public void onUsbDeviceConnection(UsbDeviceConnection usbDevConn) {
//            handler.obtainMessage(MSG_SHOW_LOG,"md usb device connection ok.").sendToTarget();
            LockActivity.this.usbDevConn=usbDevConn;
            if(LockActivity.this.isFinishing()||LockActivity.this.isDestroyed()) {
//                Log.e(TAG,"√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√");
                LockActivity.this.usbDevConn.close();
            }
        }
    };
    @Override
    public void onResultError(ApiException e) {
        onError(e);
    }
    @Override
    public void onPermissionError(ApiException e) {
        onError(e);
    }
    /**
     * 广播接收器
     *
     * @author kevin
     */
    public class MesReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            head_text_03.setText(intent.getStringExtra("timeStr"));
            head_text_01.setText(intent.getStringExtra("timeData"));
            if (context == null) {
                context.unregisterReceiver(this);
            }
        }
    }
    @Override
    protected void onDestroy() {
        Logger.e("LockActivity"+"onDestroy");
       // TTSUtils.getInstance().release();
        if(usbDevConn==null){
        }else{
            usbDevConn.close();
        }
        if( null != mTts ){
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
        realm.close();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mesReceiver);
        unbindService(mdSrvConn);
        if(Camera.getNumberOfCameras()!=0){
            mFRAbsLoop.shutdown();
            AFT_FSDKError err = engine.AFT_FSDK_UninitialFaceEngine();
            Log.d(TAG, "AFT_FSDK_UninitialFaceEngine =" + err.getCode());

            ASAE_FSDKError err1 = mAgeEngine.ASAE_FSDK_UninitAgeEngine();
            Log.d(TAG, "ASAE_FSDK_UninitAgeEngine =" + err1.getCode());
            ASGE_FSDKError err2 = mGenderEngine.ASGE_FSDK_UninitGenderEngine();
            Log.d(TAG, "ASGE_FSDK_UninitGenderEngine =" + err2.getCode());

        }
        super.onDestroy();

    }



    private CameraSurfaceView mSurfaceView;
    private CameraGLSurfaceView mGLSurfaceView;
    private Camera mCamera;
    AFT_FSDKVersion version = new AFT_FSDKVersion();
    AFT_FSDKEngine engine = new AFT_FSDKEngine();
    ASAE_FSDKVersion mAgeVersion = new ASAE_FSDKVersion();
    ASAE_FSDKEngine mAgeEngine = new ASAE_FSDKEngine();
    ASGE_FSDKVersion mGenderVersion = new ASGE_FSDKVersion();
    ASGE_FSDKEngine mGenderEngine = new ASGE_FSDKEngine();
    List<AFT_FSDKFace> result = new ArrayList<>();
    private int mWidth, mHeight, mFormat;
    int mCameraID;
    int mCameraRotate;
    boolean mCameraMirror;
    byte[] mImageNV21 = null;
    FRAbsLoop mFRAbsLoop = null;
    AFT_FSDKFace mAFT_FSDKFace = null;
    class FRAbsLoop extends AbsLoop {
        AFR_FSDKVersion version = new AFR_FSDKVersion();
        AFR_FSDKEngine engine = new AFR_FSDKEngine();
        AFR_FSDKFace result = new AFR_FSDKFace();

        @Override
        public void setup() {
            AFR_FSDKError error = engine.AFR_FSDK_InitialEngine(FaceDB.appid, FaceDB.fr_key);
            Log.d(TAG, "AFR_FSDK_InitialEngine = " + error.getCode());
            error = engine.AFR_FSDK_GetVersion(version);
            Log.d(TAG, "FR=" + version.toString() + "," + error.getCode()); //(210, 178 - 478, 446), degree = 1　780, 2208 - 1942, 3370
        }

        @Override
        public void loop() {
            if (mImageNV21 != null) {
                AFR_FSDKError error = engine.AFR_FSDK_ExtractFRFeature(mImageNV21, mWidth, mHeight, AFR_FSDKEngine.CP_PAF_NV21, mAFT_FSDKFace.getRect(), mAFT_FSDKFace.getDegree(), result);
                Log.d(TAG, "Face=" + result.getFeatureData()[0] + "," + result.getFeatureData()[1] + "," + result.getFeatureData()[2] + "," + error.getCode());
                AFR_FSDKMatching score = new AFR_FSDKMatching();
                float max = 0.0f;
                String name = null;
                Log.e(TAG, "loop: " + ((BaseApplication) getApplicationContext().getApplicationContext()).mFaceDB.mFaceList.size());
                if (((BaseApplication) getApplicationContext().getApplicationContext()).mFaceDB.mFaceList.size() > 0) {
                    //是否识别成功(如果第一次没成功就再次循环验证一次)
                    for (Map.Entry<String, AFR_FSDKFace> entry : ((BaseApplication) getApplicationContext().getApplicationContext()).mFaceDB.mFaceList.entrySet()) {
                        error = engine.AFR_FSDK_FacePairMatching(result, entry.getValue(), score);
                        Log.d(TAG, "Score:" + score.getScore() + ", AFR_FSDK_FacePairMatching=" + error.getCode());
                        if (max < score.getScore()) {
                            max = score.getScore();
                            name =  entry.getKey();

                        }
                    }
                    if (max > 0.60f) {
                        SharedPreferences userInfo = getSharedPreferences("user_info", 0);
                        long secondTime = System.currentTimeMillis();
                        if (secondTime - firstTime > 3000) {
                            //找到相关住户执行开门
                            firstTime = secondTime;
                            Log.d(TAG, "fit Score:" + max + ", NAME:" + name);
                            deviceId = userInfo.getString("deviceId", "");
                            isopenCabinet.isopen(deviceId,name,"face");
                        }

                    } else {
                        recindex = recindex + 1;
                        if (recindex == 3) {

                            recindex = 0;
                        }
                    }
                } else {

                }
                mImageNV21 = null;
            }
        }

        @Override
        public void over() {
            AFR_FSDKError error = engine.AFR_FSDK_UninitialEngine();
            Log.d(TAG, "AFR_FSDK_UninitialEngine : " + error.getCode());
        }

    }

private long firstTime=0;
    int recindex = 0;

    @Override
    public Camera setupCamera() {
        // TODO Auto-generated method stub
        if (Camera.getNumberOfCameras() != 0) {
            mCamera = Camera.open(mCameraID);
            try {
                Camera.Parameters parameters = mCamera.getParameters();

                parameters.setPreviewSize(mWidth, mHeight);
                parameters.setPreviewFormat(mFormat);
                mCamera.setDisplayOrientation(90);
                for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
                    Log.d(TAG, "SIZE:" + size.width + "x" + size.height);
                }
                for (Integer format : parameters.getSupportedPreviewFormats()) {
                    Log.d(TAG, "FORMAT:" + format);
                }

                List<int[]> fps = parameters.getSupportedPreviewFpsRange();
                for (int[] count : fps) {
                    Log.d(TAG, "T:");
                    for (int data : count) {
                        Log.d(TAG, "V=" + data);
                    }
                }
                mCamera.setParameters(parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mCamera != null) {
                mWidth = mCamera.getParameters().getPreviewSize().width;
                mHeight = mCamera.getParameters().getPreviewSize().height;
            }
            return mCamera;
        }
        return null;
    }

    @Override
    public void setupChanged(int format, int width, int height) {

    }

    @Override
    public boolean startPreviewImmediately() {
        return true;
    }

    @Override
    public Object onPreview(byte[] data, int width, int height, int format, long timestamp) {
        AFT_FSDKError err = engine.AFT_FSDK_FaceFeatureDetect(data, width, height, AFT_FSDKEngine.CP_PAF_NV21, result);
        Log.d(TAG, "AFT_FSDK_FaceFeatureDetect =" + err.getCode());
        Log.d(TAG, "Face=" + result.size());
        for (AFT_FSDKFace face : result) {
            Log.d(TAG, "Face:" + face.toString());
        }
        if (mImageNV21 == null) {
            if (!result.isEmpty()) {
                mAFT_FSDKFace = result.get(0).clone();
                mImageNV21 = data.clone();
            } else {

            }
        }

        Rect[] rects = new Rect[result.size()];
        for (int i = 0; i < result.size(); i++) {
            rects[i] = new Rect(result.get(i).getRect());
        }
        result.clear();
        return rects;
    }

    @Override
    public void onBeforeRender(CameraFrameData data) {

    }

    @Override
    public void onAfterRender(CameraFrameData data) {
        mGLSurfaceView.getGLES2Render().draw_rect((Rect[]) data.getParams(), Color.GREEN, 2);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(Camera.getNumberOfCameras()!=0){
            CameraHelper.touchFocus(mCamera, event, v, this);

        }
        return false;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            Log.d(TAG, "Camera Focus SUCCESS!");
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


}
