package com.android.ocat.global.utils;

import android.content.Context;

import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.CurrencyRateResponse;
import com.android.ocat.global.entity.FinanceRecord;
import com.android.ocat.global.entity.Rates;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class FinanceAlgorithm {
    private SharedPreferenceUtil util;
    private int userId;


    public FinanceAlgorithm(Context context) {
        this.util = new SharedPreferenceUtil(Constant.FILE_NAME, context);
        User user = (User) util.getObject(Constant.USER_JSON, User.class);
        userId = user.getId();
    }

    public FinanceAlgorithm(SharedPreferenceUtil util) {
        this.util = util;
        User user = (User) util.getObject(Constant.USER_JSON, User.class);
        userId = user.getId();
    }

    public void requestCurrencyRate(final String[] currency_code) {
        final int len = currency_code.length;
        String url = Constant.URL_CURRENCY_RATE;
        OkHttpUtil.get(url, new MyCallBack() {
            @Override
            public void onFinish(String status, String json) {
                super.onFinish(status, json);
                Gson gson = new Gson();
                CurrencyRateResponse response = gson.fromJson(json, CurrencyRateResponse.class);
                List<Rates> rates = response.getRates();
                int count = 0;
                for (Rates data : rates) {
                    for (int i = 0; i < len; i++) {
                        if (data.getCurrency_code().equals(currency_code[i])) {
                            float rateFloat = data.getRate();
                            util.putString(currency_code[i], String.format("%.2f", rateFloat * 100));
                            count++;
                            break;
                        }
                    }
                    if (count == len) {
                        break;
                    }
                }
                util.putBoolean("isSuccess", true);
                util.putLong("time", System.currentTimeMillis());
            }
        });
    }

    public void requestFinanceDateRange() {
        String url = Constant.URL + Constant.FINANCE_SELECT_DATE_RANGE;
        RequestBody requestBody = new FormBody.Builder().add(Constant.UID, Integer.toString(userId)).build();
        OkHttpUtil.post(url,requestBody,new MyCallBack(){
            @Override
            public void onFinish(String status, String json) {
                super.onFinish(status, json);

                Gson gson = new Gson();
                ServerResponse<List<String>> response = gson.fromJson(json, new TypeToken<ServerResponse<List<String>>>() {}.getType());
                if (response.getStatus() != Constant.SUCCESS) {
                    util.putInt(Constant.ARRAY_SIZE, 0);
                } else {
                    List<String> list = response.getData();
                    util.putString(Constant.ALL_DATES, gson.toJson(list));
                    util.putInt(Constant.ARRAY_SIZE, list.size());
                }
            }
        });
    }

    public void requestFinanceRecord() {
        // finance_record data parsing
        String url = Constant.URL + Constant.FINANCE_SELECT;
        RequestBody requestBody = new FormBody.Builder().add(Constant.UID, Integer.toString(userId)).build();
        OkHttpUtil.post(url, requestBody, new MyCallBack(){
            @Override
            public void onFinish(String status, String json) {
                super.onFinish(status, json);
                Gson gson = new Gson();
                ServerResponse<List<List<FinanceRecord>>> response = gson.fromJson(json, new TypeToken<ServerResponse<List<List<FinanceRecord>>>>(){}.getType());
                if (response.getStatus() != Constant.SUCCESS) {
                    util.putInt(Constant.MONTHLY_RECORD, 0);
                } else {
                    List<List<FinanceRecord>> allRecords = response.getData();
                    int index = 0;
                    for (List<FinanceRecord> monthlyRecord : allRecords) {
                        util.putString(Integer.toString(index), gson.toJson(monthlyRecord));
                        index++;
                    }
                }
            }
        });
    }
}
