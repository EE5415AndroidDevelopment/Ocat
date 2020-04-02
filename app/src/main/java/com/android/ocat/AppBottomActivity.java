package com.android.ocat;

import android.content.Context;
import android.os.Bundle;

import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.CurrencyCode;
import com.android.ocat.global.entity.CurrencyRateResponse;
import com.android.ocat.global.entity.FinanceCategory;
import com.android.ocat.global.entity.FinanceInsertData;
import com.android.ocat.global.entity.FinanceRecord;
import com.android.ocat.global.entity.Rates;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.FinanceAlgorithm;
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

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class AppBottomActivity extends AppCompatActivity {
    private Context context;
    private FinanceAlgorithm financeAlgorithm;

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

        /**
         * currency_rate data parsing
         * request currency_rate
         * request user finance record
         */
        context = AppBottomActivity.this;
        financeAlgorithm = new FinanceAlgorithm(context);
        final String[] currency_code = getResources().getStringArray(R.array.currency_code);
        financeAlgorithm.requestCurrencyRate(currency_code);
        financeAlgorithm.requestFinanceDateRange();
        financeAlgorithm.requestFinanceRecord();
        financeAlgorithm.requestFinanceSum();
    }
}
