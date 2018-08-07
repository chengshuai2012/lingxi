package com.retrofit2.converter.gson;

import com.link.cloud.BaseApplication;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.orhanobut.logger.Logger;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.bean.ResultResponse;
import com.link.cloud.bean.Voucher;
import com.link.cloud.utils.JsonUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Shaozy on 2016/8/14.
 */
final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    private static final String F_BODY = "body: %s";

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String body = value.string();

        if (BaseApplication.getInstance().log) {
            Logger.e(String.format(F_BODY, body));
        }
        int status;
        String msg;
        try {
            if (this.adapter.getClass().equals(RestResponse.class)) {
                //云通讯发送短信验证接口回掉
                return adapter.fromJson(body);
            } else {
                ResultResponse resultResponse = (ResultResponse) JsonUtil.toBean(body, ResultResponse.class);
                status = resultResponse.getStatus();
                msg = resultResponse.getMsg();
                if (status == 0) {
                    //result==0表示成功返回，继续用本来的Model类解析
                    return adapter.fromJson(body);
                } else {
                    if (this.adapter.getClass().equals(Voucher.class)) {
                        //消费接口需要特殊处理，页面需要展示
                        if (BaseApplication.DEBUG) Logger.e("消费失败也返回了");
                        return adapter.fromJson(body);
                    } else {
                        //ErrResponse 将msg解析为异常消息文本
                        throw new ResultException(status, msg);
                    }
                }
            }
        } finally {
            value.close();
        }
    }
}