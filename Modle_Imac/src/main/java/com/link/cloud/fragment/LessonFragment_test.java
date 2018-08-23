package com.link.cloud.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.link.cloud.activity.BindAcitvity;
import com.link.cloud.activity.CallBackValue;
import com.link.cloud.activity.EliminateActivity;
import com.link.cloud.activity.NewMainActivity;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.bean.RetrunLessons;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.contract.UserLessonContract;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.greendao.gen.PersonDao;
import com.link.cloud.greendaodemo.Person;
import com.link.cloud.setting.TtsSettings;
import com.link.cloud.ui.RollListView;
import com.link.cloud.utils.ModelImgMng;
import com.link.cloud.utils.VenueUtils;
import com.link.cloud.view.CardAdapter;
import com.link.cloud.view.CardCallBack;
import com.link.cloud.view.CardConfig;
import com.link.cloud.view.SwipeCardCallBack;
import com.link.cloud.view.SwipeCardLayoutManager;
import com.link.cloud.view.UniversalAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/7/31.
 */

public class LessonFragment_test extends BaseFragment implements UserLessonContract.UserLesson,SendLogMessageTastContract.sendLog,VenueUtils.VenueCallBack{
    @Bind(R.id.layout_two)
    LinearLayout layout_two;
    @Bind(R.id.layout_three)
    LinearLayout layout_three;
    @Bind(R.id.bind_member_next)
    Button next_bt;
    @Bind(R.id.lesson_bt_next)
    Button button_sure;
    @Bind(R.id.lesson_confirm_next)
    Button lesson_confirm_next;
    @Bind(R.id.bind_member_name)
    TextView menber_name;
    @Bind(R.id.bind_member_cardtype)
    TextView cardtype;
    @Bind(R.id.bind_member_cardnumber)
    TextView cardnumber;
    @Bind(R.id.bind_member_begintime)
    TextView startTime;
    @Bind(R.id.layout_error_text)
    LinearLayout layout_error_text;
    @Bind(R.id.bind_member_endtime)
    TextView endTime;
    @Bind(R.id.bind_member_sex)
    TextView menber_sex;
    @Bind(R.id.bind_member_phone)
    TextView menber_phone;
    @Bind(R.id.lesson_count)
    TextView lesson_count;
    @Bind(R.id.minus_circle)
    ImageView minus_circle;
    @Bind(R.id.plus_circle)
    ImageView plus_circle;
    @Bind(R.id.text_error)
    TextView text_error;
    @Bind(R.id.lessonmessageInfo)
    RollListView horizontalListView;
    @Bind(R.id.lessonLayout)
    LinearLayout lessonLayout;
    @Bind(R.id.ll_lesson_count)
    LinearLayout ll_lesson_count;
    @Bind(R.id.selectLesson)
    LinearLayout selectLesson;
    @Bind(R.id.one_card)
    LinearLayout one_card;
    @Bind(R.id.up_lesson)
    Button up_lesson;
    @Bind(R.id.next_lesson)
    Button next_lesson;
    @Bind(R.id.mActivity_review)
    RecyclerView mActivity_review;
    @Bind(R.id.recyclerView_card)
    RecyclerView recyclerView_card;
    public Context mContext;
    public UserLessonContract presenter;
    SendLogMessageTastContract sendLogMessageTastContract;
    public static CallBackValue callBackValue;
    EliminateActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=(EliminateActivity) activity;
        callBackValue=(CallBackValue)activity;

    } @Override
    public void VeuenMsg(int state, String data, String uids, String feature, String score,String userType) {

        LessonCallBack(state,data,userType);
    }

    @Override
    public void ModelMsg(int state, ModelImgMng modelImgMng, String feature) {

    }

    public  LessonFragment_test(){
    }
    boolean isFirst =true;
    public void LessonCallBack(int state,String uid,String userType){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (state) {
                    case 0:
                        if(coachID==null){
                            text_error.setText(R.string.coach_finger);
                            mTts.startSpeaking(activity.getResources().getString(R.string.coach_finger),mTtsListener);
                        }else if(studentID==null){
                            isFirst=true;
                            text_error.setText(R.string.student_finger);
                            mTts.startSpeaking(activity.getResources().getString(R.string.student_finger),mTtsListener);
                        }else {
                            isFirst=false;
                            text_error.setText(R.string.wait_moment);
                            mTts.startSpeaking(activity.getResources().getString(R.string.wait_moment),mTtsListener);
                        }

                        break;
                    case 1:
                        if("1".equals(userType)){
                            if(coachID==null){
                                Logger.e(2+"<<<<<<<<<<<<");
                                text_error.setText(R.string.coach_first);
                                mTts.startSpeaking(activity.getResources().getString(R.string.coach_first),mTtsListener);
                            }else {
                                Logger.e(3+"<<<<<<<<<<<<");
                                studentID=uid;
                                text_error.setText(R.string.check_success);
                                mTts.startSpeaking(activity.getResources().getString(R.string.check_success),mTtsListener);
                                String deviceId=activity.getSharedPreferences("user_info",0).getString("deviceId","");
                                presenter.eliminateLesson(deviceId,activity.lessonType,studentID,coachID,"");
                            }
                        }
                        if("2".equals(userType)){
                            Logger.e(4+"<<<<<<<<<<<<");
                            text_error.setText(R.string.student_finger);
                            mTts.startSpeaking(activity.getResources().getString(R.string.student_finger),mTtsListener);
                            coachID = uid;
                        }

//                    LessonFragment_test fragment = LessonFragment_test.newInstance(userInfo);
//                    ((EliminateActivity) this.getParentFragment()).addFragment(fragment, 1);
                        break;
                    case 2:
                        text_error.setText(R.string.sign_error);
                        mTts.startSpeaking(activity.getResources().getString(R.string.sign_error),mTtsListener);
                        break;
                    case 3:

                        text_error.setText(R.string.move_finger);
                        mTts.startSpeaking(activity.getResources().getString(R.string.move_finger),mTtsListener);
                        break;
//                case 5:
//                    text_error.setText("请移开手指");
//                    break;
//                case 6:
//                    text_error.setText("");
//                    break;
                }
            }
        });


    }
    public static LessonFragment_test newInstance(){
        LessonFragment_test fragment= new LessonFragment_test();
        Bundle args=new Bundle();
//        args.putSerializable(Constant.EXTRAS_ELIMINATE_INFO, (Serializable) userInfo);
        fragment.setArguments(args);
        return fragment;
    }
    VenueUtils venueUtils;
    public static String voicerLocal="xiaoyan";
    // 引擎类型
    private SharedPreferences mSharedPreferences;
    public SpeechSynthesizer mTts;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this.getContext();
        sendLogMessageTastContract=new SendLogMessageTastContract();
        sendLogMessageTastContract.attachView(this);
        venueUtils = BaseApplication.getVenueUtils();
        mTts = SpeechSynthesizer.createSynthesizer(activity, mTtsInitListener);
        mSharedPreferences = activity.getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);
    }
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
        tempBuffer.append(ResourceUtil.generateResourcePath(activity, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(activity, ResourceUtil.RESOURCE_TYPE.assets, "tts/"+ BindAcitvity.voicerLocal+".jet"));
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
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }
    String coachID,studentID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    int lessonCount =0;
    @Override
    protected void initData() {
        presenter = new UserLessonContract();
        this.presenter.attachView(this);
        lessonCount=0;
        layout_two.setVisibility(View.GONE);
        layout_error_text.setVisibility(View.VISIBLE);
        layout_three.setVisibility(View.VISIBLE);
        one_card.setVisibility(View.GONE);
        mActivity_review.setVisibility(View.VISIBLE);

        next_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eliminateSuccess){
                    mActivity_review.setVisibility(View.GONE);
                    activity.setActivtyChange("3");
                    if(activity.lessonType==2){
                        next_bt.setVisibility(View.GONE);
                            ll_lesson_count.setVisibility(View.VISIBLE);
                    }
                    recyclerView_card.setVisibility(View.VISIBLE);
                    SwipeCardLayoutManager swmanamger = new SwipeCardLayoutManager(activity);
                    CardAdapter mAdatper = new CardAdapter(cardInfos,activity.getApplicationContext());
                    recyclerView_card.setLayoutManager(swmanamger);
                    recyclerView_card.setAdapter(mAdatper);
                    CardConfig.initConfig(activity);
                    ItemTouchHelper.Callback callback=new CardCallBack(cardInfos,mAdatper,recyclerView_card);
                    ItemTouchHelper helper=new ItemTouchHelper(callback);
                    helper.attachToRecyclerView(recyclerView_card);
                    mAdatper.setPostionListener(new CardAdapter.PositionChangedLister() {
                        @Override
                        public void postionChanged(String cardNo) {
                            CardNumber=cardNo;
                        }
                    });
                    eliminateSuccess=false;

                }else {
                    String deviceId=activity.getSharedPreferences("user_info",0).getString("deviceId","");
                    presenter.selectLesson(deviceId,activity.lessonType,lessonID,studentID,coachID,"",CardNumber,0);
                }
            }
        });

    }
    String lessonID,CardNumber;


    @Override
    protected void initListeners() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                venueUtils.initVenue(NewMainActivity.getMdbind(),activity,LessonFragment_test.this,true,false);
            }
        }.start();
        Logger.e("LessonFragment_test============initListeners");
    }

    @Override
    protected void onInvisible() {
        Logger.e("LessonFragment_test============onInvisible");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_bind_member;
    }

    @Override
    protected void initViews(View self, Bundle bundle) {
        Logger.e("LessonFragment_test============initViews");
        lessonID=null;
        eliminateSuccess=false;
        minus_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lessonCount==0){
                    return;
                }
                lessonCount--;
                lesson_count.setText(lessonCount+"");
            }
        });
        plus_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lessonCount>=9){
                    return;
                }
                lessonCount++;
                lesson_count.setText(lessonCount+"");
            }
        });
        lesson_confirm_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lessonCount==0){
                    Toast.makeText(activity, R.string.confirm_lesson,Toast.LENGTH_SHORT).show();
                    return;
                }
                String deviceId=activity.getSharedPreferences("user_info",0).getString("deviceId","");
                presenter.selectLesson(deviceId,activity.lessonType,lessonID,studentID,coachID,"",CardNumber,lessonCount);
            }
        });

    }
    List<RetrunLessons.DataBean.LessonInfoBean.CardInfoBean> cardInfos;
    String CardName;
    @Override
    protected void onVisible() {
        Logger.e("LessonFragment_test============onVisible");
    }
    boolean eliminateSuccess;
    @Override
    public void eliminateSuccess(RetrunLessons lessonResponse) {
        Logger.e("eliminateSuccess========="+lessonResponse.toString());
        eliminateSuccess=true;
        activity.setActivtyChange("2");
        layout_two.setVisibility(View.VISIBLE);
        layout_error_text.setVisibility(View.GONE);
        layout_three.setVisibility(View.GONE);
        SwipeCardLayoutManager swmanamger = new SwipeCardLayoutManager(activity);
        UniversalAdapter mAdatper = new UniversalAdapter(lessonResponse.getData().getLessonInfo(),activity.getApplicationContext());
        mActivity_review.setLayoutManager(swmanamger);
        mActivity_review.setAdapter(mAdatper);
        CardConfig.initConfig(activity);
        ItemTouchHelper.Callback callback=new SwipeCardCallBack(lessonResponse.getData().getLessonInfo(),mAdatper,mActivity_review);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mActivity_review);
        mAdatper.setPostionListener(new UniversalAdapter.PositionChangedLister() {
            @Override
            public void postionChanged(String id, List<RetrunLessons.DataBean.LessonInfoBean.CardInfoBean> cardInfo,String card) {
                lessonID=id;
                cardInfos=cardInfo;
                CardName = card;
            }
        });

    }

    @Override
    public void sendLogSuccess(RestResponse resultResponse) {

    }

    @Override
    public void selectLessonSuccess(RetrunLessons lessonResponse) {
        callBackValue.setActivtyChange("4");
        recyclerView_card.setVisibility(View.INVISIBLE);
        one_card.setVisibility(View.VISIBLE);
        next_bt.setVisibility(View.GONE);
        menber_name.setText(CardName);
        menber_phone.setText(CardNumber);
        ll_lesson_count.setVisibility(View.GONE);
    }

    @Override
    public void onPermissionError(ApiException e) {
        Logger.e("EliminateLessonFragment:--onPermissionError");
        onError(e);
    }
    @Override
    public void onResultError(ApiException e) {
        Logger.e("EliminateLessonFragment:--onResultError"+e.getDisplayMessage());
        onError(e);
    }

    @Override
    public void onError(ApiException error) {
        String reg = "[^\u4e00-\u9fa5]";
        String syt=error.getMessage().replaceAll(reg, "");
        text_error.setText(syt);
        next_bt.setText(syt);
        mTts.startSpeaking(syt,mTtsListener);

    }
    @Override
    public void onStop() {
        Logger.e("LessonFragment_test============onStop");
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}