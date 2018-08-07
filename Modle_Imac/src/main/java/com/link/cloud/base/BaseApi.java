package com.link.cloud.base;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.link.cloud.BaseApplication;
import com.link.cloud.constant.Constant;
import com.link.cloud.utils.Utils;
import com.orhanobut.logger.Logger;
import com.link.cloud.converter.gson.GsonConverterFactory;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by Shaozy on 2016/8/10.
 */

public class BaseApi {
    private static BaseApi ourInstance;
    private BaseService baseService;
    private static OkHttpClient mOkHttpClient;
    //短缓存有效期为1秒钟
    public static final int CACHE_STALE_SHORT = 1;
    //长缓存有效期为7天
    public static final int CACHE_STALE_LONG = 60 * 60 * 24 * 7;
    public static BaseApi getInstance() {
//        if (ourInstance == null)
            ourInstance = new BaseApi();
        return ourInstance;
    }
    private BaseApi() {
        initOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.REST_API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(BaseApplication.getInstance().gson))
                .client(mOkHttpClient)
                .build();
        this.baseService = retrofit.create(BaseService.class);
    }
    private void initOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {
            synchronized (BaseApi.class) {
                if (mOkHttpClient == null) {
                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(BaseApplication.getInstance().getApplicationContext().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);
                    mOkHttpClient = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(false)
                            .cache(cache)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(interceptor)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                            .writeTimeout(20, TimeUnit.SECONDS)//设置写入超时时间
                            .build();
                }
            }
        }
    }

    // 云端响应头拦截器，用来配置缓存策略
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        private static final String F_BREAK = " %n";
        private static final String F_URL = " %s";
        private static final String F_TIME = " in %.1fms";
        private static final String F_HEADERS = "%s";
        private static final String F_RESPONSE = F_BREAK + "Response: %d";
        private static final String F_BODY = "body: %s";

        private static final String F_BREAKER = F_BREAK + "-------------------------------------------" + F_BREAK;
        private static final String F_REQUEST_WITHOUT_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS;
        private static final String F_RESPONSE_WITHOUT_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BREAKER;
        private static final String F_REQUEST_WITH_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS + F_BODY + F_BREAK;
        private static final String F_RESPONSE_WITH_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BODY + F_BREAK + F_BREAKER;

        @Override
        public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                JsonObject postBody = bodyToJsonObject(original.body());
            if (postBody == null) {
                Logger.e(postBody+">>>>>>>>>>>>>>>>>>>>>>>>");
                return chain.proceed(original);
            }
                String code = "link";
                String datetime = System.currentTimeMillis() + "";
                String appkey = Utils.getMetaData(Constant.APP_KEY);
                String sign = Utils.generateSign(code, appkey, datetime);
                Request.Builder requestBuilder = original.newBuilder();

                postBody.addProperty("key", appkey);
                postBody.addProperty("datetime", datetime);
                postBody.addProperty("code", code);
                postBody.addProperty("sign", sign);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), postBody.toString());
                requestBuilder.post(requestBody);
                Request request = requestBuilder.build();

                //return chain.proceed(request);
                long t1 = System.nanoTime();
                Response originalResponse = chain.proceed(request);
                long t2 = System.nanoTime();
                double time = (t2 - t1) / 1e6d;

                if (BaseApplication.getInstance().log) {
                    if (request.method().equals("GET")) {
                        Logger.e(String.format("GET " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITHOUT_BODY, request.url(), time, request.headers(), originalResponse.code(), originalResponse.headers()));
                    } else if (request.method().equals("POST")) {
                        Logger.e(String.format("POST " + F_REQUEST_WITH_BODY + F_RESPONSE_WITHOUT_BODY, request.url(), time, request.headers(), postBody.toString(), originalResponse.code(), originalResponse.headers()));
                    } else if (request.method().equals("PUT")) {
                        Logger.e(String.format("PUT " + F_REQUEST_WITH_BODY + F_RESPONSE_WITHOUT_BODY, request.url(), time, request.headers(), postBody.toString(), originalResponse.code(), originalResponse.headers()));
                    } else if (request.method().equals("DELETE")) {
                        Logger.e(String.format("DELETE " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITHOUT_BODY, request.url(), time, request.headers(), originalResponse.code(), originalResponse.headers()));
                    }
                }


            return originalResponse;
        }

        private String stringifyRequestBody(Request request) {
            try {
                final Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            } catch (final IOException e) {
                return "did not work";
            }
        }

        public JsonObject bodyToJsonObject(final RequestBody request) {
            try {
                final RequestBody copy = request;
                final Buffer buffer = new Buffer();
                if (copy != null)
                    copy.writeTo(buffer);
                else
                    return null;
                return new JsonParser().parse(buffer.readUtf8()).getAsJsonObject();
            } catch (final Exception e) {
                return null;
            }
        }
    };

    public BaseService getBaseService() {
        return baseService;
    }
}
