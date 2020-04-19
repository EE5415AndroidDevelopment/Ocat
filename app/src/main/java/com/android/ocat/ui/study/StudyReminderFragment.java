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

import com.android.ocat.R;
import com.android.ocat.global.db.ClassDatabaseHelper;
import com.android.ocat.global.entity.Course;
import com.android.ocat.global.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StudyReminderFragment extends Fragment {
    private int year, month, day;//获取今天的日月年
    private DatePicker datePicker;
    private Toolbar toolbar;
    private SharedPreferenceUtil util;
    private View view;

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
                        case R.id.reminderAdd:
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

        datePicker = view.findViewById(R.id.datePicker);

        /**
         * 方式一：直接调用（）
         */
//        System.out.println("++++++++++++++++++CourseList++++++++++++++++++++++");
//        List<Course> coursesList = StudyClassTableFragment.coursesList;
//        for (Course course : coursesList) {
//            System.out.println(course);
//        }
        /**
         *  方式二：数据库读取（）
         */
        ClassDatabaseHelper databaseHelper = new ClassDatabaseHelper(view.getContext(), "database.db", null, 1);
//        DatabaseHelper databaseHelper = StudyClassTableFragment.databaseHelper;

        List<Course> coursesList = new ArrayList<>(); //课程列表
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from courses", null);
        if (cursor.moveToFirst()) {
            do {
                coursesList.add(new Course(
                        cursor.getString(cursor.getColumnIndex("course_name")),
                        cursor.getString(cursor.getColumnIndex("teacher")),
                        cursor.getString(cursor.getColumnIndex("class_room")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getInt(cursor.getColumnIndex("class_start")),
                        cursor.getInt(cursor.getColumnIndex("class_end"))));
            } while(cursor.moveToNext());
        }
        cursor.close();
        for (Course course : coursesList) {
            System.out.println(course);
        }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("==================StudyReminderFragment Destroy===============");
    }

}
