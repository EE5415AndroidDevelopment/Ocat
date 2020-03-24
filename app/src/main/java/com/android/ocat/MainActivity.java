package com.android.ocat;

/**
 * Things Not Be Done Yet!!!
 * 1.   Person Center Activity: save preferences after user updating info
 * 2.
 */

/**
 * GitHub Test
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

        // request server connection
        String url = Constant.URL + Constant.LOGIN;
        RequestBody requestBody = new FormBody.Builder().add(Constant.EMAIL, email).add(Constant.PASSWORD,password).build();
        OkHttpUtil.post(url,requestBody, new MyCallBack(){
            @Override
            public void onFinish(String status, String json) {
                super.onFinish(status, json);

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

                    // welcome tip
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, "Welcome! "+serverResponse.getData().getUsername(), Toast.LENGTH_SHORT).show();
                    // activity jump
                            Intent intent = new Intent(MainActivity.this, AppBottomActivity.class);
                            startActivity(intent);
                            Looper.loop();

                } else {
                    // login fail
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, serverResponse.getMessages(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
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
