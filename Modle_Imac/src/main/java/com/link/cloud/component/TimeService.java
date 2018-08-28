package com.link.cloud.component;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.link.cloud.BaseApplication;
import com.link.cloud.activity.WelcomeActivity;

/**
 * Created by OFX002 on 2018/4/24.
 */

public class TimeService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(BaseApplication.COUNT_CHANGE);
        registerReceiver(receiver,filter);
        return START_STICKY;
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("onReceive: ","count"+count );
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)&&count==0) {
                Intent sayHelloIntent = new Intent(context, WelcomeActivity.class);
                sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(sayHelloIntent);
                android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
            }
            if(intent.getAction().equals(BaseApplication.COUNT_CHANGE)){
                count=intent.getIntExtra("count",0);
            }
        }

    };
    int count =0;
}
