package com.link.cloud.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.R;
//import com.link.cloud.contract.RegisterTaskContract;
import com.orhanobut.logger.Logger;

import com.link.cloud.activity.BindAcitvity;
import com.link.cloud.activity.CallBackValue;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.Member;
import com.link.cloud.bean.ReturnBean;
import com.link.cloud.constant.Constant;
import com.link.cloud.contract.BindTaskContract;
import com.link.cloud.core.BaseFragment;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/30.
 */

public class RegisterFragment_Two extends BaseFragment {
//    @Bind(R.id.bind_bt_back)
//    Button back;
    @Bind(R.id.layout_two)
    LinearLayout layout_two;
    @Bind(R.id.layout_three)
    LinearLayout layout_three;
    @Bind(R.id.bind_member_next)
    Button next;
    @Bind(R.id.userType)
    TextView user_Type;
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
    private Member mMemberInfo;
    @Bind(R.id.card_value)

    TextView cardValue;
    @Bind(R.id.card_name)

    TextView cardName;

    public BindTaskContract presenter;
    private static int MAXT_FINGER = 3;//表示注册几个指静脉模版
    Handler mHandler;
    private SharedPreferences userInfo;
    public static CallBackValue callBackValue;
    private BindAcitvity acitvity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.acitvity=(BindAcitvity) activity;
        callBackValue=(CallBackValue)activity;
    }
    public static RegisterFragment_Two newInstance(Member memberInfo) {
        RegisterFragment_Two fragment = new RegisterFragment_Two();
        Bundle args = new Bundle();
        args.putSerializable(Constant.EXTRAS_MEMBER, memberInfo);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.e("RegisterFragment_Two"+"onCreate");
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onError(ApiException error) {
        Logger.e("RegisterFragment_Two"+"onError");
        super.onError(error);
    }

    @Override
    protected void onVisible() {
        Logger.e("RegisterFragment_Two"+"onVisible");
    }
    @Override
    protected void initListeners() {
        Logger.e("RegisterFragment_Two"+"initListeners");
    }

//    @Override
//    public void handleTips(String tips) {
//        Logger.e("RegisterFragment_Two"+"handleTips");
//    }

    @Override
    protected void onInvisible() {
        Logger.e("RegisterFragment_Two"+"onInvisible");
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        Logger.e("RegisterFragment_Two"+"initViews");
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMemberInfo = (Member) bundle.getSerializable(Constant.EXTRAS_MEMBER);
            userInfo = acitvity.getSharedPreferences("user_info_bind", 0);
            userInfo.edit().putString("userType",mMemberInfo.getMemberdata().getUserInfo().getUserType()).commit();
            userInfo.edit().putString("uid",mMemberInfo.getMemberdata().getUserInfo().getUid()).commit();
            userInfo.edit().putString("numberValue",mMemberInfo.getMemberdata().getUserInfo().getPhone()).commit();
            userInfo.edit().putInt("sex",mMemberInfo.getMemberdata().getUserInfo().getSex()).commit();
            userInfo.edit().putString("img",mMemberInfo.getMemberdata().getUserInfo().getImg());
            userInfo.edit().putString("name",mMemberInfo.getMemberdata().getUserInfo().getName()).commit();
            if ("1".equals(mMemberInfo.getMemberdata().getUserInfo().getUserType()))
            {
                user_Type.setText(R.string.member);
            }else if("2".equals(mMemberInfo.getMemberdata().getUserInfo().getUserType())){
                user_Type.setText(R.string.employee);
            }
//            else if(mMemberInfo.getMemberdata().getUserInfo().getUserType()==2){
//                user_Type.setText("教练");
//            }
            menber_name.setText(mMemberInfo.getMemberdata().getUserInfo().getName());
            String phoneNum =mMemberInfo.getMemberdata().getUserInfo().getPhone();
            if (mMemberInfo.getMemberdata().getUserInfo().getPhone().length() == 11) {
                phoneNum = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length());
            }
            menber_phone.setText(phoneNum);
            if (mMemberInfo.getMemberdata().getUserInfo().getSex()!=0){
                menber_sex.setText(R.string.girl);
            }else {
                menber_sex.setText(R.string.man);
            }
            try {
                cardName.setText(mMemberInfo.getMemberdata().getMemberCard()[0].getCardName());
                cardValue.setText(mMemberInfo.getMemberdata().getMemberCard()[0].getEndTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if (mMemberInfo.getMemberdata().getMemberCard()!=null) {
//                cardtype.setText(mMemberInfo.getMemberdata().getMemberCard()[0].getCardName());
//                cardnumber.setText(mMemberInfo.getMemberdata().getMemberCard()[0].getCardNumber());
//                startTime.setText(mMemberInfo.getMemberdata().getMemberCard()[0].getBeginTime()+"-");
//                endTime.setText(mMemberInfo.getMemberdata().getMemberCard()[0].getEndTime());
//                userInfo.edit().putString("cardName",mMemberInfo.getMemberdata().getMemberCard()[0].getCardName()).commit();
//                userInfo.edit().putString("cardNumber",mMemberInfo.getMemberdata().getMemberCard()[0].getCardNumber()).commit();
//                userInfo.edit().putString("binginTime",mMemberInfo.getMemberdata().getMemberCard()[0].getBeginTime()).commit();
//                userInfo.edit().putString("endTime",mMemberInfo.getMemberdata().getMemberCard()[0].getEndTime()).commit();
//            }
        }

//        //测试数据
//                cardtype.setText("会员卡");
//                cardnumber.setText("666666666");
//                startTime.setText("2017/11/01"+"-");
//                endTime.setText("2018/11/05");
//                menber_name.setText("测试员");
//                menber_phone.setText("15911111111");
//                menber_sex.setText("男");
    }
//    public static Bitmap getBitmap(String path) throws IOException {
//        URL url = new URL(path);
//        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        if(conn.getResponseCode() == 200){
//            InputStream inputStream = conn.getInputStream();
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//            return bitmap;
//        }
//        return null;
//    }

//    @Override
//    public void onSuccess(Member memberInfo) {
//        callBackValue.setActivtyChange("4");
//        layout_three.setVisibility(View.GONE);
//        layout_two.setVisibility(View.VISIBLE);
//        userInfo = acitvity.getSharedPreferences("user_info_bind", 0);
//        userInfo.edit().putInt("userType",memberInfo.getMemberdata().getUserInfo().getUserType()).commit();
//        userInfo.edit().putString("uid",memberInfo.getMemberdata().getUserInfo().getUid()).commit();
//        userInfo.edit().putString("numberValue",memberInfo.getMemberdata().getUserInfo().getPhone()).commit();
//        userInfo.edit().putInt("sex",memberInfo.getMemberdata().getUserInfo().getSex()).commit();
//        userInfo.edit().putString("img",memberInfo.getMemberdata().getUserInfo().getImg());
//        userInfo.edit().putString("name",memberInfo.getMemberdata().getUserInfo().getName()).commit();
//        menber_name.setText(mMemberInfo.getMemberdata().getUserInfo().getName());
//        String phoneNum =mMemberInfo.getMemberdata().getUserInfo().getPhone();
//        if (mMemberInfo.getMemberdata().getUserInfo().getPhone().length() == 11) {
//            phoneNum = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length());
//        }
//        menber_phone.setText(phoneNum);
//        if (mMemberInfo.getMemberdata().getUserInfo().getSex()!=0){
//            menber_sex.setText("女");
//        }else {
//            menber_sex.setText("男");
//        }
//        if (memberInfo.getMemberdata().getMemberCard()!=null) {
//            cardtype.setText(memberInfo.getMemberdata().getMemberCard().getCardName());
//            cardnumber.setText(memberInfo.getMemberdata().getMemberCard().getCardNumber());
//            startTime.setText(memberInfo.getMemberdata().getMemberCard().getBeginTime()+"-");
//            endTime.setText(memberInfo.getMemberdata().getMemberCard().getEndTime());
//            userInfo.edit().putString("cardName",memberInfo.getMemberdata().getMemberCard().getCardName()).commit();
//            userInfo.edit().putString("cardNumber",memberInfo.getMemberdata().getMemberCard().getCardNumber()).commit();
//            userInfo.edit().putString("binginTime",memberInfo.getMemberdata().getMemberCard().getBeginTime()).commit();
//            userInfo.edit().putString("endTime",memberInfo.getMemberdata().getMemberCard().getEndTime()).commit();
//        }
//    }


    @Override
    protected void initData() {
        Logger.e("RegisterFragment_Two"+"initData");
    }
    @Override
    protected int getLayoutId() {
        Logger.e("RegisterFragment_Two"+"getLayoutId");
        return R.layout.layout_bind_member;
    }
//    @Override
//    public void bindSuccess(Member returnBean) throws InterruptedException {
//        Logger.e("RegisterFragment_Two"+"bindSuccess");
//    }
    @OnClick({R.id.bind_member_next})
    public void onClick(View view){
                Logger.e("RegisterFragment_Two"+"onClick");
                callBackValue.setActivtyChange("3");
                layout_two.setVisibility(View.GONE);
                layout_three.setVisibility(View.VISIBLE);
//                RegisterFragment_Three memberInfoFragment = RegisterFragment_Three.newInstance(mMemberInfo);
//              ((BindVeinMainFragment) getParentFragment()).addFragment(memberInfoFragment, 2);
    }
    @Override
    public void onDestroy() {
//        Logger.e("RegisterFragment_Two"+"onDestroy");
        super.onDestroy();
    }
}
