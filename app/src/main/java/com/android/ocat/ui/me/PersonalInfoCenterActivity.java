package com.android.ocat.ui.me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.widget.Toast;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.CountDown;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.android.ocat.global.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class PersonalInfoCenterActivity extends AppCompatActivity {
    private SharedPreferenceUtil sharedPreferenceUtil;
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
    private static final int MESSAGE_CODE = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_CODE:
                    Button temp = (Button) msg.obj;
                    Toast.makeText(PersonalInfoCenterActivity.this, R.string.sendCodeSuccess, Toast.LENGTH_LONG).show();
                    // reset countDown
                    new CountDown(Constant.PERIOD, temp).start();

            }
        }
    };



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
        if (sharedPreferenceUtil.getBoolean(Constant.HAS_CONNECTION)) {
            email = meEmailValueText.getText().toString();
            if (TextUtils.isEmpty(email)) {
                playSound();
                animationShake(meEmailValueText);
                Toast.makeText(PersonalInfoCenterActivity.this, R.string.emailEmpty, Toast.LENGTH_LONG).show();
            } else {
                if (email.equals(user.getEmail())) {
                    sendCodeMethod = Constant.CODE_METHOD_OLD;
                }

                String url = Constant.URL + Constant.SEND_CODE;
                RequestBody requestBody = new FormBody.Builder().add(Constant.EMAIL, email).add(Constant.CODE_METHOD, sendCodeMethod).build();
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
                            Toast.makeText(PersonalInfoCenterActivity.this, serverResponse.getMessages(), Toast.LENGTH_LONG).show();
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
    }

    public void onSubmitClicked(View view) {
        if (sharedPreferenceUtil.getBoolean(Constant.HAS_CONNECTION)) {
            email = meEmailValueText.getText().toString();
            username = meUsernameValueText.getText().toString();
            phone = mePhoneValueText.getText().toString();
            code = enterCodeEdit.getText().toString();

            if (TextUtils.isEmpty(email)) {
                playSound();
                animationShake(meEmailValueText);
                Toast.makeText(PersonalInfoCenterActivity.this, R.string.emailEmpty, Toast.LENGTH_LONG).show();
            }else if(TextUtils.isEmpty(code)) {
                playSound();
                animationShake(enterCodeEdit);
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
    }

    public void onCancelClicked(View view) {
        finish();
    }

    private void playSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(PersonalInfoCenterActivity.this, R.raw.error);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.start();
    }

    private void animationShake(EditText editText) {
        Animation shake = AnimationUtils.loadAnimation(PersonalInfoCenterActivity.this, R.anim.shake);
        editText.startAnimation(shake);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("==================PersonalInfoCenterActivity Destroy===============");
    }


}
