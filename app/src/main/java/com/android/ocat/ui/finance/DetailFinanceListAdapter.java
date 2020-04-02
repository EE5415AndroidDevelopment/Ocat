package com.android.ocat.ui.finance;

import android.content.Context;
import android.graphics.Color;
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

import java.util.List;

public class DetailFinanceListAdapter extends BaseAdapter {
    Context context;
    private final List<String> categoryArray, date, currencyCode, currencyValue;
    private final List<Integer> in_out;

    public DetailFinanceListAdapter(Context context, List<String> categoryArray, List<String>date, List<Integer> in_out, List<String> currencyCode, List<String> currencyValue) {
        this.context = context;
        this.categoryArray = categoryArray;
        this.date = date;
        this.in_out = in_out;
        this.currencyCode = currencyCode;
        this.currencyValue = currencyValue;
    }

    @Override
    public int getCount() {
        return categoryArray.size();
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
            convertView = inflater.inflate(R.layout.list_detail_finance, parent, false);
            viewHolder.category = convertView.findViewById(R.id.category);
            viewHolder.date = convertView.findViewById(R.id.date);
            viewHolder.currencyCode = convertView.findViewById(R.id.currencyCode);
            viewHolder.currencyValue = convertView.findViewById(R.id.currencyValue);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.category.setText(categoryArray.get(position));
        viewHolder.date.setText(date.get(position));
        viewHolder.currencyCode.setText(currencyCode.get(position));
        switch (in_out.get(position)) {
            case Constant.FINANCE_IN:
                viewHolder.currencyValue.setTextColor(Color.parseColor("#D81B60"));
                break;
            case Constant.FINANCE_OUT:
                viewHolder.currencyValue.setTextColor(Color.parseColor("#008577"));
                break;
        }
        viewHolder.currencyValue.setText(currencyValue.get(position));
        return convertView;
    }

    private static class ViewHolder {
        TextView category;
        TextView date;
        TextView currencyCode;
        TextView currencyValue;
    }
}
