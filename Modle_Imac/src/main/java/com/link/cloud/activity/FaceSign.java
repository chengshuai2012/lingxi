package com.link.cloud.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.ageestimation.ASAE_FSDKAge;
import com.arcsoft.ageestimation.ASAE_FSDKEngine;
import com.arcsoft.ageestimation.ASAE_FSDKError;
import com.arcsoft.ageestimation.ASAE_FSDKVersion;
import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKMatching;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.arcsoft.facetracking.AFT_FSDKEngine;
import com.arcsoft.facetracking.AFT_FSDKError;
import com.arcsoft.facetracking.AFT_FSDKFace;
import com.arcsoft.facetracking.AFT_FSDKVersion;
import com.arcsoft.genderestimation.ASGE_FSDKEngine;
import com.arcsoft.genderestimation.ASGE_FSDKError;
import com.arcsoft.genderestimation.ASGE_FSDKGender;
import com.arcsoft.genderestimation.ASGE_FSDKVersion;
import com.guo.android_extend.java.AbsLoop;
import com.guo.android_extend.tools.CameraHelper;
import com.guo.android_extend.widget.CameraFrameData;
import com.guo.android_extend.widget.CameraGLSurfaceView;
import com.guo.android_extend.widget.CameraSurfaceView;
import com.guo.android_extend.widget.CameraSurfaceView.OnCameraListener;
import com.link.cloud.BaseApplication;
import com.link.cloud.R;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.contract.MatchVeinTaskContract;
import com.link.cloud.core.BaseAppCompatActivity;
import com.link.cloud.utils.FaceDB;
import com.link.cloud.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by gqj3375 on 2017/4/28.
 */

public class FaceSign extends BaseAppCompatActivity implements OnCameraListener, View.OnTouchListener, Camera.AutoFocusCallback, MatchVeinTaskContract.MatchVeinView {
    private final String TAG = this.getClass().getSimpleName();
    @Bind(R.id.bind_one_Cimg)
    ImageView bind_one_Cimg;
    @Bind(R.id.bind_one_line)
    View bind_one_line;
    @Bind(R.id.bind_two_Cimg)
    ImageView bind_two_Cimg;
    @Bind(R.id.bind_one_tv)
    TextView bind_one_tv;
    @Bind(R.id.bind_two_tv)
    TextView bind_two_tv;
    @Bind(R.id.layout_page_title)
    TextView layoutPageTitle;
    @Bind(R.id.layout_page_time)
    TextView layoutPageTime;
    @Bind(R.id.face_back)
    LinearLayout faceBack;
    @Bind(R.id.surfaceView)
    CameraSurfaceView surfaceView;
    @Bind(R.id.glsurfaceView)
    CameraGLSurfaceView glsurfaceView;
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
    @Bind(R.id.card_number)
    TextView cardNumber;
    @Bind(R.id.confirm)
    TextView confirm;
    @Bind(R.id.confirm_layout)
    LinearLayout confirmLayout;
    @Bind(R.id.member_info)
    LinearLayout memberInfo;
    @Bind(R.id.sign_face_camera)
    LinearLayout sign_face_camera;
    private MesReceiver mesReceiver;
    private int mWidth, mHeight, mFormat;
    private CameraSurfaceView mSurfaceView;
    private CameraGLSurfaceView mGLSurfaceView;
    private Camera mCamera;
    String deviceId;
    public static CallBackValue callBackValue;
    MatchVeinTaskContract matchVeinTaskContract;
    AFT_FSDKVersion version = new AFT_FSDKVersion();
    AFT_FSDKEngine engine = new AFT_FSDKEngine();
    ASAE_FSDKVersion mAgeVersion = new ASAE_FSDKVersion();
    ASAE_FSDKEngine mAgeEngine = new ASAE_FSDKEngine();
    ASGE_FSDKVersion mGenderVersion = new ASGE_FSDKVersion();
    ASGE_FSDKEngine mGenderEngine = new ASGE_FSDKEngine();
    List<AFT_FSDKFace> result = new ArrayList<>();
    List<ASAE_FSDKAge> ages = new ArrayList<>();
    List<ASGE_FSDKGender> genders = new ArrayList<>();

    int mCameraID;
    int mCameraRotate;
    boolean mCameraMirror;
    byte[] mImageNV21 = null;
    FRAbsLoop mFRAbsLoop = null;
    AFT_FSDKFace mAFT_FSDKFace = null;
    Handler mHandler;
    boolean isPostted = false;

    Runnable hide = new Runnable() {
        @Override
        public void run() {
            isPostted = false;
        }
    };

