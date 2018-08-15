package com.link.cloud.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.BaseApplication;
import com.link.cloud.R;
import com.link.cloud.activity.CallBackValue;
import com.link.cloud.activity.LessonDownActivity;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.RetrunLessons;
import com.link.cloud.bean.UserInfo;
import com.link.cloud.contract.UserLessonContract;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.greendao.gen.PersonDao;
import com.link.cloud.greendaodemo.Person;
import com.link.cloud.ui.HorizontalListViewAdapter;
import com.link.cloud.ui.RollListView;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.Bind;

import static com.alibaba.sdk.android.ams.common.util.HexUtil.hexStringToByte;

/**
 * Created by Administrator on 2017/7/31.
 */

public class LessonFragment_test2 extends BaseFragment implements UserLessonContract.UserLesson{
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
    String[] lessonId,lessonName,lessonDate;
    int num,indext;
    RetrunLessons lessonResponse;
    private int selectPosition = 0;//用于记录用户选择的变量
    private HorizontalListViewAdapter hListViewAdapter;
    public  String lessonnum=new String();
    View.OnClickListener clickListener;
    public Context mContext;
    public Runnable runnable,runnable2;
    public UserLessonContract presenter;
    public static CallBackValue callBackValue;
    UserInfo caochInfo,userinfo;
    byte[] featuer = null;
    int state;
    byte[] img1 = null;
    boolean ret = false;
    int[] pos = new int[1];
    float[] score = new float[1];
    String userType;
    String userid,caochId,clerkid;
    LessonDownActivity activity;
    PersonDao personDao;
    boolean flog=true;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=(LessonDownActivity) activity;
        callBackValue=(CallBackValue)activity;
    }
    public LessonFragment_test2(){
    }
    public static LessonFragment_test2 newInstance(){
        LessonFragment_test2 fragment= new LessonFragment_test2();
        Bundle args=new Bundle();
//        args.putSerializable(Constant.EXTRAS_ELIMINATE_INFO, (Serializable) userInfo);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this.getContext();
//        mediaPlayer=MediaPlayer.create(activity,R.raw.failure_sign);
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    QueryBuilder qb = personDao.queryBuilder();
                    List<Person> users = qb.where(PersonDao.Properties.Id.eq((long)pos[0]+1)).list();
                    userType=users.get(0).getUserType();
                    Logger.e("userType======="+userType);
                    if (caochId==null&&"2".equals(userType)){
                        callBackValue.setActivtyChange("2");
                        caochId=users.get(0).getUid();
                         text_error.setText("请会员放置手指");
                    }else if (caochId!=null&&"0".equals(userType)){
                        userid=users.get(0).getUid();
                        callBackValue.setActivtyChange("3");
                        layout_error_text.setVisibility(View.GONE);
                        layout_three.setVisibility(View.GONE);
                        layout_two.setVisibility(View.GONE);
                        lessonLayout.setVisibility(View.VISIBLE);
                        flog=false;
                        Logger.e("LessonFragment========userid"+userid+"caochId"+caochId);
                        SharedPreferences userinfo=activity.getSharedPreferences("user_info",0);
                       String device=userinfo.getString("device","");
                        for (int i=0;i<num;i++) {
                            lessonId[0]="222222";
                            lessonName[0]="高温瑜伽";
                            lessonDate[0]="2018/05/28-2018/05/28";
                        }
                        hListViewAdapter=new HorizontalListViewAdapter(getContext(),1,"张教练","王大壮","13022222222","222222","高温瑜伽","2018/05/28-2018/05/28");
                        horizontalListView.setAdapter(hListViewAdapter);
                        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                hListViewAdapter.setSelectIndex(position);
                                selectPosition = position;
                                //lessonnum=lessonResponse.getLessonResponse().lessonInfo[position].getLessonId();
                                hListViewAdapter.notifyDataSetChanged();
                            }
                        });
                        clickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                switch (v.getId()) {
                                    case R.id.up_lesson:
//                        uplesson();
                                        Logger.e("eliminateSuccess========="+"R.id.up_lesson");
                                        break;
                                    case R.id.next_lesson:
//                        nextlesson();
                                        Logger.e("eliminateSuccess========="+"next_lesson");
                                        break;
                                }
                            }
                        };
                        up_lesson.setOnClickListener(clickListener);
                        next_lesson.setOnClickListener(clickListener);
//                        presenter.eliminateLesson(device,1,userid,caochId,clerkid);
                    }else {
                        text_error.setText("请教练放置手指");
                    }
                    break;
                case 1:
                    break;
                case 2:
                    text_error.setText("验证失败...");
                    break;
                case 3:
                    text_error.setText("请会员放置手指");
                    break;
                case 4:
                    text_error.setText("请教练放置手指");
                    break;
