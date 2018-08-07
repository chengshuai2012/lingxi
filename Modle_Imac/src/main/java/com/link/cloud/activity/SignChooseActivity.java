package com.link.cloud.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.core.BaseAppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 49488 on 2018/7/31.
 */

public class SignChooseActivity extends BaseAppCompatActivity {
    @Bind(R.id.layout_page_title)
    TextView layoutPageTitle;
    @Bind(R.id.layout_page_time)
    TextView layoutPageTime;
    @Bind(R.id.sing_choose_veune)
    LinearLayout singChooseVeune;
    @Bind(R.id.sign_choose_face)
    LinearLayout signChooseFace;
    public class MesReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            layoutPageTime.setText(intent.getStringExtra("timethisStr"));
//            Logger.e("NewMainActivity" + intent.getStringExtra("timeStr"));
            if (context == null) {

            }
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.layout_choose;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        layoutPageTitle.setText("WELCOME");
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }
    private MesReceiver mesReceiver;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mesReceiver);
    }
    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.sign_choose_face,R.id.sing_choose_veune})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.sing_choose_veune:
                Intent intent = new Intent(SignChooseActivity.this,SigeActivity.class);
                startActivity(intent);
                finish();
                break;
                case R.id.sign_choose_face:
                    Intent intent2 = new Intent(SignChooseActivity.this,FaceSign.class);
                    startActivity(intent2);
                    finish();
                break;
        }
    }
    @Override
    protected void initData() {
        mesReceiver=new MesReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NewMainActivity.ACTION_UPDATEUI);
        registerReceiver(mesReceiver, intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
