package com.link.cloud.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SilenceInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){

        }


        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            startApp(context);
        }


        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
        }
    }

    public void startApp(Context context){
        Intent resolveIntent = context.getPackageManager().getLaunchIntentForPackage("com.link.cloud");
        context.startActivity(resolveIntent);

    }
}