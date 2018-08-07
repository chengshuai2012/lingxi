package com.link.cloud.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.link.cloud.R;
import com.link.cloud.activity.WorkService;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.bean.RetrunLessons;
import com.link.cloud.bean.UserInfo;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.greendao.gen.PersonDao;
import com.link.cloud.greendaodemo.Person;
import com.link.cloud.ui.HorizontalListViewAdapter;
import com.link.cloud.ui.RollListView;
import com.link.cloud.utils.FileUtils;
import com.link.cloud.utils.Utils;
import com.orhanobut.logger.Logger;
import com.link.cloud.BaseApplication;

import com.link.cloud.activity.CallBackValue;
import com.link.cloud.activity.EliminateActivity;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.LessonResponse;
import com.link.cloud.contract.EliminateLessonContract;
import com.link.cloud.contract.UserLessonContract;
import com.link.cloud.core.BaseFragment;

import org.apache.commons.lang.StringUtils;
import org.greenrobot.greendao.query.QueryBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import md.com.sdk.MicroFingerVein;


import static com.alibaba.sdk.android.ams.common.util.HexUtil.bytesToHexString;
import static com.alibaba.sdk.android.ams.common.util.HexUtil.hexStringToByte;
import static com.link.cloud.utils.Utils.byte2hex;

/**
 * Created by Administrator on 2017/7/31.
 */

