package com.link.cloud.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.activity.MainActivity;
import com.link.cloud.greendao.gen.PersonDao;
import com.orhanobut.logger.Logger;
import com.link.cloud.BaseApplication;

import com.link.cloud.activity.BindAcitvity;
import com.link.cloud.activity.CallBackValue;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.Member;
import com.link.cloud.constant.Constant;
import com.link.cloud.contract.BindTaskContract;
import com.link.cloud.core.BaseFragment;

import java.util.HashMap;
import java.util.Map;


import butterknife.Bind;
import md.com.sdk.MicroFingerVein;

import static java.lang.String.valueOf;

/**
 * Created by Administrator on 2017/8/30.
 */

public class RegisterFragment_Three extends BaseFragment implements BindTaskContract.BindView {

//    @Bind(R.id.bind_put_tv)
//    TextView tvTips;
//    @Bind(R.id.bind_put_img)
//    ImageView bind_put_img;
//    @Bind(R.id.bind_put_error)
//    ImageView bind_put_error;
//    @Bind(R.id.bind_put_succese)
//    ImageView bingSuccessImage;

    @Bind(R.id.text_error)
    TextView text_error;
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
    private String deviceID;
    private Member mMemberInfo;
    Context context;
    public BindTaskContract presenter;
    private static int MAXT_FINGER = 3;//表示注册几个指静脉模版
    Handler mHandler;
    CallBackValue callBackValue;
    MainActivity mainActivity;
    Map<String, Object> datemap= new HashMap<String, Object>();;
    public BindAcitvity activity;
    Runnable runnable,runnable1;
    private SharedPreferences userInfo;
    private PersonDao personDao;
    MediaPlayer mediaPlayer0,mediaPlayer1,mediaPlayer2,mediaPlayer3;
    public static RegisterFragment_Three newInstance(Member memberInfo) {
        RegisterFragment_Three fragment = new RegisterFragment_Three();
        Bundle args = new Bundle();
        args.putSerializable(Constant.EXTRAS_MEMBER, memberInfo);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=(BindAcitvity)activity;
        callBackValue=(CallBackValue)activity;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.e("RegisterFragment_Three-----------onCreate ");
        super.onCreate(savedInstanceState);
//        mediaPlayer0=MediaPlayer.create(activity,R.raw.error_acquire);//采集错误
//        mediaPlayer1=MediaPlayer.create(activity,R.raw.error_finger);//请放置同一根手指
//        mediaPlayer2=MediaPlayer.create(activity,R.raw.no_finger);//未检测到手指
//        mediaPlayer3=MediaPlayer.create(activity,R.raw.no_move_finger);//未移开手指
//        mHandler=new Handler();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.layout_putfinger;
    }
    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        Logger.e("RegisterFragment_Three-----------initViews ");
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMemberInfo = (Member) bundle.getSerializable(Constant.EXTRAS_MEMBER);
            userInfo = activity.getSharedPreferences("user_info", 0);
            deviceID = userInfo.getString("DeviceID", "");

        }
    }
    private volatile boolean bRun=false;
    boolean timestart=false;
    boolean  bopen=false;
    private boolean bWorkModel=false;//建模是否进行
    private Thread mdWorkThread=null;//进行建模或认证的全局工作线程
    private int deviceTouchState=1;//触摸：0，移开1，设备断开或其他状态2
    MicroFingerVein microFingerVein;
    private void setupParam() {
        bRun=true;
        mdWorkThread=new Thread(runnablemol);
        mdWorkThread.start();
    }
    Runnable  runnablemol=new Runnable() {
        @Override
        public void run() {
            int state=0;
            int[] pos = new int[1];
            float[] score = new float[1];
            boolean ret=false;
            int[] tipTimes={0,0};//后两次次建模时用了不同手指，重复提醒限制3次
            int modOkProgress=0;
            while(bRun) {
//                text_error.setText("请按图示放置手指");
                int count=microFingerVein.fvdev_get_count();
                if (count==0){
                    continue;
                }
                state=microFingerVein.fvdev_get_state();
                //设备连接正常则进入正常建模或认证流程
                if(state != 0) {
                    if(state==1||state==2) {
                        continue;
                    }
                    deviceTouchState=0;
                    byte[] img=microFingerVein.fvdev_grab();
                    if(img==null) {
                        continue;
                    }
                    byte[] feauter1=microFingerVein.fv_extract_model(img1,null,null);
                    if(feauter1 == null) {
                        continue;
                    }
                    else if(bWorkModel) {
                        //建模
                        modOkProgress++;
                        if(modOkProgress==1) {//first model
                            tipTimes[0]=0;
                            tipTimes[1]=0;
                            img1=img;
                            Logger.e("BindActivity"+"model 1 ok");
//                            text_error.setText("请再次放置手指");
                        }else if(modOkProgress==2) {//second model
//                            handlermodle.sendEmptyMessage(2);
                            if (microFingerVein.fvdev_get_state()==3){
//                                text_error.setText("请移开手指");
//                                handlermodle.sendEmptyMessage(5);
                            }
                            img2=img;
                            ret=microFingerVein.fv_index(feauter1,1,img, pos,score);
                            if(ret && score[0] > 0.4) {
                                Logger.e("BindActivity"+"model 2 ok");
                                feauter2=microFingerVein.fv_extract_model(img,null,null);
                                if(feauter2 != null) {
                                    tipTimes[0]=0;
                                    tipTimes[1]=0;
                                }else {//第二次建模从图片中取特征值无效
//                                    handlermodle.sendEmptyMessage(1);
                                    modOkProgress=1;
                                    if(++tipTimes[0]<=3) {
                                    }else {//连续超过3次放了不同手指则忽略此次建模重来
                                        modOkProgress=0;
                                    }
                                }
                            }else {
                                modOkProgress=1;
                                if(++tipTimes[0]<=3) {
                                }else {//连续超过3次放了不同手指则忽略此次建模重来
                                    modOkProgress=0;
                                }
                            }
                        }else if(modOkProgress==3) {//third model
                            ret=microFingerVein.fv_index(feauter2,1,img, pos,score);
                            if (ret && score[0] > 0.4) {
                                Logger.e("BindActivity"+"model 3 ok");
                                feauter = microFingerVein.fv_extract_model(feauter2,img2,img);
                                if(feauter!=null) {//成功生成一个3次建模并融合的融合特征数组
                                    tipTimes[0]=0;
                                    tipTimes[1]=0;
                                }else {//第三次建模从图片中取特征值无效
                                    modOkProgress=2;
                                    if(++tipTimes[1]<=3) {
                                    }
                                }
                                bopen=false;
                            } else {
                                modOkProgress=2;
                                continue;
                            }
                        }else if(modOkProgress>3||modOkProgress<=0) {
                            modOkProgress=0;
                        }
                    }

                    while (state == 3  && bRun) {
                        deviceTouchState=0;
                        state=microFingerVein.fvdev_get_state();
                        if(!bopen){//等待手指拿开的中途设备断开了
                        }
                    }
                    //--------------------------------------------------------------
                    continue;
                }else {//触摸state==0时
                    if(bopen) {
                        deviceTouchState = 1;
                    }
                }
            }
            if (bopen){
                bopen=false;
            }
        }
    };
    @Override
    protected void initListeners() {
    }
    @Override
    protected void initData() {
        Logger.e("RegisterFragment_Three-----------initData ");
        microFingerVein=MicroFingerVein.getInstance(activity);
        setupParam();
        presenter = new BindTaskContract();
        this.presenter.attachView(this);
    }
    @Override
    protected void onVisible() {
        callBackValue.setActivtyChange("3");
        Logger.e("RegisterFragment_Three-----------onVisible ");
        //注册需要初始化指静脉模块
    }
    @Override
    protected void onInvisible() {
    }
    private void nextOperate() {
    }
    private void bindVeinMemeber() {
        Logger.e("RegisterFragment_Three-----------bindVeinMemeber ");
        if (BaseApplication.DEBUG)
            this.showProgress(true, false, "请稍后...");
//            this.presenter.bindVeinMemeber();                //绑定接口
            Logger.e("BingVeinFragment:"+mMemberInfo.toString());
    }

    @Override
    public void onStop() {
        super.onStop();
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
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void bindSuccess(Member returnBean) {
    }

    @Override
    public void onError(ApiException e) {
        super.onError(e);
//        this.showProgress(false);
////        bind_put_error.setVisibility(View.VISIBLE);
//        if (BaseApplication.DEBUG && !Utils.isEmpty(e.getDisplayMessage()))
//            this.showToast(e.getDisplayMessage(), Toast.LENGTH_LONG);
//        String errorMsg = "";
//        if (e.getCode() == ApiException.REGISTER_TEMPLATE_ERROR) {
//            errorMsg = getResources().getString(R.string.error_failure_register_template);
//            mediaPlayer0.start();
//        } else if (e.getCode() == ApiException.MATCH_TEMPLATE_ERROR) {
//            errorMsg = getResources().getString(R.string.error_failure_match_template);
//            mediaPlayer1.start();
//        } else if (e.getCode() == ApiException.MISSING_FINGER_ERROR) {
//            errorMsg = getResources().getString(R.string.error_failure_missing_finger);
//            mediaPlayer2.start();
//        } else if (e.getCode() == ApiException.MOVING_FINGER_ERROR) {
//            errorMsg = getResources().getString(R.string.error_failure_moving_finger);
//            mediaPlayer3.start();
//        } else {
//            errorMsg = e.getDisplayMessage();
//        }
////        bindFailureImage.setVisibility(View.VISIBLE);
////        tvTips.setText(errorMsg);
//       runnable1= new Runnable() {
//            @Override
//            public void run() {
//                nextOperate();
////                bind_put_error.setVisibility(View.GONE);
////                tvTips.setText("请按图示放置手指" );
//            }
//        };
//        mHandler.postDelayed(runnable1,3000);
    }
    @Override
    public void onPermissionError(ApiException e) {
        onError(e);
    }

    @Override
    public void onResultError(ApiException e) {
        onError(e);
    }
    @Override
    public void onDestroy() {
        this.showProgress(false);
        this.presenter.detachView();
        this.presenter = null;

        super.onDestroy();
        if (runnable1!=null) {
            mHandler.removeCallbacks(runnable1);
        }
    if (runnable!=null) {
        mHandler.removeCallbacks(runnable);
    }
        if (mediaPlayer0!=null) {
            mediaPlayer0.stop();
            mediaPlayer0.release();
            mediaPlayer0 = null;
        }else if (mediaPlayer1!=null) {
            mediaPlayer1.stop();
            mediaPlayer1.release();
            mediaPlayer1 = null;
        }
        else if (mediaPlayer2!=null) {
            mediaPlayer2.stop();
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }
        else if (mediaPlayer3!=null){
            mediaPlayer3.stop();
            mediaPlayer3.release();
            mediaPlayer3 = null;
        }
    }

//    public void savefile (String str){
//        FileOutputStream out = null;
//        BufferedWriter writer = null;
//        try {
//            out = activity.openFileOutput("userfinger", Context.MODE_PRIVATE);
//            writer = new BufferedWriter(new OutputStreamWriter(out));
//            writer.write(str);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if(writer!= null) {
//                    String version = "1.0";
//                    String datetime = System.currentTimeMillis() + "";
//                    String appkey = Utils.getMetaData(Constant.APP_KEY);
//                    String sign = Utils.generateSign(version, appkey, datetime);
//                    datemap.put("appkey",appkey);
//                    datemap.put("datetime",datetime);
//                    datemap.put("version",version);
//                    datemap.put("sign",sign);
//                    datemap.put("deviceId",deviceID);
//                    datemap.put("userType",1);
//                    datemap.put("numberType",1);
//                    datemap.put("numberValue","15911111111");
//                    Context context=getContext();//首先，在Activity里获取context
//                    File file=context.getFilesDir();
//                    String path=file.getAbsolutePath();
//                    System.out.println(path);
//                    File files=new File("/data/user/0/com.soonvein.cloud/files/userfinger");
//                    if(files!=null) {
////                        post_file(datemap, files);
//                    }
//                    writer.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    protected void post_file( final Map<String, Object> map, File file) {
//        OkHttpClient client = new OkHttpClient();
//        // form 表单形式上传
//        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        if(file != null){
//            // MediaType.parse() 里面是上传的文件类型。
//            RequestBody body = RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), file);
//            String filename = file.getName();
//            // 参数分别为， 请求key ，文件名称 ， RequestBody
//            requestBody.addFormDataPart("featureFile", filename, body);
//        }
//        if (map != null) {
//            // map 里面是请求中所需要的 key 和 value
//            for (Map.Entry entry : map.entrySet()) {
//                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
//            }
//        }
//        Request request = new Request.Builder().url("http://120.24.169.32:8082/api/bindUserFinger").post(requestBody.build()).tag(getContext()).build();
//        // readTimeout("请求超时时间" , 时间单位);
//        client.newBuilder().readTimeout(2000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i("lfq" ,"onFailure");
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String str = response.body().string();
//                    Log.i("lfq", response.message() + " , body " + str);
//                } else {
//                    Log.i("lfq" ,response.message() + " error : body " + response.body().string());
//                }
//            }
//        });
//    }
}
