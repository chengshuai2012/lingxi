package com.link.cloud.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.hotelmanager.xzy.util.OpenDoorUtil;
import com.link.cloud.BaseApplication;
import com.link.cloud.R;
import com.link.cloud.activity.LockActivity;
import com.link.cloud.activity.WorkService;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.CabinetNumber;
import com.link.cloud.bean.CabinetNumberData;
import com.link.cloud.bean.CabinetRecord;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.bean.PagesInfoBean;
import com.link.cloud.bean.Person;
import com.link.cloud.bean.ResultResponse;
import com.link.cloud.bean.SignUser;
import com.link.cloud.bean.Sign_data;
import com.link.cloud.bean.SyncFeaturesPage;
import com.link.cloud.bean.UpDateBean;
import com.link.cloud.constant.Constant;
import com.link.cloud.contract.AdminopenCabinet;
import com.link.cloud.contract.CabinetNumberContract;
import com.link.cloud.contract.ClearCabinetContract;
import com.link.cloud.contract.DownloadFeature;
import com.link.cloud.contract.SyncUserFeature;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.utils.APKVersionCodeUtils;
import com.link.cloud.utils.FileUtils;
import com.link.cloud.utils.ToastUtils;
import com.link.cloud.utils.Utils;
import com.link.cloud.view.CheckUsedRecored;
import com.link.cloud.view.LockMessage;
import com.orhanobut.logger.Logger;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android_serialport_api.SerialPort;
import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;


/**
 * Created by 30541 on 2018/3/28.
 */
public class MainFragment extends BaseFragment implements AdminopenCabinet.adminopen,DownloadFeature.download,ClearCabinetContract.clearCabinet,SyncUserFeature.syncUser,CabinetNumberContract.cabinetNumber{
    private EditText etPwd;
    private Button btCancel;
    private Button btConfirm;
    @Bind(R.id.head_text_01)
    TextView head_text_01;
    @Bind(R.id.head_text_03_main)
    TextView head_text_03;
    @Bind(R.id.text_num1)
    TextView text_num1;
    @Bind(R.id.text_num2)
    TextView text_num2;
    @Bind(R.id.text_num3)
    TextView text_num3;
    @Bind(R.id.main_bt_01)
    LinearLayout main_bt_01;
    @Bind(R.id.main_bt_02)
    LinearLayout main_bt_02;
    @Bind(R.id.main_bt_03)
    LinearLayout main_bt_03;
    @Bind(R.id.layout_one)
    LinearLayout layout_one;
    @Bind(R.id.adminmessage)
    LinearLayout adminmessage;
    @Bind(R.id.edit_01)
    EditText clearlock;
    @Bind(R.id.edit_02)
    EditText openlockplate;
    @Bind(R.id.edit_03)
    EditText openlock;
    @Bind(R.id.textView2)
     TextView textView2;
    @Bind(R.id.cabinet_used)
    EditText cabinet_used;
    @Bind(R.id.openlock_one)
    EditText opelock_one;
    @Bind(R.id.head_text_02)
    TextView head_text_02;
    AdminopenCabinet adminopenCabinet;
    ClearCabinetContract clearCabinetContract;
    SyncUserFeature syncUserFeature;
    OpenDoorUtil openDoorUtil;
    MesReceiver mesReceiver;
    LockActivity activity;
    ExitAlertDialog1 exitAlertDialog1;
    public static boolean isStart=false;
    CabinetNumberContract cabinetNumberContract;
    ConnectivityManager connectivityManager;//用于判断是否有网络
    WorkService workService;
    String opentime=null;
    String pwdmodel="0";
    SerialPort serialPort;
    DownloadFeature downloadFeature;
    boolean cleanOther=false,cleanAll=false;//清柜开关
    boolean openOther=false,openAll=false;
    private InputMethodManager imm;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=(LockActivity) activity;
    }
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminopenCabinet=new AdminopenCabinet();
        clearCabinetContract=new ClearCabinetContract();
        adminopenCabinet.attachView(this);
        clearCabinetContract.attachView(this);
        syncUserFeature=new SyncUserFeature();
        syncUserFeature.attachView(this);
        cabinetNumberContract=new CabinetNumberContract();
        cabinetNumberContract.attachView(this);
        downloadFeature=new DownloadFeature();
        downloadFeature.attachView(this);
        openDoorUtil=new OpenDoorUtil();
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user_info",0);
       String pwd= sharedPreferences.getString("devicepwd",null);
        if (pwd==null){
            sharedPreferences.edit().putString("devicepwd","888888").commit();
            Logger.e("MainFragment========="+"devicepwd");
        }
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }
    @Override
    protected void initListeners() {
    }
    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        TextView textView=findView(R.id.versionName);
        textView.setText( APKVersionCodeUtils.getVerName(activity));
        long count = Realm.getDefaultInstance().where(CabinetNumber.class).count();
        if(isAdded()){
            long isUser = Realm.getDefaultInstance().where(CabinetNumber.class).equalTo("isUser", getResources().getString(R.string.isuser)).count();
            text_num1.setText(getResources().getString(R.string.cabinet_all)+count);
            text_num2.setText(getResources().getString(R.string.cabinet_isuser)+isUser);
            text_num3.setText(getResources().getString(R.string.cabinet_leave)+(count-isUser));
        }
