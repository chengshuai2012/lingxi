package com.link.cloud.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
import com.guo.android_extend.java.ExtOutputStream;
import com.link.cloud.R;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.FaceBindBean;
import com.link.cloud.bean.Member;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.contract.RegisterTaskContract;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.core.BaseAppCompatActivity;
import com.link.cloud.utils.FaceDB;
import com.link.cloud.utils.Utils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 49488 on 2018/7/27.
 */

public class BindFaceActivity extends BaseAppCompatActivity implements CallBackValue, RegisterTaskContract.RegisterView, SendLogMessageTastContract.sendLog {
    private MesReceiver mesReceiver;
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
    SurfaceView svCameraSurfaceview;
    @Bind(R.id.take_photo)
    TextView takePhoto;
    @Bind(R.id.introduce_face)
    LinearLayout introduceFace;
    @Bind(R.id.face_register)
    LinearLayout faceRegister;
    private SurfaceView sv_camera_surfaceview;
    private Camera camera;
    private AFR_FSDKFace mAFR_FSDKFace;
    protected int getLayoutId() {
        return R.layout.activity_bind_face;

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        etPhoneNum.setInputType(InputType.TYPE_NULL);
        layoutPageTitle.setText(getResources().getString(R.string.face_bind));
        this.presenter = new RegisterTaskContract();
        this.presenter.attachView(this);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

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

                if (Utils.isEmpty(phoneNum) || !Utils.isMobileNumberValid(phoneNum)) {
                    Toast.makeText(this, "手机号不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                userInfo = getSharedPreferences("user_info", 0);
                deviceID = userInfo.getString("deviceId", "");
                numberType = userInfo.getInt("numberType", 0);
                presenter.getMemInfo(deviceID, 1, phoneNum);
                break;
            case R.id.face_back:
                finish();
                break;
            case R.id.confirm:
                if(!isFinish){
                setActivtyChange("3");
                faceRegister.setVisibility(View.VISIBLE);
                memberInfo.setVisibility(View.GONE);
                svCameraSurfaceview.getHolder().addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                        //打开照相机
                        camera = Camera.open();
                        //给照相机设置参数
                        Camera.Parameters parameters=camera.getParameters();
                        //设置保存格式
                        parameters.setPictureFormat(PixelFormat.JPEG);
                        //设置质量
                        parameters.set("jpeg-quality",85);
                        //给照相机设置参数
                        camera.setParameters(parameters);
                        //将照相机捕捉的画面展示到SurfaceView
                        try {
                            camera.setPreviewDisplay(svCameraSurfaceview.getHolder());
                            //开启预览
                            camera.startPreview();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                    }
                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {

                    }
                });}else {
                        finish();
                    }

        break;
            case R.id.take_photo:

                    camera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            //将字节数组
                            Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                            saveData(bitmap);
                            try {
                                File file= new File(Environment.getExternalStorageDirectory()+"/register.jpg");
                                if(file.exists()){
                                    file.delete();
                                }
                                FileOutputStream fileOutputStream=new FileOutputStream(file.getAbsolutePath());
                                bitmap.compress(Bitmap.CompressFormat.JPEG,85,fileOutputStream);
                                camera.stopPreview();
                                camera.startPreview();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });




                break;

        }
    }
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
            Log.d("com.arcsoft", "Face=" + result1.getFeatureData()[0] + "," + result1.getFeatureData()[1] + "," + result1.getFeatureData()[2] + "," + error1.getCode());
            mBitmap=null;
            if (error1.getCode() == error1.MOK) {
                mAFR_FSDKFace = result1.clone();
                Toast.makeText(BindFaceActivity.this, "人脸识别成功", Toast.LENGTH_SHORT).show();
                savefaceinfo();
            } else {
                Toast.makeText(BindFaceActivity.this, "人脸特征无法检测，请换一张图片", Toast.LENGTH_SHORT).show();
            }
            error1 = engine1.AFR_FSDK_UninitialEngine();
            Log.d("com.arcsoft", "AFR_FSDK_UninitialEngine : " + error1.getCode());
        } else {
            Toast.makeText(BindFaceActivity.this, "没有检测到人脸，请换一张图片", Toast.LENGTH_SHORT).show();
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
            presenter.bindFace(deviceID, 1, member.getMemberdata().getUserInfo().getPhone(), Integer.parseInt(member.getMemberdata().getUserInfo().getUserType()), Environment.getExternalStorageDirectory() + "/register.jpg",getExternalCacheDir().getPath() + "/face.data");
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

    @Override
    public void onError(ApiException e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionError(ApiException e) {

    }

    @Override
    public void onResultError(ApiException e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(Member member) {
        Logger.e("RegisterFragment_OnedeviceID");
        this.member = member;
        setActivtyChange("2");
        layout_bing_phone.setVisibility(View.INVISIBLE);
        memberInfo.setVisibility(View.VISIBLE);
        name.setText(member.getMemberdata().getUserInfo().getName());
        tel.setText(member.getMemberdata().getUserInfo().getPhone());
        try {
            cardNum.setText(member.getMemberdata().getMemberCard().getCardNumber());
            valueCout.setText(member.getMemberdata().getMemberCard().getCardName());
            valueDate.setText(member.getMemberdata().getMemberCard().getEndTime());
        } catch (Exception e) {
        }

    }

    @Override
    public void onBindFaceSuccess(FaceBindBean faceBindBean) {
        isFinish =true;
        faceRegister.setVisibility(View.GONE);
        setActivtyChange("4");
        memberInfo.setVisibility(View.VISIBLE);
        name.setText(faceBindBean.getData().getUserInfo().getName());
        if(faceBindBean.getData().getUserInfo().getSex()==0){
            tel.setText("男");
        }else {
            tel.setText("女");
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAFR_FSDKFace = null;
        unregisterReceiver(mesReceiver);
    }
}
