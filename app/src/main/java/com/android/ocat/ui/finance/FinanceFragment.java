package com.android.ocat.ui.finance;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.CurrencyRateResponse;
import com.android.ocat.global.entity.Rates;
import com.android.ocat.global.utils.FinanceAlgorithm;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the homepage of Finance, also the currencyRate page
 */
public class FinanceFragment extends Fragment {
    private ListView mListView;
    private FinanceRateListAdapter mFinanceRateListAdapter;
    private FloatingActionButton floatingActionButton;
    private int len;
    private String[] countries, currency_code, currency_rate;
    private int[] flags;
    private SharedPreferenceUtil util;
    private boolean isStop;

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finance, container, false);
//        setHasOptionsMenu(true);

        mListView = view.findViewById(R.id.listView);

        // load data
        util = new SharedPreferenceUtil(Constant.FILE_NAME, getContext());
        countries = getResources().getStringArray(R.array.countries_array);
        len = countries.length;
        currency_code = getResources().getStringArray(R.array.currency_code);
        currency_rate = new String[len];
        flags = new int[]{R.drawable.china, R.drawable.china, R.drawable.canada, R.drawable.australia, R.drawable.usa};
        FinanceAlgorithm financeAlgorithm = new FinanceAlgorithm(getContext(), util);
        financeAlgorithm.requestCurrencyRate(currency_code);
        for (int i = 0; i < len; i++) {
            currency_rate[i] = util.getString(currency_code[i]);
        }

        // ListView
        mFinanceRateListAdapter = new FinanceRateListAdapter(getContext(), countries, currency_code, currency_rate, flags);
        mListView.setAdapter(mFinanceRateListAdapter);
        mListView = view.findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currency_rate[position] = "100";
                float baseValue = Float.parseFloat(util.getString(currency_code[position]));
                for (int i = 0; i < currency_rate.length; i++) {
                    if (i == position) {
                        continue;
                    }
                    float value = Float.parseFloat(util.getString(currency_code[i]));
                    float newValue = value * 100 / baseValue;
                    currency_rate[i] = String.format("%.2f", newValue);
                }
                mFinanceRateListAdapter.notifyDataSetChanged();
            }
        });

        // FloatingActionButton
        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasRecord = util.getBoolean(Constant.HAS_RECORD);
                if (hasRecord) {
                    Navigation.findNavController(v).navigate(R.id.navigation_bookKeeping);
                } else {
                    Intent intent = new Intent(getActivity(), RecordNowActivity.class);
                    startActivity(intent);
                    Toast.makeText(getContext(), R.string.noRecordWelcome, Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // request server, over-write data
        if (isStop) {
            FinanceAlgorithm financeAlgorithm = new FinanceAlgorithm(getContext(), util);
            financeAlgorithm.requestCurrencyRate(currency_code);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isStop = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("==================Finance Fragment Destroy===============");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
