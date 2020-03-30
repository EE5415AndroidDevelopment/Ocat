package com.android.ocat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.CountDown;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.android.ocat.global.utils.SendCodeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class Forget2Activity extends AppCompatActivity {
    private String email;
    private Button codeButton;
    private TextView emailText;
    private EditText passwordEdit;
    private EditText password2Edit;
    private EditText codeEdit;
    private int statusCode;
    private SendCodeUtil sendCodeUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget2);

        // load previous activity data
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");

        emailText = findViewById(R.id.emailText);
        codeButton = findViewById(R.id.getCodeForget);
        codeEdit = findViewById(R.id.enterCodeForgetEdit);
        passwordEdit = findViewById(R.id.passwordForgetEdit);
        password2Edit = findViewById(R.id.password2ForgetEdit);

        // set TextView
        emailText.setText(email);
    }

    public void onSubmitClicked(View view) {
        // gain user input
        String password = passwordEdit.getText().toString();
        String password2 = password2Edit.getText().toString();
        String code = codeEdit.getText().toString();

        // check input validity
        // check null value
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)) {
            Toast.makeText(Forget2Activity.this, R.string.passwordNull, Toast.LENGTH_LONG).show();
        } else if (!password.equals(password2)) {
            Toast.makeText(Forget2Activity.this, R.string.passwordNotSame, Toast.LENGTH_LONG).show();
        } else if (password.length() < 6 || password2.length() < 6) {
            Toast.makeText(Forget2Activity.this, R.string.passwordTooShort, Toast.LENGTH_LONG).show();
        } else {
            // request server connection
            String url = Constant.URL + Constant.FORGET_2;
            RequestBody requestBody = new FormBody.Builder().add(Constant.EMAIL, email).add(Constant.PASSWORD, password).add(Constant.CODE, code).build();
            OkHttpUtil.post(url, requestBody, new MyCallBack() {
                @Override
                public void onFinish(String status, String json) {
                    super.onFinish(status, json);
                    // 解析数据
                    Gson gson = new Gson();
                    ServerResponse<User> serverResponse = gson.fromJson(json, new TypeToken<ServerResponse<User>>() {
                    }.getType());
                    int statusCode = serverResponse.getStatus();

                    if (statusCode == 0) {
                        Looper.prepare();
                        Toast.makeText(Forget2Activity.this, R.string.operationSuccess, Toast.LENGTH_LONG).show();
                        // 延时返回登陆界面
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(Forget2Activity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }, 200);
                        Looper.loop();

                    } else {
                        Looper.prepare();
                        Toast.makeText(Forget2Activity.this, serverResponse.getMessages(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }
            });
        }
    }

    public void onSendCodeClicked(View view) {
        // gain user input
        String password = passwordEdit.getText().toString();
        String password2 = password2Edit.getText().toString();
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)){
            Toast.makeText(Forget2Activity.this, R.string.passwordNull, Toast.LENGTH_LONG).show();
        } else if (!password.equals(password2)) {
            Toast.makeText(Forget2Activity.this, R.string.passwordNotSame, Toast.LENGTH_LONG).show();
        } else {
            sendCodeUtil = new SendCodeUtil(Forget2Activity.this, email, Constant.CODE_METHOD_OLD, codeButton);
            sendCodeUtil.sendCode();
        }
    }

    public void onCancelClicked(View view) {
        Intent intent = new Intent(Forget2Activity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
