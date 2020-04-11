package com.android.ocat.ui.study;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.ocat.MainActivity;
import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.utils.SharedPreferenceUtil;

import java.util.Calendar;

public class StudyReminderFragment extends Fragment {
    private int year, month, day;//获取今天的日月年
    private DatePicker datePicker;
    private Toolbar toolbar;
    private SharedPreferenceUtil util;

    // update toolbar menu when fragment is visible
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            toolbar = StudyFragment.toolbar;
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.reminderAdd:
                            return true;
//                    case R.id.reminderAllPlans:
//                        Toast.makeText(getContext(), "allPlans", Toast.LENGTH_LONG).show();
//                        return true;
                    }
                    return false;
                }
            });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study_reminder, container, false);
        setHasOptionsMenu(true);

        datePicker = view.findViewById(R.id.datePicker);

        Calendar calendar = Calendar.getInstance();
        /**
         * 初始化时获得日期
         */
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year1, int month1, int day1) {
                /**
                 *year1,month1,day1是改变后获取的新日期
                 */
                year = year1;
                month = month1;
                day = day1;
                shoulddo(year, month, day);
            }
        });


        return view;
    }

    private void shoulddo(int i, int i1, int i2) {
        String  str=i+"年"+(1+i1)+"月"+i2+'日';
        //此处可以导入已经储存好的课程日期及事件名称，若能匹配上则在窗口中显示今日事件。

        /*if(能匹配上)*/

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getContext());
        builder.setTitle("今日安排");
        builder.setMessage(str);//此处放入事件名称
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    { }
                });
        builder.create();
        builder.show();
    }
}
