package com.link.cloud.fragment;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//import com.baidu.tts.client.SpeechSynthesizer;
import com.link.cloud.R;
import com.link.cloud.ActMainActivity;

import com.link.cloud.base.ClearEditText;
import com.link.cloud.constant.Constant;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.utils.Utils;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayFragment extends BaseFragment {
    String TagP="PayFragment";
    String TAG="SoonveinCloud";
//    @Bind(R.id.commit)
//    Button commit;
//    @Bind(R.id.reset)
//    Button reset;
//@Bind(R.id.pay_keypad_0)
//Button number0;
//    @Bind(R.id.pay_keypad_1)
//    Button number1;
//    @Bind(R.id.pay_keypad_2)
//    Button number2;
//    @Bind(R.id.pay_keypad_3)
//    Button number3;
//    @Bind(R.id.pay_keypad_4)
//    Button number4;
//    @Bind(R.id.pay_keypad_5)
//    Button number5;
//    @Bind(R.id.pay_keypad_6)
//    Button number6;
//    @Bind(R.id.pay_keypad_7)
//    Button number7;
//    @Bind(R.id.pay_keypad_8)
//    Button number8;
//    @Bind(R.id.pay_keypad_9)
//    Button number9;
//    @Bind(R.id.pay_keypad_delect)
//    Button delect;
//    @Bind(R.id.pay_keypad_ok)
//    Button btOK;
//    @Bind(R.id.etCost)
//    ClearEditText etCost;
//    @Bind(R.id.etRemark)
//    ClearEditText etRemark;
//    @Bind(R.id.etPhoneNum)
//    ClearEditText etPhoneNum;
//    MediaPlayer mediaPlayer;
//    ActMainActivity mainActivity;
    Editable editable;
    int index;
    public static PayFragment newInstance() {
        PayFragment fragment = new PayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mediaPlayer=MediaPlayer.create(getActivity(),R.raw.error_phone);

    }
    @Override
    protected int getLayoutId() {
        return 0;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
//        etPhoneNum.getText().clear();

    }

    @Override
    protected void initListeners() {
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initData() {
//        etPhoneNum.setShowSoftInputOnFocus(false);
//        etCost.setShowSoftInputOnFocus(false);
    }
    @Override
    protected void onVisible() {
//        etPhoneNum.getText().clear();
    }
    @Override
    protected void onInvisible() {
    }
    @Override
    public void onStop() {
        super.onStop();
//        etPhoneNum.getText().clear();
    }

//    @OnClick({R.id.pay_keypad_ok,R.id.pay_keypad_0,R.id.pay_keypad_1,R.id.pay_keypad_2,R.id.pay_keypad_3,R.id.pay_keypad_4,
//            R.id.pay_keypad_5,R.id.pay_keypad_6,R.id.pay_keypad_7,R.id.pay_keypad_8,R.id.pay_keypad_9,R.id.pay_keypad_delect})
    public void onClick(View view) {
//        if (etCost.hasFocus()){
//            index = etCost.getSelectionStart();
//            editable = etCost.getText();
//        }else if (etRemark.hasFocus()){
//            index = etRemark.getSelectionStart();
//            editable = etRemark.getText();
//        }else {
//            index = etPhoneNum.getSelectionStart();
//            editable = etPhoneNum.getText();
//        }
//            switch (view.getId()) {
//            case R.id.pay_keypad_0:
//                   editable.insert(index, "0");
//                break;
//            case R.id.pay_keypad_1:
//                    editable.insert(index, "1");
//                break;
//            case R.id.pay_keypad_2:
//                    editable.insert(index, "2");
//                break;
//            case R.id.pay_keypad_3:
//                    editable.insert(index, "3");
//                break;
//            case R.id.pay_keypad_4:
//                    editable.insert(index, "4");
//                break;
//            case R.id.pay_keypad_5:
//                    editable.insert(index, "5");
//                break;
//            case R.id.pay_keypad_6:
//                    editable.insert(index, "6");
//                break;
//            case R.id.pay_keypad_7:
//                    editable.insert(index, "7");
//                break;
//            case R.id.pay_keypad_8:
//                    editable.insert(index, "8");
//                break;
//            case R.id.pay_keypad_9:
//                    editable.insert(index, "9");
//                break;
//            case R.id.pay_keypad_delect:
//                if (index>0) {
//                    editable.delete(index - 1, index);
//                }else {
//                }
//                break;
//            case R.id.pay_keypad_ok:
//                String phone = etPhoneNum.getText().toString().trim();
//                String cost = etCost.getText().toString().trim();
//                String remark = etRemark.getText().toString().trim();
//                etPhoneNum.setError(null);
//                etCost.setError(null);
//                etRemark.setError(null);
//                View focusView = null;
//                boolean cancel = false;
//                if (Utils.isEmpty(phone)||!Utils.isMobileNumberValid(phone)) {
//                    etPhoneNum.setError(getResources().getString(R.string.error_invalid_mobile_phone));
//                   mediaPlayer.start();
//                    focusView = etPhoneNum;
//                    cancel = true;
//                }else if (Utils.isEmpty(remark)) {
//                    etRemark.setError(getResources().getString(R.string.error_invalid_cost_empty));
//                    focusView = etRemark;
//                    cancel = true;
//                }
//                if (cancel) {
//                    focusView.requestFocus();
//                } else {
                    //这里是测试，跳过指静脉验证
//                    Voucher voucher = new Voucher();
//                    voucher.setName("测试");
//                    voucher.setPhone("13007436471");
//                    voucher.setCost("50");
//                    voucher.setBalance("100");
//                    CostInFragment fragment = CostInFragment.newInstance(voucher);
//                    //消费
//                    ((PayMainFragment) this.getParentFragment()).addFragment(fragment, 1);
//                    PayFragment_Two fragment = PayFragment_Two.newInstance(Constant.ACTION_CONSUME, phone, cost, remark);
//                    ((PayMainFragment) this.getParentFragment()).addFragment(fragment, 1);
//                }
//                break;
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
//        etPhoneNum.getText().clear();
        return rootView;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {

        super.onResume();
//        etPhoneNum.getText().clear();
//        etCost.getText().clear();
//        etRemark.getText().clear();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
