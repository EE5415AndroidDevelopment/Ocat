package com.android.ocat;

/**
 * 总体
 * 未完成
 * 1.   APP美化（图标，背景，等）
 * 2.   输入格式合法性校验（纯数字，日期格式，）
 */

/**
 * Me
 * 未完成：
 * 1.   Person Center Activity:  添加修改密码功能
 */

/**
 *
 * Finance
 * 未完成
 *  1.  数据库时区问题
 *  2.  账号初始记录为初值
 *
 *  4.  悬浮按钮美化
 *  5.  应用顶部栏返回键实现返回上一级响应（困难）
 *  6.  汇率可以支持实时输入（困难）
 */

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.android.ocat.global.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity{
    private EditText emailEdit;
    private EditText passwordEdit;
    private TextView change;
    private String email;
    private String password;
    private String changeText;
    private static final int REQUEST_CODE = 1;
    private int count = 0;

    private Resources resources;
    private Configuration configuration;
    private DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        change = findViewById(R.id.userChange);
        changeText = change.getText().toString();

        // prepare for switching language
        resources = getResources();
        configuration = resources.getConfiguration();
        displayMetrics = resources.getDisplayMetrics();
    }

    public void onSignInClicked(View view) {
        // gain user input
        email = emailEdit.getText().toString();
        password = passwordEdit.getText().toString();
        System.out.println("++++++++++++++MainActivityClicked+++++++++++++++");

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.emailEmpty), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.passwordEmpty), Toast.LENGTH_LONG).show();
        } else {

            // request server connection
            String url = Constant.URL + Constant.LOGIN;
            RequestBody requestBody = new FormBody.Builder().add(Constant.EMAIL, email).add(Constant.PASSWORD,password).build();
            System.out.println("++++++++++++++++++Prepare Server Request+++++++++++++++++++=");
            OkHttpUtil.post(url,requestBody, new MyCallBack(){
                @Override
                public void onFinish(String status, String json) {
                    super.onFinish(status, json);

                    System.out.println("++++++++++++++++++Start Server Request+++++++++++++++++++=");
                    // parsing data
                    Gson gson = new Gson();
                    ServerResponse<User> serverResponse= gson.fromJson(json, new TypeToken<ServerResponse<User>>(){}.getType());
                    int statusCode = serverResponse.getStatus();

                    if (statusCode == 0) {
                        // login success
                        // save server response
                        SharedPreferenceUtil util = new SharedPreferenceUtil(Constant.FILE_NAME,MainActivity.this);
                        util.putBoolean(Constant.IS_SGININ, true);
                        util.putString(Constant.USER_JSON, gson.toJson(serverResponse.getData()));
                        util.putString("language", changeText);
                        util.putBoolean(Constant.REFRESH_NOW, false);

                        // welcome tip
                        Looper.prepare();
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.welcome)+serverResponse.getData().getUsername(), Toast.LENGTH_SHORT).show();
                        // activity jump
                                Intent intent = new Intent(MainActivity.this, AppBottomActivity.class);
                                startActivity(intent);
                                finish();
                                Looper.loop();
                    } else {
                        // login fail
                        Looper.prepare();
                        ToastUtil.createToast(MainActivity.this, statusCode);
                        Looper.loop();
                    }
                }
            });
        }
    }

    public void onSignUpClicked(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void onForgetClicked(View view) {
        Intent intent = new Intent(MainActivity.this, Forget1Activity.class);
        startActivity(intent);
    }

    public void onSwitchLanguageClicked(View view) {
        if (changeText.contains("ENGLISH")) {
            configuration.locale = Locale.US;
        } else if (changeText.contains("中文")) {
            configuration.locale = Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(configuration, displayMetrics);

        // reload main activity
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // display email after register activity
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                emailEdit.setText(data.getData().toString());
            }
        }
    }
}
