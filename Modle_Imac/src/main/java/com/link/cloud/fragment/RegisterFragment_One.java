package com.link.cloud.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.link.cloud.BaseApplication;
import com.link.cloud.BuildConfig;
import com.link.cloud.R;
import com.link.cloud.activity.BindAcitvity;
import com.link.cloud.activity.CallBackValue;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.FaceBindBean;
import com.link.cloud.bean.Member;
import com.link.cloud.contract.RegisterTaskContract;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.utils.Utils;
import com.orhanobut.logger.Logger;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterFragment_One extends BaseFragment implements RegisterTaskContract.RegisterView{

    @Bind(R.id.phone_regist_one)
    EditText etPhoneNum;
//    @Bind (R.id.bind_keypad_x)
//    Button numberx;
    @Bind(R.id.bind_keypad_0)
    Button number0;
    @Bind(R.id.bind_keypad_1)
    Button number1;
    @Bind(R.id.bind_keypad_2)
    Button number2;
    @Bind(R.id.bind_keypad_3)
    Button number3;
    @Bind(R.id.bind_keypad_4)
    Button number4;
    @Bind(R.id.bind_keypad_5)
    Button number5;
    @Bind(R.id.bind_keypad_6)
    Button number6;
    @Bind(R.id.bind_keypad_7)
    Button number7;
    @Bind(R.id.bind_keypad_8)
    Button number8;
    @Bind(R.id.bind_keypad_9)
    Button number9;
    @Bind(R.id.bind_keypad_delect)
    Button delect;
    @Bind(R.id.bind_keypad_ok)
    Button next;
    @Bind(R.id.bind_step_code)
    LinearLayout bind_step_code;
    @Bind(R.id.phonecode_regist_one)
    EditText phonecode_regist_one;
    @Bind(R.id.regist_code_bt)
    Button regist_code_bt;
    @Bind(R.id.regist_code)
    EditText regist_code;
    @Bind(R.id.bind_step_phone)
    LinearLayout bind_step_phone;
    @Bind(R.id.error_layout)
    RelativeLayout error_layout;
    @Bind(R.id.error_tv)
    TextView error_tv;
    @Bind(R.id.code_tv)
    TextView code_tv;
//    String TAG="RegisterFragment";
    public RegisterTaskContract presenter;
    private Subscription countDown;
    Editable editable;
    String deviceID=null;
    int index;
    SharedPreferences userInfo;
    //用户保存手机号码与验证码，获取验证码成功添加一条记录，点击下一步删除一条记录
    private static final HashSet verifySet = new HashSet();
    //记录当前用户信息
    private NextAction action = NextAction.GET_MEMBER_INFO;
    private Context context;
    BindAcitvity bindActivity;
//    private MediaPlayer mPlayer,mPlayer1,mPlayer2;
    public BindAcitvity acitvity;
    public static CallBackValue callBackValue;
    int numberType=1;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.acitvity=(BindAcitvity) activity;
        callBackValue=(CallBackValue) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mPlayer=MediaPlayer.create(getActivity(),R.raw.error_phone);
//        mPlayer1=MediaPlayer.create(getActivity(),R.raw.error_verify);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        bindActivity= new BindAcitvity();
        return rootView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        presenter.detachView();
        ButterKnife.unbind(this);
    }
    enum NextAction {
        GET_MEMBER_INFO(0),
        GET_VERIFY_CODE(1),
        REGISTER(2);
        int state = 0;
        NextAction(int state) {
            this.state = state;
        }
        public int getState() {
            return state;
        }
    }
    public static RegisterFragment_One newInstance() {
        RegisterFragment_One fragment = new RegisterFragment_One();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.layout_bing_phone;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initData() {
        etPhoneNum.setShowSoftInputOnFocus(false);
        phonecode_regist_one.setShowSoftInputOnFocus(false);
        regist_code.setShowSoftInputOnFocus(false);
        etPhoneNum.requestFocus();//获取焦点 光标出现
        etPhoneNum.setText("");
        regist_code.setText("");
        phonecode_regist_one.setText("");
        action = NextAction.GET_MEMBER_INFO;
        etPhoneNum.setClickable(true);
        bind_step_code.setVisibility(View.GONE);
//        verifyLayout.setVisibility(View.GONE);
//        getVerifyCode.setClickable(true);
//        getVerifyCode.setText(R.string.get_verify_code);
    }

    @Override
    protected void initListeners() {
        this.presenter = new RegisterTaskContract();
        this.presenter.attachView(this);
    }

    @Override
    protected void onVisible() {
        initData();
    }

    @Override
    protected void onInvisible() {
        if (BuildConfig.DEBUG)
        if (countDown != null)
            countDown.unsubscribe();
    }

    @Override
    public void onSuccess(Member memberInfo) {
        this.showProgress(false);
        callBackValue.setActivtyChange("2");
        if (memberInfo != null) {
//            this.userMenber = memberInfo;
            this.showProgress(false);
                RegisterFragment_Two memberInfoFragment = RegisterFragment_Two.newInstance(memberInfo);
                ((BindVeinMainFragment) getParentFragment()).addFragment(memberInfoFragment, 1);
//            }
        } else {
            //接口成功回调，但是没有返回用户信息
            if (countDown != null)
                countDown.unsubscribe();
            this.showProgress(false, false, "接口成功回调，但是没有返回用户信息");
        }
    }

    @Override
    public void onBindFaceSuccess(FaceBindBean faceBindBean) {

    }

    @Override
    public void onFaceSuccess(FaceBindBean faceBindBean) {

    }

    @Override
    public void onError(ApiException e) {
        super.onError(e);
        this.showProgress(false);
        this.showProgress(false, false, e.getDisplayMessage());
    }
    @Override
    public void onPermissionError(ApiException e) {
        onError(e);
    }
    @Override
    public void onResultError(ApiException e) {
        onError(e);
    }
    @OnClick({R.id.bind_keypad_ok,R.id.bind_keypad_0,R.id.bind_keypad_1,R.id.bind_keypad_2,R.id.bind_keypad_3,R.id.bind_keypad_4,
            R.id.bind_keypad_5,R.id.bind_keypad_6,R.id.bind_keypad_7,R.id.bind_keypad_8,R.id.bind_keypad_9,R.id.bind_keypad_delect})
    public void onClick(View view) {
        if (regist_code.hasFocus()){
            index = regist_code.getSelectionStart();
            editable = regist_code.getText();
        }else if (phonecode_regist_one.hasFocus()){
            index=phonecode_regist_one.getSelectionStart();
            editable=phonecode_regist_one.getText();
        }
        else{
            index = etPhoneNum.getSelectionStart();
            editable = etPhoneNum.getText();
        }
         String phoneNum, registcode;
        View focusView = null;
        boolean cancel = false;
        switch (view.getId()) {
            case R.id.bind_keypad_0:
                editable.insert(index, "0");
                break;
            case R.id.bind_keypad_1:
                editable.insert(index, "1");
                break;
            case R.id.bind_keypad_2:
                editable.insert(index, "2");
                break;
            case R.id.bind_keypad_3:
                editable.insert(index, "3");
                break;
            case R.id.bind_keypad_4:
                editable.insert(index, "4");
                break;
            case R.id.bind_keypad_5:
                editable.insert(index, "5");
                break;
            case R.id.bind_keypad_6:
                editable.insert(index, "6");
                break;
            case R.id.bind_keypad_7:
                editable.insert(index, "7");
                break;
            case R.id.bind_keypad_8:
                editable.insert(index, "8");
                break;
            case R.id.bind_keypad_9:
                editable.insert(index, "9");
                break;
            case R.id.bind_keypad_delect:
                if (index>0) {
                    editable.delete(index - 1, index);
                }
                break;
            case R.id.bind_keypad_ok:
                Animation shakeAnim = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.shake_xx);
                phoneNum = etPhoneNum.getText().toString().trim();
                registcode=regist_code.getText().toString().trim();
                Verfiy verfiy = new Verfiy(phoneNum, registcode);
                etPhoneNum.setError(null);
                cancel = false;
                focusView = null;
                Logger.e("RegisterFragment_One------"+action);
                switch (action) {
                    case GET_MEMBER_INFO:
//                        if (Utils.isEmpty(phoneNum) || !Utils.isMobileNumberValid(phoneNum)) {
//                            Logger.e("RegisterFragment_One------GET_MEMBER_INFO");
//                            mPlayer.start();
//                            etPhoneNum.setError(getResources().getString(R.string.error_invalid_mobile_phone));
//                            focusView = etPhoneNum;
//                            cancel = true;
//                            break;
//                        }
                        showProgress(true);
                        userInfo = acitvity.getSharedPreferences("user_info", 0);
                        deviceID = userInfo.getString("deviceId", "");
                        numberType=userInfo.getInt("numberType",0);
                        Logger.e("RegisterFragment_OnedeviceID");
                        showProgress(true);
                        ConnectivityManager connectivityManager;
                        connectivityManager =(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                        NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                        if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    presenter.getMemInfo(deviceID, numberType, phoneNum);
                                }
                            }).start();
                        }else {
                            Toast.makeText(getContext(),R.string.please_check_net,Toast.LENGTH_LONG).show();
                        }
                        break;

                }
                if (cancel) {
                    focusView.requestFocus();
                }
                break;
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
        etPhoneNum.getText().clear();
