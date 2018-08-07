package com.link.cloud.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.link.cloud.R;
import com.orhanobut.logger.Logger;

import com.link.cloud.activity.CallBackValue;
import com.link.cloud.activity.NewMainActivity;
import com.link.cloud.activity.PayActivity;
import com.link.cloud.bean.Voucher;
import com.link.cloud.constant.Constant;
import com.link.cloud.core.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PayFragment_Three extends BaseFragment {
//    @Bind(R.id.pay_Card_Cost)
//    TextView pay_Card_Cost;
//    @Bind(R.id.pay_Card_Balance)
//    TextView pay_Card_Balance;
//    @Bind(R.id.pay_Member_Name)
//    TextView pay_Member_Name;
//    @Bind(R.id.pay_Member_Phone)
//    TextView pay_Member_Phone;

    private Voucher voucher;
    public Handler handler=new Handler();
    private Runnable runnable;
    CallBackValue callBackValue;
    private PayActivity activity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=(PayActivity)activity;
        callBackValue=(CallBackValue)getActivity();
    }
    public static PayFragment_Three newInstance(Voucher voucher) {
        PayFragment_Three fragment = new PayFragment_Three();
        Bundle args = new Bundle();
        args.putSerializable(Constant.EXTRAS_PAY_INFO, voucher);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            voucher = (Voucher) bundle.getSerializable(Constant.EXTRAS_PAY_INFO);
            Logger.e("SignFragment_Three:"+voucher.toString());
//           pay_Member_Name.setText(voucher.getName());
//            pay_Member_Phone.setText(voucher.getPhone());
//            pay_Card_Cost.setText(voucher.getCost());
//            pay_Card_Balance.setText(voucher.getBalance());
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
                ,5000);
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
