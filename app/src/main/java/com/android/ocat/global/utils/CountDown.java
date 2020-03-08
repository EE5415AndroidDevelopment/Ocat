package com.android.ocat.global.utils;

import android.os.CountDownTimer;
import android.widget.Button;

import com.android.ocat.R;
import com.android.ocat.global.Constant;

public class CountDown extends CountDownTimer {
    private Button button;
    public CountDown(long millisInFuture, Button view) {
        super(millisInFuture, Constant.INTERVAL);
        button = view;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        button.setClickable(false);
        button.setText("TRY AGAIN IN ("+millisUntilFinished / 1000 +") s");

    }

    @Override
    public void onFinish() {
        button.setText(R.string.sendCodeAgain);
        button.setClickable(true);
    }
}
