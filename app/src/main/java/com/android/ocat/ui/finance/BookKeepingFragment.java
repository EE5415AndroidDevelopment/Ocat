package com.android.ocat.ui.finance;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.FinanceSum;
import com.android.ocat.global.utils.FinanceAlgorithm;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class BookKeepingFragment extends Fragment {
    private ListView mListView;
    private BookKeepingListAdapter mBookKeepingListAdapter;
    private FloatingActionButton floatingActionButton;
    private SharedPreferenceUtil util;
    private int arraySize;
    private String[] time_array, count_array, sum;
    private int i = 0;
    private boolean flag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_keeping, container, false);

//        System.out.println("++++++++++++++++onCreateView+++++++++++++++++++");
//        System.out.println(++i);
        Gson gson = new Gson();
        util = new SharedPreferenceUtil(Constant.FILE_NAME, getContext());
        List<String> allDatesList = gson.fromJson(util.getString(Constant.ALL_DATES), new TypeToken<List<String>>() {}.getType());
        List<FinanceSum> sumList = gson.fromJson(util.getString(Constant.FINANCE_SUM), new TypeToken<List<FinanceSum>>() {}.getType());
        arraySize = allDatesList.size();
        time_array = new String[arraySize];
        count_array = new String[arraySize];
        for (int i = 0; i < arraySize; i++) {
            time_array[i] = allDatesList.get(i);
            FinanceSum financeSum = sumList.get(i);
            int sum = financeSum.getTotalIn() - financeSum.getTotalOut();
            count_array[i] = Integer.toString(sum);
        }

        // ListView
        mListView = view.findViewById(R.id.listView);
        mBookKeepingListAdapter = new BookKeepingListAdapter(getContext(), time_array, count_array);
        mListView.setAdapter(mBookKeepingListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                util.putInt("position", position);
                Navigation.findNavController(view).navigate(R.id.navigation_detailFinance);
            }
        });

        // FloatingActionButton
        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordNowActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flag) {
            mBookKeepingListAdapter.update();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        flag = true;
    }
}
