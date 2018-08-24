package com.link.cloud.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hotelmanager.xzy.util.OpenDoorUtil;
import com.link.cloud.R;
import com.link.cloud.activity.LockActivity;
import com.link.cloud.activity.WorkService;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.CabinetNumber;
import com.link.cloud.bean.CabinetRecord;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.bean.Lockdata;
import com.link.cloud.contract.IsopenCabinet;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.message.MessageEvent;
import com.link.cloud.model.MdFvHelper;
import com.link.cloud.utils.CountDownTimer;
import com.link.cloud.utils.FileUtils;
import com.link.cloud.utils.Finger_identify;
import com.link.cloud.utils.Utils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by 30541 on 2018/3/28.
 */
public class FirstFragment extends BaseFragment implements IsopenCabinet.isopen {

    @Bind(R.id.head_layout_01)
    RelativeLayout head_layout_01;
    @Bind(R.id.head_text_02)
    TextView head_text_02;
    @Bind(R.id.layout_three)
    LinearLayout layout_three;
    @Bind(R.id.open_lock_layout)
    LinearLayout open_lock_layout;
    @Bind(R.id.text_start)
    TextView text_start;
    @Bind(R.id.text_number)
    TextView text_number;
    @Bind(R.id.text_end)
    TextView text_end;
    @Bind(R.id.text_error)
    TextView text_error;
//    @Bind(R.id.head_text_03)
    TextView head_text_03;
//    @Bind(R.id.time_forfinger)
//    @Bind(R.id.open_layout)
//    LinearLayout open_Layout;
//    @Bind(R.id.code_layout)
//    LinearLayout code_layout;
    TextView time_forfinger;
    OpenDoorUtil openDoorUtil;
    int state = 0;
    boolean timestart;
    int[] pos = new int[1];
    float[] score = new float[1];
    LockActivity activity;
    IsopenCabinet isopenCabinet;
    MesReceiver mesReceiver;
    boolean flog = true;
    String userUid;
    WorkService workService;
    private final static int MSG_SHOW_LOG=3;
    private final static int MSG_SHOW_START=0;
    private final static int MSG_SHOW_SUCCESS=1;
    Context context;
    String opentype=null;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (LockActivity) activity;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.e("FirstFragment"+"======onCreate===isHidden="+this.isHidden()+"isVisible()"+this.isVisible());
        super.onCreate(savedInstanceState);
        isopenCabinet = new IsopenCabinet();
        EventBus.getDefault().register(this);
        isopenCabinet.attachView(this);
        openDoorUtil = new OpenDoorUtil();
        context=getContext();
        this.isHidden();
        this.isVisible();
    }
    @Override
    public void onResume() {
        Logger.e("FirstFragment"+"======onResume=======");
        super.onResume();
        layout_three.setVisibility(View.VISIBLE);
        time_out();
        setupParam();
        code_mumber=(EditText)activity.findViewById(R.id.qrcode);
        code_mumber.setFocusable(true);
        code_mumber.setCursorVisible(true);
        code_mumber.setFocusableInTouchMode(true);
        code_mumber.requestFocus();
       /**
        * EditText编辑框内容发生变化时的监听回调
        */
        code_mumber.addTextChangedListener(new EditTextChangeListener());
    }
    @Override
    public void onStart() {
        super.onStart();
    }


    public static FirstFragment newInstance() {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initListeners() {
        Logger.e("FirstFragment"+"======initListeners=======");
    }
    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        Logger.e("FirstFragment"+"======initViews=======");
        time_forfinger=(TextView)findView(R.id.time_forfinger);
        head_text_03=(TextView)findView(R.id.head_text_03);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LockActivity.ACTION_UPDATEUI);
        intentFilter.addAction(LockActivity.ACTION_UPDATE);
        mesReceiver = new MesReceiver();
        activity.registerReceiver(mesReceiver, intentFilter);
        if(isAdded()){
            head_text_02.setText(R.string.eposit_open);
            text_error.setText(R.string.finger_right);
        }

        workService = new WorkService();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.layout_01;
    }
    @Override
    protected void initData() {
        Logger.e("FirstFragment"+"======initData=======");
    }
    @Override
    protected void onVisible() {
    }
    @Override
    protected void onInvisible() {
        if (timer!=null) {
            timer.cancel();
        }
        Logger.e("FirstFragment"+"======onInvisible=======");
    }
    @OnClick(R.id.head_layout_01)
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.head_layout_01:
                if (Utils.isFastClick()) {
                    activity.bRun = false;
                    isview = true;
                    mdWorkThread=null;
                    runnablemol=null;
                    if (handler!=null){
                        handler.removeCallbacksAndMessages(null);
                    }
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (((BindVeinMainFragment) getParentFragment()).bindViewPager.getCurrentItem()!=0){
                    ((BindVeinMainFragment) getParentFragment()).setFragment(0);
                    }
                }
                break;
        }
    }

    CountDownTimer timer;
    boolean time_start=false;
    boolean isview=false;
    private void time_out() {
    /**
     * 倒计时60秒，一次1秒
     */
    timer = new CountDownTimer(40 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            time_start=true;
            // TODO Auto-generated method stub
            if (isview==false) {
                time_forfinger.setVisibility(View.VISIBLE);
                time_forfinger.setText(millisUntilFinished / 1000 + "");
            }
        }
        @Override
        public void onFinish() {
            if (activity.bRun=true) {
                activity.bRun = false;
            }
            if (((BindVeinMainFragment) getParentFragment()).bindViewPager.getCurrentItem()!=0) {
                ((BindVeinMainFragment) getParentFragment()).setFragment(0);
            }
        }
    };
}
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_START:
                    if(msg.obj!=null) {
                        if(isAdded()){
                            text_error.setText(R.string.finger_right);
                        }
                    }
                    break;
                case MSG_SHOW_SUCCESS:
                    ConnectivityManager connectivityManager;//用于判断是否有网络
                    connectivityManager =(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                    NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                    if (info!=null) {
                        activity.bRun = false;
                        SharedPreferences userinfo = activity.getSharedPreferences("user_info", 0);
//                    String deviceId = userinfo.getString("deviceId", "");
                        if (userUid != null && text_error != null) {
                            if(isAdded()){
                                text_error.setText(R.string.check_successful);

                            }

                        }
                        if (userUid != null) {
                            EventBus.getDefault().post(new MessageEvent(0, FileUtils.loadDataFromFile(activity, "deviceId.text"), userUid));
                        } else if (userUid == null) {
                            text_error.setText("");
                        }
                        Logger.e("FirstFragment-opentype" + opentype);
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                    Thread.sleep(1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                }
//                            }).start();
                    }else {
                        if(isAdded()){
                            Toast.makeText(getContext(),getResources().getString(R.string.network_error),Toast.LENGTH_LONG).show();
                        }

                    }
                    break;
                case 2:
//                    text_error.setText("暂无签到数据");
                    break;
                case MSG_SHOW_LOG:
                    if (text_error!=null) {
                        if (msg.obj != null && handler != null) {

                            text_error.setText((String) (msg.obj));
                        }
                    }
                    break;
                case 7:
                   if(isAdded()){
                       activity.mTts.startSpeaking(getResources().getString(R.string.check_failed), activity.mTtsListener);
                   }
                    ConnectivityManager connectivityManager1;//用于判断是否有网络
                    connectivityManager1 =(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                    NetworkInfo info1 =connectivityManager1.getActiveNetworkInfo(); //获取活动的网络连接信息
                    if (info1!=null) {
                        if (context != null) {
                            if(isAdded()){
                                text_error.setText(R.string.check_failed);
                            }
                        }
                    }else {
                        if(isAdded()){

                            Toast.makeText(getContext(),getResources().getString(R.string.network_error),Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case 8:
                    if(isAdded()){

                        text_error.setText(R.string.move_finger);
                    }
                    break;
            }
        }
    };
    private Thread mdWorkThread=null;
    private void setupParam(){
        if(activity.microFingerVein!=null){
            activity.microFingerVein.close();
        }
        Logger.e("FirstFragment"+"======setupParam=======");
         activity.bRun = true;
           mdWorkThread = new Thread(runnablemol);
           mdWorkThread.start();
    }
    boolean ret = false;
    long start=0,end=0;
    Runnable  runnablemol=new Runnable() {
        @Override
        public void run() {
            int[] tipTimes = {0, 0};//后两次次建模时用了不同手指，重复提醒限制3次
            int modOkProgress = 0;
            Logger.e("FirstFragment"+"activity.bRun"+activity.bRun+"activity.bopen"+activity.bopen);
            while (activity.bRun) {
                if(!activity.bopen) {
                Logger.e("FirstFragment"+"activity.bRun"+activity.bRun+"activity.bopen"+activity.bopen);
                modOkProgress++;
                activity.bopen = activity.microFingerVein.fvdev_open();//开启指定索引的设备
                int cnt = activity.microFingerVein.fvdev_get_count();
                if(cnt == 0){
                    continue;
                }
                if (modOkProgress>10){
//                       Utils.showPromptToast(getContext(),"请重启设备再试。。。");
                    activity.bRun=false;
                }
                continue;
            }
                state = activity.microFingerVein.fvdev_get_state();
                //设备连接正常则进入正常建模或认证流程
                if (state != 0) {
                    time_start=false;
                    if (timer!=null) {
                        timer.cancel();
                    }
                    Logger.e("FirstFragment===========state" + state);
                    byte[] img= MdFvHelper.tryGetFirstBestImg(activity.microFingerVein,0,5);
                    Logger.e("FirstFragment===========img" + img);
                    if (img == null) {
                        continue;
                    }
                    userUid=Finger_identify.Finger_identify(activity,img);
                    if (userUid!=null){
                        if (handler != null) {
                            if(isAdded()){
                                handler.obtainMessage(MSG_SHOW_SUCCESS,getResources().getString(R.string.check_successful)).sendToTarget();
                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (handler != null) {
                            Log.e("Identify failed,", "ret=" + ret + ",pos=" + pos[0] + ", score=" + score[0]);
                            start = System.currentTimeMillis();

                                Message message = new Message();
                                message.what = 7;
                                handler.sendMessage(message);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else {

                    if (handler != null&&getContext()!=null) {
                        if(isAdded()){
                            handler.obtainMessage(MSG_SHOW_LOG,getResources().getString(R.string.finger_right)).sendToTarget();
                        }
                }
                    if (time_start==false&&timer!=null) {
                        try {
                            timer.start();
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                             e.printStackTrace();
                        }
                    }
                }
            }
        }
    };
    EditText code_mumber;
//    @OnClick({R.id.open_qrcode,R.id.open_finger})
//    public void  OnClick(View view){
//        switch (view.getId()){
//            case R.id.open_finger:
//                open_Layout.setVisibility(View.GONE);
//                layout_three.setVisibility(View.VISIBLE);
//                time_out();
//                setupParam();
//                break;
//            case R.id.open_qrcode:
//                open_Layout.setVisibility(View.GONE);
//                code_layout.setVisibility(View.VISIBLE);
//                code_mumber=(EditText)activity.findViewById(R.id.qrcode);
//                code_mumber.setFocusable(true);
//                code_mumber.setCursorVisible(true);
//                code_mumber.setFocusableInTouchMode(true);
//                code_mumber.requestFocus();
//                /**
//                 * EditText编辑框内容发生变化时的监听回调
//                 */
//                code_mumber.addTextChangedListener(new EditTextChangeListener());
//                break;
//        }
//    }
    public class EditTextChangeListener implements TextWatcher {
        long lastTime;
        /**
         * 编辑框的内容发生改变之前的回调方法
         */
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Logger.e("MyEditTextChangeListener"+"beforeTextChanged---" + charSequence.toString());
        }
        /**
         * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入
         * 我们可以在这里实时地 通过搜索匹配用户的输入
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Logger.e("MyEditTextChangeListener"+"onTextChanged---" + charSequence.toString());
        }
        /**
         * 编辑框的内容改变以后,用户没有继续输入时 的回调方法
         */
        @Override
        public void afterTextChanged(Editable editable) {
            String str=code_mumber.getText().toString();
            Logger.e("MyEditTextChangeListener"+ "afterTextChanged---"+code_mumber.getText().toString());
            if (str.contains("\n")) {
                    if(System.currentTimeMillis()-lastTime<1500){
                        code_mumber.setText("");
                        return;
                    }
                    lastTime=System.currentTimeMillis();
                    SharedPreferences userinfo = activity.getSharedPreferences("user_info", 0);
                   String  deviceId = userinfo.getString("deviceId", "");
                   ConnectivityManager connectivityManager;
                    connectivityManager =(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
                    NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
                    if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                                isopenCabinet.openByQrCode(0,deviceId, code_mumber.getText().toString());
                    }
                    code_mumber.setText("");
            }
        }
    }

    @Override
    public void onError(ApiException e) {
        isopen=0;
        activity.bRun=false;
        super.onError(e);
        String reg = "[^\u4e00-\u9fa5]";
        String syt=e.getMessage().replaceAll(reg, "");
        activity.mTts.startSpeaking(syt, activity.mTtsListener);
        Logger.e("FirstFragment"+syt);
        if (handler!=null) {
            handler.obtainMessage(MSG_SHOW_LOG, syt).sendToTarget();
            try {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (((BindVeinMainFragment) getParentFragment()).bindViewPager.getCurrentItem() != 0) {
                            ((BindVeinMainFragment) getParentFragment()).setFragment(0);
                        }
                    }
                }, 5000);
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onPermissionError(ApiException e) {
        onError(e);
    }
    @Override
    public void onResultError(ApiException e) {
        onError(e);
    }
    int  nuberlock=0;
    List<CabinetNumber> cabinet;
    String lockplate;
    String opentime=null;
    int isopen=0;
    ConnectivityManager connectivityManager;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(MessageEvent event){
        Logger.e("FirstFragment"+"========messageEventBus+type="+event.type+"isopen=="+isopen);
        if (event.type==0&&isopen<1) {
            isopenCabinet = new IsopenCabinet();
            isopenCabinet.attachView(this);
            connectivityManager =(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
            NetworkInfo info =connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
            if (info != null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                starttime=System.currentTimeMillis();
                if (starttime-lasttime>2000) {
                    isopenCabinet.isopen(event.type, event.deviceId, event.userId, "vein");
                    lasttime=System.currentTimeMillis();
                }
            }else {
                if(isAdded()){
                    activity.mTts.startSpeaking(getResources().getString(R.string.network_error),activity.mTtsListener);
                }
            }
        }
        isopen++;
}


    long starttime=0,lasttime=0;
    @Override
    public void isopenSuccess(Lockdata resultResponse) {
        isopen=0;
        isview=true;
        if (timer!=null){
        timer.cancel();
        }
        layout_three.setVisibility(View.GONE);
        open_lock_layout.setVisibility(View.VISIBLE);
        String numstr=resultResponse.getLockdata().getCabinetnumber();
        CabinetRecord cabinetRecord=new CabinetRecord();
        cabinetRecord.setMemberName(resultResponse.getLockdata().getName());
        String number=resultResponse.getLockdata().getNumberValue();
        if (number.length() == 11) {
            number = number.substring(0, 3) + "****" + number.substring(7, number.length());
        }
            cabinetRecord.setPhoneNum(number);
            cabinetRecord.setOpentime(opentime);
        if(isAdded()){

            cabinetRecord.setIsUsed(getResources().getString(R.string.isuser));
            cabinetRecord.setCabinetStating(getResources().getString(R.string.eposit_open));
        }
            cabinetRecord.setCabinetNumber(numstr);
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(cabinetRecord);
            };
        });
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    List<CabinetNumber> list = realm.where(CabinetNumber.class).equalTo("cabinetNumber", numstr).findAll();
                    if (list.size() != 0) {
                        CabinetNumber cabinetNumber = list.get(0);
                        if(isAdded()){
                            cabinetNumber.setIsUser(getResources().getString(R.string.isuser));
                        }
                        realm.copyToRealm(cabinetNumber);
                    }
                }
            });

        new Thread(new Runnable() {
            @Override
            public void run() {
                cabinet=Realm.getDefaultInstance().where(CabinetNumber.class).equalTo("cabinetNumber",numstr).findAll();
                if (cabinet.size() > 0) {
                    Logger.e("FirstFragment" + numstr + "==========================" + cabinet.size());
                    try {
                        lockplate = cabinet.get(0).getCabinetLockPlate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    nuberlock = Integer.parseInt(cabinet.get(0).getCircuitNumber());
                    if (nuberlock > 10) {
                        nuberlock = nuberlock % 10;
                        Logger.e("FirstFragment===" + nuberlock);
                        if (nuberlock == 0) {
                            nuberlock = 10;
                        }
                    }
                    try {
                        if (Integer.parseInt(lockplate) <= 10) {
                            activity.serialpprt_wk1.getOutputStream().write(openDoorUtil.openOneDoor(Integer.parseInt(lockplate), nuberlock));
                            Logger.e("FirstFragment===1" + Integer.parseInt(lockplate) + "====" + nuberlock);
                        } else if (Integer.parseInt(lockplate) > 10 && Integer.parseInt(lockplate) <= 20) {
                            activity.serialpprt_wk2.getOutputStream().write(openDoorUtil.openOneDoor(Integer.parseInt(lockplate) % 10, nuberlock));
                            Logger.e("FirstFragment===2" + Integer.parseInt(lockplate) + "====" + nuberlock);
                        } else if (Integer.parseInt(lockplate) > 20 && Integer.parseInt(lockplate) <= 30) {
                            activity.serialpprt_wk3.getOutputStream().write(openDoorUtil.openOneDoor(Integer.parseInt(lockplate) % 10, nuberlock));
                            Logger.e("FirstFragment===3" + Integer.parseInt(lockplate) + "====" + nuberlock);
                        }
                    } catch (Exception e) {
                    } finally {
                        if (timer != null) {
                            timer.cancel();
                        }
                    }
                }
            }
        }).start();
        if(isAdded()){
            activity.mTts.startSpeaking(getResources().getString(R.string.open_1)+resultResponse.getLockdata().getCabinetnumber()+getResources().getString(R.string.open_2),activity.mTtsListener);
        }
//        Logger.e("opencabind==="+"CabinetLockPlate: "+users.get(0).getCabinetLockPlate()+"Cabinetnumber: "+resultResponse.getLockdata().getCabinetnumber()+"nuberlock: "+nuberlock);
        text_number.setText(resultResponse.getLockdata().getCabinetnumber());
        try {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (((BindVeinMainFragment) getParentFragment()).bindViewPager.getCurrentItem()!=0) {
                        ((BindVeinMainFragment) getParentFragment()).setFragment(0);
                    }
                }
            }, 3000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void codeSuccess(Code_Message resultResponse) {
        isopen=0;
        isview=true;
        if (timer!=null){
            timer.cancel();
        }
        layout_three.setVisibility(View.GONE);
        open_lock_layout.setVisibility(View.VISIBLE);
        String numstr=resultResponse.getData().getCabinetNumber();
        CabinetRecord cabinetRecord=new CabinetRecord();
        cabinetRecord.setMemberName(resultResponse.getData().getName());
        String number=resultResponse.getData().getNumberValue();
        if (number.length() == 11) {
            number = number.substring(0, 3) + "****" + number.substring(7, number.length());
        }
        cabinetRecord.setPhoneNum(number);
        cabinetRecord.setOpentime(opentime);
        if(isAdded()){

            cabinetRecord.setIsUsed(getResources().getString(R.string.isuser));
            cabinetRecord.setCabinetStating(getResources().getString(R.string.eposit_open));
        }
        cabinetRecord.setCabinetNumber(numstr);
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(cabinetRecord);
            };
        });

            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    List<CabinetNumber> list = realm.where(CabinetNumber.class).equalTo("cabinetNumber", numstr).findAll();
                    if (list.size() != 0) {
                        CabinetNumber cabinetNumber = list.get(0);
                        if(isAdded()){
                            cabinetNumber.setIsUser(getResources().getString(R.string.isuser));
                        }
                        realm.copyToRealm(cabinetNumber);
                    }
                }
            });

        new Thread(new Runnable() {
            @Override
            public void run() {
                cabinet=Realm.getDefaultInstance().where(CabinetNumber.class).equalTo("cabinetNumber",numstr).findAll();

                if (cabinet.size() > 0) {
                    Logger.e("FirstFragment" + numstr + "==========================" + cabinet.size());
                    try {
                        lockplate = cabinet.get(0).getCabinetLockPlate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    nuberlock = Integer.parseInt(cabinet.get(0).getCircuitNumber());
                    if (nuberlock > 10) {
                        nuberlock = nuberlock % 10;
                        Logger.e("FirstFragment===" + nuberlock);
                        if (nuberlock == 0) {
                            nuberlock = 10;
                        }
                    }
                    try {
                        if (Integer.parseInt(lockplate) <= 10) {
                            activity.serialpprt_wk1.getOutputStream().write(openDoorUtil.openOneDoor(Integer.parseInt(lockplate), nuberlock));
                            Logger.e("FirstFragment===1" + Integer.parseInt(lockplate) + "====" + nuberlock);
                        } else if (Integer.parseInt(lockplate) > 10 && Integer.parseInt(lockplate) <= 20) {
                            activity.serialpprt_wk2.getOutputStream().write(openDoorUtil.openOneDoor(Integer.parseInt(lockplate) % 10, nuberlock));
                            Logger.e("FirstFragment===2" + Integer.parseInt(lockplate) + "====" + nuberlock);
                        } else if (Integer.parseInt(lockplate) > 20 && Integer.parseInt(lockplate) <= 30) {
                            activity.serialpprt_wk3.getOutputStream().write(openDoorUtil.openOneDoor(Integer.parseInt(lockplate) % 10, nuberlock));
                            Logger.e("FirstFragment===3" + Integer.parseInt(lockplate) + "====" + nuberlock);
                        }
                    } catch (Exception e) {
                    } finally {
                        if (timer != null) {
                            timer.cancel();
                        }
                    }
                }
            }
        }).start();
        if(isAdded()){
            activity.mTts.startSpeaking(getResources().getString(R.string.open_1)+resultResponse.getData().getCabinetNumber()+getResources().getString(R.string.open_2),activity.mTtsListener);
        }
//        Logger.e("opencabind==="+"CabinetLockPlate: "+users.get(0).getCabinetLockPlate()+"Cabinetnumber: "+resultResponse.getLockdata().getCabinetnumber()+"nuberlock: "+nuberlock);
        text_number.setText(resultResponse.getData().getCabinetNumber());
        try {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (((BindVeinMainFragment) getParentFragment()).bindViewPager.getCurrentItem()!=0) {
                        ((BindVeinMainFragment) getParentFragment()).setFragment(0);
                    }
                }
            }, 3000);
        }catch (Exception e){
            e.printStackTrace();
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
            String time=intent.getStringExtra("timeStr");
            head_text_03.setText(time);
            if (context == null) {
                context.unregisterReceiver(this);
            }else {
                opentime=time;
            }
        }
    }
    @Override
    public void onPause() {
        activity.bRun=false;
        if (mdWorkThread!=null&&mdWorkThread.isAlive()){
            try {
                mdWorkThread.join();
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        activity.unregisterReceiver(mesReceiver);//释放广播接收者
        Logger.e("FirstFragment"+"OnDestroy");
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        if(activity.microFingerVein!=null) {
            activity.microFingerVein.close();
        }
        activity.bRun=false;

        mdWorkThread=null;
        runnablemol=null;
        isview=true;
        if (timer!=null) {
            timer.cancel();
        }
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
        }

        super.onDestroy();
    }

}
