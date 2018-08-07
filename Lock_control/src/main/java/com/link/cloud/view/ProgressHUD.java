
package com.link.cloud.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.BaseApplication;


import com.link.cloud.utils.Utils;

import java.util.HashMap;

public class ProgressHUD extends Dialog implements Runnable {
    private static final int DISMISS_DELAY = 1000;

    private static HashMap<String, ProgressHUD> dialogMap;

    private static Handler mHandler;

    private OnProgressDismissListener listener;

    private ProgressBar progress;

    private ImageView iconImg;

    private TextView message;

    private ProgressHUD(Context context) {
        super(context, android.R.style.Animation_Translucent);
        getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(android.R.color.transparent)));
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

//        View rootView = getLayoutInflater().inflate(R.layout.dialog_hud, null);
//        setContentView(rootView);
//        progress = (ProgressBar) findViewById(R.id.DialogHud_Progress);
//        iconImg = (ImageView) findViewById(R.id.DialogHud_Icon);
//        message = (TextView) findViewById(R.id.message);
    }

    private static synchronized HashMap<String, ProgressHUD> getDialogMap() {
        if (dialogMap == null) {
            dialogMap = new HashMap<String, ProgressHUD>();
        }
        return dialogMap;
    }

    private static synchronized Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    public static ProgressHUD createHud(Context ctx) {
        if (ctx == null) {
            return null;
        }
        if (ctx instanceof Activity) {
            if (((Activity) ctx).getParent() != null) {
                ctx = ((Activity) ctx).getParent();
            }
            Window window = ((Activity) ctx).getWindow();
            if (window != null) {
                Object tag = window.getDecorView().getTag();
                if (tag != null && (tag instanceof ProgressHUD)) {
                    return (ProgressHUD) tag;
                } else {
                    ProgressHUD hud = new ProgressHUD(ctx);
                    window.getDecorView().setTag(hud);
                    return hud;
                }
            }
        } else {
            ProgressHUD hud = getDialogMap().get(ctx.getClass().getName());
            if (hud == null) {
                hud = new ProgressHUD(ctx);
                hud.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                getDialogMap().put(ctx.getClass().getName(), hud);
            }
            return hud;
        }
        return null;
    }

    @Override
    public void run() {
        if (listener != null) {
            listener.onProgressDismiss(this);
            listener = null;
        }
        getHandler().removeCallbacks(this);
        if (isShowing()) {
            dismiss();
        }
    }

    public static void showProgress(Context ctx, int resId) {
        if (ctx == null) {
            return;
        }
        showProgress(ctx, ctx.getString(resId));
    }

    public static void showProgress(Context ctx, String msg) {
        showProgress(ctx, msg, true);
    }

    public static void showProgress(Context ctx, int resId, boolean cancelable) {
        if (ctx == null) {
            return;
        }
        showProgress(ctx, ctx.getString(resId), cancelable);
    }

    public static void showProgress(Context ctx, String msg, boolean cancelable) {
        ProgressHUD hud = createHud(ctx);
        if (hud != null) {
            hud.setCancelable(cancelable);
            hud.setCanceledOnTouchOutside(cancelable);
            getHandler().removeCallbacks(hud);
            if (hud.isShowing()) {
                hud.dismiss();
            }
            if ((ctx instanceof Activity) && ((Activity) ctx).isFinishing()) {
                return;
            }
            if (!Utils.isEmpty(msg)) {
                hud.message.setText(msg);
            } else {
                hud.message.setText(BaseApplication.getInstance().getApplicationContext().getString(R.string.loading));
            }
            hud.progress.setVisibility(View.VISIBLE);
            hud.message.setVisibility(View.VISIBLE);
            hud.iconImg.setVisibility(View.GONE);
            hud.show();
        }
    }

    public static void dismissProgress(Context ctx, boolean success, int resId) {
        dismissProgress(ctx, success, ctx.getString(resId));
    }

    public static void dismissProgress(Context ctx, boolean success, int resId, OnProgressDismissListener listener) {
        dismissProgress(ctx, success, ctx.getString(resId), listener);
    }

    public static void dismissProgress(Context ctx) {
        ProgressHUD hud = createHud(ctx);
        if (hud != null) {
            getHandler().removeCallbacks(hud);
            if (hud.isShowing()) {
                hud.dismiss();
            }
        }
    }

    public static void dismissProgress(Context ctx, boolean success, String msg) {
        dismissProgress(ctx, success, msg, null);
    }

    public static void dismissProgress(Context ctx, boolean success, String msg, OnProgressDismissListener listener) {
        ProgressHUD hud = createHud(ctx);
        if (hud != null) {
            getHandler().removeCallbacks(hud);
            if (hud.isShowing()) {
                hud.dismiss();
            }
            if ((ctx instanceof Activity) && ((Activity) ctx).isFinishing()) {
                return;
            }
            if (Utils.isEmpty(msg)) {
                hud.message.setVisibility(View.GONE);
            } else {
                hud.message.setVisibility(View.VISIBLE);
                hud.message.setText(msg);
            }
            hud.progress.setVisibility(View.GONE);
            hud.iconImg.setVisibility(View.VISIBLE);
//            hud.iconImg.setImageResource(success ? R.drawable.icon_success
//                    : R.drawable.icon_failure);
            hud.listener = listener;
            hud.show();
            getHandler().postDelayed(hud, DISMISS_DELAY);
        }
    }

    public static boolean isShowing(Context ctx) {
        ProgressHUD hud = createHud(ctx);
        if (hud != null) {
            return hud.isShowing();
        }
        return false;
    }

    public static void setCanceledOnTouchOutside(Context ctx, boolean cancelAble) {
        ProgressHUD hud = createHud(ctx);
        if (hud != null) {
            hud.setCanceledOnTouchOutside(cancelAble);
        }
    }

    public interface OnProgressDismissListener {
        void onProgressDismiss(ProgressHUD dialog);
    }
}
