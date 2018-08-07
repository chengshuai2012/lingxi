package com.link.cloud;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anupcowkur.reservoir.Reservoir;
import com.dd.ShadowLayout;

import com.link.cloud.constant.Constant;
import com.link.cloud.core.BaseAppCompatActivity;
import com.link.cloud.fragment.BindVeinMainFragment;
import com.link.cloud.fragment.EliminateLessonMainFragment;
import com.link.cloud.fragment.LoginFragment;
import com.link.cloud.fragment.PayMainFragment;
import com.link.cloud.fragment.SignInMainFragment;
import com.link.cloud.utils.ReservoirUtils;
import com.link.cloud.utils.ToastUtils;
import com.link.cloud.utils.Utils;
import com.link.cloud.view.NoScrollViewPager;
import com.link.cloud.view.TabViewPager;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.OnClick;

public class ActMainActivity extends BaseAppCompatActivity implements Runnable {

//    @Bind(R.id.container)
//    TabViewPager container;
//    @Bind(R.id.loginFrame)
//    LinearLayout loginFrame;
//    @Bind(R.id.mainFrame)
//    ShadowLayout mainFrame;
//    @Bind(R.id.appName)
//    TextView appName;
//    @Bind(R.id.infoLayout)
//    LinearLayout infoLayout;
//    @Bind(R.id.main_content)
//    LinearLayout mainContent;
//    @Bind(R.id.fabExit)
//    FloatingActionButton fabExit;
//    @Bind(R.id.logo)
//    ImageView logo;
//    @Bind(R.id.companyName)
//    TextView companyName;

    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private LoginFragment loginFragment;
    private SignInMainFragment signInFragment;
    private PayMainFragment payFragment;
    private EliminateLessonMainFragment lessonFragment;
    private BindVeinMainFragment bindFrameng;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewTreeObserver vto;
    private boolean firstVisible = false;
    Animation anim_fade_in;
    Animation anim_fade_out;
    ObjectAnimator rotationAnimator, translateAnimatorIn, translateAnimatorOut;
    OvershootInterpolator interpolator;
    private NoScrollViewPager viewpager;
    private int[] iconRes;
    private String[] titles;
    private boolean hasMeasured = false;
    private ExitAlertDialog exitAlertDialog;
//     MediaPlayer mediaPlayer0,mediaPlayer1;
    private Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
//        mediaPlayer0=MediaPlayer.create(this,R.raw.put_password);
//        mediaPlayer1=MediaPlayer.create(this,R.raw.error_password);
    }
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        //创建桌面快捷方式
        if (!Utils.shortcut2DesktopCreated()) {
            //Logger.e("创建桌面快捷方式");
            createShortcutToDesktop();
        }
        exitAlertDialog = new ExitAlertDialog(this);
        //Wedone: 创建一个Intent用于检测USB设备
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbReceiver, filter);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        vto = container.getViewTreeObserver();
        anim_fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        anim_fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        loginFragment = LoginFragment.newInstance();
//        fts.add(R.id.loginFrame, loginFragment);
        fts.commit();
        bindFrameng = BindVeinMainFragment.newInstance();
        mFragmentList.add(bindFrameng);
        signInFragment = SignInMainFragment.newInstance();
        mFragmentList.add(signInFragment);
        payFragment = PayMainFragment.newInstance();
        mFragmentList.add(payFragment);
        lessonFragment= EliminateLessonMainFragment.newInstance();
        mFragmentList.add(lessonFragment);
        firstVisible = true;
//        logo.setImageResource(R.drawable.logo);

        if (Utils.getMetaData(Constant.CHANNEL_NAME).indexOf("Black") > -1) {
//            infoLayout.setVisibility(View.INVISIBLE);
        } else if (Utils.getMetaData(Constant.CHANNEL_NAME).indexOf("CANARY") > -1) {
//            infoLayout.setVisibility(View.VISIBLE);
//            companyName.setVisibility(View.INVISIBLE);
        }
        interpolator = new OvershootInterpolator();
