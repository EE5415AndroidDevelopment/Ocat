package com.android.ocat.ui.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.android.ocat.global.utils.SendCodeUtil;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.android.ocat.global.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class PersonalInfoCenterActivity extends AppCompatActivity {
    private SharedPreferenceUtil sharedPreferenceUtil;
    private SendCodeUtil sendCodeUtil;
    private User user;
    private EditText meUsernameValueText;
    private EditText meEmailValueText;
    private EditText mePhoneValueText;
    private EditText enterCodeEdit;
    private Button codeButton;
    private String username;
    private String email;
    private String phone;
    private String code;
    private int statusCode;
    private String sendCodeMethod = Constant.CODE_METHOD_NEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_center);
        sharedPreferenceUtil = new SharedPreferenceUtil(Constant.FILE_NAME, PersonalInfoCenterActivity.this);
        meUsernameValueText = findViewById(R.id.meUsernameValueText);
        meEmailValueText = findViewById(R.id.meEmailValueText);
        mePhoneValueText = findViewById(R.id.mePhoneValueText);
        enterCodeEdit = findViewById(R.id.enterCodeEdit);
        codeButton = findViewById(R.id.getCode);

        meUsernameValueText.setLongClickable(false);
        meUsernameValueText.setTextIsSelectable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        user = (User) sharedPreferenceUtil.getObject(Constant.USER_JSON, User.class);
        meUsernameValueText.setText(user.getUsername());
        meEmailValueText.setText(user.getEmail());
        mePhoneValueText.setText(user.getPhone());
    }

    public void onSendCodeClicked(View view) {
        email = meEmailValueText.getText().toString();
        System.out.println(email);
        System.out.println(user.getEmail());
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(PersonalInfoCenterActivity.this, R.string.emailEmpty, Toast.LENGTH_LONG).show();
        } else {
            if (email.equals(user.getEmail())) {
                sendCodeMethod = Constant.CODE_METHOD_OLD;
            }
            sendCodeUtil = new SendCodeUtil(PersonalInfoCenterActivity.this, email, sendCodeMethod, codeButton);
            sendCodeUtil.sendCode();
        }
    }

    public void onSubmitClicked(View view) {
        email = meEmailValueText.getText().toString();
        username = meUsernameValueText.getText().toString();
        phone = mePhoneValueText.getText().toString();
        code = enterCodeEdit.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(PersonalInfoCenterActivity.this, R.string.emailEmpty, Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(code)) {
            Toast.makeText(PersonalInfoCenterActivity.this, R.string.codeEmpty, Toast.LENGTH_LONG).show();
        }else {
            String url = Constant.URL + Constant.UPDATE;
            RequestBody requestBody = new FormBody.Builder()
                    .add(Constant.USERNAME, username)
                    .add(Constant.EMAIL, email)
                    .add(Constant.PHONE, phone)
                    .add(Constant.CODE, code)
                    .add(Constant.CODE_METHOD, sendCodeMethod).build();
            OkHttpUtil.post(url, requestBody, new MyCallBack() {
                @Override
                public void onFinish(String status, String json) {
                    super.onFinish(status, json);

                    Gson gson = new Gson();
                    ServerResponse<User> serverResponse = gson.fromJson(json, new TypeToken<ServerResponse<User>>() {
                    }.getType());
                    statusCode = serverResponse.getStatus();

                    if (statusCode == 0) {
                        Looper.prepare();
                        Toast.makeText(PersonalInfoCenterActivity.this, R.string.operationSuccess, Toast.LENGTH_SHORT).show();
                        user.setUsername(username);
                        user.setEmail(email);
                        user.setPhone(phone);
                        sharedPreferenceUtil.putString(Constant.USER_JSON, gson.toJson(user));
                        finish();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        ToastUtil.createToast(PersonalInfoCenterActivity.this, statusCode);
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
