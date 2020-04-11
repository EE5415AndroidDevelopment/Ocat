package com.android.ocat.global.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtil {
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15,TimeUnit.SECONDS)
            .readTimeout(15,TimeUnit.SECONDS)
            .build();

    // get请求
    public static void get(String url, MyCallBack callBack) {
        callBack.url = url;
        final Request request = new Request.Builder().url(url).build();
        CLIENT.newCall(request).enqueue(callBack);
    }

    // post请求
    public static void post(String url, RequestBody requestBody, MyCallBack callBack) {
        System.out.println("++++++++++++++++++++++++++Post请求+++++++++++++++++++++++++++++++");
        callBack.url = url;
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        CLIENT.newCall(request).enqueue(callBack);
    }
}
