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
import android.widget.Toast;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.FinanceRecord;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_keeping, container, false);

        // ListView
//        util = new SharedPreferenceUtil(Constant.FINANCE_RECORD_FILE_NAME, getContext());
//        Gson gson = new Gson();
//        String jsonStr = util.getString("financeRecord");
//        List<FinanceRecord> list = gson.fromJson(jsonStr, new TypeToken<List<FinanceRecord>>() {}.getType());
//        String title = list.get(0).getConsumeTime().substring(0, 7);

        int dataNum = 12;
        final String[] time_array = new String[dataNum];
        final String[] count_array = new String[dataNum];
        for (int i = 1; i <= dataNum; i++) {
            time_array[i - 1] = "2019/" + (dataNum - i + 1);
            count_array[i - 1] = 100 * i + "";
        }

        mListView = view.findViewById(R.id.listView);
        mBookKeepingListAdapter = new BookKeepingListAdapter(getContext(), time_array, count_array);
        mListView.setAdapter(mBookKeepingListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Navigation.findNavController(view).navigate(R.id.navigation_detailFinance);
            }
        });

        // FloatingActionButton
        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.navigation_bookKeeping);
                Intent intent = new Intent(getActivity(), RecordNow.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
