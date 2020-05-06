package com.android.ocat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.NewItem;
import com.android.ocat.global.utils.FinanceAlgorithm;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

public class AppBottomActivity extends AppCompatActivity {
    private Context context;
    private FinanceAlgorithm financeAlgorithm;
    private SharedPreferenceUtil util;
    public static List<NewItem> newItems;

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
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
NavigationUI.setupWithNavController(navView, navController);

        /**
         * currency_rate data parsing
         * request currency_rate
         * request user finance record
         */
        context = AppBottomActivity.this;
        util = new SharedPreferenceUtil(Constant.FILE_NAME, context);
        if (util.getBoolean(Constant.HAS_CONNECTION)) {
            financeAlgorithm = new FinanceAlgorithm(context);
            final String[] currency_code = getResources().getStringArray(R.array.currency_code);
            financeAlgorithm.requestCurrencyRate(currency_code);
            financeAlgorithm.requestFinanceDateRange();
            financeAlgorithm.requestFinanceRecord();
            financeAlgorithm.requestFinanceSum();
        } else {
            new AlertDialog.Builder(AppBottomActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(R.string.error)
                    .setMessage(R.string.noNetworkConnection)
                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (util.getBoolean(Constant.HAS_CONNECTION)) {
            boolean refreshNow = util.getBoolean(Constant.REFRESH_NOW);
            if (refreshNow) {
                System.out.println("++++++++++++++++AppBottomActivity Resume++++++++++++++++");
                util.putBoolean(Constant.REFRESH_NOW, false);
                financeAlgorithm.requestFinanceDateRange();
                financeAlgorithm.requestFinanceRecord();
                financeAlgorithm.requestFinanceSum();
            }
        }
    }
}
