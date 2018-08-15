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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.BaseApplication;
import com.link.cloud.R;
import com.link.cloud.activity.CallBackValue;
import com.link.cloud.activity.EliminateActivity;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.bean.RetrunLessons;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.contract.UserLessonContract;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.greendao.gen.PersonDao;
import com.link.cloud.greendaodemo.Person;
import com.link.cloud.ui.RollListView;
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

public class LessonFragment_down extends BaseFragment implements UserLessonContract.UserLesson,SendLogMessageTastContract.sendLog{
    @Bind(R.id.layout_two)
    LinearLayout layout_two;
    @Bind(R.id.layout_three)
    LinearLayout layout_three;
    @Bind(R.id.bind_member_next)
    Button next_bt;
    @Bind(R.id.lesson_bt_next)
    Button button_sure;
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
    @Bind(R.id.text_error)
    TextView text_error;
    @Bind(R.id.lessonmessageInfo)
    RollListView horizontalListView;
    @Bind(R.id.lessonLayout)
    LinearLayout lessonLayout;
    @Bind(R.id.selectLesson)
    LinearLayout selectLesson;
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

    }
    public LessonFragment_down(){
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
    public static LessonFragment_down newInstance(){
        LessonFragment_down fragment= new LessonFragment_down();
        Bundle args=new Bundle();
//        args.putSerializable(Constant.EXTRAS_ELIMINATE_INFO, (Serializable) userInfo);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this.getContext();
        sendLogMessageTastContract=new SendLogMessageTastContract();
        sendLogMessageTastContract.attachView(this);
    }
    String coachID,studentID;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    if(coachID==null){
                        text_error.setText("请教练放置手指");
                    }else if(studentID==null){
                        text_error.setText("请学员放置手指");
                    }else {
                        text_error.setText("请稍后...");
                    }

                    break;
                case 1:
                    Logger.e(1+"<<<<<<<<<<<<");
                    text_error.setText("验证成功...");
                    if("1".equals(userType)){
                        if(coachID==null){
                            Logger.e(2+"<<<<<<<<<<<<");
                            text_error.setText("请教练先放手指");
                        }else {
                            Logger.e(3+"<<<<<<<<<<<<");
                            studentID=uid;
                            String deviceId=activity.getSharedPreferences("user_info",0).getString("deviceId","");
                            presenter.eliminateLesson(deviceId,2,studentID,coachID,"");
                        }
                    }
                    if("2".equals(userType)){
                        Logger.e(4+"<<<<<<<<<<<<");
                        text_error.setText("请学员放置手指");
                        coachID = uid;
                    }

//                    LessonFragment_test fragment = LessonFragment_test.newInstance(userInfo);
//                    ((EliminateActivity) this.getParentFragment()).addFragment(fragment, 1);
                    break;
                case 2:
                    text_error.setText("验证失败...");
                    break;
                case 3:
                    text_error.setText("请移开手指");
                    break;
                case 4:
                    if(coachID==null){
                        text_error.setText("请教练放置手指");
                    }else {
                        text_error.setText("请学员放置手指");
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
    @Override
    protected void initData() {
        presenter = new UserLessonContract();
        this.presenter.attachView(this);
        layout_two.setVisibility(View.GONE);
        layout_error_text.setVisibility(View.VISIBLE);
        layout_three.setVisibility(View.VISIBLE);
        personDao=BaseApplication.getInstances().getDaoSession().getPersonDao();
        next_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eliminateSuccess){
                    mActivity_review.setVisibility(View.GONE);
                    recyclerView_card.setVisibility(View.VISIBLE);
                    activity.setActivtyChange("3");
                    if(activity.lessonType==2){

                    }
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
                    presenter.selectLesson(deviceId,2,lessonID,studentID,coachID,"",CardNumber,lessonCount);
                }
            }
        });

    }
    String lessonID,CardNumber;


    @Override
    protected void initListeners() {
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
    }
    int lessonCount=0;
    List<RetrunLessons.DataBean.LessonInfoBean.CardInfoBean> cardInfos;
    @Override
    protected void onVisible() {
        Logger.e("LessonFragment_test============onVisible");
    }
    boolean eliminateSuccess;
    @Override
    public void eliminateSuccess(RetrunLessons lessonResponse) {
        Logger.e("eliminateSuccess========="+lessonResponse.toString());
        eliminateSuccess=true;
        lessonCount=0;
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
            public void postionChanged(String id, List<RetrunLessons.DataBean.LessonInfoBean.CardInfoBean> cardInfo,String xsa) {
                lessonID=id;
                cardInfos=cardInfo;
            }
        });

    }

    @Override
    public void sendLogSuccess(RestResponse resultResponse) {

    }

    @Override
    public void selectLessonSuccess(RetrunLessons lessonResponse) {
        callBackValue.setActivtyChange("4");
        lessonLayout.setVisibility(View.GONE);
        selectLesson.setVisibility(View.VISIBLE);
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