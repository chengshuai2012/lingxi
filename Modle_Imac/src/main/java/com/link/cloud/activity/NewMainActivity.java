package com.link.cloud.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.link.cloud.BaseApplication;
import com.link.cloud.R;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.bean.MdDevice;
import com.link.cloud.bean.PagesInfoBean;
import com.link.cloud.bean.SyncFeaturesPage;
import com.link.cloud.bean.SyncUserFace;
import com.link.cloud.bean.UpDateBean;
import com.link.cloud.component.MdUsbService;
import com.link.cloud.contract.DownloadFeature;
import com.link.cloud.contract.SyncUserFeature;
import com.link.cloud.greendao.gen.PersonDao;
import com.link.cloud.greendaodemo.Person;
import com.link.cloud.utils.APKVersionCodeUtils;
import com.link.cloud.utils.CleanMessageUtil;
import com.link.cloud.utils.FileUtils;
import com.link.cloud.utils.Utils;
import com.link.cloud.utils.VenueUtils;
import com.link.cloud.view.ExitAlertDialogshow;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import md.com.sdk.MicroFingerVein;


/**
 * Created by Administrator on 2017/8/18.
 */

public class NewMainActivity extends AppCompatActivity implements DownloadFeature.download,SyncUserFeature.syncUser{
    @Bind(R.id.fabExit)
    FloatingActionButton fabExit;
    @Bind(R.id.layout_title)
    TextView tvTitle;
    @Bind(R.id.layout_time)
    TextView tv_time;
    @Bind(R.id.bt_main_bind)
    TextView btn_bind;
    @Bind(R.id.textView2)
    TextView timeText;
    @Bind(R.id.data_time)
    TextView data_time;
    //    @Bind(R.id.test_push)
//    TextView textView;
    private Utils utils;
//    ImageButton btn_bind;
    private SharedPreferences userInfo;
    private static String ACTION_USB_PERMISSION = "com.android.USB_PERMISSION";
    private MediaPlayer mediaPlayer,mediaPlayer1,mediaPlayer2,mediaPlayer3;
    private MesReceiver mesReceiver;
    private String deviceID;
    public static final String ACTION_UPDATEUI = "com.link.cloud.updateTiem";
    public static final String ACTION_DATABASES = "com.link.cloud.databases";
    ObjectAnimator rotationAnimator,translateAnimatorIn, translateAnimatorOut;
    OvershootInterpolator interpolator;
    ExitAlertDialogshow exitAlertDialogshow;
    ExitAlertDialog exitAlertDialog;
    private PersonDao personDao;

