package com.android.ocat.ui.study;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.android.ocat.MainActivity;
import com.android.ocat.R;
import com.android.ocat.global.db.ClassDatabaseHelper;
import com.android.ocat.global.db.ReminderDataBaseHelper;
import com.android.ocat.global.entity.Course;
import com.android.ocat.global.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StudyReminderFragment extends Fragment {
//    static List<String> taskDates = new ArrayList<>();
    static List<String> tasks = new ArrayList<>();
    static List<Integer> taskIds = new ArrayList<>();
    private int year, month, day;//获取今天的日月年
    private DatePicker datePicker;
    private Toolbar toolbar;
    private View view;
    private ReminderDataBaseHelper reminderDataBaseHelper;
    private ClassDatabaseHelper classDatabaseHelper;
    private Cursor cursor;

//    // update toolbar menu when fragment is visible
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
                        case R.id.reminderAll:
                            Navigation.findNavController(view).navigate(R.id.navigation_toDoList);
                            return true;
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
        view = inflater.inflate(R.layout.fragment_study_reminder, container, false);
        setHasOptionsMenu(true);

        reminderDataBaseHelper = new ReminderDataBaseHelper(view.getContext(), "reminder.db", null, 1);
        classDatabaseHelper = new ClassDatabaseHelper(view.getContext(), "class.db", null, 1);
        datePicker = view.findViewById(R.id.datePicker);

        // load whole DB
        tasks.clear();
        taskIds.clear();
//        taskDates.clear();
        Cursor cursor = reminderDataBaseHelper.selectAll();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String detail = cursor.getString(cursor.getColumnIndex("detail"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                tasks.add(date + " " + detail);
                taskIds.add(id);
//                taskDates.add(date);
            } while (cursor.moveToNext());
        }

        final Calendar calendar = Calendar.getInstance();
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
        // gain Calendar onClick date
        String str = i + "." + (1 + i1) + "." + i2;
        Date dateOfWeek = new Date();
        dateOfWeek.setYear(i);
        dateOfWeek.setMonth(i1);
        dateOfWeek.setDate(i2);

        StringBuilder sb = new StringBuilder();

        // read reminderDB by date
        cursor = reminderDataBaseHelper.selectByDate(str);
        if (cursor.moveToFirst()) {
            sb.append(getResources().getString(R.string.reminder));
            sb.append("\n");
            do {
                String detail = cursor.getString(cursor.getColumnIndex("detail"));
                sb.append(detail);
                sb.append("\n");
            } while (cursor.moveToNext());
        }

        // read classDB
        cursor = classDatabaseHelper.selectByDayOfWeek(dateOfWeek.getDay() - 1);
        if (cursor.moveToFirst()) {
            sb.append(getResources().getString(R.string.class_table));
            sb.append("\n");
            do {
                String courseName = cursor.getString(cursor.getColumnIndex("course_name"));
                sb.append(courseName);
                sb.append("\n");
            } while (cursor.moveToNext());
        }

        // show alert window
        if (sb.length() != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(str);
            builder.setMessage(sb.toString());//打印list
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.create();
            builder.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("==================StudyReminderFragment Destroy===============");
    }
}
