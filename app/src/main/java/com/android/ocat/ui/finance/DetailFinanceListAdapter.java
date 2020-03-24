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

public class DetailFinanceListAdapter extends BaseAdapter {
    Context context;
    private final String [] category_array;
    private final String[] in_out;
    private final String[] currency_code;
    private final String [] currency_value;

    public DetailFinanceListAdapter(Context context, String [] category_array, String[] in_out, String[] currency_code, String [] currency_value){
        this.context = context;
        this.category_array = category_array;
        this.in_out = in_out;
        this.currency_code = currency_code;
        this.currency_value = currency_value;
    }

    @Override
    public int getCount() {
        return category_array.length;
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
            viewHolder.category = (TextView)
                    convertView.findViewById(R.id.category);
            viewHolder.inOut = (TextView)
                    convertView.findViewById(R.id.inOut);
            viewHolder.currencyCode = (TextView) convertView.findViewById(R.id.currencyCode);
            viewHolder.currencyValue = (TextView) convertView.findViewById(R.id.currencyValue);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.category.setText(category_array[position]);
        viewHolder.inOut.setText(in_out[position]);
        viewHolder.currencyCode.setText(currency_code[position]);
        viewHolder.currencyValue.setText(currency_value[position]);
        return convertView;
    }

    private static class ViewHolder {
        TextView category;
        TextView inOut;
        TextView currencyCode;
        TextView currencyValue;
    }
}
