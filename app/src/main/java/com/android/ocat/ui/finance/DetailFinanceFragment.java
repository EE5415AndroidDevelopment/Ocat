package com.android.ocat.ui.finance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.FinanceRecord;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class DetailFinanceFragment extends Fragment {
    private ListView mListView;
    private DetailFinanceListAdapter adapter;
    private SharedPreferenceUtil util;
    private List<String> category_array, in_out, currency_code, currency_value;
    private List<Integer> id_array, index_delete;
    private int size;
    private int position;
    private Button okButton, cancelButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_finance, container, false);

        mListView = view.findViewById(R.id.listView);
        okButton = view.findViewById(R.id.ok);
        cancelButton = view.findViewById(R.id.cancel);

        /**
         * set data arrays
         */
        util = new SharedPreferenceUtil(Constant.FILE_NAME, getContext());
        position = util.getInt("position");
        final Gson gson = new Gson();
        String jsonStr = util.getString(Integer.toString(position));
        List<FinanceRecord> list = gson.fromJson(jsonStr, new TypeToken<List<FinanceRecord>>() {}.getType());
        size = list.size();
        id_array = new ArrayList<>();
        index_delete = new ArrayList<>();
        category_array = new ArrayList<>();
        in_out = new ArrayList<>();
        currency_code = new ArrayList<>();
        currency_value = new ArrayList<>();
        for (int j = 0; j < size; j++) {
            FinanceRecord record = list.get(j);
            id_array.add(record.getId());
            category_array.add(record.getCateId().getName());
            int flag = record.getInOut();
            if (flag == 0) {
                in_out.add(getResources().getString(R.string.in));
            } else {
                in_out.add(getResources().getString(R.string.out));
            }
            currency_code.add(record.getCurrencyCode().getName());
            currency_value.add(record.getCurrencyValue());
        }

        adapter = new DetailFinanceListAdapter(getContext(), category_array, in_out, currency_code, currency_value);
        mListView.setAdapter(adapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this task?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                category_array.remove(position);
                                in_out.remove(position);
                                currency_code.remove(position);
                                currency_value.remove(position);
                                index_delete.add(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index_delete.size() == 0) {
                    getActivity().onBackPressed();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.warning)
                            .setMessage(getResources().getString(R.string.warning_message))
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = Constant.URL + Constant.FINANCE_DELETE_ONE;
                                    for (Integer id: index_delete) {
                                        RequestBody requestBody = new FormBody.Builder()
                                                .add(Constant.ID, Integer.toString(id_array.get(id)))
                                                .build();
                                        OkHttpUtil.post(url, requestBody, new MyCallBack(){
                                            @Override
                                            public void onFinish(String status, String json) {
                                                super.onFinish(status, json);

//                                                ServerResponse serverResponse = gson.fromJson(json, ServerResponse.class);
//                                                System.out.println(serverResponse.getStatus());

                                            }
                                        });
                                    }
                                    getActivity().onBackPressed();
                                }
                            })
                            .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