//        etVerifyCode.getText().clear();
//        verifyLayout.setVisibility(View.GONE);
    }
    @Override
    public void onDestroy() {
        this.showProgress(false);
        super.onDestroy();
//        if (mPlayer!=null) {
//            mPlayer.stop();
//            mPlayer.release();
//            mPlayer = null;
//        }else if (mPlayer1!=null) {
//            mPlayer1.stop();
//            mPlayer1.release();
//            mPlayer1 = null;
//        }
    }
    public boolean validateVerifyCode(Verfiy verfiy) {
        if (verifySet.contains(verfiy))
            return true;
        else
            return false;
    }
    //重新获取验证码倒计时
    public void reNewCountDown() {
        countDown = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> arg0) {
                int i = 30;
                    while (i >= 0 && RegisterFragment_One.this.isVisble) {
                        try {
                            Thread.sleep(1000);
                            arg0.onNext(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i--;
                    }

                arg0.onCompleted();
            }
        })
                .subscribeOn(Schedulers.newThread())// 此方法为上面发出事件设置线程为新线程
                .observeOn(AndroidSchedulers.mainThread())// 为消耗事件设置线程为UI线程
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        regist_code_bt.setClickable(false);
                    }

                    @Override
                    public void onCompleted() {
                        regist_code_bt.setClickable(true);
                        regist_code_bt.setText(R.string.get_verify_code);
                    }
                    @Override
                    public void onError(Throwable arg0) {
                    }
                    @Override
                    public void onNext(Integer arg0) {
                        regist_code_bt.setText(arg0 + "秒后可再次发送");
                    }
                });
    }

    //验证码失效倒计时
    private void verifyCodeExpriedCountDown(String currentPhoneNum, String currentVerifyCode) {
        //五分钟后验证码失效
        Observable.timer(60 * 5, TimeUnit.SECONDS)
                .subscribe(aLong -> {
                    Verfiy verfiy = new Verfiy(currentPhoneNum, currentVerifyCode);
                    if (RegisterFragment_One.verifySet != null)
                        RegisterFragment_One.verifySet.remove(verfiy);
                    if (BaseApplication.DEBUG) {
                        Logger.e("verifyCodeExpriedCountDown " + verfiy + ";verify size : " + RegisterFragment_One.verifySet.size());
                    }
                });
    }
    class Verfiy {
        private String phone;
        private String verifyCode;
        public Verfiy(String phone, String verifyCode) {
            this.phone = phone;
            this.verifyCode = verifyCode;
        }
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o.getClass() == Verfiy.class) {
                Verfiy n = (Verfiy) o;
                return n.phone.equals(phone) && n.verifyCode.equals(verifyCode);
            }
            return false;
        }
        public int hashCode() {
            return (phone + verifyCode).hashCode();
        }
        @Override
        public String toString() {
            return "Verfiy{" +
                    "phone='" + phone + '\'' +
                    ", verifyCode='" + verifyCode + '\'' +
                    '}';
        }
    }
}
