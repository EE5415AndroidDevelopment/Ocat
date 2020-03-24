package com.android.ocat;

import android.os.Bundle;

import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.CurrencyRateResponse;
import com.android.ocat.global.entity.FinanceRecord;
import com.android.ocat.global.entity.Rates;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class AppBottomActivity extends AppCompatActivity {
    private int len;
    private SharedPreferenceUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bottom);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_hotspots, R.id.navigation_finance, R.id.navigation_me, R.id.navigation_study)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

//        if (!util.getBoolean("isSuccess") && System.currentTimeMillis() < util.getLong("time")) {
//
//        }

        // currency_rate data parsing
        final String[] currency_code = getResources().getStringArray(R.array.currency_code);
        len = currency_code.length;
        String url = Constant.URL_CURRENCY_RATE;
        OkHttpUtil.get(url, new MyCallBack() {
            @Override
            public void onFinish(String status, String json) {
                super.onFinish(status, json);
                Gson gson = new Gson();
                CurrencyRateResponse response = gson.fromJson(json, CurrencyRateResponse.class);
                util = new SharedPreferenceUtil(Constant.CURRENCY_RATE_FILE_NAME,AppBottomActivity.this);
                List<Rates> rates = response.getRates();
                Map<String, Rates> map = new HashMap<>();
                for (Rates data : rates) {
                    map.put(data.getCurrency_code(), data);
                }
                for (int i = 0; i < len; i++) {
                    Rates rate = map.get(currency_code[i]);
                    float rateFloat = rate.getRate();
                    util.putString(currency_code[i], String.format("%.2f", rateFloat * 100));
                }
                util.putBoolean("isSuccess", true);
                util.putLong("time", System.currentTimeMillis());
            }
        });

        // finance_record data parsing
        String url_2 = Constant.URL + Constant.FIANCE_SELECT_MONTHLY;
        RequestBody requestBody = new FormBody.Builder().add(Constant.YEAR, "2020").add(Constant.MONTH, "3").build();
        OkHttpUtil.post(url_2, requestBody, new MyCallBack(){
            @Override
            public void onFinish(String status, String json) {
                super.onFinish(status, json);
                Gson gson = new Gson();
                ServerResponse<List<FinanceRecord>> response = gson.fromJson(json, new TypeToken<ServerResponse<List<FinanceRecord>>>(){}.getType());
                util = new SharedPreferenceUtil(Constant.FINANCE_RECORD_FILE_NAME, AppBottomActivity.this);
                util.putString("financeRecord", gson.toJson(response.getData()));
            }
        });
    }
}
