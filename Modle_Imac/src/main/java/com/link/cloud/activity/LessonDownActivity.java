package com.link.cloud.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.link.cloud.bean.UserInfo;
import com.link.cloud.greendao.gen.PersonDao;
import com.link.cloud.greendaodemo.Person;
import com.orhanobut.logger.Logger;

import com.link.cloud.base.DataCleanMassage;
import com.link.cloud.bean.Member;
import com.link.cloud.core.BaseAppCompatActivity;
import com.link.cloud.fragment.DownLessonMainFragment;
import com.link.cloud.utils.CleanMessageUtil;
import com.link.cloud.view.NoScrollViewPager;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import md.com.sdk.MicroFingerVein;


/**
 * Created by Administrator on 2017/8/17.
 */

public class LessonDownActivity extends BaseAppCompatActivity implements CallBackValue {
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
    UserInfo userInfo;
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    public static final String ACTION_UPDATEUI = "action.updateTiem";
    //记录当前用户信息
    private Member memberInfo;
    private DownLessonMainFragment eliminateLessonMainFragment;

    private LessonDownActivity.MesReceiver mesReceiver;
//    private MediaPlayer mediaPlayer,mediaPlayer0,mediaPlayer1,mediaPlayer2;
    private boolean hasFinish = false;

    Runnable runnable;
    private int recLen = 40;
    byte[] feauter = null;
    byte[] feauter1 = null;
    byte[] feauter2 = null;
    int[] state = new int[1];
    byte[] img1 = null;
    byte[] img2 = null;
    byte[] img3 = null;
    boolean ret = false;
    int[] pos = new int[1];
    float[] score = new float[1];
    int run_type = 2;
    private PersonDao personDao;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
//        mediaPlayer0=MediaPlayer.create(this,R.raw.putfinger_member);
//        mediaPlayer1=MediaPlayer.create(this,R.raw.select_lesson);
//        mediaPlayer2=MediaPlayer.create(this,R.raw.sign_success);
//        mediaPlayer=MediaPlayer.create(this,R.raw.putfinger_coach);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public void setActivtyChange(String string) {
        switch (string) {
            case "1":
//                mediaPlayer.start();
//                mediaPlayer.start();
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
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }).start();
                    break;
                case 1:
                    setActivtyChange("3");
                    text_error.setText("验证成功...");
                    QueryBuilder qb = personDao.queryBuilder();
                    List<Person> users = qb.where(PersonDao.Properties.Id.eq((long)pos[0]+1)).list();
                    userInfo.setName(users.get(0).getName());
                    userInfo.setPhone(users.get(0).getNumber());
                    userInfo.setUserType(users.get(0).getUserType());
                    userInfo.setUid(users.get(0).getUid());
                    userInfo.setSex(users.get(0).getSex());
//                    LessonFragment_test fragment = LessonFragment_test.newInstance(userInfo);
                    //签到
//                    ((EliminateActivity) this.getParentFragment()).addFragment(fragment, 1);
                    break;
                case 2:
                    text_error.setText("验证失败...");
                    break;
                case 3:
                    text_error.setText("请移开手指");
                    break;
//                case 4:
//                    text_error.setText("放置手指错误，请放置同一根手指");
//                    break;
//                case 5:
//                    text_error.setText("请移开手指");
//                    break;
//                case 6:
//                    text_error.setText("");
//                    break;
            }
        }
    };
    @Override
    protected int getLayoutId() {
        return R.layout.layout_main_bind;
    }
    @Override
    protected void onStart() {
        super.onStart();
//        mediaPlayer.start();
    }
    public void run()
    {
        layout_error_text.setVisibility(View.VISIBLE);
//        ret = MicroFingerVein.fvdevOpen();
        if (ret != true) {
            Log.i("fingetopen","failed");
        } else {
            Log.i("fingetopen","success");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
//                    identify_process();
                }
            }
        }).start();

    }
//    private void identify_process()
//    {
//        try {
//            Thread.sleep(30);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        ret = MicroFingerVein.fvdevGetState(state);
//        if (ret != true) {
//            MicroFingerVein.fvdevOpen();
//            return;
//        }
//        if (state[0] != 3) {
//            return;
//        }
//        img1 = MicroFingerVein.fvdevGrabImage();
//        if (img1 == null) {
//            return;
//        }
//        ret = MicroFingerVein.fvSearchFeature(feauter1, 1, img1, pos, score);
//        if (ret == true && score[0] > 0.63) {
//            Log.e("Identify success,", "pos=" + pos[0] + ", score=" + score[0]);
////            handler.sendEmptyMessage(3);
//        } else {
//            Log.e("Identify failed,", "ret=" + ret + ",pos=" + pos[0] + ", score=" + score[0]);
//            handler.sendEmptyMessage(4);
//        }
//        while (state[0] == 3) {
//            MicroFingerVein.fvdevGetState(state);
//        }
//    }
    StringBuffer sb = new StringBuffer();
    int i=0;
    void  executeSql() {
        String sql = "select FINGERMODEL from PERSON" ;
        Cursor cursor = BaseApplication.getInstances().getDaoSession().getDatabase().rawQuery(sql,null);
        byte[][] featureS=new byte[cursor.getCount()][];
        while (cursor.moveToNext()){
            int nameColumnIndex = cursor.getColumnIndex("FINGERMODEL");
            String strValue=cursor.getString(nameColumnIndex);
//                featureS[i][]=hexStringToByte(strValue);
            i++;
        }
//         feauter=
    }
    /**
     * 把16进制字符串转换成字节数组
     * @param hex
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }
    @Override
    protected void initViews(Bundle savedInstanceState) {
        mesReceiver=new MesReceiver();
//        timeStr = (TextView) findViewById(R.id.tv_time);
        tvTitle.setText("下课");
        bind_one_tv.setText("请教练放置手指");
        bind_two_tv.setText("请会员放置手指");
        bind_three_tv.setText("确认课程信息");
        bind_four_tv.setText("签到打卡成功");
        eliminateLessonMainFragment=new DownLessonMainFragment();
        mFragmentList.add(eliminateLessonMainFragment);
        FragmentManager fm=getSupportFragmentManager();
        SectionsPagerAdapter mfpa=new SectionsPagerAdapter(fm,mFragmentList); //new myFragmentPagerAdater记得带上两个参数
        viewPager.setAdapter(mfpa);
        viewPager.setCurrentItem(0);
    }
    @Override
    public void onBackPressed() {
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
        if (runnable!=null) {
            handler.removeCallbacks(runnable);
        }
        unregisterReceiver(mesReceiver);
        finish();
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
    /**
     * 广播接收器
     *
     * @author kevin
     */
    public class MesReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            timeStr.setText(intent.getStringExtra("timeStr"));
//            Logger.e("NewMainActivity" + intent.getStringExtra("timeStr"));
            if (context == null) {
                context.unregisterReceiver(this);
            }
        }
    }
}
