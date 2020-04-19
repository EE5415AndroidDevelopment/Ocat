package com.android.ocat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.MusicButton;
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
    private SharedPreferenceUtil util;
    private EditText emailEdit;
    private EditText passwordEdit;
    private MusicButton musicButton;
    private ImageView imageView;
    private TextView change;
    private String email;
    private String password;
    private String changeText;
    private static final int REQUEST_CODE = 1;
    private MediaPlayer mediaPlayer;
    private boolean hasConnection;

    private Resources resources;
    private Configuration configuration;
    private DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        util = new SharedPreferenceUtil(Constant.FILE_NAME,MainActivity.this);

        // gain network state
        ConnectivityManager mConnectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            hasConnection = mNetworkInfo.isAvailable();
            util.putBoolean(Constant.HAS_CONNECTION, hasConnection);
        } else {
            hasConnection = false;
            util.putBoolean(Constant.HAS_CONNECTION, false);
        }

        // auto sign in
        if (util.getBoolean(Constant.IS_SGININ)) {
            util.putBoolean(Constant.REFRESH_NOW, true);// refresh finance record now
            util.putBoolean(Constant.DB_INITIALIZE, true);// flag for full overwrite db
            Intent intent = new Intent(MainActivity.this, AppBottomActivity.class);
            startActivity(intent);
            finish();
        } else {
            // has internet connection
            if (hasConnection) {
                emailEdit = findViewById(R.id.emailEdit);
                passwordEdit = findViewById(R.id.passwordEdit);
                change = findViewById(R.id.userChange);
                musicButton = findViewById(R.id.music);
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.background);

                changeText = change.getText().toString();

                // prepare for switching language
                resources = getResources();
                configuration = resources.getConfiguration();
                displayMetrics = resources.getDisplayMetrics();

                Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                    @Override
                    public boolean queueIdle() {
                        musicButton.performClick();
                        return false;
                    }
                });

                musicButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        musicButton.playMusic();
                        try {
                            if (mediaPlayer != null) {
                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.pause();
                                } else {
                                    mediaPlayer.start();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.start();
                        mediaPlayer.setLooping(true);
                    }
                });


            } else {
                // no internet connection
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle(R.string.error)
                        .setMessage(R.string.noNetworkConnection)
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }
        }
    }

    public void onSignInClicked(View view) {
        // gain user input
        email = emailEdit.getText().toString();
        password = passwordEdit.getText().toString();
        System.out.println("++++++++++++++MainActivity Sign In Clicked+++++++++++++++");

        if (TextUtils.isEmpty(email)) {
            playSoundEffect(Constant.SOUND_EFFECT_ERROR);
            animationShake(emailEdit);
            Toast.makeText(MainActivity.this, getResources().getString(R.string.emailEmpty), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            playSoundEffect(Constant.SOUND_EFFECT_ERROR);
            animationShake(passwordEdit);
            Toast.makeText(MainActivity.this, getResources().getString(R.string.passwordEmpty), Toast.LENGTH_LONG).show();
        } else {

            // request server connection
            String url = Constant.URL + Constant.LOGIN;
            RequestBody requestBody = new FormBody.Builder().add(Constant.EMAIL, email).add(Constant.PASSWORD,password).build();
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
                        mediaPlayer.stop();
                        util.putBoolean(Constant.IS_SGININ, true);
                        util.putString(Constant.USER_JSON, gson.toJson(serverResponse.getData()));
                        util.putString("language", changeText);
                        util.putBoolean(Constant.REFRESH_NOW, false);
                        util.putBoolean(Constant.DB_INITIALIZE, true);// flag for full overwrite db now

                        // welcome tip
                        Looper.prepare();
                        playSoundEffect(Constant.SOUND_EFFECT_SUCCESS);
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.welcome)+serverResponse.getData().getUsername(), Toast.LENGTH_SHORT).show();
                        // activity jump
                        finish();
                        Intent intent = new Intent(MainActivity.this, AppBottomActivity.class);
                        startActivity(intent);
                        Looper.loop();
                    } else {
                        // login fail
                        Looper.prepare();
                        playSoundEffect(Constant.SOUND_EFFECT_ERROR);
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
        mediaPlayer.stop();
        finish();
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

    private void playSoundEffect(int flag) {
        int res = 0;
        switch (flag) {
            case Constant.SOUND_EFFECT_SUCCESS:
                res = R.raw.success_miao;
                break;
            case Constant.SOUND_EFFECT_ERROR:
                res = R.raw.error;
                break;
        }
        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, res);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.start();
    }

    private void animationShake(EditText editText) {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
        editText.startAnimation(shake);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));
    }
}