//        rotationAnimator = ObjectAnimator.ofFloat(fabExit, "rotation", 0, 360 * 2);
        rotationAnimator.setDuration(1000);
        rotationAnimator.setInterpolator(interpolator);
    }
    private void ShowChoise() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Dialog);
        builder.setTitle("选择测试地址");
        //    指定下拉列表的显示数据
        final String[] cities = {"正式环境","测试环境","预发布环境"};
        //    设置一个下拉的列表选择项
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Constant.REST_API_URL = "http://biocloud.wedonetech.com/vein-api/";
                        Log.i("ip", Constant.REST_API_URL);
                        break;
                    case 1:
                        Constant.REST_API_URL = "http://120.77.84.143:8082/vein-api/";
                        Log.i("ip", Constant.REST_API_URL);
                        break;
                    case 2:
                        Constant.REST_API_URL = "http://vein.dpjcw.cn/vein-api/";
                        Log.i("ip", Constant.REST_API_URL);
                        break;
                }
            }
        });
        builder.show();
    }
    @Override
    protected void initToolbar(Bundle savedInstanceState) {
    }
    @Override
    protected void initListeners() {
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
//                if (!hasMeasured && container.getMeasuredWidth() > 0) {
//                    container.initTabs(iconRes, titles, container.getMeasuredWidth());
//                    container.setSelected(false);
//                    container.setAdapter(mSectionsPagerAdapter);
////                    container.setAdapter(mPageAdapter);
//                    hasMeasured = true;
//                }
                return true;
            }
        });
        mHandler = new Handler();
        mHandler.postDelayed(this, 500);
    }
    @Override
    protected void initData() {
//        iconRes = new int[]{R.drawable.tab_bind, R.drawable.tab_eliminaterlesson, R.drawable.tab_pay, R.drawable.tab_eliminaterlesson};
        titles = new String[]{"绑定手指", "签到", "会员消费","取消课程"};
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (firstVisible) {
            if (TextUtils.isEmpty(Utils.getCurrentDeviceID())) {
                exitButtonOut();
//                mainFrame.setVisibility(View.INVISIBLE);
                anim_fade_in.setAnimationListener(new LoginLayoutFadeInAnimationListener());
//                loginFrame.startAnimation(anim_fade_in);
            } else {
//                loginFrame.setVisibility(View.INVISIBLE);
                anim_fade_in.setAnimationListener(new MainLayoutFadeInAnimationListener());
//                mainFrame.startAnimation(anim_fade_in);
            }
            firstVisible = false;
        }
    }
    public void showLoginFrame() {
        anim_fade_in.setAnimationListener(new LoginLayoutFadeInAnimationListener());
        anim_fade_out.setAnimationListener(new MainLayoutFadeOutAnimationListener());
//        mainFrame.startAnimation(anim_fade_out);
//        loginFrame.startAnimation(anim_fade_in);
    }

    public void showMainFrame() {
        anim_fade_in.setAnimationListener(new MainLayoutFadeInAnimationListener());
        anim_fade_out.setAnimationListener(new LoginLayoutFadeOutAnimationListener());
//        mainFrame.startAnimation(anim_fade_in);
//        loginFrame.startAnimation(anim_fade_out);
    }
    private void exitButtonIn() {
        //先判断当前显示的是哪个页面
//        if (loginFrame.getVisibility() == View.VISIBLE
//                && fabExit.getVisibility() == View.VISIBLE) {
//            return;
//        }
        if (translateAnimatorIn == null) {
//            translateAnimatorIn = ObjectAnimator.ofFloat(fabExit, "TranslationY", -200, 1);
            translateAnimatorIn.setDuration(1500);
            translateAnimatorIn.setInterpolator(interpolator);
            translateAnimatorIn.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (translateAnimatorOut != null && translateAnimatorOut.isRunning())
                        translateAnimatorOut.cancel();

//                    fabExit.setVisibility(View.VISIBLE);
                    rotationAnimator.start();
                }
                @Override
                public void onAnimationEnd(Animator animation) {

                }
                @Override
                public void onAnimationCancel(Animator animation) {

                }
                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        translateAnimatorIn.start();
    }
    private void exitButtonOut() {
//        if (fabExit.getVisibility() == View.GONE) {
//            return;
//        }
        if (translateAnimatorOut == null) {
//            translateAnimatorOut = ObjectAnimator.ofFloat(fabExit, "TranslationY", 1, -200);
            translateAnimatorOut.setDuration(1200);
            translateAnimatorOut.setInterpolator(interpolator);
            translateAnimatorOut.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (translateAnimatorIn != null && translateAnimatorIn.isRunning())
                        translateAnimatorIn.cancel();
                    rotationAnimator.start();
                }
                @Override
                public void onAnimationEnd(Animator animation) {
//                    fabExit.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationCancel(Animator animation) {
                }
                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        translateAnimatorOut.start();
    }
    private float mLastMotionX;
    private float mLastMotionY;
    private int touchSlop = 0;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final float x = ev.getX();
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                final float y = ev.getY();
                final int yDiff = (int) Math.abs(y - mLastMotionY);
                boolean yMoved = yDiff > touchSlop;
                if (yMoved) {//上下滑动
                    if (yDiff > xDiff) {
                        if ((mLastMotionY - y) > 0 && yDiff > 20) {
                            //上滑隐藏退出按钮
                            exitButtonOut();
                        } else if ((y - mLastMotionY) > 0 && yDiff > 100) {
                            //下滑显示退出按钮
                            exitButtonIn();
                        }
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // Release the drag.
                break;
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = ev.getY();
                mLastMotionX = ev.getX();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @OnClick({R.id.fabExit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabExit:
            exitAlertDialog.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUsbReceiver);
        mHandler.removeCallbacksAndMessages(null);
    }

    //用户检查指静脉设备连接状态
    @Override
    public void run() {
        mHandler.postDelayed(this, 60 * 1000);//一分钟检查一次指静脉设备状态
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter implements View.OnTouchListener{

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    }

    class MainLayoutFadeInAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
//            mainFrame.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    class MainLayoutFadeOutAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
//            mainFrame.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    class LoginLayoutFadeInAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
            exitButtonOut();
//            loginFrame.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    class LoginLayoutFadeOutAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
//            loginFrame.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    private class ExitAlertDialog extends Dialog{
        private Context mContext;
        private EditText etPwd;
        private Button btCancel;
        private Button btConfirm;
        public ExitAlertDialog(Context context, int theme) {
            super(context, theme);
            mContext = context;
//            initDialog();
        }
        public ExitAlertDialog(Context context) {
            super(context, R.style.customer_dialog);
            mContext = context;
//            initDialog();
        }
//        private void initDialog() {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_exit_confirm, null);
//            setContentView(view);
//            btCancel = (Button) view.findViewById(R.id.btCancel);
//            btConfirm = (Button) view.findViewById(R.id.btConfirm);
//            etPwd = (EditText) view.findViewById(R.id.deviceCode);
//            btCancel.setOnClickListener(this);
//            btConfirm.setOnClickListener(this);
//            this.setOnDismissListener(dialog -> {
//                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                mInputMethodManager.hideSoftInputFromWindow(etPwd.getWindowToken(), 0);
//                exitButtonOut();
//            });
//        }
        @Override
        public void show() {
            etPwd.setText("");
            super.show();
        }
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
////                case R.id.btCancel:
//                    this.dismiss();
//                    break;
////                case R.id.btConfirm:
//                    String pwd = etPwd.getText().toString().trim();
//                    if (Utils.isEmpty(pwd)) {
//                        ToastUtils.show(mContext, "请输入密码", ToastUtils.LENGTH_SHORT);
////                        mediaPlayer0.start();
//                        return;
//                    }
//                    String repwd;
//                    try {
//                        repwd = Reservoir.get(Constant.KEY_PASSWORD, String.class);
//                    } catch (Exception e) {
//                        repwd = "888888";
//                    }
//                    if (!pwd.equals(repwd)) {
//                        ToastUtils.show(mContext, "密码不正确", ToastUtils.LENGTH_SHORT);
////                        mediaPlayer1.start();
//                        return;
//                    }
//                    try {
//                        Reservoir.delete(Constant.KEY_DEVICE_ID);
//                    } catch (Exception e) {
//                    }
//                    showLoginFrame();
//                    this.dismiss();
//                    break;
//            }
//        }
    }

    private void createShortcutToDesktop() {
        Intent intent = new Intent();
        intent.setClass(this, this.getClass());
        /*以下两句是为了在卸载应用的时候同时删除桌面快捷方式*/
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");

        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重建
        shortcut.putExtra("duplicate", false);
        // 设置名字
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, this.getString(R.string.app_name));
        // 设置图标
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));
        // 设置意图和快捷方式关联程序
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

        sendBroadcast(shortcut);

        ReservoirUtils.getInstance().put(Constant.EXTRAS_SHORTCUT, true);
    }

    public static final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (device != null && 0x0481 == device.getVendorId() && 0x5641 == device.getProductId()) {
                switch (action) {

                }
            }
        }
    };
}