    @Override
    public void signSuccess(Code_Message signedResponse) {
        setActivtyChange("2");
        sign_face_camera.setVisibility(View.GONE);
        memberInfo.setVisibility(View.VISIBLE);
        memberInfo.setVisibility(View.VISIBLE);
        name.setText(signedResponse.getData().getUserInfo().getName());
        tel.setText(signedResponse.getData().getUserInfo().getName());
        try {
            cardNum.setText(signedResponse.getData().getMemberCardInfo().get(0).getCardNumber());
            valueCout.setText(signedResponse.getData().getMemberCardInfo().get(0).getCardName());
            valueDate.setText(signedResponse.getData().getMemberCardInfo().get(0).getEndTime());
        } catch (Exception e) {

        }

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

    public void setActivtyChange(String string) {
        switch (string) {
            case "1":
                bind_one_Cimg.setImageResource(R.drawable.flow_circle_pressed);
                bind_one_line.setBackgroundResource(R.color.colorText);
                bind_one_tv.setTextColor(getResources().getColor(R.color.colorText));
                bind_two_Cimg.setImageResource(R.drawable.flow_circle);
                bind_two_tv.setTextColor(getResources().getColor(R.color.edittv));
                break;
            case "2":
                bind_one_Cimg.setImageResource(R.drawable.flow_circle);
                bind_one_line.setBackgroundResource(R.color.edittv);
                bind_one_tv.setTextColor(getResources().getColor(R.color.edittv));
                bind_two_Cimg.setImageResource(R.drawable.flow_circle_pressed);
                bind_two_tv.setTextColor(getResources().getColor(R.color.colorText));
                break;

        }
    }

    @Override
    public void checkInSuccess(Code_Message code_message) {

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

    class FRAbsLoop extends AbsLoop {
        AFR_FSDKVersion version = new AFR_FSDKVersion();
        AFR_FSDKEngine engine = new AFR_FSDKEngine();
        AFR_FSDKFace result = new AFR_FSDKFace();

        @Override
        public void setup() {
            AFR_FSDKError error = engine.AFR_FSDK_InitialEngine(FaceDB.appid, FaceDB.fr_key);
            Log.d(TAG, "AFR_FSDK_InitialEngine = " + error.getCode());
            error = engine.AFR_FSDK_GetVersion(version);
            Log.d(TAG, "FR=" + version.toString() + "," + error.getCode()); //(210, 178 - 478, 446), degree = 1　780, 2208 - 1942, 3370
        }

        @Override
        public void loop() {
            if (mImageNV21 != null) {
                AFR_FSDKError error = engine.AFR_FSDK_ExtractFRFeature(mImageNV21, mWidth, mHeight, AFR_FSDKEngine.CP_PAF_NV21, mAFT_FSDKFace.getRect(), mAFT_FSDKFace.getDegree(), result);
                Log.d(TAG, "Face=" + result.getFeatureData()[0] + "," + result.getFeatureData()[1] + "," + result.getFeatureData()[2] + "," + error.getCode());
                AFR_FSDKMatching score = new AFR_FSDKMatching();
                float max = 0.0f;
                String name = null;
                Log.e(TAG, "loop: " + ((BaseApplication) getApplicationContext().getApplicationContext()).mFaceDB.mFaceList.size());
                if (((BaseApplication) getApplicationContext().getApplicationContext()).mFaceDB.mFaceList.size() > 0) {
                    //是否识别成功(如果第一次没成功就再次循环验证一次)
                    for (Map.Entry<String, AFR_FSDKFace> entry : ((BaseApplication) getApplicationContext().getApplicationContext()).mFaceDB.mFaceList.entrySet()) {
                        error = engine.AFR_FSDK_FacePairMatching(result, entry.getValue(), score);
                        Log.d(TAG, "Score:" + score.getScore() + ", AFR_FSDK_FacePairMatching=" + error.getCode());
                        if (max < score.getScore()) {
                            max = score.getScore();
                            name =  entry.getKey();

                        }
                    }
                    if (max > 0.57f) {
                        SharedPreferences userInfo = getSharedPreferences("user_info", 0);
                        Log.d(TAG, "fit Score:" + max + ", NAME:" + name);
                        deviceId = userInfo.getString("deviceId", "");
                        matchVeinTaskContract.signedMember(deviceId, name, "face");
                    } else {
                        recindex = recindex + 1;
                    if (recindex == 3) {
                        //错误失败3次以上才提示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FaceSign.this,"未识别",Toast.LENGTH_SHORT).show();
                            }
                        });

                        recindex = 0;
                    }
                }
            } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FaceSign.this,"无人脸数据",Toast.LENGTH_SHORT).show();
                        }
                    });

            }
                mImageNV21 = null;
            }
        }

        @Override
        public void over() {
            AFR_FSDKError error = engine.AFR_FSDK_UninitialEngine();
            Log.d(TAG, "AFR_FSDK_UninitialEngine : " + error.getCode());
        }

    }
