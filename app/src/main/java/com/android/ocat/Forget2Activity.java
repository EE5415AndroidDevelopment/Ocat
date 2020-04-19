package com.android.ocat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
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
import android.widget.TextView;
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

public class Forget2Activity extends AppCompatActivity {
    private String email;
    private Button codeButton;
    private TextView emailText;
    private EditText passwordEdit;
    private EditText password2Edit;
    private EditText codeEdit;
    private int statusCode;
    private static final int MESSAGE_CODE = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_CODE:
                    Button temp = (Button) msg.obj;
                    Toast.makeText(Forget2Activity.this, R.string.sendCodeSuccess, Toast.LENGTH_LONG).show();
                    // reset countDown
                    new CountDown(Constant.PERIOD, temp).start();

            }
        }
    };


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

        if (checkPassword(passwordEdit, password2Edit)) {
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(Forget2Activity.this, R.string.codeEmpty, Toast.LENGTH_LONG).show();
                playSound();
                animationShake(codeEdit);
            } else {
                String url = Constant.URL + Constant.FORGET_2;
                RequestBody requestBody = new FormBody.Builder().add(Constant.EMAIL, email).add(Constant.PASSWORD, password).add(Constant.CODE, code).build();
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
                            Toast.makeText(Forget2Activity.this, R.string.operationSuccess, Toast.LENGTH_LONG).show();
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
                            playSound();
                            ToastUtil.createToast(Forget2Activity.this, statusCode);
                            Looper.loop();
                        }
                    }
                });
            }
        }
    }

    public void onSendCodeClicked(View view) {
        String url = Constant.URL + Constant.SEND_CODE;
        RequestBody requestBody = new FormBody.Builder().add(Constant.EMAIL, email).add(Constant.CODE_METHOD, Constant.CODE_METHOD_OLD).build();
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
                    Toast.makeText(Forget2Activity.this, serverResponse.getMessages(), Toast.LENGTH_LONG).show();
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

    public void onCancelClicked(View view) {
        Intent intent = new Intent(Forget2Activity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private boolean checkPassword(EditText passwordEdit, EditText password2Edit) {
        String password = passwordEdit.getText().toString();
        String password2 = password2Edit.getText().toString();
        boolean flag = false;
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)){
            Toast.makeText(Forget2Activity.this, R.string.passwordEmpty, Toast.LENGTH_LONG).show();
        } else if (!password.equals(password2)) {
            Toast.makeText(Forget2Activity.this, R.string.passwordNotSame, Toast.LENGTH_LONG).show();
        } else if (password.length() < 6 || password2.length() < 6) {
            playSound();
            Toast.makeText(Forget2Activity.this, R.string.passwordTooShort, Toast.LENGTH_LONG).show();
        } else {
            flag = true;
        }
        if (!flag) {
            playSound();
            animationShake(passwordEdit);
            animationShake(password2Edit);
        }
        return flag;
    }

    private void playSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(Forget2Activity.this, R.raw.error);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.start();
    }

    private void animationShake(EditText editText) {
        Animation shake = AnimationUtils.loadAnimation(Forget2Activity.this, R.anim.shake);
        editText.startAnimation(shake);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));
    }
}
