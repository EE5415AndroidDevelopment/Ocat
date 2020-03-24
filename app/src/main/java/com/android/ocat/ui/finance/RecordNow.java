package com.android.ocat.ui.finance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.android.ocat.R;

public class RecordNow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_now);
    }

    public void onAddClicked(View view) {

    }

    public void onSubmitClicked(View view) {

    }

    public void onCancelClicked(View view) {
        finish();
    }
}
