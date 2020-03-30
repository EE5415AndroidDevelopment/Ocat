package com.android.ocat.ui.finance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.FinanceInsertData;
import com.android.ocat.global.entity.FinanceInsertRequest;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class RecordNowActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private String[] itemsCategory, itemsCurrencyCode;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private int uid, cateId, currencyCode, inOut;
    private String currencyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_now);

        linearLayout = findViewById(R.id.recordNowContainer);
        itemsCategory = getResources().getStringArray(R.array.finance_category);
        itemsCurrencyCode = getResources().getStringArray(R.array.currency_code);
        sharedPreferenceUtil = new SharedPreferenceUtil(Constant.FILE_NAME, getBaseContext());
        User user = (User) sharedPreferenceUtil.getObject(Constant.USER_JSON, User.class);
        uid = user.getId();

        View childView = linearLayout.getChildAt(0);
        initSpinner(childView);


    }

    public void onAddClicked(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View newView = inflater.inflate(R.layout.layout_edit, null, false);
        initSpinner(newView);

        linearLayout.addView(newView, linearLayout.getChildCount() - Constant.MIN_RECORD_NOW + 1);
    }

    private void initSpinner(View view) {
        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);
        Spinner spinnerCurrencyCode = view.findViewById(R.id.spinnerCurrencyCode);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemsCategory);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemsCurrencyCode);
        spinnerCategory.setAdapter(adapter1);
        spinnerCurrencyCode.setAdapter(adapter2);
        spinnerCurrencyCode.setSelection(1,true);

        EditText editText = view.findViewById(R.id.aEditText);

    }

    public void onRemoveClicked(View view) {
        int index = linearLayout.getChildCount();
        if (index > Constant.MIN_RECORD_NOW) {
            linearLayout.removeViewAt(linearLayout.getChildCount() - Constant.MIN_RECORD_NOW);
        }
        else {
            Toast.makeText(RecordNowActivity.this, getResources().getString(R.string.atLeastOneItem),Toast.LENGTH_LONG).show();
        }
    }

    public void onSubmitClicked(View view) {
        String url = Constant.URL + Constant.FINANCE_INSERT_ONE;
        for (int i = 0; i < linearLayout.getChildCount() - 3; i++) {
            inOut = 1;

            View childView = linearLayout.getChildAt(i);
            Spinner spinnerCategory = childView.findViewById(R.id.spinnerCategory);
            Spinner spinnerCurrencyCode = childView.findViewById(R.id.spinnerCurrencyCode);
            EditText editText = childView.findViewById(R.id.aEditText);

            cateId = spinnerCategory.getSelectedItemPosition() + 1;
            currencyCode = spinnerCurrencyCode.getSelectedItemPosition() + 1;
            currencyValue = editText.getText().toString();

            RequestBody requestBody = new FormBody.Builder()
                    .add(Constant.UID, Integer.toString(uid))
                    .add(Constant.CATE_ID, Integer.toString(cateId))
                    .add(Constant.IN_OUT, Integer.toString(inOut))
                    .add(Constant.CURRENCY_CODE, Integer.toString(currencyCode))
                    .add(Constant.CURRENCY_VALUE, currencyValue)
                    .build();
            OkHttpUtil.post(url, requestBody, new MyCallBack(){
                @Override
                public void onFinish(String status, String json) {
                    super.onFinish(status, json);
                }
            });
        }
    }

    public void onCancelClicked(View view) {
        finish();
    }
}
