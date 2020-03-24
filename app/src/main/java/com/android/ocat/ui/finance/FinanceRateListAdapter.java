package com.android.ocat.ui.finance;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.ocat.R;

public class FinanceRateListAdapter extends BaseAdapter {
    Context context;
    private final String [] countries_array;
    private final String[] currency_symbol;
    private final String[] currency_rate;
    private final int [] images;

    public FinanceRateListAdapter(Context context, String [] countries_array, String[] currency_symbol, String[] currency_rate, int [] images){
        this.context = context;
        this.countries_array = countries_array;
        this.currency_symbol = currency_symbol;
        this.currency_rate = currency_rate;
        this.images = images;
    }

    @Override
    public int getCount() {
        return countries_array.length;
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
            convertView = inflater.inflate(R.layout.list_finance_rate, parent, false);
            viewHolder.txtCountry = (TextView)
                    convertView.findViewById(R.id.aCountrytxt);
            viewHolder.txtSymbol = (TextView)
                    convertView.findViewById(R.id.aSymboltxt);
            viewHolder.flag = (ImageView) convertView.findViewById(R.id.aFlag);
            viewHolder.editText = (EditText) convertView.findViewById(R.id.aEditText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtCountry.setText(countries_array[position]);
        viewHolder.txtSymbol.setText(currency_symbol[position]);
        viewHolder.flag.setImageResource(images[position]);
        viewHolder.editText.setText(currency_rate[position]);
        return convertView;
    }

    private static class ViewHolder {
        TextView txtCountry;
        TextView txtSymbol;
        ImageView flag;
        EditText editText;
    }
}

