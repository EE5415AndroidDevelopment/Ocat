package com.android.ocat.ui.finance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.android.ocat.Forget2Activity;
import com.android.ocat.MainActivity;
import com.android.ocat.R;
import com.android.ocat.RegisterActivity;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.CurrencyCode;
import com.android.ocat.global.entity.FinanceCategory;
import com.android.ocat.global.entity.FinanceInsertData;
import com.android.ocat.global.entity.FinanceInsertRequest;
import com.android.ocat.global.entity.FinanceRecord;
import com.android.ocat.global.entity.FinanceSum;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.FinanceAlgorithm;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Record Now
 */
public class RecordNowActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private String[] itemsCategory, itemsCurrencyCode;
    private List<String> allDates;
    private List<List<FinanceRecord>> allRecords;
    private List<FinanceSum> sumList;
    private FinanceSum sum, newSum;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private int uid, cateId, currencyCode, inOut;
    private String currencyValue;
    private Date date;
    private String dateString;
    private Gson gson;
    private boolean isNewMonth = false;
    private boolean hasRecord = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_now);

        gson = new Gson();
        linearLayout = findViewById(R.id.recordNowContainer);
        itemsCategory = getResources().getStringArray(R.array.finance_category);
        itemsCurrencyCode = getResources().getStringArray(R.array.currency_code);
        sharedPreferenceUtil = new SharedPreferenceUtil(Constant.FILE_NAME, getBaseContext());
        hasRecord = sharedPreferenceUtil.getBoolean(Constant.HAS_RECORD);
        User user = (User) sharedPreferenceUtil.getObject(Constant.USER_JSON, User.class);
        uid = user.getId();

        // obtain user historical finance record
        if (hasRecord) {
            allDates = gson.fromJson(sharedPreferenceUtil.getString(Constant.ALL_DATES), new TypeToken<List<String>>() {
            }.getType());
            allRecords = gson.fromJson(sharedPreferenceUtil.getString(Constant.MONTHLY_RECORD), new TypeToken<List<List<FinanceRecord>>>() {
            }.getType());
            sumList = gson.fromJson(sharedPreferenceUtil.getString(Constant.FINANCE_SUM), new TypeToken<List<FinanceSum>>() {
            }.getType());
            sum = sumList.get(0);
        } else {
            allDates = new ArrayList<>();
            allRecords = new ArrayList<>();
            sumList = new ArrayList<>();
            sum = new FinanceSum();
        }

        // obtain current date
        date = new Date();
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM");
        dateString = formatter1.format(date);

        View childView = linearLayout.getChildAt(1);
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
        List<FinanceRecord> list1 = new ArrayList<>();

        // if current month is new, update local data
        if (!hasRecord || !dateString.equals(allDates.get(0))) {
            isNewMonth = true;
            allDates.add(0, dateString);
            sharedPreferenceUtil.putString(Constant.ALL_DATES, gson.toJson(allDates));
            newSum = new FinanceSum();
            newSum.setTotalIn(0);
            newSum.setTotalOut(0);
        }
        for (int i = linearLayout.getChildCount() - 4; i >= 0; i--) {
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
            OkHttpUtil.post(url, requestBody, new MyCallBack() {
                @Override
                public void onFinish(String status, String json) {
                    super.onFinish(status, json);
                }
            });

            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            FinanceRecord record = new FinanceRecord();
            User user = new User();
            FinanceCategory category = new FinanceCategory();
            CurrencyCode code = new CurrencyCode();
            user.setId(uid);
            category.setId(cateId);
            code.setName(itemsCurrencyCode[currencyCode - 1]);
            record.setUserId(user);
            record.setCateId(category);
            record.setInOut(inOut);
            record.setCurrencyCode(code);
            record.setCurrencyValue(currencyValue);
            record.setConsumeTime(formatter2.format(date));

            if (isNewMonth) {
                list1.add(record);
                if (inOut == 0) {
                    newSum.setTotalIn(newSum.getTotalIn() + Integer.parseInt(currencyValue));
                } else {
                    newSum.setTotalOut(newSum.getTotalOut() + Integer.parseInt(currencyValue));
                }
            } else {
                allRecords.get(0).add(0, record);
                if (inOut == 0) {
                    sum.setTotalIn(sum.getTotalIn() + Integer.parseInt(currencyValue));
                } else {
                    sum.setTotalOut(sum.getTotalOut() + Integer.parseInt(currencyValue));
                }
            }
        }
        if (isNewMonth) {
            allRecords.add(0, list1);
            sumList.add(0, newSum);
        }
        sharedPreferenceUtil.putString(Constant.MONTHLY_RECORD, gson.toJson(allRecords));
        sharedPreferenceUtil.putString(Constant.FINANCE_SUM, gson.toJson(sumList));
        // flag of re-request service for records' id
        sharedPreferenceUtil.putBoolean(Constant.REFRESH_NOW, true);
        if (!hasRecord) {
            sharedPreferenceUtil.putBoolean(Constant.HAS_RECORD, true);
            Toast.makeText(RecordNowActivity.this, R.string.operationSuccess, Toast.LENGTH_LONG).show();
        }
        finish();
    }

    public void onCancelClicked(View view) {
        sharedPreferenceUtil.putBoolean(Constant.REFRESH_NOW, false);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("==================RecordNow Activity Destroy===============");
    }
}
