package com.link.cloud.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.link.cloud.R;
import com.link.cloud.BaseApplication;

import com.link.cloud.TestActivityManager;
import com.link.cloud.view.ProgressHUD;

/**
 * Created by Administrator on 2017/5/8.
 */

public class MyToast {
    private Toast mToast;
    private TextView textView;
    private MyToast(Context context,CharSequence text,int duration){
       View view= LayoutInflater.from(context).inflate(R.layout.toast_er_layout,null);
        textView=(TextView)view.findViewById(R.id.error_password);
        textView.setText(text);
        mToast=new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(view);

   }
    public static MyToast maketext(Context context,CharSequence text,int duration){
        return new MyToast(context,text,duration);
    }
    public void show(){
        if (mToast!=null){
            mToast.show();
        }
    }
    public void setGravity(int gravity, int xOffset, int yOffset){
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }
    /**
     * ProgressHUD
     *
     * @param obj 不定参数，约定obj[0]为Boolean类型，obje[1]为String类型
     */
    public void showProgress(boolean show, Object... obj) {
        if (show) {
            boolean cancelable = false;
            String msg = BaseApplication.getInstance().getApplicationContext().getString(R.string.loading);
            if (obj != null && obj.length > 0) {
                cancelable = (boolean) obj[0];
                msg = (String) obj[1];
            }
            ProgressHUD.showProgress(TestActivityManager.getInstance().getCurrentActivity(), msg, cancelable);
        } else {
            if (obj != null && obj.length > 0) {
                boolean success = (boolean) obj[0];
                String msg = (String) obj[1];
                ProgressHUD.dismissProgress(TestActivityManager.getInstance().getCurrentActivity(), success, msg);
            } else {
                ProgressHUD.dismissProgress(TestActivityManager.getInstance().getCurrentActivity());
            }
        }
    }
}
