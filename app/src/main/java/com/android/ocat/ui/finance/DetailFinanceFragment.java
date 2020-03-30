package com.android.ocat.ui.finance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.FinanceRecord;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class DetailFinanceFragment extends Fragment {
    ListView mListView;
    DetailFinanceListAdapter adapter;
    private SharedPreferenceUtil util;
    private String[] category_array, in_out, currency_code, currency_value;
    private int size;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_finance, container, false);

        /**
         * set data arrays
         */
        util = new SharedPreferenceUtil(Constant.FILE_NAME, getContext());
        position = util.getInt("position");
        Gson gson = new Gson();
        String jsonStr = util.getString(Integer.toString(position));
        List<FinanceRecord> list = gson.fromJson(jsonStr, new TypeToken<List<FinanceRecord>>() {}.getType());
        size = list.size();
        category_array = new String[size];
        in_out = new String[size];
        currency_code = new String[size];
        currency_value = new String[size];
        for (int j = 0; j < list.size(); j++) {
            FinanceRecord record = list.get(j);
            category_array[j] = record.getCateId().getName();
            int flag = record.getInOut();
            if (flag == 0) {
                in_out[j] = getResources().getString(R.string.in);
            } else {
                in_out[j] = getResources().getString(R.string.out);
            }
            currency_code[j] = record.getCurrencyCode().getName();
            currency_value[j] = record.getCurrencyValue();
        }

        mListView = view.findViewById(R.id.listView);
        adapter = new DetailFinanceListAdapter(getContext(), category_array, in_out, currency_code, currency_value);
        mListView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