public class LessonFragment_test extends BaseFragment implements UserLessonContract.UserLesson,SendLogMessageTastContract.sendLog{
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
    SendLogMessageTastContract sendLogMessageTastContract;
    public static CallBackValue callBackValue;
    UserInfo caochInfo,userinfo;
    byte[] featuer = null;
    int state;
    byte[] img1 = null;
    boolean ret = false;
    int[] pos = new int[1];
    float[] score = new float[1];
    String userType;
    String userid,caochId,clerkid,personUid;
    EliminateActivity activity;
    PersonDao personDao;
    boolean flog=true;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=(EliminateActivity) activity;
        callBackValue=(CallBackValue)activity;
    }
    public  LessonFragment_test(){
    }
    public static LessonFragment_test newInstance(){
        LessonFragment_test fragment= new LessonFragment_test();
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
//        mediaPlayer=MediaPlayer.create(activity,R.raw.failure_sign);
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    PersonDao personDao=BaseApplication.getInstances().getDaoSession().getPersonDao();
                    QueryBuilder queryBuilder=personDao.queryBuilder();
                    List<Person>list=queryBuilder.where(PersonDao.Properties.Uid.eq(personUid)).list();
                    userType=list.get(0).getUserType();
                    Logger.e("userType======="+userType);
                    if (caochId==null&&"2".equals(userType)){
                        callBackValue.setActivtyChange("2");
                        caochId=personUid;
                         text_error.setText("请会员放置手指");
                    }else if (caochId!=null&&"1".equals(userType)){
                        userid=personUid;
                        callBackValue.setActivtyChange("3");
//                        layout_error_text.setVisibility(View.GONE);
//                        layout_three.setVisibility(View.GONE);
//                        layout_two.setVisibility(View.GONE);
//                        lessonLayout.setVisibility(View.VISIBLE);
                        flog=false;
                        Logger.e("LessonFragment========userid"+userid+"caochId"+caochId);
                        SharedPreferences userinfo=activity.getSharedPreferences("user_info",0);
                       String device=userinfo.getString("device","");
                        presenter.eliminateLesson(FileUtils.loadDataFromFile(activity,"deviceId.text"),1,userid,caochId,clerkid);
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
                presenter.selectLesson("1000834GS7K",1,lessonResponse.getLessonResponse().getLessonInfo()[selectPosition].getLessonId(),
                        userid,caochId,clerkid);
            }
        });
        if (flog=true){
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
            while (bRun) {
                state = WorkService.microFingerVein.fvdev_get_state();
                //设备连接正常则进入正常建模或认证流程
//                Logger.e("BindActivty===========state"+state);
                if (state != 0) {
                    Logger.e("BindActivty===========state" + state);
                    if (state == 1 || state == 2) {
                        continue;
                    } else if (state == 3) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    byte[] img = WorkService.microFingerVein.fvdev_grab();
                    Logger.e("BindActivty===========img" + img);
                    if (img == null) {
                        continue;
                    }
                    personUid=findfeature(img);
//                    ret=WorkService.microFingerVein.fv_index(featuer, featuer.length / 3352, img, pos, score);
//                    Logger.e("BindActivty===========count" +featuer.length / 3352 +"pos==="+pos[0]+"score= ="+score[0]);
                    if (personUid!=null ) {
                        Log.e("Identify success,", "pos=" + pos[0] + ", score=" + score[0]);
                        if (handler != null) {
                            Message message = new Message();
                            message.what = 0;
                            handler.sendMessage(message);
                        }
                        bopen = false;
                    } else {
                        if (handler != null) {
                            Log.e("Identify failed,", "ret=" + ret + ",pos=" + pos[0] + ", score=" + score[0]);
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else if (handler!=null){//触摸state==0时
                    try {
                        if (handler!=null) {
                            if(caochId!=null) {
                                handler.sendEmptyMessage(3);
                            }else {
                                handler.sendEmptyMessage(4);
                            }
                            Thread.sleep(200);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    if(bopen) {
//                        deviceTouchState = 1;
//                    }
                }
            }
            if (bopen){
                WorkService.microFingerVein.close();
                bopen=false;
            }
        }
    };
    String findfeature(byte[] img){
        long startime=System.currentTimeMillis();
        Log.e("SignFragment_one","startime:"+startime);
        personDao= BaseApplication.getInstances().getDaoSession().getPersonDao();
        String sql = "select FEATURE,UID from PERSON";
        int i =0;
        Cursor cursor = BaseApplication.getInstances().getDaoSession().getDatabase().rawQuery(sql,null);
        byte[][] feature=new byte[cursor.getCount()][];
        String [] Uids=new String[cursor.getCount()];
        while (cursor.moveToNext()){
            int nameColumnIndex = cursor.getColumnIndex("FEATURE");
            String strValue=cursor.getString(nameColumnIndex);
            feature[i]=hexStringToByte(strValue);
            Uids[i]=cursor.getString(cursor.getColumnIndex("UID"));
            i++;
        }
        int len = 0;
        // 计算一维数组长度
        for (byte[] element : feature) {
            len += element.length;
        }
        // 复制元素
        byte[] featuer = new byte[len];
        int index = 0;
        for (byte[] element : feature) {
            for (byte element2 : element) {
                featuer[index++] = element2;
            }
        }
        long endtime=System.currentTimeMillis();
        Log.e("SignFragment_one","endtime:"+endtime);
        boolean  identifyResult = WorkService.microFingerVein.fv_index(featuer, featuer.length / 3352, img, pos, score);//比对是否通过
        identifyResult = identifyResult && score[0] > 0.63;//得分是否达标
        DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strBeginDate = dateTimeformat.format(new Date());
        String Uid=null;
        if (Uids.length>0) {
            Uid = Uids[pos[0]];
        }
        String uids= StringUtils.join(Uids,",");
        SharedPreferences userinfo=activity.getSharedPreferences("user_info",0);
       String deviceId=userinfo.getString("deviceId","");
        ConnectivityManager connectivityManager;//用于判断是否有网络
        connectivityManager =(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
        NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
        if (identifyResult) {
            if (info!=null) {
                sendLogMessageTastContract.sendLog(deviceId, Uid, uids, bytesToHexString(img), strBeginDate, score[0] + "", "验证成功");
            }
            Logger.e("SignActivity"+"pos="+pos[0]+"score="+score[0]+"成功");
            return Uid;
        }else {
            if (info!=null) {
                sendLogMessageTastContract.sendLog(deviceId, Uid, uids, bytesToHexString(img), strBeginDate, score[0] + "", "验证失败");
            }
            Logger.e("SignActivity"+"pos="+pos[0]+"score="+score[0]+"失败");
            return null;
        }
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
        this.lessonResponse=lessonResponse;
        callBackValue.setActivtyChange("3");
        layout_error_text.setVisibility(View.GONE);
        layout_three.setVisibility(View.GONE);
        lessonLayout.setVisibility(View.VISIBLE);
        num=lessonResponse.getLessonResponse().getLessonInfo().length;
        lessonId=new String[num];
        lessonName=new String[num];
        lessonDate=new String[num];
        for (int i=0;i<num;i++) {
            lessonId[i]=lessonResponse.getLessonResponse().getLessonInfo()[i].getLessonId();
            lessonName[i]=lessonResponse.getLessonResponse().getLessonInfo()[i].getLessonName();
            lessonDate[i]=lessonResponse.getLessonResponse().getLessonInfo()[i].getLessonDate();
        }
        lessonnum=lessonResponse.getLessonResponse().getLessonInfo()[0].getLessonId();
        hListViewAdapter=new HorizontalListViewAdapter(getContext(),selectPosition,lessonResponse.getLessonResponse().getCoach(),lessonResponse.getLessonResponse().getMembername(),lessonResponse.getLessonResponse().getMemberphone(),lessonId[indext],lessonName[indext],lessonDate[indext]);
        horizontalListView.setAdapter(hListViewAdapter);
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                hListViewAdapter.setSelectIndex(position);
                selectPosition = position;
                lessonnum=lessonResponse.getLessonResponse().lessonInfo[position].getLessonId();
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
//        checkButton();
        Logger.e("eliminateSuccess========="+lessonResponse.toString());
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
    private Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                Logger.e("LessonFragment" + "handle" + lessonResponse.toString());
                hListViewAdapter = new HorizontalListViewAdapter(getContext(), indext, lessonResponse.getLessonResponse().getCoach(), lessonResponse.getLessonResponse().getMembername(), lessonResponse.getLessonResponse().getMemberphone(), lessonId[indext], lessonName[indext], lessonDate[indext]);
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
        lessonnum=lessonResponse.getLessonResponse().lessonInfo[indext].getLessonId();
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
        lessonnum=lessonResponse.getLessonResponse().lessonInfo[indext].getLessonId();
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