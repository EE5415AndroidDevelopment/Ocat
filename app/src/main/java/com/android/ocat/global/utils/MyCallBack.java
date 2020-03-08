package com.android.ocat.global.utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyCallBack implements Callback {
    private final String TAG = MyCallBack.class.getSimpleName();
    public String url;
    public String json;

    @Override
    public void onFailure(Call call, IOException e) {
        Log.d(TAG, "url: " + url);
        Log.d(TAG, "请求失败: " + e.toString());
        onFinish("failure", e.toString());

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Log.d(TAG, "url: " + url);
        json = response.body().string();
        Log.d(TAG, "请求成功: " + json);
        onFinish("success", json);
    }

    public void onFinish(String status, String json) {
        Log.d(TAG, "url: " + url + "&status: " + status);
    }
}
