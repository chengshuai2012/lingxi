package com.link.cloud.activity;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.BaseApplication;
import com.link.cloud.R;
import com.link.cloud.bean.MdDevice;
import com.link.cloud.component.MdUsbService;
import com.link.cloud.core.BaseAppCompatActivity;
import com.link.cloud.fragment.DownLessonMainFragment;
import com.link.cloud.utils.CleanMessageUtil;
import com.link.cloud.utils.ModelImgMng;
import com.link.cloud.utils.VenueUtils;
import com.link.cloud.view.NoScrollViewPager;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import md.com.sdk.MicroFingerVein;


/**
 * Created by Administrator on 2017/8/17.
 */

public class LessonDownActivity extends BaseAppCompatActivity implements CallBackValue ,VenueUtils.VenueCallBack{
    @Bind(R.id.bing_main_page)
    NoScrollViewPager viewPager;
    @Bind(R.id.layout_page_time)
    TextView timeStr;
    @Bind(R.id.layout_page_title)
    TextView tvTitle;
    @Bind(R.id.home_back_bt)
    LinearLayout home_back;
    @Bind(R.id.bind_one_Cimg)
    ImageView bind_one_Cimg;
    @Bind(R.id.bind_one_line)
    View bind_one_line;
    @Bind(R.id.layout_main_error)
    LinearLayout layout_error_text;
    @Bind(R.id.bind_two_Cimg)
    ImageView bind_two_Cimg;
    @Bind(R.id.bind_two_line)
    View bind_two_line;
    @Bind(R.id.bind_three_Cimg)
    ImageView bind_three_Cimg;
    @Bind(R.id.bind_three_line)
    View bind_three_line;
    @Bind(R.id.bind_four_Cimg)
    ImageView bind_four_Cimg;
    @Bind(R.id.bind_one_tv)
    TextView bind_one_tv;
    @Bind(R.id.bind_two_tv)
    TextView bind_two_tv;
    @Bind(R.id.bind_three_tv)
    TextView bind_three_tv;
    @Bind(R.id.bind_four_tv)
    TextView bind_four_tv;
    @Bind(R.id.mian_text_error)
    TextView text_error;
    @Bind(R.id.text_tile)
    TextView text_tile;
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    public static final String ACTION_UPDATEUI = "com.link.cloud.updateTiem";
    private DownLessonMainFragment eliminateLessonMainFragment;
    private MesReceiver mesReceiver;
    VenueUtils venueUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        venueUtils= BaseApplication.getVenueUtils();
        Intent intent=new Intent(this,MdUsbService.class);
        bindService(intent,mdSrvConn, Service.BIND_AUTO_CREATE);
        super.onCreate(savedInstanceState);
    }
    public static MdDevice mdDevice;

    private List<MdDevice> mdDevicesList=new ArrayList<MdDevice>();

    private Handler listManageH=new Handler(new Handler.Callback() {

        @Override

        public boolean handleMessage(Message msg) {

            switch (msg.what){

                case MSG_REFRESH_LIST:{

                    mdDevicesList.clear();

                    mdDevicesList=getDevList();

                    if(mdDevicesList.size()>0){

                        mdDevice=mdDevicesList.get(0);


                        venueUtils.initVenue(mdDeviceBinder,LessonDownActivity.this,LessonDownActivity.this,true,false);

                    }else {

                        listManageH.sendEmptyMessageDelayed(MSG_REFRESH_LIST,1500L);

                    }

                    break;

                }

            }

            return false;

        }

    });

    private List<MdDevice> getDevList(){

        List<MdDevice> mdDevList=new ArrayList<MdDevice>();

        if(mdDeviceBinder!=null) {

            int deviceCount= MicroFingerVein.fvdev_get_count();

            for (int i = 0; i < deviceCount; i++) {

                MdDevice mdDevice = new MdDevice();

                mdDevice.setNo(i);

                mdDevice.setIndex(mdDeviceBinder.getDeviceNo(i));

                mdDevList.add(mdDevice);

            }

        }else{

            Logger.e("microFingerVein not initialized by MdUsbService yet,wait a moment...");

        }

        return mdDevList;

    }



    public MdUsbService.MyBinder mdDeviceBinder;

    private String TAG="BindActivity";

    private ServiceConnection mdSrvConn=new ServiceConnection() {

        @Override

        public void onServiceConnected(ComponentName name, IBinder service) {

            mdDeviceBinder=(MdUsbService.MyBinder)service;



            if(mdDeviceBinder!=null){

                mdDeviceBinder.setOnUsbMsgCallback(mdUsbMsgCallback);

                listManageH.sendEmptyMessage(MSG_REFRESH_LIST);

                Log.e(TAG,"bind MdUsbService success.");

            }else{

                Log.e(TAG,"bind MdUsbService failed.");

                finish();

            }

        }

        @Override

        public void onServiceDisconnected(ComponentName name) {

            Log.e(TAG,"disconnect MdUsbService.");

        }



    };

    private final int MSG_REFRESH_LIST=0;

    private MdUsbService.UsbMsgCallback mdUsbMsgCallback=new MdUsbService.UsbMsgCallback(){

        @Override

        public void onUsbConnSuccess(String usbManufacturerName, String usbDeviceName) {

            String newUsbInfo="USB厂商："+usbManufacturerName+"  \nUSB节点："+usbDeviceName;

            Log.e(TAG,newUsbInfo);

        }

        @Override

        public void onUsbDisconnect() {

            Log.e(TAG,"USB连接已断开");

            venueUtils.StopIdenty();

        }

    };




    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public void setActivtyChange(String string) {
        switch (string) {
            case "1":
                layout_error_text.setVisibility(View.VISIBLE);
                bind_one_Cimg.setImageResource(R.drawable.flow_circle_pressed);
                bind_one_line.setBackgroundResource(R.color.colorText);
                bind_one_tv.setTextColor(getResources().getColor(R.color.colorText));
                bind_two_Cimg.setImageResource(R.drawable.flow_circle);
                bind_two_line.setBackgroundResource(R.color.edittv);
                bind_two_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_three_Cimg.setImageResource(R.drawable.flow_circle);
                bind_three_line.setBackgroundResource(R.color.edittv);
                bind_three_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_four_Cimg.setImageResource(R.drawable.flow_circle);
                bind_four_tv.setTextColor(getResources().getColor(R.color.edittv));
                break;
            case "2":
//                mediaPlayer0.start();
                bind_one_Cimg.setImageResource(R.drawable.flow_circle);
                bind_one_line.setBackgroundResource(R.color.edittv);
                bind_one_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_two_Cimg.setImageResource(R.drawable.flow_circle_pressed);
                bind_two_line.setBackgroundResource(R.color.colorText);
                bind_two_tv.setTextColor(getResources().getColor(R.color.colorText));
                bind_three_Cimg.setImageResource(R.drawable.flow_circle);
                bind_three_line.setBackgroundResource(R.color.edittv);
                bind_three_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_four_Cimg.setImageResource(R.drawable.flow_circle);
                bind_four_tv.setTextColor(getResources().getColor(R.color.edittv));
                break;
            case "3":
//                mediaPlayer1.start();
                bind_one_Cimg.setImageResource(R.drawable.flow_circle);
                bind_one_line.setBackgroundResource(R.color.edittv);
                bind_one_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_two_Cimg.setImageResource(R.drawable.flow_circle);
                bind_two_line.setBackgroundResource(R.color.edittv);
                bind_two_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_three_Cimg.setImageResource(R.drawable.flow_circle_pressed);
                bind_three_line.setBackgroundResource(R.color.colorText);
                bind_three_tv.setTextColor(getResources().getColor(R.color.colorText));
                bind_four_Cimg.setImageResource(R.drawable.flow_circle);
                bind_four_tv.setTextColor(getResources().getColor(R.color.edittv));
                break;
            case "4":
//                mediaPlayer2.start();
                bind_one_Cimg.setImageResource(R.drawable.flow_circle);
                bind_one_line.setBackgroundResource(R.color.edittv);
                bind_one_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_two_Cimg.setImageResource(R.drawable.flow_circle);
                bind_two_line.setBackgroundResource(R.color.edittv);
                bind_two_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_three_Cimg.setImageResource(R.drawable.flow_circle);
                bind_three_line.setBackgroundResource(R.color.edittv);
                bind_three_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_four_Cimg.setImageResource(R.drawable.flow_circle_pressed);
                bind_four_tv.setTextColor(getResources().getColor(R.color.colorText));
                break;
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.layout_main_bind;
    }
    @Override
    protected void onStart() {
        super.onStart();
        text_tile.setText("上课");
    }
    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvTitle.setText("上课");
        bind_one_tv.setText("放置手指");
        bind_two_tv.setText("选择课程");
        bind_three_tv.setText("选择卡号");
        bind_four_tv.setText("上课成功");
//        eliminateLessonMainFragment=new DownLessonMainFragment();
//        mFragmentList.add(eliminateLessonMainFragment);
//        FragmentManager fm=getSupportFragmentManager();
//        SectionsPagerAdapter mfpa=new SectionsPagerAdapter(fm,mFragmentList); //new myFragmentPagerAdater记得带上两个参数
//        viewPager.setAdapter(mfpa);
//        viewPager.setCurrentItem(0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initData() {
        mesReceiver=new MesReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UPDATEUI);
        registerReceiver(mesReceiver, intentFilter);

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

        listManageH.removeCallbacksAndMessages(null);

        unbindService(mdSrvConn);

        venueUtils.StopIdenty();

        CleanMessageUtil.clearAllCache(getApplicationContext());

        unregisterReceiver(mesReceiver);
        finish();

    }


    @Override
    public void VeuenMsg(int state, String data, String uids, String feature, String score, String userTpye) {

    }

    @Override
    public void ModelMsg(int state, ModelImgMng modelImgMng, String feature) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;
        public SectionsPagerAdapter(FragmentManager fm,ArrayList<Fragment> mFragmentList) {
            super(fm);
            this.list=mFragmentList;
        }
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }
        @Override
        public int getCount() {
            return list.size();
        }
    }
    @OnClick(R.id.home_back_bt)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.home_back_bt:
                Intent intent=new Intent();
                intent.setClass(this,NewMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
        }
    }

    @Override
    public void onBackPressed() {
    }
    /**
     * 广播接收器
     *
     * @author kevin
     */
    public class MesReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            timeStr.setText(intent.getStringExtra("timethisStr"));
            if (context == null) {
                context.unregisterReceiver(this);
            }
        }
    }
}
