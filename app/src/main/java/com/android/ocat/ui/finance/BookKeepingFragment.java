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
    private String[] time_array, count_array;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_keeping, container, false);

        // ListView
        Gson gson = new Gson();
        util = new SharedPreferenceUtil(Constant.FILE_NAME, getContext());
        List<String> list = gson.fromJson(util.getString(Constant.ALL_DATES), new TypeToken<List<String>>() {}.getType());
        arraySize = util.getInt(Constant.ARRAY_SIZE);
        time_array = new String[arraySize];
        count_array = new String[arraySize];
        for (int i = 0; i < arraySize; i++) {
            time_array[i] = list.get(i);
            count_array[i] = 100 * i + "";
        }

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
//                Navigation.findNavController(v).navigate(R.id.navigation_bookKeeping);
                Intent intent = new Intent(getActivity(), RecordNowActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