//        time_out();
//        timer.start();
        activity.bRun=true;
//        Logger.e("MainFragment===="+"count=="+listcount.size()+"used=="+listuser.size()+"surplus=="+(listcount.size()-listuser.size()));
    }
    @Override
    protected int getLayoutId() {
        return R.layout.first_fragment;
    }
    @Override
    protected void initData() {
        mesReceiver = new MesReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LockActivity.ACTION_UPDATEUI);
        intentFilter.addAction(LockActivity.ACTION_UPDATE);
        activity.registerReceiver(mesReceiver, intentFilter);
//        timer.cancel();
        head_text_02.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                pwdmodel="1";
                exitAlertDialog1=new ExitAlertDialog1(getContext());
                exitAlertDialog1.setCancelable(false);
                exitAlertDialog1.show();
                return false;
            }
        });
    }
//    CountDownTimer timer;
//    private void time_out() {
//        /**
//         * 倒计时60秒，一次1秒
//         */
//        timer = new CountDownTimer(40 * 1000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Logger.e("ThirdFragment"+millisUntilFinished / 1000);
//            }
//            @Override
//            public void onFinish() {
//            }
//        };
//    }
    @Override
    protected void onVisible() {
    }
    @Override
    protected void onInvisible() {
    }
    @OnClick({R.id.main_bt_01,R.id.main_bt_02,R.id.chang_pdw,R.id.main_bt_03,R.id.head_text_02,R.id.clean_other,R.id.clean_all,R.id.openlock_all,
            R.id.openlock_other,R.id.openlock_button,R.id.back_home,R.id.back,R.id.head_text_03_main,R.id.button4,R.id.record_button,R.id.lock_message})
    public void Onclick(View view){
        String device=null;
        device=FileUtils.loadDataFromFile(getContext(),"deviceId.text");
        CheckUsedRecored checkUsedRecored;
        LockMessage lockMessage;
        SharedPreferences user=activity.getSharedPreferences("user_info",0);
        connectivityManager =(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
        NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
        switch (view.getId()){
            case R.id.main_bt_01:
                if (Utils.isFastClick()) {
                    if (info!=null) {
                        ((BindVeinMainFragment) getParentFragment()).setFragment(1);
                    }else {
                        if(isAdded()){
                            activity.mTts.startSpeaking(getResources().getString(R.string.network_error),activity.mTtsListener);
                            Toast.makeText(getContext(),getResources().getString(R.string.network_error),Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            case R.id.main_bt_02:
                if (Utils.isFastClick()) {
                    if (info != null) {
                        ((BindVeinMainFragment) getParentFragment()).setFragment(2);
                    } else {
                        if(isAdded()){
                            activity.mTts.startSpeaking(getResources().getString(R.string.network_error),activity.mTtsListener);
                            Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            case R.id.main_bt_03:
                if (Utils.isFastClick()) {
                    if (info != null) {
                        ((BindVeinMainFragment) getParentFragment()).setFragment(3);
                    } else {
                        if(isAdded()){
                            activity.mTts.startSpeaking(getResources().getString(R.string.network_error),activity.mTtsListener);
                            Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            case R.id.clean_other:
                if (info!=null) {
                    cleanOther = true;
                    if (!"".equals(clearlock.getText().toString().trim()) && !"0".equals(clearlock.getText().toString().trim())) {
                        RealmResults<CabinetNumber> all = Realm.getDefaultInstance().where(CabinetNumber.class).equalTo("cabinetNumber", clearlock.getText().toString().trim()).equalTo("isUser", "占用").findAll();
                        if (all.size() > 0) {
                            if(isAdded()){
                                Toast.makeText(activity, getResources().getString(R.string.cabinet_clear) + clearlock.getText().toString() + getResources().getString(R.string.successful), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if(isAdded()){
                                Toast.makeText(activity, getResources().getString(R.string.cabinet_clear) + clearlock.getText().toString() + getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                        clearCabinetContract.clearCabinet(device, "" + clearlock.getText().toString().trim());

                            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                    @Override
                                public void execute(Realm realm) {
                                        //先查找后得到User对象

                                 RealmResults<CabinetNumber> list = realm.where(CabinetNumber.class).equalTo("cabinetNumber",clearlock.getText().toString().trim() ).findAll();
                                        if (list.size() != 0) {
                                            CabinetNumber cabinetRecord = list.get(0);
                                            if(isAdded()){
                                                cabinetRecord.setIsUser(getResources().getString(R.string.isfree));
                                            }
                                        }
                                    }

                            });
                    } else {
                        if(isAdded()){
                            Toast.makeText(activity, getResources().getString(R.string.clear_failed)+ clearlock.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    if(isAdded()){
                        activity.mTts.startSpeaking(getResources().getString(R.string.network_error),activity.mTtsListener);
                        Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.clean_all:
                if (info!=null) {
                    cleanAll = true;
                    RealmResults<CabinetNumber> users = Realm.getDefaultInstance().where(CabinetNumber.class).equalTo("isUser", "占用").findAll();
                    int y = users.size();
                      Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                         @Override
                                public void execute(Realm realm) {

                                    if (users.size() > 0) {
                                        for (int x = 0; x < y; x++) {
                                            Logger.e(x+">>>>>>>>>>");
                                            Logger.e(users.size()+">>>>>>>>>>");
                                            //先查找后得到User对象
                                            CabinetNumber cabinetRecord = users.get(0);
                                            if(isAdded()){
                                                cabinetRecord.setIsUser(getResources().getString(R.string.isfree));
                                            }
                                        }
                                    }
                                }
                            });
                    clearCabinetContract.clearCabinet(FileUtils.loadDataFromFile(getContext(),"deviceId.text"), "");
                }else {
                    if(isAdded()){
                        Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.openlock_button:
                String number;
                number=opelock_one.getText().toString();
                RealmResults<CabinetNumber> list = Realm.getDefaultInstance().where(CabinetNumber.class).equalTo("cabinetNumber", number).findAll();
                if (list.size()>0){
                   int cabinetLockPlate=Integer.parseInt(list.get(0).getCabinetLockPlate());
                    int circuitNumber=Integer.parseInt(list.get(0).getCircuitNumber());
                   int cabinetNumber= Integer.parseInt(list.get(0).getCabinetNumber());
                    CabinetRecord cabinetRecord1=new CabinetRecord();
                    if(isAdded()){
                        cabinetRecord1.setMemberName(getResources().getString(R.string.manager));
                        cabinetRecord1.setPhoneNum("***********");
                        cabinetRecord1.setCabinetNumber(cabinetNumber+"");
                        cabinetRecord1.setCabinetStating(getResources().getString(R.string.manager_open));
                    }
                    cabinetRecord1.setOpentime(opentime);
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealm(cabinetRecord1);
                        }
                    });
                    Logger.e("MainFragment"+"======cabinetLockPlate"+cabinetLockPlate+"circuitNumber"+circuitNumber);
                    if (cabinetLockPlate<11) {
                        try {
                            circuitNumber=circuitNumber%10;
                            if (circuitNumber==0){
                                circuitNumber=10;
                            }
                            activity.serialpprt_wk1.getOutputStream().write(openDoorUtil.openOneDoor(cabinetLockPlate, circuitNumber));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Logger.e("openlockplate:"+cabinetLockPlate+"openlock:"+circuitNumber);
                    }else if (cabinetLockPlate<21&&cabinetLockPlate>10){
                        try {
                            circuitNumber=circuitNumber%10;
                            if (circuitNumber==0){
                                circuitNumber=10;
                            }
                            int openlocknum=0;
                            if (cabinetLockPlate!=20){
                                openlocknum=cabinetLockPlate%10;
                            }
                            else {
                                openlocknum=10;
                            }
                            activity.serialpprt_wk2.getOutputStream().write(openDoorUtil.openOneDoor(openlocknum, circuitNumber));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Logger.e("openlockplate:"+cabinetLockPlate+"openlock:"+circuitNumber);
                    }else if (cabinetLockPlate<31&&cabinetLockPlate>20){
                        try {
                            circuitNumber=circuitNumber%10;
                            if (circuitNumber==0){
                                circuitNumber=10;
                            }
                            int openlocknum=0;
                            if (cabinetLockPlate!=30){
                                openlocknum=cabinetLockPlate%10;
                            }
                            else {
                                openlocknum=10;
                            }
                            activity.serialpprt_wk3.getOutputStream().write(openDoorUtil.openOneDoor(openlocknum, circuitNumber));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Logger.e("openlockplate:"+cabinetLockPlate+"openlock:"+circuitNumber);
                    }
                }
                break;
            case R.id.openlock_all:
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        List<CabinetNumber> all = realm.getDefaultInstance().where(CabinetNumber.class).findAll();
                        for(int i=0;i<all.size();i++) {
                            CabinetRecord cabinetRecord1=new CabinetRecord();
                            if(isAdded()){
                                cabinetRecord1.setMemberName(activity.getResources().getString(R.string.manager));
                                cabinetRecord1.setPhoneNum("***********");
                                cabinetRecord1.setCabinetNumber(all.get(i).getCabinetNumber());
                                cabinetRecord1.setCabinetStating(activity.getResources().getString(R.string.open_all));
                            }
                            cabinetRecord1.setOpentime(opentime);
                            realm.copyToRealm(cabinetRecord1);
                        }
                    }
                });

                try {
                    serialPort =new SerialPort(new File("/dev/ttysWK1"),9600,0);
                    serialPort.getOutputStream().write(openDoorUtil.openAllDoor());
                }catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    serialPort =new SerialPort(new File("/dev/ttysWK2"),9600,0);
                    serialPort.getOutputStream().write(openDoorUtil.openAllDoor());
                }catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    serialPort =new SerialPort(new File("/dev/ttysWK3"),9600,0);
                    serialPort.getOutputStream().write(openDoorUtil.openAllDoor());
                }catch (IOException e){
                    e.printStackTrace();
                }
                if (info!=null) {
                    adminopenCabinet.adminopen(device, "");
                }else {
                    if(isAdded()){
                        Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.openlock_other:
                if ( !TextUtils.isEmpty(openlockplate.getText().toString())&&!TextUtils.isEmpty(openlock.getText().toString())) {
                    if (Integer.parseInt(openlockplate.getText().toString().trim())<11) {
                        try {
                            activity.serialpprt_wk1.getOutputStream().write(openDoorUtil.openOneDoor(Integer.parseInt(openlockplate.getText().toString().trim()), Integer.parseInt(openlock.getText().toString().trim())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Logger.e("openlockplate:"+openlockplate.getText().toString()+"    openlock:"+openlock.getText().toString().trim());
                    }else if (Integer.parseInt(openlockplate.getText().toString().trim())<21&Integer.parseInt(openlockplate.getText().toString().trim())>10){
                        try {
                            int openlocknum=0;
                            if (Integer.parseInt(openlockplate.getText().toString().trim())!=20){
                                openlocknum=Integer.parseInt(openlockplate.getText().toString().trim())%10;
                            }
                            else {
                                openlocknum=10;
                            }
                            activity.serialpprt_wk2.getOutputStream().write(openDoorUtil.openOneDoor(openlocknum, Integer.parseInt(openlock.getText().toString().trim())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Logger.e("openlockplate:"+openlockplate.getText().toString()+"openlock:"+openlock.getText().toString().trim());
                    }else if (Integer.parseInt(openlockplate.getText().toString().trim())<31&Integer.parseInt(openlockplate.getText().toString().trim())>20){
                        try {
                            int openlocknum=0;
                            if (Integer.parseInt(openlockplate.getText().toString().trim())!=30){
                                openlocknum=Integer.parseInt(openlockplate.getText().toString().trim())%10;
                            }
                            else {
                                openlocknum=10;
                            }
                            activity.serialpprt_wk3.getOutputStream().write(openDoorUtil.openOneDoor(openlocknum, Integer.parseInt(openlock.getText().toString().trim())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Logger.e("openlockplate:"+openlockplate.getText().toString()+"openlock:"+openlock.getText().toString().trim());
                    }
                }
                else if (TextUtils.isEmpty(openlock.getText().toString())&&!TextUtils.isEmpty(openlockplate.getText().toString())){
                    if (Integer.parseInt(openlockplate.getText().toString().trim())<11) {
                        for (int i = 0; i <= 10; i++) {
                            try {
                                Thread.sleep(500);
                                activity.serialpprt_wk1.getOutputStream().write(openDoorUtil.openOneDoor(Integer.parseInt(openlockplate.getText().toString().trim()), i));
                            } catch (Exception e) {
                            }
                        }
                    }else if (Integer.parseInt(openlockplate.getText().toString().trim())<21&Integer.parseInt(openlockplate.getText().toString().trim())>10){
                        for (int i = 0; i <= 10; i++) {
                            try {
                                Thread.sleep(500);
                                activity.serialpprt_wk2.getOutputStream().write(openDoorUtil.openOneDoor(Integer.parseInt(openlockplate.getText().toString().trim())%10, i));
                            } catch (Exception e) {
                            }
                        }
                    }else {
                        for (int i = 0; i <= 10; i++) {
                            try {
                                Thread.sleep(500);
                                activity.serialpprt_wk3.getOutputStream().write(openDoorUtil.openOneDoor(Integer.parseInt(openlockplate.getText().toString().trim())%10, i));
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                if (info!=null) {
                    adminopenCabinet.adminopen(device, "" + openlock.getText().toString().trim());
                }else {
                    if(isAdded()){
                        Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                    }
         }
                break;
            case R.id.back:
                if(clearlock.hasFocus()){
                    imm.hideSoftInputFromWindow(clearlock.getWindowToken(), 0);
                }
                if(openlockplate.hasFocus()){
                    imm.hideSoftInputFromWindow(openlockplate.getWindowToken(), 0);
                }
                if(openlock.hasFocus()){
                    imm.hideSoftInputFromWindow(openlock.getWindowToken(), 0);
                }
                if(cabinet_used.hasFocus()){
                    imm.hideSoftInputFromWindow(cabinet_used.getWindowToken(), 0);
                }
                if(opelock_one.hasFocus()){
                    imm.hideSoftInputFromWindow(opelock_one.getWindowToken(), 0);
                }
                adminmessage.setVisibility(View.GONE);
                layout_one.setVisibility(View.VISIBLE);
                TextView textView=findView(R.id.versionName);
                textView.setText( APKVersionCodeUtils.getVerName(activity));
                long count = Realm.getDefaultInstance().where(CabinetNumber.class).count();
                long isUser = Realm.getDefaultInstance().where(CabinetNumber.class).equalTo("isUser", getResources().getString(R.string.isuser)).count();
                if(isAdded()){
                    text_num1.setText(getResources().getString(R.string.cabinet_all)+count);
                    text_num2.setText(getResources().getString(R.string.cabinet_isuser)+isUser);
                    text_num3.setText(getResources().getString(R.string.cabinet_leave)+(count-isUser));
                }
//        time_out();
                break;
            case R.id.button4:
                SharedPreferences sharedPreferences2=activity.getSharedPreferences("user_info",0);
                String deviceID=sharedPreferences2.getString("deviceId", "");
                textView2.setText(deviceID);
                break;
            case R.id.head_text_03_main:
                if (info!=null) {
                    SharedPreferences sharedPreferences = activity.getSharedPreferences("user_info", 0);
                    String deviceId = sharedPreferences.getString("deviceId", "");
                        activity.exitAlertDialog.show();
                        syncUserFeature.syncSign(deviceId);
                        syncUserFeature.syncUser(deviceId);
                    //    downloadFeature.getPagesInfo(deviceId);

                }else {
                    if(isAdded()){
                        activity.mTts.startSpeaking(getResources().getString(R.string.network_error),activity.mTtsListener);
                        Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.record_button:
               String number1=cabinet_used.getText().toString();
                RealmResults<CabinetRecord> recordList = Realm.getDefaultInstance().where(CabinetRecord.class).equalTo("cabinetNumber", number1).findAll();
                checkUsedRecored=new CheckUsedRecored(getContext(),recordList,number1);
                checkUsedRecored.show();
                break;
            case R.id.lock_message:
                RealmResults<CabinetNumber>cabinetNumberList  = Realm.getDefaultInstance().where(CabinetNumber.class).findAll();
                lockMessage=new LockMessage(getContext(),cabinetNumberList);
                lockMessage.show();
                break;
            case R.id.chang_pdw:
                pwdmodel="2";
                exitAlertDialog1=new ExitAlertDialog1(getContext());
                exitAlertDialog1.setCancelable(false);
                exitAlertDialog1.show();
                break;
            case R.id.back_home:
                activity.microFingerVein.close();
                Intent intent = new Intent();
                // 为Intent设置Action、Category属性
                intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
                intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
                startActivity(intent);
                break;
        }
    }

    private class ExitAlertDialog1 extends Dialog implements View.OnClickListener {
        private Context mContext;
        private EditText etPwd;
        private Button btCancel;
        private Button btConfirm;
        private TextView texttilt;
        public ExitAlertDialog1(Context context, int theme) {
            super(context, theme);
            mContext = context;
            initDialog();
        }
        public ExitAlertDialog1(Context context) {
            super(context, R.style.customer_dialog);
            mContext = context;
            initDialog();
        }
        private void initDialog() {
            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_exit_confirm, null);
            setContentView(view);
            btCancel = (Button) view.findViewById(R.id.btCancel);
            btConfirm = (Button) view.findViewById(R.id.btConfirm);
            etPwd = (EditText) view.findViewById(R.id.deviceCode);
            texttilt=(TextView)view.findViewById(R.id.text_title);
            btCancel.setOnClickListener(this);
            btConfirm.setOnClickListener(this);
        }
        @Override
        public void show() {
            etPwd.setText("");
            if (pwdmodel=="1"){
            }else if (pwdmodel=="2"){
                if(isAdded()){
                    texttilt.setText(R.string.chang_pwd);
                    etPwd.setHint(getResources().getString(R.string.put_new_pwd));
                }
            }
            super.show();
        }
        String devicepwd;
        SharedPreferences userInfo;
        Intent intent;
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btCancel:

                    imm.hideSoftInputFromWindow(etPwd.getWindowToken(), 0);
                    this.dismiss();
                    break;
                case R.id.btConfirm:

                    imm.hideSoftInputFromWindow(etPwd.getWindowToken(), 0);
                    if(pwdmodel.equals("1")){
                    String pwd = etPwd.getText().toString().trim();
                    if (Utils.isEmpty(pwd)) {
                        if(isAdded()){
                            ToastUtils.show(mContext, getResources().getString(R.string.put_pwd), ToastUtils.LENGTH_SHORT);
                        }
                        return;
                    }
                    String repwd;
                    try {
                        repwd = Reservoir.get(Constant.KEY_PASSWORD, String.class);
                    } catch (Exception e) {
                        userInfo=activity.getSharedPreferences("user_info",0);
                        repwd = userInfo.getString("devicepwd","0");
                    }
                    if (!pwd.equals(repwd)) {
                        if(isAdded()){
                            ToastUtils.show(mContext, getResources().getString(R.string.error_password), ToastUtils.LENGTH_SHORT);
                        }
                        return;
                    }else {
                        userInfo = activity.getSharedPreferences("user_info", 0);
                        userInfo.edit().putString("devicepwd", pwd).commit();
                        layout_one.setVisibility(View.GONE);
                        adminmessage.setVisibility(View.VISIBLE);
                        this.dismiss();
                    }
                  }else if (pwdmodel.equals("2")){
                        userInfo=activity.getSharedPreferences("user_info",0);
                        String pwd = etPwd.getText().toString().trim();
                        if (userInfo.getString("devicepwd","").toString().trim()==pwd) {
                            if(isAdded()){
                                ToastUtils.show(mContext, getResources().getString(R.string.same_pwd), ToastUtils.LENGTH_SHORT);
                            }
                        }else {
                            userInfo.edit().putString("devicepwd",pwd).commit();
                            if(isAdded()){
                                ToastUtils.show(mContext, getResources().getString(R.string.chang_pwd_successful), ToastUtils.LENGTH_SHORT);
                            }
                        }
                    }
                    break;
            }
        }
    }
    @Override
    public void downloadNotReceiver(DownLoadData resultResponse) {

    }

    @Override
    public void downloadApK(UpDateBean resultResponse) {

    }

    @Override
    public void syncUserFeaturePagesSuccess(SyncFeaturesPage syncFeaturesPage) {

    }
    int totalPage=0,currentPage=1,downloadPage=0;
    @Override
    public void getPagesInfo(PagesInfoBean pagesInfoBean) {
      totalPage = pagesInfoBean.getData().getPageCount();
        if(totalPage==0){
            activity.exitAlertDialog.dismiss();
        }
        ExecutorService service = Executors.newFixedThreadPool(1);
        for(int x =0 ;x<pagesInfoBean.getData().getPageCount();x++){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    downloadFeature.syncUserFeaturePages(FileUtils.loadDataFromFile(getContext(), "deviceId.text"), currentPage++);
                }
            };
            service.execute(runnable);
        }

    }
    public  void hidden(){


    }
    @Override
    public void downloadSuccess(DownLoadData resultResponse) {

    }

//    ArrayList<Person> SyncFeaturesPages = new ArrayList<>();
//    int totalPage=0,currentPage=0,downloadPage=0;
//    @Override
//    public void getPagesInfo(PagesInfoBean resultResponse) {
//        if (resultResponse.getData().getCount()>0) {
//            totalPage = resultResponse.getData().getPageCount();
//            for (int x = 0; x < 8; x++) {
//                if (x > totalPage - 1) {
//                    return;
//                }
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        currentPage++;
//                        Logger.e(currentPage + "currentPage");
//                        downloadFeature.syncUserFeaturePages(FileUtils.loadDataFromFile(activity, "deviceId.text"), currentPage);
//                    }
//                }).start();
//            }
//        }else {
//            activity.exitAlertDialog.dismiss();
//        }
//    }
//
//    @Override
//    public void syncUserFeaturePagesSuccess(SyncFeaturesPage resultResponse) {
//        if (resultResponse.getData().size()>0) {
//            downloadPage++;
//            Logger.e(downloadPage + "downloadPage");
//            if (totalPage > 8 && currentPage < totalPage) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        currentPage++;
//                        Logger.e(currentPage + "currentPage");
//                        downloadFeature.syncUserFeaturePages(FileUtils.loadDataFromFile(activity, "deviceId.text"), currentPage);
//                    }
//                }).start();
//            }
//            SyncFeaturesPages.addAll(resultResponse.getData());
//            if (downloadPage == totalPage) {
//                PersonDao personDao = BaseApplication.getInstances().getDaoSession().getPersonDao();
//                personDao.insertInTx(resultResponse.getData());
//                Logger.e(SyncFeaturesPages.size() + getResources().getString(R.string.syn_data));
//                NetworkInfo info = connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
//                if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
//                    downloadFeature.appUpdateInfo(FileUtils.loadDataFromFile(activity, "deviceId.text"));
//                } else {
//                    Toast.makeText(activity, getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
//                }
//                activity.exitAlertDialog.dismiss();
//            }
//        }else {
//            activity.exitAlertDialog.dismiss();
//        }
//    }

    @Override
    public void onError(ApiException error) {
        if (activity.exitAlertDialog.isShowing()) {
            activity.exitAlertDialog.dismiss();
        }
        super.onError(error);
    }
    @Override
    public void onResultError(ApiException e) {
        onError(e);
    }
    @Override
    public void onPermissionError(ApiException e) {
        onError(e);
    }
    @Override
    public void adminopenSuccess(ResultResponse resultResponse) {
        if (openOther){
            openOther=false;
        }else if (openAll){
            openAll=false;
        }
    }

    @Override
    public void ClearCabinetSuccess(ResultResponse resultResponse) {
        if (cleanOther){
        String number;
        number=clearlock.getText().toString().trim();
            RealmResults<CabinetNumber> list = Realm.getDefaultInstance().where(CabinetNumber.class).equalTo("cabinetNumber",number).findAll();
            Logger.e("ClearCabinetSuccess==========="+list.size());
        if (list.size()>0) {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    CabinetRecord cabinetRecord1 = new CabinetRecord();
                    cabinetRecord1.setMemberName(getResources().getString(R.string.manager));
                    cabinetRecord1.setPhoneNum("***********");
                    cabinetRecord1.setCabinetNumber(number+ "");
                    cabinetRecord1.setCabinetStating(getResources().getString(R.string.manager_clear));
                    cabinetRecord1.setOpentime(opentime);
                    realm.copyToRealm(cabinetRecord1);
                }
            });
            if(isAdded()){
                ToastUtils.show(activity, getResources().getString(R.string.clear_cabinet), ToastUtils.LENGTH_SHORT);
            }
        }
        cleanOther=false;
    }else if (cleanAll){
            Logger.e("MainFragment"+"=========cleanAll========");
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    List<CabinetNumber> all = Realm.getDefaultInstance().where(CabinetNumber.class).findAll();
                    for (int i = 0; i < all.size(); i++) {
                        CabinetRecord cabinetRecord1 = new CabinetRecord();
                        if(isAdded()){
                            cabinetRecord1.setMemberName(activity.getResources().getString(R.string.manager));
                            cabinetRecord1.setPhoneNum("***********");
                            cabinetRecord1.setCabinetNumber(all.get(i).getCabinetNumber());
                            cabinetRecord1.setCabinetStating(activity.getResources().getString(R.string.clear_all));
                        }
                        cabinetRecord1.setOpentime(opentime);
                        realm.copyToRealm(cabinetRecord1);
                    }
                }
            });
            ToastUtils.show(activity, getResources().getString(R.string.clearall_successful), ToastUtils.LENGTH_SHORT);
//            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    List<CabinetNumber> all = realm.getDefaultInstance().where(CabinetNumber.class).findAll();
//                    for(int i=0;i<all.size();i++) {
//                        CabinetRecord cabinetRecord1=new CabinetRecord();
//                        cabinetRecord1.setMemberName(activity.getResources().getString(R.string.manager));
//                        cabinetRecord1.setPhoneNum("***********");
//                        cabinetRecord1.setCabinetNumber(all.get(i).getCabinetNumber());
//                        cabinetRecord1.setCabinetStating(activity.getResources().getString(R.string.open_all));
//                        cabinetRecord1.setOpentime(opentime);
//                        realm.copyToRealm(cabinetRecord1);
//                    }
//                }
//            });
        cleanAll=false;
    }
    }
    @Override
    public void cabinetNumberSuccess(CabinetNumberData cabinetNumberData) {
    }
    @Override
    public void syncSignUserSuccess(Sign_data downLoadData) {
        List<SignUser > data = downLoadData.getData();
        Realm defaultInstance = Realm.getDefaultInstance();
        RealmResults<SignUser> all = Realm.getDefaultInstance().where(SignUser.class).findAll();
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                all.deleteAllFromRealm();
            }
        });

        defaultInstance.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int x = 0; x < data.size(); x++) {
                    realm.copyToRealm(data.get(x));
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        });
    }
    @Override
    public void syncUserSuccess(DownLoadData resultResponse) {
        ((BaseApplication) activity.getApplicationContext().getApplicationContext()).getPerson().clear();
        ((BaseApplication)activity. getApplicationContext().getApplicationContext()).getPerson().addAll(resultResponse.getData());
        List<Person> data = resultResponse.getData();
        Realm defaultInstance = Realm.getDefaultInstance();
        RealmResults<Person> all = defaultInstance.where(Person.class).findAll();
        defaultInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                all.deleteAllFromRealm();
            }
        });

        defaultInstance.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int x = 0; x < data.size(); x++) {
                    realm.copyToRealm(data.get(x));

                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (activity.baseApplication.downLoadListner!=null) {
                    activity.baseApplication.downLoadListner.finish();
                }
                activity.exitAlertDialog.dismiss();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable throwable) {
                if (activity.baseApplication.downLoadListner!=null) {
                    activity.baseApplication.downLoadListner.finish();
                }
                activity.exitAlertDialog.dismiss();
            }
        });
        Logger.e( "BaseApplication"+"resultResponse.getDown_userInfo() " + resultResponse.getData().size());
    }
    /**
     * 广播接收器
     *
     * @author kevin
     */
    public class MesReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String time=intent.getStringExtra("timeStr");
            head_text_03.setText(time);
            head_text_01.setText(intent.getStringExtra("timeData"));
            if (context == null) {
                context.unregisterReceiver(this);
            }else {
                opentime=time;
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.unregisterReceiver(mesReceiver);//释放广播接收者
    }
}