//                case 4:
//                    text_error.setText("放置手指错误，请放置同一根手指");
//                    break;
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
        button_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessonLayout.setVisibility(View.GONE);
                selectLesson.setVisibility(View.VISIBLE);
            }
        });
        next_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e("===============================");
                SharedPreferences userinfo=activity.getSharedPreferences("user_info",0);
                String deivece=userinfo.getString("device","");
//                presenter.selectLesson("1000834GS7K",1,lessonResponse.getLessonResponse().getLessonInfo()[selectPosition].getLessonId(),
//                        userid,caochId,clerkid);
            }
        });
        if (flog=true){
        executeSql();
        setupParam();
        }
    }
    boolean bopen=false;
    private volatile boolean bRun=false;
    private Thread mdWorkThread=null;//进行建模或认证的全局工作线程
    private void setupParam() {
        layout_error_text.setVisibility(View.VISIBLE);
        bRun=true;
        mdWorkThread=new Thread(runnablemol);
        mdWorkThread.start();
    }
    Runnable  runnablemol=new Runnable() {
        @Override
        public void run() {

        }
    };
    void  executeSql() {
        long startime=System.currentTimeMillis();
        Log.e("SignFragment_one","startime:"+startime);
//        Logger.e("SigeActivity-------"+"executeSql");
        personDao= BaseApplication.getInstances().getDaoSession().getPersonDao();
        personDao.loadAll();
        String sql = "select FEATURE from PERSON" ;
        int i =0;
        Cursor cursor = BaseApplication.getInstances().getDaoSession().getDatabase().rawQuery(sql,null);
        byte[][] feature=new byte[cursor.getCount()][];
        while (cursor.moveToNext()){
//            Logger.e("SigeActivity----no---");
            int nameColumnIndex = cursor.getColumnIndex("FEATURE");
            String strValue=cursor.getString(nameColumnIndex);
//            Logger.e("SigeActivity-------"+strValue);
            feature[i]=hexStringToByte(strValue);
            i++;
        }
        int len = 0;
        // 计算一维数组长度
        for (byte[] element : feature) {
            len += element.length;
        }
        // 复制元素
        featuer = new byte[len];
        int index = 0;
        for (byte[] element : feature) {
            for (byte element2 : element) {
                featuer[index++] = element2;
            }
        }
        long endtime=System.currentTimeMillis();
        Log.e("SignFragment_one","endtime:"+endtime);
//        Logger.e("SignActivity======feature"+byte2hex(featuer));
    }

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

    }

    @Override
    protected void onVisible() {
        Logger.e("LessonFragment_test============onVisible");
    }

    @Override
    public void eliminateSuccess(RetrunLessons lessonResponse) {
//        this.lessonResponse=lessonResponse;
//        callBackValue.setActivtyChange("3");
//        layout_error_text.setVisibility(View.GONE);
//        layout_three.setVisibility(View.GONE);
//        lessonLayout.setVisibility(View.VISIBLE);
//        num=lessonResponse.getLessonResponse().getLessonInfo().length;
//        lessonId=new String[num];
//        lessonName=new String[num];
//        lessonDate=new String[num];
//        for (int i=0;i<num;i++) {
//            lessonId[i]=lessonResponse.getLessonResponse().getLessonInfo()[i].getLessonId();
//            lessonName[i]=lessonResponse.getLessonResponse().getLessonInfo()[i].getLessonName();
//            lessonDate[i]=lessonResponse.getLessonResponse().getLessonInfo()[i].getLessonDate();
//        }
//        lessonnum=lessonResponse.getLessonResponse().getLessonInfo()[0].getLessonId();
//        hListViewAdapter=new HorizontalListViewAdapter(getContext(),selectPosition,lessonResponse.getLessonResponse().getCoach(),lessonResponse.getLessonResponse().getMembername(),lessonResponse.getLessonResponse().getMemberphone(),lessonId[indext],lessonName[indext],lessonDate[indext]);
//        horizontalListView.setAdapter(hListViewAdapter);
//        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                hListViewAdapter.setSelectIndex(position);
//                selectPosition = position;
//                lessonnum=lessonResponse.getLessonResponse().lessonInfo[position].getLessonId();
//                hListViewAdapter.notifyDataSetChanged();
//            }
//        });
//        clickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                switch (v.getId()) {
//                    case R.id.up_lesson:
////                        uplesson();
//                        Logger.e("eliminateSuccess========="+"R.id.up_lesson");
//                        break;
//                    case R.id.next_lesson:
////                        nextlesson();
//                        Logger.e("eliminateSuccess========="+"next_lesson");
//                        break;
//                }
//            }
//        };
//        up_lesson.setOnClickListener(clickListener);
//        next_lesson.setOnClickListener(clickListener);
////        checkButton();
        Logger.e("eliminateSuccess========="+lessonResponse.toString());
    }

    @Override
    public void selectLessonSuccess(RetrunLessons lessonResponse) {
        callBackValue.setActivtyChange("4");
        lessonLayout.setVisibility(View.GONE);
        selectLesson.setVisibility(View.VISIBLE);
    }
    private Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                Logger.e("LessonFragment" + "handle" + lessonResponse.toString());
               // hListViewAdapter = new HorizontalListViewAdapter(getContext(), indext, lessonResponse.getLessonResponse().getCoach(), lessonResponse.getLessonResponse().getMembername(), lessonResponse.getLessonResponse().getMemberphone(), lessonId[indext], lessonName[indext], lessonDate[indext]);
                horizontalListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();
                horizontalListView.invalidate();
            }
        }
    };
    public void checkButton(){
        Logger.e("eliminateSuccess========="+"checkButton"+indext+ "num:"+num);
        if (num>1){
            if (indext==0){
                Logger.e("eliminateSuccess=indext==0="+"checkButton"+indext+ "num:"+num);
                up_lesson.setEnabled(false);
                up_lesson.setBackgroundResource(R.drawable.no_up);
                next_lesson.setBackgroundResource(R.drawable.next_btn_bge);
                next_lesson.setEnabled(true);
            }else if (indext<num-1&&indext!=0){
                Logger.e("eliminateSuccess=indext<num-1&&indext!=0="+"checkButton"+indext+ "num:"+num);
                up_lesson.setBackgroundResource(R.drawable.up_btn_bg);
                up_lesson.setEnabled(true);
                next_lesson.setBackgroundResource(R.drawable.next_btn_bge);
                next_lesson.setEnabled(true);
            }else if (indext==num-1&indext!=0){
                Logger.e("eliminateSuccess=indext==num-1&indext!=0="+"checkButton"+indext+ "num:"+num);
                up_lesson.setBackgroundResource(R.drawable.up_btn_bg);
                up_lesson.setEnabled(true);
                next_lesson.setEnabled(false);
                next_lesson.setBackgroundResource(R.drawable.no_next);
            }
        }else if (num==1){
            up_lesson.setBackgroundResource(R.drawable.no_up);
            next_lesson.setBackgroundResource(R.drawable.no_next);
            next_lesson.setEnabled(false);
            up_lesson.setEnabled(false);
        }
    }
    public void uplesson(){
        indext--;
       // lessonnum=lessonResponse.getLessonResponse().lessonInfo[indext].getLessonId();
        Logger.e("eliminateSuccess========="+"uplesson:"+(indext));
//        hListViewAdapter=new HorizontalListViewAdapter(getContext(),indext-1,lessonResponse.getCoach(),lessonResponse.getMembername(),lessonResponse.getMemberphone(),lessonId[indext],lessonName[indext],lessonDate[indext]);
//        horizontalListView.setAdapter(hListViewAdapter);
//        hListViewAdapter.notifyDataSetChanged();
        Logger.e("eliminateSuccess========="+"uplesson"+lessonResponse.toString());
        Message msg=mHandler.obtainMessage();
        msg.what=2;
        mHandler.sendMessage(msg);
        checkButton();
    }
    public void nextlesson(){
        indext++;
      //  lessonnum=lessonResponse.getLessonResponse().lessonInfo[indext].getLessonId();
        Logger.e("eliminateSuccess========="+"nextlesson"+"nextlesson:"+indext);
//        hListViewAdapter=new HorizontalListViewAdapter(getContext(),indext+1,lessonResponse.getCoach(),lessonResponse.getMembername(),lessonResponse.getMemberphone(),lessonId[indext],lessonName[indext],lessonDate[indext]);
//        horizontalListView.setAdapter(hListViewAdapter);
//        hListViewAdapter.notifyDataSetChanged();
        Logger.e("eliminateSuccess========="+lessonResponse.toString());
        Message msg=mHandler.obtainMessage();
        msg.what=2;
        mHandler.sendMessage(msg);
        checkButton();
    }
    @Override
    public void onPermissionError(ApiException e) {
        Logger.e("EliminateLessonFragment:--onPermissionError");
        onError(e);
    }
    @Override
    public void onResultError(ApiException e) {
        this.showProgress(true, false, e.getDisplayMessage());
        Logger.e("EliminateLessonFragment:--onResultError"+e.getDisplayMessage());
        onError(e);
    }

    @Override
    public void onError(ApiException error) {
        Logger.e("EliminateLessonFragment:--onError"+error.getDisplayMessage());
        this.showToast(error.getDisplayMessage());
        super.onError(error);
//        this.showProgress(true, true, error.getDisplayMessage());
        if (BaseApplication.DEBUG) {
            Logger.e(error.getMessage());
            this.showToast(error.getMessage());
        }
        this.showProgress(false);
    }

    @Override
    public void onStop() {
        Logger.e("LessonFragment_test============onStop");
        super.onStop();
    }
    @Override
    public void onDestroy() {
        bRun=false;
        flog=false;
        Logger.e("LessonFragment_test============onDestroy");
        if (runnable!=null) {
        }
        if (runnable2!=null){
        }
        super.onDestroy();
    }

}