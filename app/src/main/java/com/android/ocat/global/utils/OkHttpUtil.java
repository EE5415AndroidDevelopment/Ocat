package com.android.ocat.global.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtil {
    private static final HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15,TimeUnit.SECONDS)
            .readTimeout(15,TimeUnit.SECONDS)
            .addNetworkInterceptor(logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    // get
    public static void get(String url, MyCallBack callBack) {
        callBack.url = url;
        final Request request = new Request.Builder().url(url).build();
        CLIENT.newCall(request).enqueue(callBack);
    }

    // post
    public static void post(String url, RequestBody requestBody, MyCallBack callBack) {
        callBack.url = url;
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        CLIENT.newCall(request).enqueue(callBack);
    }
}
