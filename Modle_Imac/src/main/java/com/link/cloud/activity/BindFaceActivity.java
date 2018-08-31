package com.link.cloud.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.facedetection.AFD_FSDKEngine;
import com.arcsoft.facedetection.AFD_FSDKError;
import com.arcsoft.facedetection.AFD_FSDKFace;
import com.arcsoft.facedetection.AFD_FSDKVersion;
import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.guo.android_extend.image.ImageConverter;
import com.guo.android_extend.java.ExtByteArrayOutputStream;
import com.guo.android_extend.java.ExtOutputStream;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.link.cloud.R;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.FaceBindBean;
import com.link.cloud.bean.Member;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.contract.RegisterTaskContract;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.core.BaseAppCompatActivity;
import com.link.cloud.setting.TtsSettings;
import com.link.cloud.utils.FaceDB;
import com.link.cloud.view.CameraFrameData;
import com.link.cloud.view.CameraGLSurfaceView;
import com.link.cloud.view.CameraSurfaceView;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 49488 on 2018/7/27.
 */

public class BindFaceActivity extends BaseAppCompatActivity implements CallBackValue, RegisterTaskContract.RegisterView, SendLogMessageTastContract.sendLog, View.OnTouchListener, CameraSurfaceView.OnCameraListener {
    @Bind(R.id.phone_regist_one)
    EditText etPhoneNum;
    @Bind(R.id.phonecode_regist_one)
    EditText phonecode_regist_one;
    @Bind(R.id.regist_code)
    EditText regist_code;
    @Bind(R.id.layout_page_title)
    TextView layoutPageTitle;
    @Bind(R.id.layout_page_time)
    TextView layoutPageTime;
    @Bind(R.id.face_back)
    LinearLayout faceBack;
    @Bind(R.id.bind_one_Cimg)
    ImageView bind_one_Cimg;
    @Bind(R.id.bind_one_line)
    View bind_one_line;
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
    @Bind(R.id.code_tv)
    TextView codeTv;
    @Bind(R.id.regist_code_bt)
    Button registCodeBt;
    @Bind(R.id.bind_step_code)
    LinearLayout bindStepCode;
    @Bind(R.id.bind_keypad_1)
    Button bindKeypad1;
    @Bind(R.id.bind_keypad_2)
    Button bindKeypad2;
    @Bind(R.id.bind_keypad_3)
    Button bindKeypad3;
    @Bind(R.id.bind_keypad_4)
    Button bindKeypad4;
    @Bind(R.id.bind_keypad_5)
    Button bindKeypad5;
    @Bind(R.id.bind_keypad_6)
    Button bindKeypad6;
    @Bind(R.id.bind_keypad_7)
    Button bindKeypad7;
    @Bind(R.id.bind_keypad_8)
    Button bindKeypad8;
    @Bind(R.id.bind_keypad_9)
    Button bindKeypad9;
    @Bind(R.id.bind_keypad_delect)
    Button bindKeypadDelect;
    @Bind(R.id.bind_keypad_0)
    Button bindKeypad0;
    @Bind(R.id.bind_keypad_ok)
    Button bindKeypadOk;
    @Bind(R.id.card_number)
    TextView cardNumber;
    @Bind(R.id.confirm)
    TextView confirm;
    @Bind(R.id.member_info)
    LinearLayout memberInfo;
    @Bind(R.id.layout_bing_phone)
    LinearLayout layout_bing_phone;
    public RegisterTaskContract presenter;
    @Bind(R.id.error_tv)
    TextView errorTv;
    @Bind(R.id.error_layout)
    RelativeLayout errorLayout;
    @Bind(R.id.bind_step_phone)
    LinearLayout bindStepPhone;

    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.tel)
    TextView tel;
    @Bind(R.id.card_num)
    TextView cardNum;
    @Bind(R.id.value_cout)
    TextView valueCout;
    @Bind(R.id.value_date)
    TextView valueDate;
    @Bind(R.id.confirm_layout)
    LinearLayout confirmLayout;
    @Bind(R.id.sv_camera_surfaceview)
    CameraGLSurfaceView svCameraSurfaceview;
    @Bind(R.id.take_photo)
    TextView takePhoto;
    @Bind(R.id.introduce_face)
    LinearLayout introduceFace;
    @Bind(R.id.face_register)
    LinearLayout faceRegister;
    private Camera camera;
    private AFR_FSDKFace mAFR_FSDKFace;
    private byte[] clone;

    protected int getLayoutId() {
        return R.layout.activity_bind_face;

    }
    public static String voicerLocal="xiaoyan";
    // 引擎类型
    private SharedPreferences mSharedPreferences;
    public SpeechSynthesizer mTts;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        etPhoneNum.setInputType(InputType.TYPE_NULL);
        layoutPageTitle.setText(getResources().getString(R.string.face_bind));
        this.presenter = new RegisterTaskContract();
        this.presenter.attachView(this);
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        setParam();
    }

    public SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
        }
        @Override
        public void onSpeakPaused() {
        }
        @Override
        public void onSpeakResumed() {
        }
        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
        }
        @Override
        public void onCompleted(SpeechError speechError) {
        }
        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
        }
    };
    /**
     * 参数设置
     * @return
     */
    private void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成
        //设置使用本地引擎
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        //设置发音人资源路径
        mTts.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath());
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME,voicerLocal);
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");


    }
    //获取发音人资源路径
    private String getResourcePath(){
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/"+BindAcitvity.voicerLocal+".jet"));
        return tempBuffer.toString();
    }
    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip(getResources().getString(R.string.initialization_fail)+code);
            } else {
            }
        }
    };
    Toast mToast;
    private void showTip(final String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }
    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public Camera setupCamera() {
        // TODO Auto-generated method stub
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mWidth, mHeight);
            parameters.setPreviewFormat(mFormat);
            mCamera.setDisplayOrientation(90);
            for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
                Log.d(TAG, "SIZE:" + size.width + "x" + size.height);
            }
            for (Integer format : parameters.getSupportedPreviewFormats()) {
                Log.d(TAG, "FORMAT:" + format);
            }

            List<int[]> fps = parameters.getSupportedPreviewFpsRange();
            for (int[] count : fps) {
                Log.d(TAG, "T:");
                for (int data : count) {
                    Log.d(TAG, "V=" + data);
                }
            }
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mCamera != null) {
            mWidth = mCamera.getParameters().getPreviewSize().width;
            mHeight = mCamera.getParameters().getPreviewSize().height;
        }
        return mCamera;
    }

    @Override
    public void setupChanged(int format, int width, int height) {

    }

    @Override
    public boolean startPreviewImmediately() {
        return true;
    }

    @Override
    public Object onPreview(byte[] data, int width, int height, int format, long timestamp) {
        clone = data.clone();
        return null;
    }

    @Override
    public void onBeforeRender(CameraFrameData data) {

    }

    @Override
    public void onAfterRender(CameraFrameData data) {

    }

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
    protected void initData() {
        mesReceiver=new MesReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NewMainActivity.ACTION_UPDATEUI);
        registerReceiver(mesReceiver, intentFilter);
    }

    Editable editable;
    String deviceID = null;
    int index;
    int numberType;
    SharedPreferences userInfo;

    @OnClick({R.id.bind_keypad_ok, R.id.bind_keypad_0, R.id.bind_keypad_1, R.id.bind_keypad_2, R.id.bind_keypad_3, R.id.bind_keypad_4,
            R.id.bind_keypad_5, R.id.bind_keypad_6, R.id.bind_keypad_7, R.id.bind_keypad_8, R.id.bind_keypad_9, R.id.bind_keypad_delect
            , R.id.face_back, R.id.confirm,R.id.take_photo})
    public void onClick(View view) {
        if (regist_code.hasFocus()) {
            index = regist_code.getSelectionStart();
            editable = regist_code.getText();
        } else if (phonecode_regist_one.hasFocus()) {
            index = phonecode_regist_one.getSelectionStart();
            editable = phonecode_regist_one.getText();
        } else {
            index = etPhoneNum.getSelectionStart();
            editable = etPhoneNum.getText();
        }
        String phoneNum;
        switch (view.getId()) {
            case R.id.bind_keypad_0:
                editable.insert(index, "0");
                break;
            case R.id.bind_keypad_1:
                editable.insert(index, "1");
                break;
            case R.id.bind_keypad_2:
                editable.insert(index, "2");
                break;
            case R.id.bind_keypad_3:
                editable.insert(index, "3");
                break;
            case R.id.bind_keypad_4:
                editable.insert(index, "4");
                break;
            case R.id.bind_keypad_5:
                editable.insert(index, "5");
                break;
            case R.id.bind_keypad_6:
                editable.insert(index, "6");
                break;
            case R.id.bind_keypad_7:
                editable.insert(index, "7");
                break;
            case R.id.bind_keypad_8:
                editable.insert(index, "8");
                break;
            case R.id.bind_keypad_9:
                editable.insert(index, "9");
                break;
            case R.id.bind_keypad_delect:
                if (index > 0) {
                    editable.delete(index - 1, index);
                }
                break;
            case R.id.bind_keypad_ok:
                phoneNum = etPhoneNum.getText().toString().trim();

//                if (Utils.isEmpty(phoneNum) || !Utils.isMobileNumberValid(phoneNum)) {
//                    Toast.makeText(this, R.string.error_phone, Toast.LENGTH_SHORT).show();
//                    return;
//                }
                userInfo = getSharedPreferences("user_info", 0);
                deviceID = userInfo.getString("deviceId", "");
                numberType = userInfo.getInt("numberType", 0);
                presenter.getMemFace(deviceID, 1, phoneNum);
                break;
            case R.id.face_back:
                finish();
                break;
            case R.id.confirm:
                if(!isFinish){
                    setActivtyChange("3");
                   mCameraRotate = 0;
                   mCameraMirror = false;
                   mWidth = 640;
                    mHeight = 480;
                    mFormat = ImageFormat.NV21;
                    svCameraSurfaceview .setOnTouchListener(this);
                    mGLSurfaceView = (CameraSurfaceView) findViewById(R.id.surfaceView);
                    mGLSurfaceView.setOnCameraListener(this);
                    mGLSurfaceView.setupGLSurafceView(svCameraSurfaceview, true, mCameraMirror, mCameraRotate);
                    mGLSurfaceView.debug_print_fps(false, false);
                    faceRegister.setVisibility(View.VISIBLE);
                    memberInfo.setVisibility(View.GONE);
               }else {
                        finish();
                    }

        break;
            case R.id.take_photo:
                ExtByteArrayOutputStream ops = new ExtByteArrayOutputStream();
                YuvImage yuv = new YuvImage(clone, ImageFormat.NV21, 640, 480, null);
                yuv.compressToJpeg(new Rect(0, 0, 640, 480), 85, ops);
                final Bitmap bitmap = BitmapFactory.decodeByteArray(ops.getByteArray(), 0, ops.getByteArray().length);
                try {
                    File file= new File(Environment.getExternalStorageDirectory()+"/register.jpg");
                    if(file.exists()){
                        file.delete();
                    }
                    FileOutputStream fileOutputStream=new FileOutputStream(file.getAbsolutePath());
                    bitmap.compress(Bitmap.CompressFormat.JPEG,85,fileOutputStream);
                    saveData(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(BindFaceActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
                }


                break;

        }
    }
    private MesReceiver mesReceiver;
    private int mWidth, mHeight, mFormat;
    private CameraSurfaceView mGLSurfaceView;
    private Camera mCamera;
    int mCameraRotate;
    boolean mCameraMirror;
    boolean isFinish = false;
    public static String TAG = "Camera";
    public void saveData(Bitmap mBitmap){

        byte[] data = new byte[mBitmap.getWidth() * mBitmap.getHeight() * 3 / 2];
        ImageConverter convert = new ImageConverter();
        convert.initial(mBitmap.getWidth(), mBitmap.getHeight(), ImageConverter.CP_PAF_NV21);
        if (convert.convert(mBitmap, data)) {
            Log.d(TAG, "convert ok!");
        }
        convert.destroy();

        AFD_FSDKEngine engine = new AFD_FSDKEngine();
        AFD_FSDKVersion version = new AFD_FSDKVersion();
        List<AFD_FSDKFace> result = new ArrayList<AFD_FSDKFace>();
        AFD_FSDKError err = engine.AFD_FSDK_InitialFaceEngine(FaceDB.appid, FaceDB.fd_key, AFD_FSDKEngine.AFD_OPF_0_HIGHER_EXT, 16, 5);
        Log.d(TAG, "AFD_FSDK_InitialFaceEngine = " + err.getCode());
        if (err.getCode() != AFD_FSDKError.MOK) {
            Toast.makeText(BindFaceActivity.this, "FD初始化失败，错误码：" + err.getCode(), Toast.LENGTH_SHORT).show();
        }
        err = engine.AFD_FSDK_GetVersion(version);
        Log.d(TAG, "AFD_FSDK_GetVersion =" + version.toString() + ", " + err.getCode());
        err = engine.AFD_FSDK_StillImageFaceDetection(data, mBitmap.getWidth(), mBitmap.getHeight(), AFD_FSDKEngine.CP_PAF_NV21, result);

        if (!result.isEmpty()) {
            AFR_FSDKVersion version1 = new AFR_FSDKVersion();
            AFR_FSDKEngine engine1 = new AFR_FSDKEngine();
            AFR_FSDKFace result1 = new AFR_FSDKFace();
            AFR_FSDKError error1 = engine1.AFR_FSDK_InitialEngine(FaceDB.appid, FaceDB.fr_key);
            Log.d("com.arcsoft", "AFR_FSDK_InitialEngine = " + error1.getCode());
            if (error1.getCode() != AFD_FSDKError.MOK) {
                Toast.makeText(BindFaceActivity.this, "FD初始化失败，错误码：" + error1.getCode(), Toast.LENGTH_SHORT).show();
            }
            error1 = engine1.AFR_FSDK_GetVersion(version1);
            Log.d("com.arcsoft", "FR=" + version.toString() + "," + error1.getCode()); //(210, 178 - 478, 446), degree = 1　780, 2208 - 1942, 3370
            error1 = engine1.AFR_FSDK_ExtractFRFeature(data, mBitmap.getWidth(), mBitmap.getHeight(), AFR_FSDKEngine.CP_PAF_NV21, new Rect(result.get(0).getRect()), result.get(0).getDegree(), result1);
            Log.d("com.arcsoft", "Face=" + result1.getFeatureData()[0] + "," + result1.getFeatureData()[1] + "," + result1.getFeatureData()[2] + "," + error1.getCode()+"<<<<<<<<<"+result.get(0).getDegree());
            mBitmap=null;
            if (error1.getCode() == error1.MOK) {
                mAFR_FSDKFace = result1.clone();
                Toast.makeText(BindFaceActivity.this, R.string.face_success, Toast.LENGTH_SHORT).show();
                savefaceinfo();
            } else {
                Toast.makeText(BindFaceActivity.this, R.string.none_face, Toast.LENGTH_SHORT).show();
            }
            error1 = engine1.AFR_FSDK_UninitialEngine();
            Log.d("com.arcsoft", "AFR_FSDK_UninitialEngine : " + error1.getCode());
        } else {
            Toast.makeText(BindFaceActivity.this, R.string.none_face, Toast.LENGTH_SHORT).show();
        }
        err = engine.AFD_FSDK_UninitialFaceEngine();
        Log.d(TAG, "AFD_FSDK_UninitialFaceEngine =" + err.getCode());

    }
    //提交人脸数据
    public void savefaceinfo() {
        try {
            File file = new File(this.getExternalCacheDir().getPath() + "/face.data");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fs = new FileOutputStream(this.getExternalCacheDir().getPath() + "/face.data", true);
            ExtOutputStream bos = new ExtOutputStream(fs);
            bos.writeBytes(mAFR_FSDKFace.getFeatureData());
            bos.close();
            fs.close();
            presenter.bindFace(deviceID, 1, faceBindBean.getData().getUserInfo().getPhone(), faceBindBean.getData().getUserInfo().getUserType(), Environment.getExternalStorageDirectory() + "/register.jpg",getExternalCacheDir().getPath() + "/face.data");
        } catch (Exception e) {
            e.printStackTrace();
        }}
    public void setActivtyChange(String string) {
        switch (string) {
            case "1":
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
                mTts.startSpeaking("请将人脸置于相机预览框，点击确定",mTtsListener);
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
                fingersign();
                mGLSurfaceView.stopPreview();
                mTts.startSpeaking("绑定完成",mTtsListener);
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

    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void sendLogSuccess(RestResponse resultResponse) {

    }
    Handler handler = new Handler();
    private void fingersign(){

        if (handler!=null) {

            handler.postDelayed(new Runnable() {

                @Override

                public void run() {
                    finish();

                }

            }, 3000);

        }

    }

    @Override
    public void onError(ApiException e) {
        String reg = "[^\u4e00-\u9fa5]";
        String syt=e.getMessage().replaceAll(reg, "");
        Logger.e("BindActivity"+syt);
        mTts.startSpeaking(syt,mTtsListener);
    }

    @Override
    public void onPermissionError(ApiException e) {

    }

    @Override
    public void onResultError(ApiException e) {
        String reg = "[^\u4e00-\u9fa5]";
        String syt=e.getMessage().replaceAll(reg, "");
        Logger.e("BindActivity"+syt);
        mTts.startSpeaking(syt,mTtsListener);
    }



    @Override
    public void onSuccess(Member memberInfo) {

    }

    @Override
    public void onBindFaceSuccess(FaceBindBean faceBindBean) {
        isFinish =true;
        faceRegister.setVisibility(View.GONE);
        setActivtyChange("4");
        memberInfo.setVisibility(View.VISIBLE);
        name.setText(faceBindBean.getData().getUserInfo().getName());
        name.setText(member.getMemberdata().getUserInfo().getName());
        tel.setText(member.getMemberdata().getUserInfo().getPhone());
        String userType = member.getMemberdata().getUserInfo().getUserType();
        if ("1".equals(userType)) {
            cardNum.setText(R.string.member);
        } else if ("2".equals(userType)) {
            cardNum.setText(R.string.employee);
        } else {
            cardNum.setText(R.string.coach);
        }
        if(faceBindBean.getData().getUserInfo().getSex()==0){
            tel.setText(R.string.man);
        }else {
            tel.setText(R.string.famele);
        }

    }
    FaceBindBean faceBindBean;
    @Override
    public void onFaceSuccess(FaceBindBean faceBindBean) {
        Logger.e("RegisterFragment_OnedeviceID");
        setActivtyChange("2");
        this.faceBindBean =faceBindBean;
        layout_bing_phone.setVisibility(View.INVISIBLE);
        memberInfo.setVisibility(View.VISIBLE);
        name.setText(faceBindBean.getData().getUserInfo().getName());
        tel.setText(faceBindBean.getData().getUserInfo().getPhone());
        int userType = faceBindBean.getData().getUserInfo().getUserType();
        mTts.startSpeaking("确认信息",mTtsListener);
        if (1==userType) {
            cardNum.setText(R.string.member);
        } else if (2==userType) {
            cardNum.setText(R.string.employee);
        } else {
            cardNum.setText(R.string.coach);
        }
        try {
            valueCout.setText(faceBindBean.getData().getMemberCardInfo().get(0).getCardName());
            valueDate.setText(faceBindBean.getData().getMemberCardInfo().get(0).getEndTime());
            cardNumber.setText(faceBindBean.getData().getMemberCardInfo().get(0).getCardNumber());
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAFR_FSDKFace = null;
        if(camera!=null){
            camera.release();
        }
        unregisterReceiver(mesReceiver);
    }
}
