package com.link.cloud.activity;



import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.link.cloud.bean.Member;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.core.BaseAppCompatActivity;
import com.link.cloud.fragment.SignInMainFragment;
import com.link.cloud.setting.TtsSettings;
import com.link.cloud.view.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

import static com.link.cloud.BaseApplication.venueUtils;





/**

 * Created by Administrator on 2017/8/17.

 */



public class SigeActivity extends BaseAppCompatActivity implements CallBackValue,SendLogMessageTastContract.sendLog{

//    @Bind(R.id.bing_main_page)

//    NoScrollViewPager viewPager;

    @Bind(R.id.layout_page_time)

    TextView timeStr;

    @Bind(R.id.layout_page_title)

    TextView tvTitle;

//    @Bind(R.id.home_back_bt)

//    TextView home_back;

    @Bind(R.id.bind_one_Cimg)

    ImageView bind_one_Cimg;

    @Bind(R.id.bind_one_line)

    View bind_one_line;

    @Bind(R.id.bind_one_tv)

    TextView bind_one_tv;

    @Bind(R.id.bind_two_Cimg)

    ImageView bind_two_Cimg;

    @Bind(R.id.bind_two_line)

    View bind_two_line;

    @Bind(R.id.layout_main_error)

    LinearLayout layout_main_error;

    @Bind(R.id.bind_two_tv)

    TextView bind_two_tv;

    @Bind(R.id.bind_three_Cimg)

    ImageView bind_three_Cimg;

    @Bind(R.id.bind_three_line)

    View bind_three_line;

    @Bind(R.id.bind_three_tv)

    TextView bind_three_tv;

    @Bind(R.id.bind_four_Cimg)

    ImageView bind_four_Cimg;

    @Bind(R.id.bind_four_tv)

    TextView bind_four_tv;

    @Bind(R.id.mian_text_error)

    TextView text_error;

    @Bind(R.id.text_tile)

    TextView text_tile;

    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();

    public static final String ACTION_UPDATEUI = "action.updateTiem";

    Handler mHandler=new Handler();

    public volatile boolean bRun=false;

    //记录当前用户信息

    private Member memberInfo;

    private SignInMainFragment signInMainFragment;

    public NoScrollViewPager viewPager;

    private MesReceiver mesReceiver;

    public SendLogMessageTastContract sendLogMessageTastContract;

    public boolean bopen;

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

    @Override

    public void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);

        sendLogMessageTastContract=new SendLogMessageTastContract();

        sendLogMessageTastContract.attachView(this);

        // 初始化合成对象

        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);

        mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);

        setParam();

        venueUtils = BaseApplication.getVenueUtils();

        super.onCreate(savedInstanceState);

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

                showTip(getResources().getString(R.string.mTts_stating_error)+code);

            } else {

                // 初始化成功，之后可以调用startSpeaking方法

                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，

                // 正确的做法是将onCreate中的startSpeaking调用移至这里

            }

        }

    };

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

    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);

    }

    @Override

    protected int getLayoutId() {

        return R.layout.layout_main_bind;

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @Override

    protected void onStart() {

        super.onStart();

    }

    @Override

    protected void initViews(Bundle savedInstanceState) {

        text_tile.setText(R.string.sign_member);

        viewPager=(NoScrollViewPager)findViewById(R.id.bing_main_page) ;

        mesReceiver=new MesReceiver();

        signInMainFragment=new SignInMainFragment();

        mFragmentList.add(signInMainFragment);

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

//                mediaPlayer1.start();

                mTts.startSpeaking(getResources().getString(R.string.finger_right),mTtsListener);

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

//                mediaPlayer1.start();

                mTts.startSpeaking(getResources().getString(R.string.sign_successful),mTtsListener);

                fingersign();

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

    private void fingersign(){

        if (mHandler!=null) {

            mHandler.postDelayed(new Runnable() {

                @Override

                public void run() {

                    finish();

                }

            }, 3000);

        }

    }

    @Override

    protected void initData() {

        mesReceiver=new MesReceiver();

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(NewMainActivity.ACTION_UPDATEUI);

        registerReceiver(mesReceiver, intentFilter);

    }


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

        bind_one_tv.setVisibility(View.INVISIBLE);

        bind_one_Cimg.setVisibility(View.INVISIBLE);

        bind_one_line.setVisibility(View.INVISIBLE);

//        bind_one_tv.setText("输入手机号四");

        bind_two_tv.setText(R.string.put_finger);

        bind_three_tv.setText(R.string.sign_successful);

        tvTitle.setText(R.string.sign_member);

        bind_three_line.setVisibility(View.INVISIBLE);

        bind_four_Cimg.setVisibility(View.INVISIBLE);

        bind_four_tv.setVisibility(View.INVISIBLE);

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

    public void onError(ApiException e) {

    }

    @Override

    public void onResultError(ApiException e) {

    }

    @Override

    public void onPermissionError(ApiException e) {

    }

    @Override

    public void sendLogSuccess(RestResponse resultResponse) {

    }



    @Override

    protected void onDestroy() {

        super.onDestroy();

        if (mHandler!=null){

            mHandler.removeCallbacksAndMessages(null);

        }

        mHandler=null;

        venueUtils.StopIdenty();

        unregisterReceiver(mesReceiver);

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

//    public static boolean Run=false;

    @OnClick(R.id.home_back_bt)

    public void onClick(View view){

        switch (view.getId()){

            case R.id.home_back_bt:

                bRun=false;


                finish();



        }

    }







    @Override

    public void onBackPressed() {

        // super.onBackPressed();

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

        }

    }



}