package com.link.cloud.base;

import android.content.Context;
import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.link.cloud.R;
import com.orhanobut.logger.Logger;
import com.retrofit2.converter.gson.ResultException;

import com.link.cloud.utils.NetUtil;
import com.link.cloud.BaseApplication;


import org.json.JSONException;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by rj1 on 2016/8/14.
 */
public abstract class AbsAPICallback<T> extends Subscriber<T> {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    //出错提示
    private final String networkMsg;
    private final String noResponseMsg;
    private final String parseMsg;
    private final String unknownMsg;
    private final String socketMsg;

    protected AbsAPICallback() {
        Context context = BaseApplication.getInstance().getApplicationContext();
        String networkMsg = context.getString(R.string.error_network_diable);
        String noResponseMsg = context.getString(R.string.error_internal_server);
        String parseMsg = context.getString(R.string.error_response_parse);
        String unknownMsg = context.getString(R.string.error_unkown);
        String socketMsg=context.getString(R.string.error_socket_diable);
        this.networkMsg = networkMsg;
        this.noResponseMsg = noResponseMsg;
        this.parseMsg = parseMsg;
        this.unknownMsg = unknownMsg;
        this.socketMsg=socketMsg;
    }

    protected AbsAPICallback(String networkMsg, String noResponseMsg, String parseMsg, String unknownMsg,String socketMsg) {
        this.networkMsg = networkMsg;
        this.noResponseMsg = noResponseMsg;
        this.parseMsg = parseMsg;
        this.unknownMsg = unknownMsg;
        this.socketMsg=socketMsg;
    }


    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }

        ApiException ex;
        //Logger.e("AbsAPICallback:" + e.getMessage() + ";instanceof:" + e.getClass().getName());
        if (e != null && e.getClass().getName().equals("libcore.io.GaiException")) {
            if (!NetUtil.isNetworkAvailable()) {
                ex = new ApiException(e, BAD_GATEWAY);
                ex.setDisplayMessage(networkMsg);
                onError(ex);
                Logger.e("网络错误:"+ex.getMessage());
            } else {
                ex = new ApiException(e, ApiException.UNKNOWN);
                ex.setDisplayMessage(unknownMsg + ":" + e.getMessage());          //未知错误
                onError(ex);
                Logger.e("错误1:"+ex.getMessage());
            }
        } else if (e instanceof UnknownHostException) {
            if (!NetUtil.isNetworkAvailable()) {
                ex = new ApiException(e, BAD_GATEWAY);
                ex.setDisplayMessage(networkMsg);
                onError(ex);
                Logger.e("网络错误:"+ex.getMessage());
            } else {
                ex = new ApiException(e, BAD_GATEWAY);
                ex.setDisplayMessage(noResponseMsg);
                onError(ex);
                Logger.e("错误2:"+ex.getMessage());
            }
        } else if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException)e;
            ex = new ApiException(e, httpException.code());
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                    onPermissionError(ex);          //权限错误，需要实现
                    Logger.e("权限错误:"+ex.getMessage());
                    break;
                case NOT_FOUND:
                    onError(ex);
                    Logger.e("网络错误:"+ex.getMessage());
                    break;
                case REQUEST_TIMEOUT:
                    onError(ex);
                    Logger.e("网络请求超时:"+ex.getMessage());
                    break;
                case GATEWAY_TIMEOUT:
                    onError(ex);
                    Logger.e("网络错误:"+ex.getMessage());
                    break;
                case INTERNAL_SERVER_ERROR:
                    onError(ex);
                    Logger.e("网络错误:"+ex.getMessage());
                    break;
                case BAD_GATEWAY:
                    onError(ex);
                    Logger.e("网络错误:"+ex.getMessage());
                    break;
                case SERVICE_UNAVAILABLE:
                    onError(ex);
                    Logger.e("网络错误:"+ex.getMessage());
                    break;
                default:
                    ex.setDisplayMessage(networkMsg);  //均视为网络错误
                    onError(ex);
                    Logger.e("网络错误:"+ex.getMessage());
                    break;
            }
        } else if (e instanceof ResultException) {    //服务器返回的错误
            ResultException resultException = (ResultException) e;
            ex = new ApiException(resultException, resultException.getErrCode());
            ex.setDisplayMessage(resultException.getMessage());
            onResultError(ex);
            Logger.e("服务器错误:"+ex.getMessage());
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, ApiException.PARSE_ERROR);
            ex.setDisplayMessage(parseMsg);            //均视为解析错误
            onError(ex);
            Logger.e("解析错误:"+ex.getMessage());
        }else if(e instanceof SocketTimeoutException ||e instanceof SocketException) {
            ex = new ApiException(e, ApiException.SOCKET_ERROR);
            ex.setDisplayMessage(socketMsg);  //均视为网络错误
            onError(ex);
            Logger.e("Socket连接异常:" + ex.getMessage());
        } else {
            ex = new ApiException(e, ApiException.UNKNOWN);
            ex.setDisplayMessage(unknownMsg + ":" + e.getMessage());          //未知错误
            onError(ex);
            Logger.e("错误3:"+ex.getMessage());
        }
    }


    /**
     * 错误回调
     */
    protected abstract void onError(ApiException e);

    /**
     * 权限错误，需要实现重新登录操作
     */
    protected abstract void onPermissionError(ApiException e);

    /**
     * 服务器返回的错误
     */
    protected abstract void onResultError(ApiException e);

    @Override
    public void onCompleted() {
    }

}
