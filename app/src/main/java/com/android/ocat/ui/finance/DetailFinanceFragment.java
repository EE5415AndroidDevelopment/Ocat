package com.android.ocat.ui.finance;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.FinanceRecord;
import com.android.ocat.global.entity.FinanceSum;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.utils.DateUtil;
import com.android.ocat.global.utils.FinanceAlgorithm;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * This shows user's detailed expense each month
 */
public class DetailFinanceFragment extends Fragment {
    private ListView mListView;
    private DetailFinanceListAdapter adapter;
    private SharedPreferenceUtil util;
    private Gson gson;
    private List<List<FinanceRecord>> allRecords;
    private List<FinanceRecord> records;
    private List<FinanceSum> sumList;
    private List<String> allDates, financeCategory, date, currencyCode, currencyValue;
    private List<Integer> inOut, categoryId, recordId, deleteId;
    private String[] financeCategoryArray;
    private int size, index;
    private FinanceSum sum;
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
        financeCategoryArray = getResources().getStringArray(R.array.finance_category);
        gson = new Gson();
        util = new SharedPreferenceUtil(Constant.FILE_NAME, getContext());
        index = util.getInt("position");
        allDates = gson.fromJson(util.getString(Constant.ALL_DATES), new TypeToken<List<String>>(){}.getType());
        allRecords = gson.fromJson(util.getString(Constant.MONTHLY_RECORD), new TypeToken<List<List<FinanceRecord>>>(){}.getType());
        sumList = gson.fromJson(util.getString(Constant.FINANCE_SUM), new TypeToken<List<FinanceSum>>() {}.getType());
        records = allRecords.get(index);
        sum = sumList.get(index);
        size = records.size();
        recordId = new ArrayList<>();
        deleteId = new ArrayList<>();
        categoryId = new ArrayList<>();
        date = new ArrayList<>();
        inOut = new ArrayList<>();
        currencyCode = new ArrayList<>();
        currencyValue = new ArrayList<>();
        financeCategory = new ArrayList<>();
//        System.out.println("+++++++++++SIZE++++++++++++++");
        for (int j = 0; j < size; j++) {
            FinanceRecord record = records.get(j);
            recordId.add(record.getId());
            categoryId.add(record.getCateId().getId());
            date.add(DateUtil.date2SimpleString(DateUtil.string2FullDate(record.getConsumeTime())));
            inOut.add(record.getInOut());
            currencyCode.add(record.getCurrencyCode().getName());
            currencyValue.add(record.getCurrencyValue());
        }
        System.out.println(date.toString());
        System.out.println(currencyCode.toString());
        for (int id : categoryId) {
            financeCategory.add(financeCategoryArray[id - 1]);
        }

        adapter = new DetailFinanceListAdapter(getContext(), financeCategory, date, inOut, currencyCode, currencyValue);
        mListView.setAdapter(adapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.warning)
                        .setMessage(R.string.warning_message_delete)
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                financeCategory.remove(position);
                                inOut.remove(position);
                                currencyCode.remove(position);
                                currencyValue.remove(position);
                                deleteId.add(recordId.get(position));
                                recordId.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), null)
                        .show();
                return true;
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
//            boolean flag = false;
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.warning)
                        .setMessage(getResources().getString(R.string.warning_message_submit))
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = Constant.URL + Constant.FINANCE_DELETE_ONE;
                                for (Integer id: deleteId) {
                                    RequestBody requestBody = new FormBody.Builder()
                                            .add(Constant.ID, Integer.toString(id))
                                            .build();
                                    OkHttpUtil.post(url, requestBody, new MyCallBack(){
                                        @Override
                                        public void onFinish(String status, String json) {
                                            super.onFinish(status, json);
//                                            ServerResponse response = gson.fromJson(json, ServerResponse.class);
//                                            if (response.getStatus() == 0) {
//                                                flag = true;
//                                            }
                                        }
                                    });
//                                    if (!flag) {
//                                        break;
//                                    }
                                }
                                /**
                                 * update local sharedPreferences
                                 */
                                allRecords.remove(index);
                                sumList.remove(index);
                                if (size == deleteId.size()) {
                                    // if clear all monthly records
                                    allDates.remove(index);
                                    util.putString(Constant.ALL_DATES, gson.toJson(allDates));
                                } else {
                                    // update local data
                                    for (int i = records.size() - 1; i >= 0; i--) {
                                        for (int id : deleteId) {
                                            FinanceRecord record = records.get(i);
                                            if (record.getId() == id) {
                                                if (record.getInOut() == 0) {
                                                    sum.setTotalIn(sum.getTotalIn() - Integer.parseInt(record.getCurrencyValue()));
                                                } else {
                                                    sum.setTotalOut(sum.getTotalOut() - Integer.parseInt(record.getCurrencyValue()));
                                                }
                                                records.remove(i);
                                                break;
                                            }
                                        }
                                    }
                                    allRecords.add(index, records);
                                    sumList.add(index, sum);
                                }
                                util.putString(Constant.FINANCE_SUM, gson.toJson(sumList));
                                util.putString(Constant.MONTHLY_RECORD, gson.toJson(allRecords));

                                if (allRecords.size() == 0 || allRecords == null) {
                                    util.putBoolean(Constant.HAS_RECORD, false);
                                }
                                util.putBoolean(Constant.REFRESH_NOW, true);
                                Navigation.findNavController(v).navigateUp();
                            }
                        })
                        .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util.putBoolean(Constant.REFRESH_NOW, false);
                Navigation.findNavController(v).navigateUp();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("==================Detail Finance Fragment Destroy===============");
    }
}
