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

public class BookKeepingListAdapter extends BaseAdapter {
    Context context;
    private final String [] time_array;
    private final String[] count_array;

    public BookKeepingListAdapter(Context context, String[] time_array, String[] count_array) {
        this.context = context;
        this.time_array = time_array;
        this.count_array = count_array;
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

    private static class ViewHolder {
        TextView txtTime;
        TextView txtCount;
    }
}
