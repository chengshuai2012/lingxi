package com.link.cloud.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.bean.SignUserdata;
import com.orhanobut.logger.Logger;

import com.link.cloud.activity.CallBackValue;
import com.link.cloud.activity.SigeActivity;
import com.link.cloud.constant.Constant;
import com.link.cloud.core.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignFragment_Two extends BaseFragment  {
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
    @Bind(R.id.userType)
    TextView usertype;
    @Bind(R.id.button_layout)
     LinearLayout button_layout;
    SignUserdata userdata;
    public Runnable runnable;
    public SigeActivity activity;
    public static CallBackValue callBackValue;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=(SigeActivity) activity;
        callBackValue=(CallBackValue) activity;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static SignFragment_Two newInstance(Code_Message userdata) {
        Logger.e("SignFragment_Two-------newInstance");
        SignFragment_Two fragment = new SignFragment_Two();
        Bundle args = new Bundle();
        args.putString("membername",userdata.getData().getUserInfo().getName());
        args.putInt("sex",userdata.getData().getUserInfo().getSex());
        args.putInt("usertype",userdata.getData().getUserInfo().getUserType());
        args.putString("userphone",userdata.getData().getUserInfo().getPhone());
         fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.layout_bind_member;
    }
    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        Logger.e("SignFragment_Two-------initViews");
        Bundle bundle = getArguments();
        if (bundle != null) {
            button_layout.setVisibility(View.INVISIBLE);
//            layout_three.setVisibility(View.GONE);
//            layout_two.setVisibility(View.VISIBLE);
            menber_name.setText(bundle.getString("membername"));
            if (bundle.getInt("sex")!= 0) {
                menber_sex.setText(R.string.girl);
            } else {
                menber_sex.setText(R.string.man);
            }
            String phoneNum = bundle.getString("userphone");
            if (phoneNum.length() == 11) {
                phoneNum = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length());
            }
            menber_phone.setText(phoneNum);
            if (bundle.getInt("usertype")==1) {
                usertype.setText(R.string.member);
            } else if (bundle.getInt("usertype")==2) {
                usertype.setText(R.string.employee);
            } else {
                usertype.setText(R.string.coach);
            }
        }
        callBackValue.setActivtyChange("3");
//            userdata = (SignUserdata) bundle.getSerializable(Constant.EXTRAS_SIGNED_INFO);
//            menber_name.setText(userdata.getSigndata().getUserInfo().getName());
//            String phoneNum = userdata.getSigndata().getUserInfo().getPhone();
//            if (userdata.getSigndata().getUserInfo().getPhone().length() == 11) {
//                phoneNum = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length());
//            }
//            menber_phone.setText(phoneNum);
//            if (userdata.getSigndata().getUserInfo().getSex() != 0) {
//                menber_sex.setText("女");
//            } else {
//                menber_sex.setText("男");
//            }
//            if (userdata.getSigndata().getMemberCard().getCardName()!=null){
//                cardtype.setText(userdata.getSigndata().getMemberCard().getCardName());
//                cardnumber.setText(userdata.getSigndata().getMemberCard().getCardNumber());
//                startTime.setText(userdata.getSigndata().getMemberCard().getBeginTime()+"-");
//                endTime.setText(userdata.getSigndata().getMemberCard().getEndTime());
//            }
//        }
    }
    @Override
    protected void initListeners() {
    }
    @Override
    protected void initData() {
        Logger.e("SignFragment_Two-------initData");
    }
    @Override
    protected void onVisible() {
        Logger.e("SignFragment_Two-------onVisible");
    }
    @Override
    protected void onInvisible() {
    }
    @Override
    public void onDestroy() {
        this.showProgress(false);
//        mHandler.removeCallbacks(runnable);
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
