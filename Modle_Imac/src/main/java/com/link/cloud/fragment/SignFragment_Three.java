package com.link.cloud.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.R;
import com.orhanobut.logger.Logger;

import com.link.cloud.activity.CallBackValue;
import com.link.cloud.activity.NewMainActivity;
import com.link.cloud.activity.SigeActivity;
import com.link.cloud.bean.SignedResponse;
import com.link.cloud.constant.Constant;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignFragment_Three extends BaseFragment {
//    @Bind(R.id.employee_Layout)
//    LinearLayout employeeLayout;
//    @Bind(R.id.employee_img)
//    ImageView ivEmployeeAvatar;
//    @Bind(R.id.bind_employee_name)
//    TextView tvEmployeeName;
//    @Bind(R.id.bind_employee_sex)
//    TextView tvEmployeeSex;
//    @Bind(R.id.bind_employee_position)
//    TextView tvEmployeePosition;
//    @Bind(R.id.bind_employee_phone)
//    TextView tvEmployeePhoneNum;
//    @Bind(R.id.menber_layout_1)
//    LinearLayout memberLayout1;
//    @Bind(R.id.menber_layout_2)
//    LinearLayout memberLayout2;
//    @Bind(R.id.sign_menber_img)
//    ImageView ivMemberAvatar;
//    @Bind(R.id.sign_balancetime)
//    TextView tvCardBalanceQuantity;
//    @Bind(R.id.menber_endtime)
//    TextView tvExpiry;
//    @Bind(R.id.sign_tv_name)
//    TextView tvMemberName;
//    @Bind(R.id.sign_tv_sex)
//    TextView tvMemberSex;
//    @Bind(R.id.sign_tv_phone)
//    TextView tvMemberPhoneNum;
//    @Bind(R.id.menber_cardtype)
//    TextView tvCardType;
//    @Bind(R.id.sign_CardBalance)
//    TextView tvCardBalance;
//    @Bind(R.id.sige_tvDeviceName)
//    TextView tvDeviceName;
//    @Bind(R.id.sige_tvCaseNo)
//    TextView tvCaseNo;
//    @Bind(R.id.sige_cardTypeLayout)
//    LinearLayout cardTypeLayout;
//    @Bind(R.id.sign_BalanceLayout)
//    LinearLayout cardBalanceLayout;
//    @Bind(R.id.sign_cardBalanceQuantity)
//    LinearLayout cardBalanceQuantityLayout;
    private SignedResponse signedInfo;
    public Handler handler=new Handler();
    private Runnable runnable;
    private SigeActivity activity;
    public static CallBackValue callBackValue;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=(SigeActivity) activity;
        callBackValue=(CallBackValue) activity;
    }
    public static SignFragment_Three newInstance(SignedResponse signedResponse) {
        SignFragment_Three fragment = new SignFragment_Three();
        Bundle args = new Bundle();
        args.putSerializable(Constant.EXTRAS_SIGNED_INFO, signedResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_bind_member;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            signedInfo = (SignedResponse) bundle.getSerializable(Constant.EXTRAS_SIGNED_INFO);
            Logger.e("SignFragment_Three:"+signedInfo.toString());
            if (Utils.isEmpty(signedInfo.getPosition())) {
                //没有职位信息表示会员签到
//                employeeLayout.setVisibility(View.GONE);
//                memberLayout1.setVisibility(View.VISIBLE);
//                memberLayout2.setVisibility(View.VISIBLE);

//                tvMemberName.setText(signedInfo.getName());
                String phoneNum = signedInfo.getPhone();
                if (phoneNum.length() == 11) {
                    phoneNum = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length());
                }
//                tvMemberPhoneNum.setText(phoneNum);
                //卡类型
                if (Utils.isEmpty(signedInfo.getCardType())) {
//                    cardTypeLayout.setVisibility(View.GONE);
                } else {
//                    tvCardType.setText(signedInfo.getCardType());
//                    cardTypeLayout.setVisibility(View.VISIBLE);
                }
                //卡余额
                if (Utils.isEmpty(signedInfo.getCardBalance())) {
//                    cardBalanceLayout.setVisibility(View.GONE);
                } else {
//                    tvCardBalance.setText(signedInfo.getCardBalance());
//                    cardBalanceLayout.setVisibility(View.VISIBLE);
                }
//                tvMemberSex.setText(signedInfo.getSex());
//                tvDeviceName.setText(signedInfo.getDeviceName());
//                tvCaseNo.setText(signedInfo.getCabinetNumber());
                //卡剩余次数
                if (Utils.isEmpty(signedInfo.getRemainder())) {
//                    cardBalanceQuantityLayout.setVisibility(View.GONE);
                } else {
//                    tvCardBalanceQuantity.setText(signedInfo.getRemainder());
//                    cardBalanceQuantityLayout.setVisibility(View.VISIBLE);
                }
                //卡有效截止日期
                String endTime = signedInfo.getEndTime();
                if (Utils.isEmpty(endTime)) {
//                    tvExpiry.setText("不限时间");
                } else {
//                    tvExpiry.setText("至" + Utils.stringPattern(endTime, "yyyy-MM-dd", "yyyy年MM月dd日"));
                }
            } else {
                //有职位信息表示员工签到
//                employeeLayout.setVisibility(View.VISIBLE);
//                memberLayout1.setVisibility(View.GONE);
//                memberLayout2.setVisibility(View.GONE);
//                tvEmployeeName.setText(signedInfo.getName());
//                tvEmployeeSex.setText(signedInfo.getSex());
//                tvEmployeePosition.setText(signedInfo.getPosition());
                String phoneNum = signedInfo.getPhone();
                if (phoneNum.length() == 11) {
                    phoneNum = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length());
                }
//                tvEmployeePhoneNum.setText(phoneNum);
            }
            runnable=new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(activity,NewMainActivity.class);
                    startActivity(intent);
                }
            };
            handler.postDelayed(runnable
                    ,8000);
        }
    }

    @Override
    protected void initListeners() {
    }
    @Override
    protected void initData() {
    }
    @Override
    protected void onVisible() {
    }
    @Override
    protected void onInvisible() {
    }
    @Override
    public void onDestroy() {
        this.showProgress(false);
        if (runnable!=null){
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
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
    }
}
