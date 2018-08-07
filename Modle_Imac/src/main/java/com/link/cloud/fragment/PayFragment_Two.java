package com.link.cloud.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.bean.SignUserdata;
import com.orhanobut.logger.Logger;
import com.link.cloud.BaseApplication;

import com.link.cloud.activity.CallBackValue;
import com.link.cloud.activity.PayActivity;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.SignedResponse;
import com.link.cloud.bean.Voucher;
import com.link.cloud.constant.Constant;
import com.link.cloud.contract.MatchVeinTaskContract;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PayFragment_Two extends BaseFragment implements MatchVeinTaskContract.MatchVeinView {

//    @Bind(R.id.pay_put_img)
//    ImageView pay_put_img;
//    @Bind(R.id.pay_put_error)
//    ImageView pay_put_error;
//    @Bind(R.id.pay_put_succese)
//    ImageView pay_put_succese;
//    @Bind(R.id.pay_put_tv)
    TextView tvTips;
    private static final int START_COUNTING = 1;
    private static final int COUNT_NUMBER = 30;
    public MatchVeinTaskContract presenter;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private String phoneNum, deviceID, veinFingerID, price, mark;
//    MediaPlayer mediaPlayer,mediaPlayer1,mediaPlayer2;
    //标记是从哪里跳转到这个页面
    private int doWhat = 0;
    public Runnable runnable;
    public static CallBackValue callBackValue;
    private PayActivity activity;
    private SharedPreferences userInfo;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=(PayActivity) activity;
        callBackValue=(CallBackValue) activity;
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                userInfo = getActivity().getSharedPreferences("user_info", 0);
                deviceID = userInfo.getString("DeviceID", "");
                phoneNum = "6991";
                veinFingerID = "IuqINyc2RVzlFoN50OAtont7aMLHK7dUDZFw2iALkiHUl8p2yna+MpLdF0KChyevTbJk8zXAnL3yBkdzhGZ39pW20x9AIcTncN1840KuCJU7aL4My1Yj25RuEZ1PTVMRZD" +
                        "+zDWzZ6ts0oWP+WvirmtpY5KVatmvUokJsmIkeoZl7cvQA/USyYrLFv43n3e5E5zOQN7nbd6nMqwkjSsVO1jNroaoRE9Bgj/uTECU1uhaHQMcrCHuJ1wcSPky4/yOaryPVnMZVxTVcy2y1dzZgo6O7C1cucIegLk+" +
                        "LQUYciGCZyNt1p43tJzpDKmKpmSYOdPvTcuxFVQBmyZvszbf9DtfJ6m9rasBkSqJ895HXZ6ZxrLa+UCrmBUIKUtLex2AbVhOGXPEO3sL1nb5lU9xz2wiRiB5G5JSbma3Wv5F+" +
                        "xhZOtWw857JZHCcE4QFF8N4DywtUt8rboUsac5fD0YKTqO/7RozgU0fvCRT/lKPfJcOG54EL7Hi26nbJwMI1lFs6bM0IBnD8+Gpzt2Zyema2m7ohCVioI+" +
                        "xqfrcWgy0K21i1lVsHV9kWO8GPeeLlObQTwrTgzCRnnbLfZe6PpK6TapkvygpZLa3/XLH+YV7BRri7F99yRZQ4iLfgk8rQh62uEaet0Y2FybDPnlXe2poQxMPkMHw+O8saMwVQcWwiA5o=";
                PayFragment_Two.this.showProgress(true, false, "请稍后");
                //签到需要查找会员信息
                if (doWhat == Constant.ACTION_SIGNIN) {
//                    PayFragment_Two.this.presenter.signedMember(phoneNum, deviceID, veinFingerID);
                }
                //消费
                else if (doWhat == Constant.ACTION_CONSUME) {
//                    PayFragment_Two.this.presenter.consumeRecord(phoneNum, deviceID, veinFingerID, price, "1", mark);
                }
            } else if (msg.what == 1) {
                if (getContext() == getActivity()) {
                    String errorMsg = (String) msg.obj;
                    tvTips.setText(errorMsg);
                    runnable=new Runnable() {
                        @Override
                        public void run() {
                            tvTips.setText("请按图示放置手指");

//                            pay_put_error.setVisibility(View.GONE);
                        }
                    };
                    if (Utils.isEmpty(errorMsg)) {
                    }
                    mHandler.postDelayed(runnable, 2000);
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         mediaPlayer=MediaPlayer.create(getActivity(), R.raw.error_sign_finger);
//        mediaPlayer1=MediaPlayer.create(getActivity(), R.raw.failure_sign);
//        mediaPlayer2=MediaPlayer.create(getActivity(), R.raw.failure_cost);
    }

    public static PayFragment_Two newInstance(String phoneNum, int doWhat) {
        Logger.e("SignFragment_Two-------newInstance");
        PayFragment_Two fragment = new PayFragment_Two();
        Bundle args = new Bundle();
        args.putString(Constant.EXTRAS_PHONENUM, phoneNum);
        args.putInt(Constant.EXTRAS_DOWHAT, doWhat);
        fragment.setArguments(args);
        return fragment;
    }

    public static PayFragment_Two newInstance(int doWhat, String phoneNum, String cost, String mark) {
        PayFragment_Two fragment = new PayFragment_Two();
        Bundle args = new Bundle();
        args.putInt(Constant.EXTRAS_DOWHAT, doWhat);
        args.putString(Constant.EXTRAS_PHONENUM, phoneNum);
        args.putString(Constant.EXTRAS_PRICE, cost);
        args.putString(Constant.EXTRAS_MARK, mark);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        Logger.e("SignFragment_Two-------initViews");
//        init();
        Bundle bundle = getArguments();
        if (bundle != null) {
            doWhat = bundle.getInt(Constant.EXTRAS_DOWHAT);
            userInfo = getActivity().getSharedPreferences("user_info", 0);
            deviceID = userInfo.getString("DeviceID", "");

            if (doWhat == Constant.ACTION_CONSUME) {
                phoneNum = bundle.getString(Constant.EXTRAS_PHONENUM);
                price = bundle.getString(Constant.EXTRAS_PRICE);
                mark = bundle.getString(Constant.EXTRAS_MARK);
            } else {
                phoneNum = bundle.getString(Constant.EXTRAS_PHONENUM);
            }
        }
    }

    @Override
    public void checkInSuccess(Code_Message code_message) {

    }

    @Override
    protected void initListeners() {
    }

    @Override
    protected void initData() {
        Logger.e("SignFragment_Two-------initData");
        presenter = new MatchVeinTaskContract();
        this.presenter.attachView(this);
    }

    @Override
    protected void onVisible() {
        Logger.e("SignFragment_Two-------onVisible");
        tvTips.setText("请按图示放置手指");

        //测试API接口用下面的代码
        //mHandler.sendEmptyMessage(0);
    }
//    private void init(){
//        Logger.e("SignFragment_Two-------init");
//        Message msg = mHandler1.obtainMessage();
//        msg.what = START_COUNTING;
//        msg.obj = COUNT_NUMBER;
//        mHandler1.sendMessageDelayed(msg, 30);
//    }

    @Override
    protected void onInvisible() {
    }

    @Override
    public void onDestroy() {
        this.showProgress(false);
        this.presenter.detachView();
        mHandler.removeCallbacks(runnable);
        super.onDestroy();
    }

//    @Override
//    public void verifyTemplateSuccess(String veinFingerID) {
//        Logger.e("SignFragment_Two-------verifyTemplateSuccess");
//        this.showProgress(true, false, "请稍后");
//        //签到需要查找会员信息
//        if (doWhat == Constant.ACTION_SIGNIN) {
//            this.presenter.signedMember(phoneNum, deviceID, veinFingerID);
//        }
//        //消费
//        else if (doWhat == Constant.ACTION_CONSUME) {
//            this.presenter.consumeRecord(phoneNum, deviceID, veinFingerID, price, "1", mark);
//        }
//    }
    @Override
    public void signSuccess(Code_Message signedResponse) {
        Logger.e("SignFragment_Two-------signSuccess");
        this.showProgress(false);
        callBackValue.setActivtyChange("4");
//        SignFragment_Three fragment = SignFragment_Three.newInstance(signedResponse);
        //签到
//        ((SignInMainFragment) this.getParentFragment()).addFragment(fragment, 2);
    }
//    @Override
//    public void costSuccess(Voucher voucherInfo) {
////        flag=true;
//        this.showProgress(false);
//        callBackValue.setActivtyChange("4");
//        if (voucherInfo.getStatus() == 0) {
//            PayFragment_Three fragment=PayFragment_Three.newInstance(voucherInfo);
//            ((PayMainFragment)this.getParentFragment()).addFragment(fragment,2);
////             new VoucherDialog(getContext(), voucherInfo).show();
////        } else {
////            if (!Utils.isEmpty(voucherInfo.getName())
////                    && !Utils.isEmpty(voucherInfo.getPhone())) {
////                new VoucherDialog(getContext(), voucherInfo).show();
////            } else {
////                ApiException e = new ApiException(new Throwable(voucherInfo.getMsg()), voucherInfo.getStatus());
////                e.setDisplayMessage(voucherInfo.getMsg());
////                onError(e);
////            }
//        }
//    }
    @Override
    public void onError(ApiException e) {
        Logger.e("SignFragment_Two-------onError");
        super.onError(e);
//        pay_put_error.setVisibility(View.VISIBLE);
        if (BaseApplication.DEBUG) {
            Logger.e(e.getMessage());
            this.showToast(e.getMessage());
        }
        this.showProgress(false);
        Message msg = mHandler.obtainMessage();
        msg.what = 1;
        if (!Utils.isEmpty(e.getDisplayMessage())) {
            msg.obj = e.getDisplayMessage();
        }
        mHandler.sendMessage(msg);
    }
//    @Override
//    public void verifyTemplateFailure(ApiException e) {
//        Logger.e("SignFragment_Two-------verifyTemplateFailure");
//        onError(e);
//    }

    @Override
    public void onPermissionError(ApiException e) {
        onError(e);
    }

    @Override
    public void onResultError(ApiException e) {
        onError(e);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }class VoucherDialog extends Dialog implements View.OnClickListener {
        private Voucher voucherInfo;
        private TextView tvNickname, tvPhoneNum, tvCost, tvBalance;

        //        private Button btConfilm;
        public VoucherDialog(Context context, Voucher voucherInfo) {
            super(context, R.style.customer_dialog);
            this.voucherInfo = voucherInfo;
//            setContentView(R.layout.costsuccess);
            this.setCancelable(false);
            this.setCanceledOnTouchOutside(false);
//            tvNickname = (TextView) findViewById(R.id.tvMemberName);
//            tvPhoneNum = (TextView) findViewById(R.id.tvMemberPhone);
//            tvCost = (TextView) findViewById(R.id.tvCardCost);
//            tvBalance = (TextView) findViewById(R.id.tvCardBalance);
        }

        @Override
        protected void onStart() {
            super.onStart();
            tvNickname.setText(this.voucherInfo.getName());
            String phoneNum = this.voucherInfo.getPhone();
            if (phoneNum.length() == 11) {
                phoneNum = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length());
            }
            tvPhoneNum.setText(phoneNum);
            DecimalFormat df = new DecimalFormat("#,##0.00");
            String cost = "", balance = "";
            try {
                cost = df.format(this.voucherInfo.getCost());
            } catch (Exception e) {
                cost = this.voucherInfo.getCost() + "";
            }
            try {
                balance = df.format(this.voucherInfo.getBalance());
            } catch (Exception e) {
                balance = this.voucherInfo.getBalance() + "";
            }
            tvCost.setText(cost + " 元");
            tvBalance.setText(balance + " 元");
//            speechSynthesizer.speak("本次消费"+cost+"元");
//            speechSynthesizer.speak("余额"+balance+"元");
        }

        @Override
        public void onClick(View view) {

        }
    }
}
