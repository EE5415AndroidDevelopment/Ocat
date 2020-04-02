package com.android.ocat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.CountDown;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.android.ocat.global.utils.SendCodeUtil;
import com.android.ocat.global.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class RegisterActivity extends AppCompatActivity {
    private Button codeButton;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private EditText password2Edit;
    private EditText emailEdit;
    private EditText codeEdit;
    //    EditText phoneEdit;
    private String username;
    private SendCodeUtil sendCodeUtil;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        codeButton = findViewById(R.id.getCode);
        usernameEdit = findViewById(R.id.usernameEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        password2Edit = findViewById(R.id.password2Edit);
        emailEdit = findViewById(R.id.emailEdit);
        codeEdit = findViewById(R.id.enterCodeEdit);
//        phoneEdit = findViewById(R.id.phoneEdit);
    }

    public void onSubmitClicked(View view) {
        // gain user input
        username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String password2 = password2Edit.getText().toString();
        final String email = emailEdit.getText().toString();
        String code = codeEdit.getText().toString();
//                String phone = phoneEdit.getText().toString();

        // check input validity
        // check null value
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password2) || TextUtils.isEmpty(email) || TextUtils.isEmpty(code)) {
            Toast.makeText(RegisterActivity.this, R.string.somethingNull, Toast.LENGTH_LONG).show();
        } else if (!password.equals(password2)) {
            // check password consistency
            Toast.makeText(RegisterActivity.this, R.string.passwordNotSame, Toast.LENGTH_LONG).show();
        } else {
            // request server connection
            String url = Constant.URL + Constant.REGISTER;
            RequestBody requestBody = new FormBody.Builder().add(Constant.USERNAME, username).add(Constant.PASSWORD, password).add(Constant.EMAIL, email).add(Constant.CODE, code).build();
            OkHttpUtil.post(url, requestBody, new MyCallBack() {
                @Override
                public void onFinish(String status, String json) {
                    super.onFinish(status, json);

                    // parsing data
                    Gson gson = new Gson();
                    ServerResponse<User> serverResponse= gson.fromJson(json, new TypeToken<ServerResponse<User>>(){}.getType());
                    int statusCode = serverResponse.getStatus();

                    if (statusCode == 0) {
                        // register success
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, R.string.operationSuccess, Toast.LENGTH_LONG).show();
                        Intent data = new Intent();
                        data.setData(Uri.parse(email));
                        setResult(RESULT_OK, data);
                        finish();
                        Looper.loop();

                    } else {
                        // register fail
                        Looper.prepare();
                        ToastUtil.createToast(RegisterActivity.this, statusCode);
                        Looper.loop();
                    }
                }
            });
        }
    }

    public void onSendCodeClicked(View view) {
        // gain user input
        String email = emailEdit.getText().toString();

        // check input validity
        // check null value
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, R.string.emailEmpty, Toast.LENGTH_LONG).show();
        } else {
            sendCodeUtil = new SendCodeUtil(RegisterActivity.this, email, Constant.CODE_METHOD_NEW, codeButton);
            sendCodeUtil.sendCode();
        }
    }

    public void onCancelClicked(View view) {
        finish();
    }
}
