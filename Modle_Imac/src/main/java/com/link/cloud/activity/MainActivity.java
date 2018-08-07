package com.link.cloud.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.link.cloud.R;

import md.com.sdk.MicroFingerVein;


public class MainActivity extends Activity {
    Button button;
    EditText Text;
    ImageView fingerVeinImageView;
    private
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
    int run_type = 0;


    //创建handler
    Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Text.getText().append("open device failed\n\r");
                    break;
                case 1:
                    Text.getText().append("open device success\n\r");
                    break;
                case 2:
                    Text.getText().append("put your finger on device please\n\r");
                    break;
                case 3:
                    Text.getText().append("success:pos=" + pos[0] + ",score=" + score[0] + "\n\r");
                    break;
                case 4:
                    Text.getText().append("failed:ret=" + ret + ",pos=" + pos[0] + ",score=" + score[0] + "\n\r");
                    break;
                case 5:
                    Text.getText().append("firstly model:put your finger\n\r");
                    break;
                case 6:
                    Text.getText().append("secondly model:put your finger\n\r");
                    break;
                case 7:
                    Text.getText().append("thirdly model:put your finger\n\r");
                    break;
                case 8:
                    Text.getText().append("firstly model ok\n\r");
                    break;
                case 9:
                    Text.getText().append("secondly model ok\n\r");
                    break;
                case 10:
                    Text.getText().append("thirdly model ok\n\r");
                    break;
                case 11:
                    Text.getText().append("model ok\n\r");
                    break;
                case 12:
                    Text.getText().append("model start\n\r");
                    break;
                case 13:
                    Text.getText().append("model stop\n\r");
                    break;
                case 14:
                    Text.getText().append("identify start\n\r");
                    break;
                case 15:
                    Text.getText().append("identify stop\n\r");
                    break;
                default:
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("tag","启动");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        bindTaskContract=new BindTaskContract();
//        bindTaskContract.detachView();
//        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE };
//            requestPermissions(permission, 0);
//        }
        Permition.verifyStoragePermissions(this);//检验外部存储器访问权限
        Text = (EditText) findViewById(R.id.show_log);
        //认证
        button = (Button)findViewById(R.id.identify);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (run_type == 1) {
                    run_type = 0;
                    handler.sendEmptyMessage(15);
                } else {
                    run_type = 1;
                    handler.sendEmptyMessage(14);
                    Log.e("Identify", "put your finger on device please");
                    handler.sendEmptyMessage(2);
                }
            }
        });
        //建模
        findViewById(R.id.model).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (run_type == 2) {
                    run_type = 0;
                    handler.sendEmptyMessage(13);
                } else {
                    run_type = 2;
                    handler.sendEmptyMessage(12);
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
//        Text=(EditText)findViewById(R.id.show_log);
//        Log.e("MainActivity=========",Text.getText().toString()+"");
    }
//    protected void run()
    {
//        ret = MicroFingerVein.fvdevOpen();
        if (ret != true) {
            Log.i("open","failed");
            handler.sendEmptyMessage(0);
        } else {
            Log.i("open","success");
            handler.sendEmptyMessage(1);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (run_type == 1) {
//                        identify_process();
                    } else if (run_type == 2) {
                        model_process();
                        run_type = 0;
                        handler.sendEmptyMessage(13);
                    }
                }
            }
        }).start();
//        MicroFingerVein.fvdevClose();
    }
