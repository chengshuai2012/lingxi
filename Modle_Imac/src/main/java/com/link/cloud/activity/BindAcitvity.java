package com.link.cloud.activity;



import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.link.cloud.contract.BindTaskContract;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.core.BaseAppCompatActivity;
import com.link.cloud.fragment.BindVeinMainFragment;
import com.link.cloud.setting.TtsSettings;
import com.link.cloud.utils.ModelImgMng;
import com.link.cloud.utils.VenueUtils;
import com.link.cloud.view.NoScrollViewPager;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

import static com.link.cloud.utils.Utils.byte2hex;



/**

 * Created by Administrator on 2017/8/24.

 */



public class BindAcitvity extends BaseAppCompatActivity implements CallBackValue,BindTaskContract.BindView,SendLogMessageTastContract.sendLog,VenueUtils.VenueCallBack {



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

    @Bind(R.id.bind_four_tv)

    TextView bind_four_tv;

    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();

//    public static final String ACTION_UPDATEUI = "action.updateTiem";

    BindTaskContract bindTaskContract;



    private BindVeinMainFragment bindVeinMainFragment;



    private MesReceiver mesReceiver;

    private int recLen=0;

    private Runnable runnable;

    String feature;

    // 语音合成对象

    public SpeechSynthesizer mTts;

//    // 默认本地发音人

    public static String voicerLocal="xiaoyan";

    // 引擎类型

    private SharedPreferences mSharedPreferences;

    SendLogMessageTastContract sendLogMessageTastContract;



    @Override

    public void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        // 初始化合成对象

        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);

        mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);

        setParam();
        venueUtils = BaseApplication.getVenueUtils();


    }
        VenueUtils venueUtils;


    @Override

    protected int getLayoutId() {

        return R.layout.layout_main_bind;

    }







    @Override

    protected void initToolbar(Bundle savedInstanceState) {



    }



    @Override

    protected void initListeners() {



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

    protected void onStart() {

        super.onStart();

//        microFingerVein=MicroFingerVein.getInstance(this);

        bindTaskContract=new BindTaskContract();

        bindTaskContract.attachView(this);

        sendLogMessageTastContract=new SendLogMessageTastContract();

        sendLogMessageTastContract.attachView(this);

    }

    /**

     //     * 合成回调监听。

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
                venueUtils.initVenue(NewMainActivity.getMdbind(),BindAcitvity.this,BindAcitvity.this,false,true);

                mTts.startSpeaking(getResources().getString(R.string.put_finger),mTtsListener);

                layout_error_text.setVisibility(View.VISIBLE);

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



    }



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

    }





    @Override

    public void ModelMsg(int state, ModelImgMng modelImgMng, String feature) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                switch (state){

                    case 0:

                        new Thread(new Runnable() {

                            @Override

                            public void run() {

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

                            }

                        }).start();



                        break;

                    case 1:

                        text_error.setText(R.string.move_finger);

                        break;

                    case 2:

                        text_error.setText(R.string.again_finger);

                        break;

                    case 3:

                        text_error.setText(R.string.same_finger);

                        break;


                    case 5:

                        text_error.setText(R.string.waiting);

                        //mTts.startSpeaking(getResources().getString(R.string.waiting),mTtsListener);

                        break;

                }
            }
        });





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

    protected void onDestroy() {

        super.onDestroy();
        venueUtils.StopIdenty();
        unregisterReceiver(mesReceiver);

    }





    @Override

    public void VeuenMsg(int state, String data, String uids, String feature, String score,String usertpye) {



    }







    @Override

    public void onResultError(ApiException e) {



    }



    @Override

    public void bindSuccess(Member returnBean) throws InterruptedException {

        setActivtyChange("4");

        text_error.setText(R.string.bing_success);

        fingersign();


    }
    Handler handler = new Handler();
    private void fingersign(){

        if (handler!=null) {

            handler.postDelayed(new Runnable() {

                @Override

                public void run() {
                    finish();

                }

            }, 3000);

        }

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


        }

    }

}