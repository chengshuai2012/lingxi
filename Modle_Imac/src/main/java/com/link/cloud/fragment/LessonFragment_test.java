package com.link.cloud.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import com.link.cloud.BaseApplication;
import com.link.cloud.R;
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
    String userType;
    EliminateActivity activity;
    PersonDao personDao;
    String uid;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=(EliminateActivity) activity;
        callBackValue=(CallBackValue)activity;

    } @Override
    public void VeuenMsg(int state, String data, String uids, String feature, String score) {
        LessonCallBack(state,data);
    }

    @Override
    public void ModelMsg(int state, ModelImgMng modelImgMng, String feature) {

    }

    public  LessonFragment_test(){
    }

    public void LessonCallBack(int state,String uid){
        switch (state){

            case 0:
                handler.sendEmptyMessage(0);
                break;
            case 1:
                this.uid=uid;
                QueryBuilder qb = personDao.queryBuilder();
                Logger.e(uid+">>>>>>");
                List<Person> users = qb.where(PersonDao.Properties.Uid.eq(uid)).list();
                Logger.e(users.size()+">>>>>>");
                userType = users.get(0).getUserType();
                Logger.e(userType+"");
                handler.sendEmptyMessage(1);

                break;
            case 2:
                handler.sendEmptyMessage(2);
                break;
            case 3:
                handler.sendEmptyMessage(3);
                break;
        }
    }
    public static LessonFragment_test newInstance(){
        LessonFragment_test fragment= new LessonFragment_test();
        Bundle args=new Bundle();
//        args.putSerializable(Constant.EXTRAS_ELIMINATE_INFO, (Serializable) userInfo);
        fragment.setArguments(args);
        return fragment;
    }
    VenueUtils venueUtils;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this.getContext();
        sendLogMessageTastContract=new SendLogMessageTastContract();
        sendLogMessageTastContract.attachView(this);
        venueUtils = BaseApplication.getVenueUtils();

    }
    String coachID,studentID;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    if(coachID==null){
                        text_error.setText(R.string.coach_finger);
                    }else if(studentID==null){
                        text_error.setText(R.string.student_finger);
                    }else {
                        text_error.setText(R.string.wait_moment);
                    }

                    break;
                case 1:
                    Logger.e(1+"<<<<<<<<<<<<");
                    text_error.setText(R.string.check_success);
                    if("1".equals(userType)){
                        if(coachID==null){
                            Logger.e(2+"<<<<<<<<<<<<");
                            text_error.setText(R.string.coach_first);
                        }else {
                            Logger.e(3+"<<<<<<<<<<<<");
                            studentID=uid;

                            String deviceId=activity.getSharedPreferences("user_info",0).getString("deviceId","");
                            presenter.eliminateLesson(deviceId,activity.lessonType,studentID,coachID,"");
                        }
                    }
                    if("2".equals(userType)){
                        Logger.e(4+"<<<<<<<<<<<<");
                        text_error.setText(R.string.student_finger);
                        coachID = uid;
                    }

//                    LessonFragment_test fragment = LessonFragment_test.newInstance(userInfo);
//                    ((EliminateActivity) this.getParentFragment()).addFragment(fragment, 1);
                    break;
                case 2:
                    text_error.setText(R.string.sign_error);
                    break;
                case 3:
                    text_error.setText(R.string.move_finger);
                    break;
                case 4:
                    if(coachID==null){
                        text_error.setText(R.string.coach_finger);
                    }else {
                        text_error.setText(R.string.student_finger);
                    }
                    break;
//                case 5:
//                    text_error.setText("请移开手指");
//                    break;
//                case 6:
//                    text_error.setText("");
//                    break;
            }
        }
    };
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
        personDao=BaseApplication.getInstances().getDaoSession().getPersonDao();
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