package com.android.ocat.ui.finance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.CurrencyRateResponse;
import com.android.ocat.global.entity.Rates;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  待完成
 *  1.  动态修改ListView中多个EditText
 *  2.  优化AppBottomActivity汇率保存或重新获取机制
 *  3.  单条 --> 批量获取每月记账，并存储，读取
 *
 */
public class FinanceFragment extends Fragment {

    private FinanceViewModel financeViewModel;
    private ListView mListView;
    private FinanceRateListAdapter mFinanceRateListAdapter;
    private FloatingActionButton floatingActionButton;
    private SharedPreferenceUtil util;
    private int len;

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finance, container, false);
//        setHasOptionsMenu(true);

        final String[] countries = getResources().getStringArray(R.array.countries_array);
        len = countries.length;
        final String[] currency_code = getResources().getStringArray(R.array.currency_code);
        final String[] currency_rate = new String[len];
        int[] flags = {R.drawable.china, R.drawable.china, R.drawable.canada, R.drawable.australia, R.drawable.usa};

        // data parsing
        util = new SharedPreferenceUtil(Constant.CURRENCY_RATE_FILE_NAME, getContext());
        for (int i = 0; i < len; i++) {
            currency_rate[i] = util.getString(currency_code[i]);
        }

        // ListView
        mListView = view.findViewById(R.id.listView);
        mFinanceRateListAdapter = new FinanceRateListAdapter(getContext(), countries, currency_code, currency_rate, flags);
        mListView.setAdapter(mFinanceRateListAdapter);
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
                Navigation.findNavController(v).navigate(R.id.navigation_bookKeeping);
            }
        });

        return view;
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.finance_functions_menu, menu);
//    }


}
