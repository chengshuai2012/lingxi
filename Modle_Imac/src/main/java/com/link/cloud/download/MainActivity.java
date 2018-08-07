package com.link.cloud.download;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

//1,在Manifest文件中注册Internet和读写SDCard的权限
//2,下载不能在主线程中进行，要开分线程
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button but1,but2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        but1=(Button)findViewById(R.id.but1);
//        but1.setOnClickListener(this);
//        but2=(Button)findViewById(R.id.but2);
//        but2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==but1){
            new downloadFileThread().start();
        }else if(v==but2){
            new downloadMP3Thread().start();
        }
    }

    class downloadFileThread extends Thread {
        public void run(){
            HttpDownloader httpDownloader=new HttpDownloader();
            String fileData=httpDownloader.downloadFiles("http://mystudy.bj.bcebos.com/AndroidDemo_009.xml");
            System.out.println(fileData);
        }
    }
    class downloadMP3Thread extends Thread {
        public void run(){
            HttpDownloader httpDownloader=new HttpDownloader();
            int downloadResult=httpDownloader.downloadFiles(
                    "http://fengkui.bj.bcebos.com/%E8%B6%B3%E9%9F%B3.mp3","BoBoMusic","足音.mp3");
            System.out.println("下载结果："+downloadResult);
        }
    }
}