    DownloadFeature downloadFeature;
    public MicroFingerVein microFingerVein;
//    WorkService.MyBinder myBinder=null;
//    NewMainActivity activity = null;
    BaseApplication baseApplication;
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        CleanMessageUtil.clearAllCache(getApplicationContext());
        setContentView(R.layout.layout_main_activity);
        ButterKnife.bind(this);
//            intent = new Intent(this, WorkService.class);
//            startService(intent);
        baseApplication=(BaseApplication)getApplication();
        exitAlertDialogshow=new ExitAlertDialogshow(this);
        exitAlertDialogshow.setCanceledOnTouchOutside(false);
        exitAlertDialogshow.setCancelable(false);
        Logger.e("NewMainActivity"+"=======================");
//        Permition.verifyStoragePermissions(this);//检验外部存储器访问权限
        inview();
        Intent intent=new Intent(NewMainActivity.this,MdUsbService.class);
        venueUtils = BaseApplication.getVenueUtils();
        bindService(intent,mdSrvConn, Service.BIND_AUTO_CREATE);
        init();
    }
    public boolean RootCmd(String cmd){
        Process process=null;
        DataOutputStream os=null;
        try{
            process=Runtime.getRuntime().exec("su");
            os=new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd+"\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        }catch (Exception e){
            return false;
        }finally {
                try{
                    if (os!=null){
                        os.close();
                    }
                    process.destroy();
                }catch (Exception e){
            }
            return true;
        }
    }
    ConnectivityManager connectivityManager;
    private void inview() {
        TextView textView=(TextView) findViewById(R.id.versionName);
        textView.setText( APKVersionCodeUtils.getVerName(this));
        downloadFeature=new DownloadFeature();
        downloadFeature.attachView(this);
        exitAlertDialog = new ExitAlertDialog(NewMainActivity.this);
        interpolator = new OvershootInterpolator();
        rotationAnimator = ObjectAnimator.ofFloat(fabExit, "rotation", 0, 360 * 2);
        rotationAnimator.setDuration(1000);
        rotationAnimator.setInterpolator(interpolator);
        tvTitle.setText(R.string.welcome_use);
        tv_time.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                connectivityManager =(ConnectivityManager)NewMainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                    exitAlertDialogshow.show();
                    SyncFeaturesPages.clear();
                    SharedPreferences sharedPreferences = getSharedPreferences("user_info", 0);
                    String deviceId = sharedPreferences.getString("deviceId", "");
                    totalPage=0;currentPage=1;downloadPage=0;
                    downloadFeature.getPagesInfo(deviceId);

                }else {
                    Toast.makeText(NewMainActivity.this, R.string.please_check_net,Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(NewMainActivity.this)
                        .setTitle(R.string.sure_to_exit)
                        .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                // 为Intent设置Action、Category属性
                                intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
                                intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
                                startActivity(intent);
                            }
                        })
                        .create().show();
                return false;
            }
        });
    }
    NewMainActivity activity;
    private void init() {
        utils=new Utils();
        mesReceiver = new MesReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UPDATEUI);
        registerReceiver(mesReceiver, intentFilter);
    }
    @OnClick({R.id.bt_main_bind,R.id.bt_main_sign,R.id.bt_main_up,R.id.bt_main_down,R.id.fabExit,R.id.bt_main_bind_face})
    public void OnClick(View view){
        connectivityManager =(ConnectivityManager)NewMainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
        NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
        switch (view.getId()){
            case R.id.bt_main_bind:
                if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                    if (Utils.isFastClick()) {
                        intent = new Intent(NewMainActivity.this, BindAcitvity.class);
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(NewMainActivity.this,R.string.please_check_net,Toast.LENGTH_LONG).show();
                }
            break;
            case R.id.bt_main_sign:
                if (info!=null) {
                    if (Utils.isFastClick()) {
                        if(Camera.getNumberOfCameras()==0){
                            intent = new Intent(NewMainActivity.this, SigeActivity.class);
                            startActivity(intent);
                        }else {
                            intent = new Intent(NewMainActivity.this, SignChooseActivity.class);
                            startActivity(intent);
                        }

                    }
                }else {
                    Toast.makeText(NewMainActivity.this,R.string.please_check_net,Toast.LENGTH_LONG).show();
                }
                break;
                case R.id.bt_main_bind_face:
                if (info!=null) {
                    if (Utils.isFastClick()) {
                        if(Camera.getNumberOfCameras()==0){
                            Toast.makeText(this, R.string.no_camera,Toast.LENGTH_SHORT).show();
                        }else {
                        intent = new Intent(NewMainActivity.this, BindFaceActivity.class);
                        startActivity(intent);

                        }
                    }
                }else {
                    Toast.makeText(NewMainActivity.this,R.string.please_check_net,Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_main_up:
                intent = new Intent(NewMainActivity.this,EliminateActivity.class);
                intent.putExtra("lessonType",1);
                startActivity(intent);

                break;
            case R.id.bt_main_down:
                intent = new Intent(NewMainActivity.this,EliminateActivity.class);
                intent.putExtra("lessonType",2);
                startActivity(intent);
                break;
            case R.id.fabExit:
                exitAlertDialog.show();
                break;
        }
    }
    private void exitButtonIn() {
        if (translateAnimatorIn == null) {
            translateAnimatorIn = ObjectAnimator.ofFloat(fabExit, "TranslationY", -200, 1);
            translateAnimatorIn.setDuration(1500);
            translateAnimatorIn.setInterpolator(interpolator);
            translateAnimatorIn.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (translateAnimatorOut != null && translateAnimatorOut.isRunning())
                        translateAnimatorOut.cancel();

                    fabExit.setVisibility(View.VISIBLE);
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
        if (fabExit.getVisibility() == View.GONE) {
            return;
        }
        if (translateAnimatorOut == null) {
            translateAnimatorOut = ObjectAnimator.ofFloat(fabExit, "TranslationY", 1, -200);
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
                    fabExit.setVisibility(View.GONE);
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

    public static MdUsbService.MyBinder mdDeviceBinder;
    public static MdUsbService.MyBinder getMdbind(){
        return mdDeviceBinder;
    }
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

    private final int MSG_REFRESH_LIST=0;

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

                    }else {

                        listManageH.sendEmptyMessageDelayed(MSG_REFRESH_LIST,1500L);

                    }

                    break;

                }

            }

            return false;

        }

    });

    public static MdDevice mdDevice;

    VenueUtils venueUtils;

    private List<MdDevice> getDevList(){

        List<MdDevice> mdDevList=new ArrayList<MdDevice>();

        if(mdDeviceBinder!=null) {

            int deviceCount=MicroFingerVein.fvdev_get_count();

            for (int i = 0; i < deviceCount; i++) {

                MdDevice mdDevice = new MdDevice();

                mdDevice.setIndex(i);

                mdDevice.setNo(mdDeviceBinder.getDeviceNo(i));

                mdDevList.add(mdDevice);

            }

        }else{

            Logger.e("microFingerVein not initialized by MdUsbService yet,wait a moment...");

        }

        return mdDevList;

    }







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
    @Override
    public void onError(ApiException e) {
    }
    @Override
    public void onPermissionError(ApiException e) {
    }
    @Override
    public void onResultError(ApiException e) {
    }

    @Override
    public void downloadNotReceiver(DownLoadData resultResponse) {
    }

    @Override
    public void downloadSuccess(DownLoadData resultResponse) {
    }
    @Override
    public void downloadApK(UpDateBean resultResponse) {
    }

    @Override
    public void syncUserFacePagesSuccess(SyncUserFace resultResponse) {

    }



    @Override
    public void syncUserSuccess(DownLoadData resultResponse) {
        personDao = BaseApplication.getInstances().getDaoSession().getPersonDao();
        Person person = new Person();
        if (resultResponse.getData().size() > 0) {
            personDao.deleteAll();
            personDao.insertInTx(resultResponse.getData());
            Toast.makeText(NewMainActivity.this, getResources().getString(R.string.syn_data), Toast.LENGTH_SHORT).show();
        }
    if(exitAlertDialogshow.isShowing()){
    exitAlertDialogshow.dismiss();
    }

    }


    ArrayList<Person> SyncFeaturesPages = new ArrayList<>();
    int totalPage=0,currentPage=1,downloadPage=0;
    @Override
    public void getPagesInfo(PagesInfoBean resultResponse) {
        if(resultResponse.getData().getPageCount()==0){
            exitAlertDialogshow.dismiss();
        }
        totalPage = resultResponse.getData().getPageCount();
        ExecutorService service = Executors.newFixedThreadPool(1);
        for(int x =0 ;x<resultResponse.getData().getPageCount();x++){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, currentPage+">>>>>>>" );
                    downloadFeature.syncUserFeaturePages(FileUtils.loadDataFromFile(NewMainActivity.this, "deviceId.text"), currentPage++);
                    //downloadFeature.syncUserFeaturePages("1000834GS7K", currentPage++);
                }
            };
            service.execute(runnable);
        }

    }
    SyncFeaturesPage [] syncUserFeatures=new SyncFeaturesPage[8];
    @Override
    public void syncUserFeaturePagesSuccess(final SyncFeaturesPage resultResponse) {
        Logger.e(resultResponse.getData().size() + getResources().getString(R.string.syn_data)+"current"+downloadPage);
        if (resultResponse.getData().size()>0) {
            downloadPage++;
            SyncFeaturesPages.addAll(resultResponse.getData());
            if (downloadPage == totalPage) {
                Logger.e(resultResponse.getData().size() + getResources().getString(R.string.syn_data)+"total");
                ((BaseApplication) getApplicationContext().getApplicationContext()).getPerson().clear();
                Logger.e( ((BaseApplication) getApplicationContext().getApplicationContext()).getPerson().size()+">>>>>>>>>>>>>>>");


                ((BaseApplication) getApplicationContext().getApplicationContext()).getPerson().addAll(SyncFeaturesPages);
                Logger.e( ((BaseApplication) getApplicationContext().getApplicationContext()).getPerson().size()+">>>>>>>>>>>>>>>");
                PersonDao personDao = BaseApplication.getInstances().getDaoSession().getPersonDao();
                personDao.deleteAll();
                personDao.insertInTx(SyncFeaturesPages);
                Logger.e(SyncFeaturesPages.size() + getResources().getString(R.string.syn_data));
                NetworkInfo info = connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                    downloadFeature.appUpdateInfo(FileUtils.loadDataFromFile(this, "deviceId.text"));
                } else {
                    Toast.makeText(this, getResources().getString(R.string.syn_data), Toast.LENGTH_LONG).show();
                }
                Log.e("syncUserFeatu: ", exitAlertDialogshow.isShowing()+"");
                if(exitAlertDialogshow.isShowing()){
                    exitAlertDialogshow.dismiss();
                }
            }

        }}
    private class ExitAlertDialog extends Dialog  {
        private Context mContext;
        TextView texttitle;
        private EditText etPwd;
        private Button btCancel;
        private Button btConfirm;
        public ExitAlertDialog(Context context, int theme) {
            super(context, theme);
            mContext = context;
            initDialog();
        }
        public ExitAlertDialog(Context context) {
            super(context, R.style.customer_dialog);
            mContext = context;
            initDialog();
        }
        private void initDialog() {
            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_exit_confirm, null);
            setContentView(view);
            texttitle=(TextView)view.findViewById(R.id.text_title);
            SharedPreferences sharedPreference=getSharedPreferences("user_info",0);
            texttitle.setText("设备ID:"+sharedPreference.getString("deviceId",""));
        }
        @Override
        public void show() {
            super.show();
        }

    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        venueUtils.StopIdenty();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CleanMessageUtil.clearAllCache(getApplicationContext());
