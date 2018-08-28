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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.activity.CallBackValue;
import com.link.cloud.activity.NewMainActivity;
import com.link.cloud.activity.SigeActivity;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.contract.MatchVeinTaskContract;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.utils.FileUtils;
import com.link.cloud.utils.ModelImgMng;
import com.link.cloud.utils.VenueUtils;
import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import butterknife.Bind;

import static com.link.cloud.BaseApplication.venueUtils;



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

    @Bind(R.id.card_value)

    TextView cardValue;
    @Bind(R.id.card_name)

    TextView cardName;
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

    RelativeLayout button_layout;

    public SigeActivity activity;

    private SharedPreferences userInfo;

    public static CallBackValue callBackValue;

    MatchVeinTaskContract matchVeinTaskContract;

    SendLogMessageTastContract sendLogMessageTastContract;

    Context context;

    String deviceId;

    @Override

    public void onAttach(Activity activity) {

        super.onAttach(activity);

        this.activity=(SigeActivity) activity;

        callBackValue=(CallBackValue) activity;



    }

    public  void initVenue(){

        venueUtils.initVenue(NewMainActivity.getMdbind(),activity,SignFragment_One.this,true,false);

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
        initVenue();
    }

    EditText code_mumber;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @Override

    protected void initData() {

        layout_error_text.setVisibility(View.VISIBLE);

        matchVeinTaskContract=new MatchVeinTaskContract();

        matchVeinTaskContract.attachView(this);

        deviceId=FileUtils.loadDataFromFile(activity,"deviceId.text");

    }

    @Override

    protected void onVisible() {

    }

    @Override

    protected void onInvisible() {

    }

    ConnectivityManager connectivityManager;



    @Override

    public void VeuenMsg(int state, String datas,String uidss,String features,String scores,String usertpye ) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (state){

                    case 0:

                        if (text_error!=null) {

                            text_error.setText(R.string.finger_right);

                        }

                        break;



                    case 1:



                        if(text_error!=null) {

                            text_error.setText(R.string.check_successful);

                        }

                        ConnectivityManager connectivityManager;//用于判断是否有网络

                        connectivityManager =(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务

                        NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //

                        if (info!=null) {

                            matchVeinTaskContract.signedMember(deviceId, datas, "vein");

                        }else {

                            activity.mTts.startSpeaking(getResources().getString(R.string.network_error),activity.mTtsListener);

                            //showProgress(false, false, "网络已断开");

                        }



                        DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        dateTimeformat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

                        String strBeginDate = dateTimeformat.format(new Date());

                        new Thread(new Runnable() {

                            @Override

                            public void run() {

                                sendLogMessageTastContract.sendLog(deviceId, datas, uidss, features, strBeginDate, scores + "",activity.getResources().getString(R.string.check_successful));

                            }

                        }).start();



                        break;



                    case 2:

                        if (text_error!=null) {

                            activity.mTts.startSpeaking(getResources().getString(R.string.check_failed),activity.mTtsListener);

                            text_error.setText(R.string.check_failed);

                        }

                        DateFormat dateTimeformat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        dateTimeformat1.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

                        String strBeginDate1 = dateTimeformat1.format(new Date());

                        new Thread(new Runnable() {

                            @Override

                            public void run() {

                                sendLogMessageTastContract.sendLog(deviceId, datas, uidss, features, strBeginDate1, scores + "",activity.getResources().getString(R.string.check_failed));

                            }

                        }).start();



                        break;

                }

            }
        });





    }


    @Override

    public void ModelMsg(int state, ModelImgMng modelImgMng, String feature) {



    }







    @Override

    public void onDestroy() {

        super.onDestroy();


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





    @Override

    public void signSuccess(Code_Message code_message) {

        button_layout.setVisibility(View.INVISIBLE);
            layout_three.setVisibility(View.GONE);
            layout_two.setVisibility(View.VISIBLE);
        menber_name.setText(code_message.getData().getUserInfo().getName());
        if (code_message.getData().getUserInfo().getSex()!= 0) {
            menber_sex.setText(R.string.girl);
        } else {
            menber_sex.setText(R.string.man);
        }
        try {
            cardName.setText(code_message.getData().getMemberCardInfo().get(0).getCardName());
            cardValue.setText(code_message.getData().getMemberCardInfo().get(0).getEndTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String phoneNum = code_message.getData().getUserInfo().getPhone();
        if (phoneNum.length() == 11) {
            phoneNum = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length());
        }
        menber_phone.setText(phoneNum);
        if (code_message.getData().getUserInfo().getUserType()==1) {
            userType.setText(R.string.member);
        } else if (code_message.getData().getUserInfo().getUserType()==2) {
            userType.setText(R.string.employee);
        } else {
            userType.setText(R.string.coach);
        }
        callBackValue.setActivtyChange("3");
    }







    @Override

    public void checkInSuccess(Code_Message code_message) {

        button_layout.setVisibility(View.INVISIBLE);
        layout_three.setVisibility(View.GONE);
        layout_two.setVisibility(View.VISIBLE);
        menber_name.setText(code_message.getData().getUserInfo().getName());
        if (code_message.getData().getUserInfo().getSex()!= 0) {
            menber_sex.setText(R.string.girl);
        } else {
            menber_sex.setText(R.string.man);
        }
        try {
            cardName.setText(code_message.getData().getMemberCardInfo().get(0).getCardName());
            cardValue.setText(code_message.getData().getMemberCardInfo().get(0).getEndTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String phoneNum = code_message.getData().getUserInfo().getPhone();
        if (phoneNum.length() == 11) {
            phoneNum = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length());
        }
        menber_phone.setText(phoneNum);
        if (code_message.getData().getUserInfo().getUserType()==1) {
            userType.setText(R.string.member);
        } else if (code_message.getData().getUserInfo().getUserType()==2) {
            userType.setText(R.string.employee);
        } else {
            userType.setText(R.string.coach);
        }
        callBackValue.setActivtyChange("3");

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



}