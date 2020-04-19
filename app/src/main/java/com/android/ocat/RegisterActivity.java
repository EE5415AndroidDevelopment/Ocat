package com.android.ocat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.CountDown;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
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
    private static final int MESSAGE_CODE = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_CODE:
                    Button temp = (Button) msg.obj;
                    Toast.makeText(RegisterActivity.this, R.string.sendCodeSuccess, Toast.LENGTH_LONG).show();
                    // reset countDown
                    new CountDown(Constant.PERIOD, temp).start();

            }
        }
    };


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
        String username = usernameEdit.getText().toString();
        final String password = passwordEdit.getText().toString();
        String password2 = password2Edit.getText().toString();
        final String email = emailEdit.getText().toString();
        String code = codeEdit.getText().toString();
        if (TextUtils.isEmpty(username)) {
            playSound();
            animationShake(usernameEdit);
            Toast.makeText(RegisterActivity.this, R.string.somethingNull, Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)){
            playSound();
            animationShake(passwordEdit);
            animationShake(password2Edit);
            Toast.makeText(RegisterActivity.this, R.string.somethingNull, Toast.LENGTH_LONG).show();
        } else if (password.length() < 6 || password2.length() < 6) {
            playSound();
            animationShake(passwordEdit);
            animationShake(password2Edit);
            Toast.makeText(RegisterActivity.this, R.string.passwordTooShort, Toast.LENGTH_LONG).show();
        } else if (!password.equals(password2)) {
            // check password consistency
            playSound();
            animationShake(passwordEdit);
            animationShake(password2Edit);
            Toast.makeText(RegisterActivity.this, R.string.passwordNotSame, Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(email)) {
            playSound();
            animationShake(emailEdit);
            Toast.makeText(RegisterActivity.this, R.string.emailEmpty, Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(code)) {
            playSound();
            animationShake(codeEdit);
            Toast.makeText(RegisterActivity.this, R.string.codeEmpty, Toast.LENGTH_LONG).show();
        } else {
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
                        playSound();
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
            playSound();
            animationShake(emailEdit);
            Toast.makeText(RegisterActivity.this, R.string.emailEmpty, Toast.LENGTH_LONG).show();
        } else {
            String url = Constant.URL + Constant.SEND_CODE;
            RequestBody requestBody = new FormBody.Builder().add(Constant.EMAIL, email).add(Constant.CODE_METHOD, Constant.CODE_METHOD_NEW).build();
            OkHttpUtil.post(url,requestBody, new MyCallBack(){
                @Override
                public void onFinish(String status, String json) {

                    // request server connection
                    Gson gson = new Gson();
                    ServerResponse<User> serverResponse = gson.fromJson(json, new TypeToken<ServerResponse<User>>() {}.getType());
                    int statusCode = serverResponse.getStatus();
                    if (statusCode != 0) {
                        // sendCode failure
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, serverResponse.getMessages(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        // sendCode success
                        // tips
                        Message message = new Message();
                        message.what = MESSAGE_CODE;
                        message.obj = codeButton;
                        handler.sendMessage(message);
                    }
                }
            });

        }
    }

    public void onCancelClicked(View view) {
        finish();
    }

    private void playSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(RegisterActivity.this, R.raw.error);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.start();
    }

    private void animationShake(EditText editText) {
        Animation shake = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.shake);
        editText.startAnimation(shake);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));
    }
}
