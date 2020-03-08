package com.android.ocat.global.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class SendCodeUtil {
    private Context context;
    private String email;
    private String sendCodeMethod;
    private Button codeButton;
    private int statusCode;

    public SendCodeUtil(Context context, String email, String sendCodeMethod, Button codeButton) {
        this.email = email;
        this.sendCodeMethod = sendCodeMethod;
        this.codeButton = codeButton;
        this.context = context;
    }

    public void sendCode() {
        // request server connection
        String url = Constant.URL + Constant.SEND_CODE;
        RequestBody requestBody = new FormBody.Builder().add(Constant.EMAIL, email).add(Constant.CODE_METHOD, sendCodeMethod).build();
        OkHttpUtil.post(url,requestBody, new MyCallBack(){
            @Override
            public void onFinish(String status, String json) {

                // request server connection
                Gson gson = new Gson();
                ServerResponse<User> serverResponse = gson.fromJson(json, new TypeToken<ServerResponse<User>>() {}.getType());
                statusCode = serverResponse.getStatus();
                if (statusCode != 0) {
                    // sendCode failure
                    Looper.prepare();
                    Toast.makeText(context, serverResponse.getMessages(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
        if (statusCode == 0) {
            // sendCode success
            // tips
            Toast.makeText(context, R.string.sendCodeSuccess, Toast.LENGTH_LONG).show();
            // reset countDown
            new CountDown(Constant.PERIOD, codeButton).start();

        }

    }
}
