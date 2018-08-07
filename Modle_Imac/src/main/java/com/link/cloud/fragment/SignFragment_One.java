package com.link.cloud.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.BaseApplication;
import com.link.cloud.R;
import com.link.cloud.activity.BindAcitvity;
import com.link.cloud.activity.NewMainActivity;
import com.link.cloud.activity.WorkService;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.bean.Member;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.bean.SignUserdata;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.greendao.gen.PersonDao;
import com.link.cloud.greendaodemo.Person;
import com.link.cloud.model.MdFvHelper;
import com.link.cloud.utils.Utils;
import com.orhanobut.logger.Logger;
import org.apache.commons.lang.StringUtils;
import com.link.cloud.activity.CallBackValue;
import com.link.cloud.activity.SigeActivity;
import com.link.cloud.contract.MatchVeinTaskContract;
import com.link.cloud.core.BaseFragment;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import md.com.sdk.MicroFingerVein;


import static android.content.Context.MODE_MULTI_PROCESS;
import static com.alibaba.sdk.android.ams.common.util.HexUtil.bytesToHexString;
import static com.alibaba.sdk.android.ams.common.util.HexUtil.hexStringToByte;
import static com.link.cloud.utils.Utils.byte2hex;

public class SignFragment_One extends BaseFragment implements MatchVeinTaskContract.MatchVeinView,SendLogMessageTastContract.sendLog{
    @Bind(R.id.layout_two)
    LinearLayout layout_two;
    @Bind(R.id.layout_three)
    LinearLayout layout_three;
    @Bind(R.id.bind_member_next)
    Button next;
    @Bind(R.id.bind_member_name)
    TextView menber_name;
    @Bind(R.id.bind_member_cardtype)
    TextView cardtype;
    @Bind(R.id.userType)
    TextView userType;
    @Bind(R.id.bind_member_cardnumber)
    TextView cardnumber;
    @Bind(R.id.bind_member_begintime)
    TextView startTime;
    @Bind(R.id.bind_member_endtime)
    TextView endTime;
    @Bind(R.id.bind_member_sex)
    TextView menber_sex;
    @Bind(R.id.bind_member_phone)
    TextView menber_phone;
    @Bind(R.id.layout_error_text)
    LinearLayout layout_error_text;
    @Bind(R.id.text_error)
    TextView text_error;
    @Bind(R.id.button_layout)
    LinearLayout button_layout;
    byte[] aByte={0};
    int action;
    public SigeActivity activity;
    private SharedPreferences userInfo;
    public static CallBackValue callBackValue;
    MatchVeinTaskContract matchVeinTaskContract;
    SendLogMessageTastContract sendLogMessageTastContract;
//    byte[] featuer = null;
    Context context;
    int state = 0;
    int[] pos = new int[1];
    float[] score = new float[1];
    boolean ret = false;
//    int[] tipTimes = {0, 0};//后两次次建模时用了不同手指，重复提醒限制3次
//    int modOkProgress = 0;
    private PersonDao personDao;
    String deviceId;
//    boolean bopen=false;
//    MicroFingerVein microFingerVein;
    String userUid;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=(SigeActivity) activity;
        callBackValue=(CallBackValue) activity;
    }
    public static SignFragment_One newInstance() {
        SignFragment_One fragment = new SignFragment_One();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=activity.getApplicationContext();
        setupParam();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.layout_bind_member;
    }
    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        callBackValue.setActivtyChange("2");
        layout_two.setVisibility(View.GONE);
        layout_three.setVisibility(View.VISIBLE);
        sendLogMessageTastContract=new SendLogMessageTastContract();
        sendLogMessageTastContract.attachView(this);
    }
    @Override
    protected void initListeners() {
    }
    EditText code_mumber;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initData() {
        layout_error_text.setVisibility(View.VISIBLE);
        matchVeinTaskContract=new MatchVeinTaskContract();
        matchVeinTaskContract.attachView(this);
        userInfo = activity.getSharedPreferences("user_info", 0);

    }
    @Override
    protected void onVisible() {
    }
    @Override
    protected void onInvisible() {
    }
    ConnectivityManager connectivityManager;
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
                userInfo = activity.getSharedPreferences("user_info", MODE_MULTI_PROCESS);
                deviceId = userInfo.getString("deviceId", "");
                connectivityManager =(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                    matchVeinTaskContract.checkInByQrCode(deviceId, code_mumber.getText().toString());
                }else {
                    activity.mTts.startSpeaking(getResources().getString(R.string.network_error),activity.mTtsListener);
                }
                code_mumber.setText("");

            }
        }
    }
 Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (text_error!=null) {
                        text_error.setText(R.string.finger_right);
                    }
                    break;
                case 1:
                   activity.showProgress(true);
                    if(text_error!=null) {
                        text_error.setText(R.string.check_successful);
                    }
                    SharedPreferences userinfo=activity.getSharedPreferences("user_info",0);
                    deviceId=userinfo.getString("deviceId","");
                    ConnectivityManager connectivityManager;//用于判断是否有网络
                    connectivityManager =(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                    NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //
                    if (info!=null) {
                        matchVeinTaskContract.signedMember(deviceId, userUid, "vein");
                    }else {
                        activity.mTts.startSpeaking(getResources().getString(R.string.network_error),activity.mTtsListener);
                        showProgress(false, false, "网络已断开");
                    }
                    break;
                case 2:
                    if (text_error!=null) {
                        activity.mTts.startSpeaking(getResources().getString(R.string.check_failed),activity.mTtsListener);
                        text_error.setText(R.string.check_failed);
                    }
                    break;
                case 3:
//                    if (text_error!=null) {
//                        text_error.setText("请抬起手指，重新放置");
//                    }
                    break;
            }
        }
    };
    @Override
    public void onPermissionError(ApiException e) {
        onError(e);
    }
    @Override
    public void onError(ApiException e) {
        super.onError(e);
        this.showProgress(false);
        ConnectivityManager connectivityManager;//用于判断是否有网络
        connectivityManager =(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
        NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
            String reg = "[^\u4e00-\u9fa5]";
            String syt=e.getMessage().replaceAll(reg, "");
            Logger.e("SignActivity"+syt);
            Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    if (text_error!=null) {
                        activity.mTts.startSpeaking(syt,activity.mTtsListener);
                        text_error.setText(syt);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            };
            new Handler(Looper.getMainLooper()).post(runnable);

        setupParam();
    }

    @Override
    public void sendLogSuccess(RestResponse resultResponse) {
    }

    @Override
    public void onResultError(ApiException e) {
        onError(e);
    }
    Handler mHandler=new Handler();

    @Override
    public void signSuccess(Code_Message code_message) {
        this.showProgress(false);
        SignFragment_Two fragment = SignFragment_Two.newInstance(code_message);
        ((SignInMainFragment)this.getParentFragment()).addFragment(fragment, 1);
    }

    @Override
    public void checkInSuccess(Code_Message code_message) {
        SignFragment_Two fragment = SignFragment_Two.newInstance(code_message);
        ((SignInMainFragment)this.getParentFragment()).addFragment(fragment, 1);
    }

    private Thread mdWorkThread=null;//进行建模或认证的全局工作线程
    private void setupParam() {
        activity.bRun=true;
        mdWorkThread=new Thread(runnablemol);
        mdWorkThread.start();
    }
    boolean isstart=false;
    Runnable  runnablemol=new Runnable() {
        @Override
        public void run() {
            while (activity.bRun) {
               try {
                   state =WorkService.microFingerVein.fvdev_get_state();
               }catch (Exception e){

               }

                //设备连接正常则进入正常建模或认证流程
                if (state != 0&&isstart==false) {
                    Logger.e("BindActivty===========state" + state);
                    if (state == 1 || state == 2) {
                        continue;
                    }
                    byte[] img= MdFvHelper.tryGetFirstBestImg(WorkService.microFingerVein,0,5);
                    if (img == null) {
                        if (handler != null) {
                        Message message = new Message();
                        message.what = 3;
                        handler.sendMessage(message);
                    }
                        continue;
                    }else {
                        isstart=true;
                    }
//                    quality=WorkService.microFingerVein.fv_quality(img);//添加图像质量评估，为0为最佳；
//                    Logger.e("BindActivty===========quality"+quality);
//                    if(quality!=0){
//                        continue;
//                    }
                    userUid=findfeature(img);
                    if (userUid!=null) {
                        Log.e("Identify success,", "pos=" + pos[0] + ", score=" + score[0]);
                        if (handler != null) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    } else {
                        if (handler != null) {
                            Log.e("Identify failed,", "ret=" + ret + ",pos=" + pos[0] + ", score=" + score[0]);
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                        }
                    }
                    while(state==3&&activity.bRun){
                        state=WorkService.microFingerVein.fvdev_get_state();
                    }
                    continue;
                }else {
                    isstart=false;
                    if (handler != null) {
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                }
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
            if (!Utils.isEmpty(strValue)) {
                if (strValue.length() != 0) {
                    feature[i] = hexStringToByte(strValue);
                    Uids[i] = cursor.getString(cursor.getColumnIndex("UID"));
                    i++;
                }
            }
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
        String uids=StringUtils.join(Uids,",");
        SharedPreferences userinfo=activity.getSharedPreferences("user_info",0);
        deviceId=userinfo.getString("deviceId","");
         ConnectivityManager connectivityManager;//用于判断是否有网络
        connectivityManager =(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
        NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
         if (identifyResult) {
          if (info!=null&&uids!=null) {
           sendLogMessageTastContract.sendLog(deviceId, Uid, uids, bytesToHexString(img), strBeginDate, score[0] + "",activity.getResources().getString(R.string.check_successful));
        }
           Logger.e("SignActivity"+"pos="+pos[0]+"score="+score[0]+"成功");
            return Uid;
        }else {
            if (info!=null&&uids!=null) {
                sendLogMessageTastContract.sendLog(deviceId, Uid, uids, bytesToHexString(img), strBeginDate, score[0] + "", activity.getResources().getString(R.string.check_failed));
            }
                    Logger.e("SignActivity"+"pos="+pos[0]+"score="+score[0]+"失败");
            return null;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        code_mumber=(EditText) activity.findViewById(R.id.qrcode);
        code_mumber.setFocusable(true);
        code_mumber.setCursorVisible(true);
        code_mumber.setFocusableInTouchMode(true);
        code_mumber.requestFocus();
        /**
         * EditText编辑框内容发生变化时的监听回调
         */
        code_mumber.addTextChangedListener(new EditTextChangeListener());

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.bRun=false;
        if (handler!=null) {
            handler.removeCallbacksAndMessages(null);
            Logger.e("SignFragment_one"+"onDestroy");
        }
        handler=null;
    }
}
