package com.link.cloud.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.AsyncPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.link.cloud.bean.RestResponse;
import com.link.cloud.contract.BindTaskContract;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.fragment.RegisterFragment_Two;
import com.link.cloud.greendao.gen.PersonDao;
import com.link.cloud.greendaodemo.Person;
import com.link.cloud.model.MdFvHelper;
import com.link.cloud.setting.TtsSettings;
import com.link.cloud.utils.Utils;
import com.orhanobut.logger.Logger;
import com.link.cloud.bean.Member;
import com.link.cloud.core.BaseAppCompatActivity;
import com.link.cloud.fragment.BindVeinMainFragment;

import com.link.cloud.view.NoScrollViewPager;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import md.com.sdk.MicroFingerVein;


import static com.link.cloud.utils.Utils.byte2hex;

/**
 * Created by Administrator on 2017/8/24.
 */

public class BindAcitvity extends BaseAppCompatActivity implements CallBackValue,BindTaskContract.BindView,SendLogMessageTastContract.sendLog {
    @Bind(R.id.bing_main_page)
    NoScrollViewPager viewPager;
    @Bind(R.id.layout_page_time)
    TextView timeStr;
    @Bind(R.id.layout_page_title)
    TextView tvTitle;
    @Bind(R.id.bind_one_Cimg)
    ImageView bind_one_Cimg;
    @Bind(R.id.bind_one_line)
    View bind_one_line;
    @Bind(R.id.layout_main_error)
    LinearLayout layout_error_text;
    @Bind(R.id.bind_two_Cimg)
    ImageView bind_two_Cimg;
    @Bind(R.id.bind_two_line)
    View bind_two_line;
    @Bind(R.id.bind_three_Cimg)
    ImageView bind_three_Cimg;
    @Bind(R.id.bind_three_line)
    View bind_three_line;
    @Bind(R.id.bind_four_Cimg)
    ImageView bind_four_Cimg;
    @Bind(R.id.bind_one_tv)
    TextView bind_one_tv;
    @Bind(R.id.bind_two_tv)
    TextView bind_two_tv;
    @Bind(R.id.bind_three_tv)
    TextView bind_three_tv;
    @Bind(R.id.mian_text_error)
    TextView text_error;
    @Bind(R.id.text_tile)
    TextView text_tile;
//    @Bind(R.id.bind_four_tv)
//    TextView bind_four_tv;
//    @Bind(R.id.bind_four_line)
//    ImageView bind_four_Pimg;
    @Bind(R.id.bind_four_tv)
    TextView bind_four_tv;
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
//    public static final String ACTION_UPDATEUI = "action.updateTiem";
    BindTaskContract bindTaskContract;
    //记录当前用户信息
    private Member memberInfo;
    private BindVeinMainFragment bindVeinMainFragment;
    private RegisterFragment_Two registerFragment_two;
//    private RegisterFragment_Three registerFragment_three;
    private MesReceiver mesReceiver;
//    private MediaPlayer mediaPlayer0,mediaPlayer1,mediaPlayer2,mediaPlayer;
    private int recLen=0;
    private Runnable runnable;
//    private Handler handler;
    private boolean hasFinish = false;
    private AsyncPlayer asyncPlayer;
    byte[] feauter = null;
    byte[] feauter1 = null;
    byte[] feauter2 = null;
    byte[] feauter3 = null;
    int[] state = new int[1];
    byte[]img=null;
    byte[] img1 = null;
    byte[] img2 = null;
    byte[] img3 = null;
    boolean ret = false;
    int[] pos = new int[1];
    float[] score = new float[1];
    int run_type = 2;
    private PersonDao personDao;
    // 语音合成对象
    public SpeechSynthesizer mTts;
    // 默认本地发音人
    public static String voicerLocal="xiaoyan";
    // 本地发音人列表
    private String[] localVoicersEntries;
    private String[] localVoicersValue ;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private SharedPreferences mSharedPreferences;
    SendLogMessageTastContract sendLogMessageTastContract;
    private static String ACTION_USB_PERMISSION = "com.android.USB_PERMISSION";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        WorkService.setActactivity(this);
        super.onCreate(savedInstanceState);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        setParam();
    }
    /**
     * 参数设置
     * @return
     */
    private void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成
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
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/"+BindAcitvity.voicerLocal+".jet"));
        return tempBuffer.toString();
    }
    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip(getResources().getString(R.string.initialization_fail)+code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };
    Toast mToast;
    private void showTip(final String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }
    @Override
    protected int getLayoutId() {
        return R.layout.layout_main_bind;
    }
    @Override
    protected void onStart() {
        super.onStart();
//        microFingerVein=MicroFingerVein.getInstance(this);
        bindTaskContract=new BindTaskContract();
        bindTaskContract.attachView(this);
        sendLogMessageTastContract=new SendLogMessageTastContract();
        sendLogMessageTastContract.attachView(this);
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
    protected void initViews(Bundle savedInstanceState) {
        mesReceiver=new MesReceiver();
        tvTitle.setText(R.string.bind_finger);
        bind_one_tv.setText(R.string.put_number);
        bind_two_tv.setText(R.string.sure_message);
        bind_three_tv.setText(R.string.put_finger);
        bind_four_tv.setText(R.string.bind_finish);
        bindVeinMainFragment=new BindVeinMainFragment();
        mFragmentList.add(bindVeinMainFragment);
        FragmentManager fm=getSupportFragmentManager();
        SectionsPagerAdapter mfpa=new SectionsPagerAdapter(fm,mFragmentList); //new myFragmentPagerAdater记得带上两个参数
        viewPager.setAdapter(mfpa);
        viewPager.setCurrentItem(0);
    }
    @Override
    public void setActivtyChange(String string) {
        switch (string) {
            case "1":
//                mediaPlayer.start();
                mTts.startSpeaking(getResources().getString(R.string.put_number),mTtsListener);
                bind_one_Cimg.setImageResource(R.drawable.flow_circle_pressed);
                bind_one_line.setBackgroundResource(R.color.colorText);
                bind_one_tv.setTextColor(getResources().getColor(R.color.colorText));
                bind_two_Cimg.setImageResource(R.drawable.flow_circle);
                bind_two_line.setBackgroundResource(R.color.edittv);
                bind_two_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_three_Cimg.setImageResource(R.drawable.flow_circle);
                bind_three_line.setBackgroundResource(R.color.edittv);
                bind_three_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_four_Cimg.setImageResource(R.drawable.flow_circle);
                bind_four_tv.setTextColor(getResources().getColor(R.color.edittv));
                break;
            case "2":
//                mediaPlayer0.start();
                mTts.startSpeaking(getResources().getString(R.string.sure_message),mTtsListener);
                bind_one_Cimg.setImageResource(R.drawable.flow_circle);
                bind_one_line.setBackgroundResource(R.color.edittv);
                bind_one_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_two_Cimg.setImageResource(R.drawable.flow_circle_pressed);
                bind_two_line.setBackgroundResource(R.color.colorText);
                bind_two_tv.setTextColor(getResources().getColor(R.color.colorText));
                bind_three_Cimg.setImageResource(R.drawable.flow_circle);
                bind_three_line.setBackgroundResource(R.color.edittv);
                bind_three_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_four_Cimg.setImageResource(R.drawable.flow_circle);
                bind_four_tv.setTextColor(getResources().getColor(R.color.edittv));
                break;
            case "3":
                mTts.startSpeaking(getResources().getString(R.string.put_finger),mTtsListener);
                layout_error_text.setVisibility(View.VISIBLE);
                setupParam();
                bind_one_Cimg.setImageResource(R.drawable.flow_circle);
                bind_one_line.setBackgroundResource(R.color.edittv);
                bind_one_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_two_Cimg.setImageResource(R.drawable.flow_circle);
                bind_two_line.setBackgroundResource(R.color.edittv);
                bind_two_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_three_Cimg.setImageResource(R.drawable.flow_circle_pressed);
                bind_three_line.setBackgroundResource(R.color.colorText);
                bind_three_tv.setTextColor(getResources().getColor(R.color.colorText));
                bind_four_Cimg.setImageResource(R.drawable.flow_circle);
                bind_four_tv.setTextColor(getResources().getColor(R.color.edittv));
                break;
            case "4":
                mTts.startSpeaking(getResources().getString(R.string.bind_finish),mTtsListener);
                bind_one_Cimg.setImageResource(R.drawable.flow_circle);
                bind_one_line.setBackgroundResource(R.color.edittv);
                bind_one_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_two_Cimg.setImageResource(R.drawable.flow_circle);
                bind_two_line.setBackgroundResource(R.color.edittv);
                bind_two_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_three_Cimg.setImageResource(R.drawable.flow_circle);
                bind_three_line.setBackgroundResource(R.color.edittv);
                bind_three_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_four_Cimg.setImageResource(R.drawable.flow_circle_pressed);
                bind_four_tv.setTextColor(getResources().getColor(R.color.colorText));
                break;
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initData() {
        text_tile.setText(R.string.membind_finger);
        mesReceiver=new MesReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NewMainActivity.ACTION_UPDATEUI);
        registerReceiver(mesReceiver, intentFilter);
//        etPhoneNum.setShowSoftInputOnFocus(false);
    }
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    String feature=new String(byte2hex(feauter3));
                    SharedPreferences userinfo=getSharedPreferences("user_info",0);
                    SharedPreferences userinfo2=getSharedPreferences("user_info_bind",0);
                    String deviceId=userinfo.getString("deviceId","");
                    ConnectivityManager connectivityManager;
                    connectivityManager =(ConnectivityManager)BindAcitvity.this.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                    NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                    if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                        bindTaskContract.bindVeinMemeber(deviceId,
                                Integer.parseInt(userinfo2.getString("userType", "1")), userinfo.getInt("numberType", 0),
                                userinfo2.getString("numberValue", ""), byte2hex(modelImgMng.getImg1()), byte2hex(modelImgMng.getImg2()), byte2hex(modelImgMng.getImg3()), feature);
                    }else {

                    }
                    break;
                case 1:
//                    mTts.startSpeaking("请移开手指",mTtsListener);
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    text_error.setText(R.string.move_finger);
                    break;
                case 2:
                    text_error.setText(R.string.again_finger);
                    break;
                case 3:
//                    mTts.startSpeaking(R.string.same_finger,mTtsListener);
                    text_error.setText(R.string.same_finger);
                    break;
                case 4:
                    text_error.setText(R.string.put_mapfinger);
                    break;
                case 5:
                    mTts.startSpeaking(getResources().getString(R.string.waiting),mTtsListener);
                    text_error.setText(R.string.waiting);
                    break;
                case 6:
                    text_error.setText(R.string.bing_success);
                    break;
            }
        }
    };
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
    public void startAD() {
        recLen=3;
        runnable = new Runnable() {
            @Override
            public void run() {
                recLen--;
                handler.postDelayed(this,1000);
                if (recLen<=0) {
                    Intent intent = new Intent();
                    intent.setClass(BindAcitvity.this, NewMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
        Logger.e("BindActivity=======+startAD()");
        handler.postDelayed(runnable, 1000);
    }
    private volatile boolean bRun=false;
    ModelImgMng modelImgMng=new ModelImgMng();
    boolean  bopen=false;
    Boolean  isstart=false;     //控制语音播报
    private boolean bWorkModel=true;//建模是否进行
    private Thread mdWorkThread=null;//进行建模或认证的全局工作线程
    private int deviceTouchState=1;//触摸：0，移开1，设备断开或其他状态2
    private void setupParam() {
        bRun=true;
        mdWorkThread=new Thread(runnablemol);
        mdWorkThread.start();
    }
    Runnable  runnablemol=new Runnable() {
        @Override
        public void run() {
            int quality;
            int state=0;
            Message message;
            int[] pos = new int[1];
            float[] score = new float[1];
            int[] pos1 = new int[1];
            float[] score1 = new float[1];
            boolean ret=false,ret1=false;
            int[] tipTimes={0,0};//后两次次建模时用了不同手指，重复提醒限制3次
            int modOkProgress=0;
            deviceTouchState=0;
            while(bRun) {
                state=WorkService.microFingerVein.fvdev_get_state();
                //设备连接正常则进入正常建模或认证流程
                if(state != 0) {
                    isstart=true;
                    Logger.e("BindActivty===========state"+state);
                    byte[] img= MdFvHelper.tryGetFirstBestImg(WorkService.microFingerVein,0,5);
                    Logger.e("BindActivty===========img"+img);
                    if(img==null) {
                        continue;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                     message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                    quality=WorkService.microFingerVein.fv_quality(img);//添加图像质量评估，为0为最佳；
                    Logger.e("BindActivty===========quality"+quality);
                    if(quality!=0){
                        continue;
                    }
                    feauter=WorkService.microFingerVein.fv_extract_model(img,null,null);
                    Logger.e("BindActivty===========feauter1"+feauter);
                    if(feauter == null) {
                        continue;
                    }else{ //建模
                        if (deviceTouchState==0) {
                            modOkProgress++;
                        }
                        deviceTouchState=1;
                        Logger.e("BindActivity" +"Progress="+modOkProgress);
                        if (modOkProgress == 1) {//first model
                            modelImgMng.setImg1(img);
                            modelImgMng.setFeature1(feauter);
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Logger.e("BindActivity" + "model 1 ok"+"modOkProgress="+modOkProgress);
//                            mTts.startSpeaking("请再次放置手指", mTtsListener);
//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                        } else if (modOkProgress == 2) {//second model
                            ret = WorkService.microFingerVein.fv_index(modelImgMng.getFeature1(), 1, img, pos, score);
                            if (ret && score[0] > 0.4) {
                                Logger.e("BindActivity" + "model 2 ok"+"modOkProgress"+modOkProgress);
                                feauter2 = WorkService.microFingerVein.fv_extract_model(img, null, null);
                                if (feauter2 != null) {
                                    tipTimes[0] = 0;
                                    tipTimes[1] = 0;
                                    modelImgMng.setImg2(img);
                                    modelImgMng.setFeature2(feauter2);
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
//                                    mTts.startSpeaking("请再次放置手指", mTtsListener);
//                                    try {
//                                        Thread.sleep(2000);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
                                } else {//第二次建模从图片中取特征值无效
                                    modOkProgress = 1;
                                    if (++tipTimes[0] <= 5) {
                                    } else {//连续超过3次放了不同手指则忽略此次建模重来
                                        modOkProgress = 0;
                                    }
                                }
                            } else {
                                if (handler!=null) {
                                    handler.sendEmptyMessage(3);
                                }
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                modOkProgress = 1;
                            }
                        } else if (modOkProgress == 3) {//third model
                            ret = WorkService.microFingerVein.fv_index(modelImgMng.getFeature1(), 1, img, pos, score);
                            Logger.e("BindActivity"+"ret"+ret +"score[0]"+score[0]+"ret1"+ret1+"score1[0]"+score1[0]);
                            if (ret && score[0] > 0.4) {
                                    Logger.e("BindActivity" + "model 3 ok" + "modOkProgress" + modOkProgress + "score[0]" + score[0]);
                                    feauter3 = WorkService.microFingerVein.fv_extract_model(modelImgMng.getImg1(), modelImgMng.getImg2(), img);
                                    if (feauter3 != null && handler != null) {//成功生成一个3次建模并融合的融合特征数组
                                        handler.sendEmptyMessage(0);
                                        message = new Message();
                                        message.what = 5;
                                        if (handler!=null) {
                                            handler.sendMessage(message);
                                        }
                                        tipTimes[0] = 0;
                                        tipTimes[1] = 0;
                                        modelImgMng.setImg3(img);
                                        modelImgMng.setFeature3(feauter3);
                                    } else {//第三次建模从图片中取特征值无效
                                        modOkProgress = 2;
                                        if (++tipTimes[0] <= 5) {
                                        } else {
                                            //连续超过3次放了不同手指则忽略此次建模重来
                                            Utils.showPromptToast(BindAcitvity.this, "连续超过3次放了不同手指则忽略此次建模重来");
                                            modOkProgress = 0;
                                        }
                                    }
                                    bRun = false;
                                    bopen = false;
                                } else {
                                    message = new Message();
                                    message.what = 3;
                                    if (handler!=null) {
                                        handler.sendMessage(message);
                                    }
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    modOkProgress = 2;
                                    continue;
                                }
                        } else if (modOkProgress > 3 || modOkProgress <= 0) {
                            modOkProgress = 0;
                        }
                    }
                }else {//触摸state==0时
                    isstart=false;
                    deviceTouchState = 0;
                   try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                    if (modOkProgress>0) {
                        message=new Message();
                        message.what=2;
                        if (handler!=null) {
                            handler.sendMessage(message);
                        }
                    }else {
                        message=new Message();
                        message.what=4;
                        if (handler!=null) {
                            handler.sendMessage(message);
                        }
                        }
                    }
                    if(bopen) {
                        deviceTouchState = 1;
                    }
                }
            }
    };
    @Override
    protected void initToolbar(Bundle savedInstanceState) {
    }
    @Override
    protected void initListeners() {
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
    @Override
    protected void onDestroy() {
        if (bopen){
            bopen=false;
        }
        bRun=false;
        if (WorkService.microFingerVein!=null) {
            WorkService.microFingerVein.close();
        }
        if (handler!=null) {
            handler.removeCallbacksAndMessages(null);
            handler.removeCallbacks(runnable);
            handler=null;
        }
        Intent intent=new Intent(this,WorkService.class);
        stopService(intent);
        super.onDestroy();
        unregisterReceiver(mesReceiver);
        finish();
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;
        public SectionsPagerAdapter(FragmentManager fm,ArrayList<Fragment> mFragmentList) {
            super(fm);
            this.list=mFragmentList;
        }
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }
        @Override
        public int getCount() {
            return list.size();
        }
    }
    @OnClick(R.id.home_back_bt)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.home_back_bt:
                if (bopen){
                    bopen=false;
                }
                bRun=false;
             Intent intent=new Intent();
             intent.setClass(BindAcitvity.this,NewMainActivity.class);
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(intent);
             finish();
             break;
        }
    }

    @Override
    public void sendLogSuccess(RestResponse resultResponse) {
    }

    @Override
    public void onPermissionError(ApiException e) {
        onError(e);
    }
    @Override
    public void onError(ApiException e) {
        String reg = "[^\u4e00-\u9fa5]";
        String syt=e.getMessage().replaceAll(reg, "");
        Logger.e("BindActivity"+syt);
        text_error.setText(syt);
        mTts.startSpeaking(syt,mTtsListener);
    }
    @Override
    public void onResultError(ApiException e) {
        onError(e);
    }
    @Override
    public void bindSuccess(Member returnBean) throws InterruptedException {
        PersonDao personDao=BaseApplication.getInstances().getDaoSession().getPersonDao();
        Person person = BaseApplication.getInstances().getDaoSession().getPersonDao().queryBuilder().orderDesc(PersonDao.Properties.Id).limit(1).build().unique();
        Logger.e("BindAcitvity===id=="+person.getId()+"Feature"+returnBean.getMemberdata().getUserInfo().getFeature());
        Long id=person.getId()+1;
        person.setId(id);
        person.setUserType(returnBean.getMemberdata().getUserInfo().getUserType());
        person.setName(returnBean.getMemberdata().getUserInfo().getName());
        person.setFeature(byte2hex(feauter3));
        person.setUid(returnBean.getMemberdata().getUserInfo().getUid());
        person.setSex(returnBean.getMemberdata().getUserInfo().getSex());
        personDao.insert(person);
        startAD();
//        SharedPreferences userinfo=getSharedPreferences("user_info",0);
//        String deiveId=userinfo.getString("deviceId","");
//        DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String strBeginDate = dateTimeformat.format(new Date());
        if (handler!=null) {
            Message message = new Message();
            message.what = 6;
            handler.sendMessage(message);
        }
        setActivtyChange("4");
    }
    /**
     * 广播接收器
     *
     * @author kevin
     */
    public class MesReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            timeStr.setText(intent.getStringExtra("timethisStr"));
//            Logger.e("NewMainActivity" + intent.getStringExtra("timeStr"));
            if (context == null) {

            }
        }
    }
}
