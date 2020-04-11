package com.android.ocat.ui.finance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.FinanceSum;
import com.android.ocat.global.utils.FinanceAlgorithm;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class BookKeepingListAdapter extends BaseAdapter {
    private Context context;
    private SharedPreferenceUtil util;
    private String [] time_array;
    private String[] count_array;
    private int i = 0;

    public BookKeepingListAdapter(Context context, String[] time_array, String[] count_array) {
        this.context = context;
        this.time_array = time_array;
        this.count_array = count_array;
        this.util = new SharedPreferenceUtil(Constant.FILE_NAME, context);
    }

    @Override
    public int getCount() {
        return time_array.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_book_keeping, parent, false);
            viewHolder.txtTime = (TextView)
                    convertView.findViewById(R.id.aTime);
            viewHolder.txtCount = (TextView)
                    convertView.findViewById(R.id.aCount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtTime.setText(time_array[position]);
        viewHolder.txtCount.setText(count_array[position]);

        return convertView;
    }

    public void update() {

//        System.out.println("++++++++++++++++onUpdate+++++++++++++++++++");
//        System.out.println(++i);

        Gson gson = new Gson();
        util = new SharedPreferenceUtil(Constant.FILE_NAME, context);
        List<String> allDatesList = gson.fromJson(util.getString(Constant.ALL_DATES), new TypeToken<List<String>>() {}.getType());
        List<FinanceSum> sumList = gson.fromJson(util.getString(Constant.FINANCE_SUM), new TypeToken<List<FinanceSum>>() {}.getType());
        int arraySize = allDatesList.size();
        time_array = new String[arraySize];
        count_array = new String[arraySize];
        for (int i = 0; i < arraySize; i++) {
            time_array[i] = allDatesList.get(i);
            FinanceSum financeSum = sumList.get(i);
            int sum = financeSum.getTotalIn() - financeSum.getTotalOut();
            count_array[i] = Integer.toString(sum);
        }

        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView txtTime;
        TextView txtCount;
    }
}