//    private void identify_process()
//    {
//        try {
//            Thread.sleep(30);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ret = MicroFingerVein.fvdevGetState(state);
//        if (ret != true) {
//            MicroFingerVein.fvdevOpen();
//            return;
//        }
//        if (state[0] != 3) {
//            return;
//        }
//
//        img1 = MicroFingerVein.fvdevGrabImage();
//        if (img1 == null) {
//            return;
//        }
//        ret = MicroFingerVein.fvSearchFeature(feauter1, 1, img1, pos, score);
//        if (ret == true && score[0] > 0.63) {
//            Log.e("Identify success,", "pos=" + pos[0] + ", score=" + score[0]);
//            handler.sendEmptyMessage(3);
//        } else {
//            Log.e("Identify failed,", "ret=" + ret + ",pos=" + pos[0] + ", score=" + score[0]);
//            handler.sendEmptyMessage(4);
//        }
//
//        while (state[0] == 3) {
//            MicroFingerVein.fvdevGetState(state);
//        }
//    }
    private void model_process()
    {
        //firstly
        Log.e("firstly model", "put your finger...");
        handler.sendEmptyMessage(5);
//        ret = first_model();
        if (ret != true) {
            return;
        }
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //secondly
        Log.e("secondly model", "put your finger...");
        handler.sendEmptyMessage(6);
//        ret = second_model();
        if (ret != true) {
            return;
        }
        //thirdly
        Log.e("third model", "put your finger...");
        handler.sendEmptyMessage(7);
//        ret = third_model();
        if (ret != true) {
            return;
        }
        Log.e("model", "ok");
        handler.sendEmptyMessage(11);
    }

//    private boolean first_model() {
//        while (run_type == 2) {
//            try {
//                Thread.sleep(30);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            ret = MicroFingerVein.fvdevGetState(state);
//            if (ret != true) {
//                MicroFingerVein.fvdevOpen();
//                continue;
//            }
//            if (state[0] != 0) {
//                img1 = MicroFingerVein.fvdevGrabImage();
//                if (img1 == null)
//                    continue;
//
//                feauter1 = MicroFingerVein.fvExtraceFeature(img1,null,null);
//                if (feauter1 != null) {
//                    Log.e("firstly model", "ok");
//                    handler.sendEmptyMessage(8);
//                    while (state[0] == 3) {//等待手指移开
//                        MicroFingerVein.fvdevGetState(state);
//                    }
//                    return true;
//                } else {
//                    while (state[0] == 3) {//等待手指移开,然后继续取图
//                        MicroFingerVein.fvdevGetState(state);
//                    }
//                }
//            }
//        }
//        return false;
//    }

//    private boolean second_model() {
//        while (run_type == 2) {
//            try {
//                Thread.sleep(30);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            ret = MicroFingerVein.fvdevGetState(state);
//            if (ret != true) {
//                MicroFingerVein.fvdevOpen();
//                continue;
//            }
//
//            if (state[0] == 0)
//                continue;
//
//            img2 = MicroFingerVein.fvdevGrabImage();
//            if (img2 == null)
//                continue;
//
//            ret = MicroFingerVein.fvSearchFeature(feauter1,1,img2, pos, score);
//            if (ret == true && score[0] > 0.4) {
//                feauter2 = MicroFingerVein.fvExtraceFeature(img2,null,null);
//                if (feauter2 != null) {
//                    Log.e("secondly model", "ok");
//                    handler.sendEmptyMessage(9);
//                    while (state[0] == 3) {
//                        MicroFingerVein.fvdevGetState(state);
//                    }
//                    return true;
//                }
//            }
//
//            while (state[0] == 3) {
//                MicroFingerVein.fvdevGetState(state);
//            }
//        }
//        return false;
//    }

//    private boolean third_model() {
//        while (run_type == 2)  {
//            try {
//                Thread.sleep(30);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            ret = MicroFingerVein.fvdevGetState(state);
//            if (ret != true) {
//                MicroFingerVein.fvdevOpen();
//                continue;
//            }
//
//            if (state[0] == 0)
//                continue;
//
//            img3 = MicroFingerVein.fvdevGrabImage();
//            if (img3 == null)
//                continue;
//
//            ret = MicroFingerVein.fvSearchFeature(feauter2,1,img3, pos, score);
//            if (ret == true && score[0] > 0.4) {
//                feauter = MicroFingerVein.fvExtraceFeature(img1, img2, img3);
//                Log.e("thirdly model", "ok");
//                handler.sendEmptyMessage(10);
//                while (state[0] == 3) {
//                    MicroFingerVein.fvdevGetState(state);
//                }
//                return true;
//            }
//
//            while (state[0] == 3) {
//                MicroFingerVein.fvdevGetState(state);
//            }
//        }
//        return false;
//    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //MicroFingerVein.fvdevClose();
    }
}
