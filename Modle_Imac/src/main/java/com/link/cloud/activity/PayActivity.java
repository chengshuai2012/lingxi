package com.link.cloud.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.link.cloud.R;
import com.orhanobut.logger.Logger;

import com.link.cloud.bean.Member;
import com.link.cloud.core.BaseAppCompatActivity;
import com.link.cloud.fragment.PayMainFragment;
import com.link.cloud.utils.CleanMessageUtil;
import com.link.cloud.view.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/8/17.
 */

public class PayActivity extends BaseAppCompatActivity implements CallBackValue{
    //    @Bind(R.id.bind_page)
//    ViewPager viewPager;
//    @Bind(R.id.tv_time)
//    TextView timeStr;
//    @Bind(R.id.tv_title)
//    TextView tvTitle;
//    @Bind(R.id.home_back_bt)
//    Button home_back;
//    @Bind(R.id.pay_one_Cimg)
//    ImageView pay_one_Cimg;
//    @Bind(R.id.pay_one_Pimg)
//    ImageView pay_one_Pimg;
//    @Bind(R.id.pay_one_tv)
//    TextView pay_one_tv;
//    @Bind(R.id.pay_two_Cimg)
//    ImageView pay_two_Cimg;
//    @Bind(R.id.pay_two_Pimg)
//    ImageView pay_two_Pimg;
//    @Bind(R.id.pay_two_tv)
//    TextView pay_two_tv;
//    @Bind(R.id.pay_three_Cimg)
//    ImageView pay_three_Cimg;
//    @Bind(R.id.pay_three_Pimg)
//    ImageView pay_three_Pimg;
//    @Bind(R.id.pay_three_tv)
//    TextView pay_three_tv;
//    @Bind(R.id.pay_four_Cimg)
//    ImageView pay_four_Cimg;
//    @Bind(R.id.pay_four_Pimg)
//    ImageView pay_four_Pimg;
//    @Bind(R.id.pay_four_tv)
//    TextView pay_four_tv;
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    public static final String ACTION_UPDATEUI = "action.updateTiem";
    //记录当前用户信息
    private Member memberInfo;
    private PayMainFragment payMainFragment;
//    private SignFragment_Two signFragment_two;
//    private SignFragment_Three signFragment_three;
    public NoScrollViewPager viewPager;
    private MesReceiver mesReceiver;
    private MediaPlayer mediaPlayer0,mediaPlayer,mediaPlayer1;
    private Handler handler;
    private int recLen=40;
    private Runnable back_runnable;
    private boolean hasFinish = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
//        mediaPlayer=MediaPlayer.create(this,R.raw.putfinger_right);
//        mediaPlayer1=MediaPlayer.create(this,R.raw.cost_success);
//        mediaPlayer0=MediaPlayer.create(this,R.raw.pay_message);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer0.start();
//        startAD();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN && hasFinish == false) {
//            hasFinish = true;
//            if (back_runnable != null) {
////                home_back.setText(" 返回首页 ");
//                handler.removeCallbacks(back_runnable);
//            }
//        }else {
//            hasFinish = false;
//            if (back_runnable != null) {
//                handler.removeCallbacks(back_runnable);
//                recLen = 40;
//            }
////            startAD();
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
        return super.dispatchTouchEvent(ev);
    }
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
//    public void startAD() {
//        recLen=40;
//        handler = new Handler();
//        back_runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (recLen<=30) {
////                    home_back.setText(" 返回首页 " + recLen + " s");
//                }else {
////                    home_back.setText(" 返回首页 ");
//                }
//                recLen--;
//                handler.postDelayed(this,1000);
//                if (recLen < 0) {
//                    Intent intent = new Intent();
//                    intent.setClass(PayActivity.this, NewMainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    recLen=40;
//                    finish();
//                }
//            }
//        };
//        handler.postDelayed(back_runnable, 1000);
//        Logger.e("SigeActivity=======");
//    }
    @Override
    protected void initViews(Bundle savedInstanceState) {
        mesReceiver=new MesReceiver();
//        tvTitle.setText("会员消费");
//        viewPager=(NoScrollViewPager)findViewById(R.id.pay_page);
//        payMainFragment=new PayMainFragment();
//        mFragmentList.add(payMainFragment);
//        FragmentManager fm=getSupportFragmentManager();
//        SectionsPagerAdapter mfpa=new SectionsPagerAdapter(fm,mFragmentList); //new myFragmentPagerAdater记得带上两个参数
//        viewPager.setAdapter(mfpa);
//        viewPager.setCurrentItem(0);
    }
    @Override
    public void setActivtyChange(String string) {
        switch (string) {
            case "1":

                mediaPlayer0.start();
//                pay_one_Cimg.setImageResource(R.drawable.flow_circle_pressed);
//                pay_one_Pimg.setImageResource(R.drawable.new_phone_pressed);
//                pay_one_tv.setTextColor(getResources().getColor(R.color.colorText));
//                pay_two_Cimg.setImageResource(R.drawable.flow_circle);
//                pay_two_Pimg.setImageResource(R.drawable.new_puting);
//                pay_two_tv.setTextColor(getResources().getColor(R.color.edittv));
//                pay_three_Cimg.setImageResource(R.drawable.flow_circle);
//                pay_three_Pimg.setImageResource(R.drawable.new_message);
//                pay_three_tv.setTextColor(getResources().getColor(R.color.edittv));
//                pay_four_Cimg.setImageResource(R.drawable.flow_circle);
//                pay_four_Pimg.setImageResource(R.drawable.new_finish);
//                pay_four_tv.setTextColor(getResources().getColor(R.color.edittv));
                break;
            case "2":
                mediaPlayer.start();
//                pay_one_Cimg.setImageResource(R.drawable.flow_circle);
//                pay_one_Pimg.setImageResource(R.drawable.new_phone);
//                pay_one_tv.setTextColor(getResources().getColor(R.color.edittv));
//                pay_two_Cimg.setImageResource(R.drawable.flow_circle_pressed);
//                pay_two_Pimg.setImageResource(R.drawable.new_puting_pressed);
//                pay_two_tv.setTextColor(getResources().getColor(R.color.colorText));
//                pay_three_Cimg.setImageResource(R.drawable.flow_circle);
//                pay_three_Pimg.setImageResource(R.drawable.new_message);
//                pay_three_tv.setTextColor(getResources().getColor(R.color.edittv));
//                pay_four_Cimg.setImageResource(R.drawable.flow_circle);
//                pay_four_Pimg.setImageResource(R.drawable.new_finish);
//                pay_four_tv.setTextColor(getResources().getColor(R.color.edittv));
                break;
            case "3":
//                pay_one_Cimg.setImageResource(R.drawable.flow_circle);
//                pay_one_Pimg.setImageResource(R.drawable.new_phone);
//                pay_one_tv.setTextColor(getResources().getColor(R.color.edittv));
//                pay_two_Cimg.setImageResource(R.drawable.flow_circle);
//                pay_two_Pimg.setImageResource(R.drawable.new_puting);
//                pay_two_tv.setTextColor(getResources().getColor(R.color.edittv));
//                pay_three_Cimg.setImageResource(R.drawable.flow_circle_pressed);
//                pay_three_Pimg.setImageResource(R.drawable.new_message_pressed);
//                pay_three_tv.setTextColor(getResources().getColor(R.color.colorText));
//                pay_four_Cimg.setImageResource(R.drawable.flow_circle);
//                pay_four_Pimg.setImageResource(R.drawable.new_finish);
//                pay_four_tv.setTextColor(getResources().getColor(R.color.edittv));
                break;
            case "4":
                mediaPlayer1.start();
//                pay_one_Cimg.setImageResource(R.drawable.flow_circle);
//                pay_one_Pimg.setImageResource(R.drawable.new_phone);
//                pay_one_tv.setTextColor(getResources().getColor(R.color.edittv));
//                pay_two_Cimg.setImageResource(R.drawable.flow_circle);
//                pay_two_Pimg.setImageResource(R.drawable.new_puting);
//                pay_two_tv.setTextColor(getResources().getColor(R.color.edittv));
//                pay_three_Cimg.setImageResource(R.drawable.flow_circle);
//                pay_three_Pimg.setImageResource(R.drawable.new_message);
//                pay_three_tv.setTextColor(getResources().getColor(R.color.edittv));
//                pay_four_Cimg.setImageResource(R.drawable.flow_circle_pressed);
//                pay_four_Pimg.setImageResource(R.drawable.new_finish_pressed);
//                pay_four_tv.setTextColor(getResources().getColor(R.color.colorText));
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initData() {
        mesReceiver=new MesReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UPDATEUI);
        registerReceiver(mesReceiver, intentFilter);
//        etPhoneNum.setShowSoftInputOnFocus(false);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
    }

    @Override
    protected void initListeners() {
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (back_runnable!=null){
            handler.removeCallbacks(back_runnable);
        }
//        unregisterReceiver(mesReceiver);
//        if (mediaPlayer!=null) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }else if (mediaPlayer0!=null) {
//            mediaPlayer0.stop();
//            mediaPlayer0.release();
//            mediaPlayer0 = null;
//        }else if (mediaPlayer1!=null) {
//            mediaPlayer1.stop();
//            mediaPlayer1.release();
//        }
        finish();
    }

//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//        ArrayList<Fragment> list;
//        public SectionsPagerAdapter(FragmentManager fm,ArrayList<Fragment> mFragmentList) {
//            super(fm);
//            this.list=mFragmentList;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return list.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//    }
//    @OnClick(R.id.home_back_bt)
//    public void onClick(View view){
//        switch (view.getId()){
//            case R.id.home_back_bt:
//                Intent intent=new Intent();
//                intent.setClass(this,NewMainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
//        }
//    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
    /**
     * 广播接收器
     *
     * @author kevin
     */
    public class MesReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            timeStr.setText(intent.getStringExtra("timeStr"));
            if (context == null) {
                context.unregisterReceiver(this);
            }
        }
    }
}