int recindex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mCameraID = getIntent().getIntExtra("Camera", 0) == 0 ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
        mCameraRotate = 0;
        mCameraMirror = false;
        mWidth = 640;
        mHeight = 480;
        mFormat = ImageFormat.NV21;
        mHandler = new Handler();
        mGLSurfaceView = (CameraGLSurfaceView) findViewById(R.id.glsurfaceView);
        mGLSurfaceView.setOnTouchListener(this);
        mSurfaceView = (CameraSurfaceView) findViewById(R.id.surfaceView);
        mSurfaceView.setOnCameraListener(this);
        mSurfaceView.setupGLSurafceView(mGLSurfaceView, true, mCameraMirror, mCameraRotate);
        mSurfaceView.debug_print_fps(true, false);

        layoutPageTitle.setText(getResources().getString(R.string.face_sign));
        AFT_FSDKError err = engine.AFT_FSDK_InitialFaceEngine(FaceDB.appid, FaceDB.ft_key, AFT_FSDKEngine.AFT_OPF_0_HIGHER_EXT, 16, 5);
        Log.d(TAG, "AFT_FSDK_InitialFaceEngine =" + err.getCode());
        err = engine.AFT_FSDK_GetVersion(version);
        Log.d(TAG, "AFT_FSDK_GetVersion:" + version.toString() + "," + err.getCode());

        ASAE_FSDKError error = mAgeEngine.ASAE_FSDK_InitAgeEngine(FaceDB.appid, FaceDB.ag_key);
        Log.d(TAG, "ASAE_FSDK_InitAgeEngine =" + error.getCode());
        error = mAgeEngine.ASAE_FSDK_GetVersion(mAgeVersion);
        Log.d(TAG, "ASAE_FSDK_GetVersion:" + mAgeVersion.toString() + "," + error.getCode());

        ASGE_FSDKError error1 = mGenderEngine.ASGE_FSDK_InitgGenderEngine(FaceDB.appid, FaceDB.sx_key);
        Log.d(TAG, "ASGE_FSDK_InitgGenderEngine =" + error1.getCode());
        error1 = mGenderEngine.ASGE_FSDK_GetVersion(mGenderVersion);
        Log.d(TAG, "ASGE_FSDK_GetVersion:" + mGenderVersion.toString() + "," + error1.getCode());
        ((BaseApplication) getApplicationContext().getApplicationContext()).mFaceDB.loadFaces();
        mFRAbsLoop = new FRAbsLoop();
        mFRAbsLoop.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_face;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {
        matchVeinTaskContract = new MatchVeinTaskContract();
        matchVeinTaskContract.attachView(this);
        mesReceiver=new MesReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NewMainActivity.ACTION_UPDATEUI);
        registerReceiver(mesReceiver, intentFilter);
    }
    @OnClick({ R.id.confirm,R.id.face_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                finish();
               break;
            case R.id.face_back:
               finish();
                break;


        }
    }
    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mFRAbsLoop.shutdown();
        AFT_FSDKError err = engine.AFT_FSDK_UninitialFaceEngine();
        Log.d(TAG, "AFT_FSDK_UninitialFaceEngine =" + err.getCode());

        ASAE_FSDKError err1 = mAgeEngine.ASAE_FSDK_UninitAgeEngine();
        Log.d(TAG, "ASAE_FSDK_UninitAgeEngine =" + err1.getCode());
        unregisterReceiver(mesReceiver);
        ASGE_FSDKError err2 = mGenderEngine.ASGE_FSDK_UninitGenderEngine();
        Log.d(TAG, "ASGE_FSDK_UninitGenderEngine =" + err2.getCode());
    }

    @Override
    public Camera setupCamera() {
        // TODO Auto-generated method stub
        mCamera = Camera.open(mCameraID);
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
        AFT_FSDKError err = engine.AFT_FSDK_FaceFeatureDetect(data, width, height, AFT_FSDKEngine.CP_PAF_NV21, result);
        Log.d(TAG, "AFT_FSDK_FaceFeatureDetect =" + err.getCode());
        Log.d(TAG, "Face=" + result.size());
        for (AFT_FSDKFace face : result) {
            Log.d(TAG, "Face:" + face.toString());
        }
        if (mImageNV21 == null) {
            if (!result.isEmpty()) {
                mAFT_FSDKFace = result.get(0).clone();
                mImageNV21 = data.clone();
            } else {
                if (!isPostted) {
                    mHandler.removeCallbacks(hide);
                    mHandler.postDelayed(hide, 2000);
                    isPostted = true;
                }
            }
        }
        //copy rects
        Rect[] rects = new Rect[result.size()];
        for (int i = 0; i < result.size(); i++) {
            rects[i] = new Rect(result.get(i).getRect());
        }
        //clear result.
        result.clear();
        //return the rects for render.
        return rects;
    }

    @Override
    public void onBeforeRender(CameraFrameData data) {

    }

    @Override
    public void onAfterRender(CameraFrameData data) {
        mGLSurfaceView.getGLES2Render().draw_rect((Rect[]) data.getParams(), Color.GREEN, 2);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CameraHelper.touchFocus(mCamera, event, v, this);
        return false;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            Log.d(TAG, "Camera Focus SUCCESS!");
        }
    }


}