//        Intent intent=new Intent(NewMainActivity.this,WorkService.class);
//        stopService(intent);
        unregisterReceiver(mesReceiver);//释放广播接收者
        listManageH.removeCallbacksAndMessages(null);
        unbindService(mdSrvConn);
    }
    Intent intent;
    public void todialog(String text) {
        toJson(text);
       android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(this);
        dialog.setTitle(titile);
        dialog.setIcon(R.drawable.app_icon_small);
        dialog.setMessage("请确认是否"+titile);
        dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Logger.e("NewMainActivity============type"+type);
                switch (type){
                    case 0:
                         intent=new Intent();
                        intent.setClass(NewMainActivity.this,EliminateActivity.class);
                        intent.putExtra("elminitaLesson",toJson(text));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                    case 1:
                         intent=new Intent();
                        intent.setClass(NewMainActivity.this,EliminateActivity.class);
                        intent.putExtra("downLesson",toJson(text));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                        break;

                    case 2:
                        intent=new Intent();
                        intent.setClass(NewMainActivity.this,PayActivity.class);
                        intent.putExtra("menbertopay",toJson(text));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.dismiss();

                        break;

                }
            }})
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }}
                ).create();
        dialog.show();
//        this.textView.append(text + "\n");
    }
    int type;String string,titile;
    private String toJson(String text){
        try {
            JSONObject object=new JSONObject(text);
            type=object.getInt("type");
            string=object.getString("data");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return string;
    }
    /**
     * 广播接收器
     *
     * @author kevin
     */
    public class MesReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            tv_time.setText(intent.getStringExtra("timethisStr"));
            timeText.setText(intent.getStringExtra("timeStr"));
            data_time.setText(intent.getStringExtra("timeData"));
            if (context == null) {
            }
        }
    }
}
