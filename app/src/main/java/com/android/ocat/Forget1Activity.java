package com.android.ocat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class Forget1Activity extends AppCompatActivity {
    private EditText emailEdit;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget1);

        emailEdit = findViewById(R.id.usernameForgetEdit);
    }

    public void onSubmitClicked(View view) {
        // gain user input
        email = emailEdit.getText().toString();

        // check input validity
        // check null value
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Forget1Activity.this, R.string.emailEmpty, Toast.LENGTH_LONG).show();
        } else {
            // request server connection
            String url = Constant.URL +Constant.FORGET_1;
            RequestBody requestBody = new FormBody.Builder().add(Constant.EMAIL, email).build();
            OkHttpUtil.post(url, requestBody, new MyCallBack(){
                @Override
                public void onFinish(String status, String json) {
                    super.onFinish(status, json);

                    // parsing data
                    Gson gson = new Gson();
                    ServerResponse<User> serverResponse= gson.fromJson(json, new TypeToken<ServerResponse<User>>(){}.getType());
                    int statusCode = serverResponse.getStatus();

                    if (statusCode == 0) {
                        // check account success
                        Intent intent = new Intent(Forget1Activity.this, Forget2Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.EMAIL, email);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        // failure
                        Looper.prepare();
                        Toast.makeText(Forget1Activity.this, getResources().getString(R.string.emailNotExists), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }
            });
        }

    }

    public void onCancelClicked(View view) {
        finish();
    }
}

