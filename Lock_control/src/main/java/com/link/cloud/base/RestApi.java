package com.link.cloud.base;

import android.content.Context;
import android.util.Base64;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.link.cloud.R;
import com.orhanobut.logger.Logger;
import com.retrofit2.converter.gson.GsonConverterFactory;
import com.link.cloud.BaseApplication;


import com.link.cloud.constant.Constant;
import com.link.cloud.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.HttpUrl;
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
 * Created by rj1 on 2016/8/26.
 */
public class RestApi {
    private static Context context;
    private static RestApi ourInstance;

    private BaseService baseService;
    private static OkHttpClient mOkHttpClient;

    public static RestApi getInstance() {
        context = BaseApplication.getInstance().getApplicationContext();
        if (ourInstance == null) {
            ourInstance = new RestApi();
        }
        return ourInstance;
    }


    private RestApi() {
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
            synchronized (RestApi.class) {
                if (mOkHttpClient == null) {
                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(BaseApplication.getInstance().getApplicationContext().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);
                    mOkHttpClient = new OkHttpClient.Builder()
                            .socketFactory(getSSLSocketFactory())
                            //.hostnameVerifier(getHostnameVerifier(new String[]{"app.cloopen.com"}))
                            .cache(cache)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(interceptor)
                            .connectTimeout(5, TimeUnit.SECONDS)
                            .readTimeout(5, TimeUnit.SECONDS)//设置读取超时时间
                            .writeTimeout(5, TimeUnit.SECONDS)//设置写入超时时间
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

            String accountSid = context.getString(R.string.ACCOUNT_SID);
            String authToken = context.getString(R.string.AUTH_TOKEN);
            String appId = context.getString(R.string.APPID);
            String templateId = context.getString(R.string.TEMPLATEID);
            String datetime = Utils.long2DateString(System.currentTimeMillis(), "yyyyMMddHHmmss");
            String sig = Utils.getMD5(accountSid + authToken + datetime);
            String authorization = Base64.encodeToString((accountSid + ":" + datetime).getBytes(), Base64.NO_WRAP);

            Request.Builder requestBuilder = original.newBuilder();
            requestBuilder.addHeader("Accept", "application/json");
            requestBuilder.addHeader("Content-Type", "application/json;charset=utf-8");
            requestBuilder.addHeader("Authorization", authorization);

            HttpUrl.Builder httpUrlBuilder = original.url().newBuilder();
            httpUrlBuilder.addQueryParameter("sig", sig);
            requestBuilder.url(httpUrlBuilder.build());

            JsonObject postBody = bodyToJsonObject(original.body());
            if (postBody == null) {
                postBody = new JsonObject();
            }
            postBody.addProperty("appId", appId);
            postBody.addProperty("templateId", templateId);

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

    protected SSLSocketFactory getSSLSocketFactory() {

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            Logger.e(e.getMessage());
            return null;
        }
        TrustManager[] trustManagers = new TrustManager[1];
        X509TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                if ((chain == null) || (chain.length == 0))
                    throw new IllegalArgumentException("null or zero-length certificate chain");
                if ((authType == null) || (authType.length() == 0))
                    throw new IllegalArgumentException("null or zero-length authentication type");

                boolean br = false;
                Principal principal = null;
                for (X509Certificate x509Certificate : chain) {
                    principal = x509Certificate.getSubjectX500Principal();
                    if (principal != null) {
                        br = true;
                        return;
                    }
                }
                if (!(br))
                    throw new CertificateException("服务端证书验证失败！");
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

        };
        trustManagers[0] = tm;
        try {
            sslContext.init(null, trustManagers, new SecureRandom());
        } catch (KeyManagementException e) {
            Logger.e(e.getMessage());
            return null;
        }
        return sslContext.getSocketFactory();
    }

    protected HostnameVerifier getHostnameVerifier(final String[] hostUrls) {

        HostnameVerifier TRUSTED_VERIFIER = new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                boolean ret = false;
                for (String host : hostUrls) {
                    if (host.equalsIgnoreCase(hostname)) {
                        ret = true;
                    }
                }
                return ret;
            }
        };

        return TRUSTED_VERIFIER;
    }

}
