package com.link.cloud.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.activity.CallBackValue;
import com.link.cloud.activity.SigeActivity;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.contract.MatchVeinTaskContract;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.greendao.gen.PersonDao;
import com.link.cloud.utils.VenueUtils;
import com.orhanobut.logger.Logger;

import butterknife.Bind;

import static android.content.Context.MODE_MULTI_PROCESS;

public class SignFragment_One extends BaseFragment implements MatchVeinTaskContract.MatchVeinView,SendLogMessageTastContract.sendLog,VenueUtils.VenueCallBack{
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

    @Override
    public void VeuenMsg(int state, String data,String uids,String feature,String score ) {
        switch (state) {
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
                    matchVeinTaskContract.signedMember(deviceId, data, "vein");
                }else {
                    activity.mTts.startSpeaking(getResources().getString(R.string.network_error),activity.mTtsListener);
                    showProgress(false, false, "网络已断开");
                }
                sendLogMessageTastContract.sendLog(deviceId, data, uids, feature, System.currentTimeMillis()+"", score + "",activity.getResources().getString(R.string.check_successful));
                break;
            case 2:
                if (text_error!=null) {
                    activity.mTts.startSpeaking(getResources().getString(R.string.check_failed),activity.mTtsListener);
                    text_error.setText(R.string.check_failed);
                }
                sendLogMessageTastContract.sendLog(deviceId, data, uids, feature, System.currentTimeMillis()+"", score + "", activity.getResources().getString(R.string.check_failed));
                break;
            case 3:
//                    if (text_error!=null) {
//                        text_error.setText("请抬起手指，重新放置");
//                    }
                break;}
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
    }
}
