package com.android.ocat.ui.study;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.db.ClassDatabaseHelper;
import com.android.ocat.global.entity.Course;
import com.android.ocat.global.entity.ServerResponse;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.android.ocat.global.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class StudyClassTableFragment extends Fragment {
    private View view;
    private Toolbar toolbar;
    private SharedPreferenceUtil util;
    private User user;
    private Gson gson;
    private String userJson;
    private int uid;
    private static final int MESSAGE_CODE = 5;
    static List<Course> coursesList;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_CODE:
                    Course temp = (Course) msg.obj;
                    createLeftView(temp);
                    createItemCourseView(temp);
            }
        }
    };

    //星期几
    private RelativeLayout day;

    //SQLite Helper类
    static ClassDatabaseHelper databaseHelper;

    int currentCoursesNumber = 0;
    int maxCoursesNumber = 0;

    // update toolbar menu when fragment is visible
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            toolbar = StudyFragment.toolbar;
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (util.getBoolean(Constant.HAS_CONNECTION)) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.classAdd:
                                Intent intent1 = new Intent(getActivity(), AddCourseActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("uid", uid);
                                intent1.putExtras(bundle);
                                startActivityForResult(intent1, 0);
                                return true;
        //                    case R.id.classAbout:
        //                        Intent intent2 = new Intent(getActivity(), StudyClassTableAboutActivity.class);
        //                        startActivityForResult(intent2, 0);
        //                        return true;
                        }
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
        view = inflater.inflate(R.layout.fragment_study_class_table, container, false);
        setHasOptionsMenu(true);
        util = new SharedPreferenceUtil(Constant.FILE_NAME, getContext());
        gson = new Gson();
        userJson = util.getString(Constant.USER_JSON);
        user = gson.fromJson(userJson, User.class);
        uid = user.getId();

        databaseHelper = new ClassDatabaseHelper(view.getContext(), "database.db", null, 1);

        //从数据库读取数据
        loadData();

        return view;
    }

    //从数据库加载数据
    private void loadData() {
        // check internet connection
        if (util.getBoolean(Constant.HAS_CONNECTION)) {
            // check pull up to refresh or auto login
            if (util.getBoolean(Constant.DB_INITIALIZE)) {
                System.out.println("+++++++++++++++++DB Operation!!!+++++++++++++++++");
                String url = Constant.URL + Constant.COURSE_SELECT_ALL;
                RequestBody requestBody = new FormBody.Builder().add(Constant.UID, Integer.toString(uid)).build();
                OkHttpUtil.post(url, requestBody, new MyCallBack() {
                    @Override
                    public void onFinish(String status, String json) {
                        ServerResponse<List<Course>> serverResponse = gson.fromJson(json, new TypeToken<ServerResponse<List<Course>>>() {
                        }.getType());
                        if (serverResponse.getStatus() == 0) {

                            databaseHelper.clearAll();

                            coursesList = serverResponse.getData();
                            for (Course course : coursesList) {

                                Message message = new Message();
                                message.what = MESSAGE_CODE;
                                message.obj = course;
                                handler.sendMessage(message);

                                // online --> local
                                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                                System.out.println("++++++++++++++++++++++数据库写入++++++++++++++++++++++++");
                                sqLiteDatabase.execSQL
                                        ("insert into courses(course_name, teacher, class_room, day, class_start, class_end) " + "values(?, ?, ?, ?, ?, ?)",
                                                new String[]{course.getCourseName(),
                                                        course.getTeacher(),
                                                        course.getClassRoom(),
                                                        course.getDay() + "",
                                                        course.getClassStart() + "",
                                                        course.getClassEnd() + ""}
                                        );
                            }
                            util.putBoolean(Constant.DB_INITIALIZE, false);
                            Looper.prepare();
                            Toast.makeText(getContext(), getResources().getString(R.string.syncSuccess), Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    }
                });
            }
            else {
                System.out.println("+++++++++++++从数据库加载++++++++++++++++++");
                coursesList = new ArrayList<>(); //课程列表
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

                //使用从数据库读取出来的课程信息来加载课程表视图
                for (Course course : coursesList) {
                    createLeftView(course);
                    createItemCourseView(course);
                }
            }
        }
    }

    public void syncServer() {
        if (util.getBoolean(Constant.HAS_CONNECTION)) {
            util.putBoolean(Constant.DB_INITIALIZE, true);
            loadData();
        }
    }

    //保存数据到数据库
    private void saveData(Course course) {
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL
                ("insert into courses(course_name, teacher, class_room, day, class_start, class_end) " + "values(?, ?, ?, ?, ?, ?)",
                        new String[] {course.getCourseName(),
                                course.getTeacher(),
                                course.getClassRoom(),
                                course.getDay()+"",
                                course.getClassStart()+"",
                                course.getClassEnd()+""}
                );

        // online sync
        String url = Constant.URL + Constant.COURSE_INSERT_ONE;
        RequestBody requestBody = new FormBody.Builder()
                .add(Constant.UID, Integer.toString(uid))
                .add(Constant.COURSE_NAME, course.getCourseName())
                .add(Constant.TEACHER, course.getTeacher())
                .add(Constant.CLASS_ROOM, course.getClassRoom())
                .add(Constant.DAY, Integer.toString(course.getDay()))
                .add(Constant.CLASS_START, Integer.toString(course.getClassStart()))
                .add(Constant.CLASS_END, Integer.toString(course.getClassEnd())).build();
        OkHttpUtil.post(url, requestBody, new MyCallBack(){
            @Override
            public void onFinish(String status, String json) {
                ServerResponse serverResponse = gson.fromJson(json, ServerResponse.class);
                int statusCode = serverResponse.getStatus();
                if (statusCode == 0) {
                    Looper.prepare();
                    Toast.makeText(getContext(), getResources().getString(R.string.operationSuccess), Toast.LENGTH_LONG).show();
                    Looper.loop();
                } else {
                    Looper.prepare();
                    ToastUtil.createToast(getContext(), statusCode);
                    Looper.loop();
                }
            }
        });

    }

    //创建"时间"视图
    private void createLeftView(Course course) {
        int endNumber = course.getClassEnd();
        if (endNumber > maxCoursesNumber) {
            for (int i = 0; i < endNumber-maxCoursesNumber; i++) {
                View leftView = LayoutInflater.from(view.getContext()).inflate(R.layout.study_class_left_view, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110,60);
                leftView.setLayoutParams(params);

                TextView text = leftView.findViewById(R.id.class_number_text);
                text.setText(String.valueOf(++currentCoursesNumber));

                LinearLayout leftViewLayout = view.findViewById(R.id.left_view_layout);
                leftViewLayout.addView(leftView);
            }
            maxCoursesNumber = endNumber;
        }
    }

    //创建单个课程视图
    private void createItemCourseView(final Course course) {
        int getDay = course.getDay();
        if ((getDay < 1 || getDay > 7) || course.getClassStart() > course.getClassEnd())
            Toast.makeText(getContext(), "星期几没写对,或课程结束时间比开始时间还早~~", Toast.LENGTH_LONG).show();
        else {
            int dayId = 0;
            switch (getDay) {
                case 1: dayId = R.id.monday; break;
                case 2: dayId = R.id.tuesday; break;
                case 3: dayId = R.id.wednesday; break;
                case 4: dayId = R.id.thursday; break;
                case 5: dayId = R.id.friday; break;
                case 6: dayId = R.id.saturday; break;
                case 7: dayId = R.id.weekday; break;
            }
            day = view.findViewById(dayId);

            int height = 60;
            final View v = LayoutInflater.from(view.getContext()).inflate(R.layout.study_class_card, null); //加载单个课程布局
            v.setY(height * (course.getClassStart()-1)); //设置开始高度,即开始
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,(course.getClassEnd()-course.getClassStart()+1)*height - 8); //设置布局高度,即跨多少节课
            v.setLayoutParams(params);
            TextView text = v.findViewById(R.id.text_view);
            text.setText(course.getCourseName() + "\n" + course.getTeacher() + "\n" + course.getClassRoom()); //显示课程名
            day.addView(v);

            //long click to delete course
            if (util.getBoolean(Constant.HAS_CONNECTION)) {
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle(R.string.warning)
                                .setMessage(getResources().getString(R.string.warning_message_submit))
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        v.setVisibility(View.GONE);//先隐藏
                                        day.removeView(v);//再移除课程视图
                                        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
                                        sqLiteDatabase.execSQL("delete from courses where course_name = ?", new String[] {course.getCourseName()});

                                        // sync online
                                        String url = Constant.URL + Constant.COURSE_DELETE_COURSE;
                                        RequestBody requestBody = new FormBody.Builder()
                                                .add(Constant.UID, Integer.toString(uid))
                                                .add(Constant.COURSE_NAME, course.getCourseName())
                                                .build();
                                        OkHttpUtil.post(url, requestBody, new MyCallBack() {
                                            @Override
                                            public void onFinish(String status, String json) {
                                                ServerResponse serverResponse= gson.fromJson(json, ServerResponse.class);
                                                int statusCode = serverResponse.getStatus();
                                                if (statusCode == 0) {
                                                    Looper.prepare();
                                                    Toast.makeText(getContext(), getResources().getString(R.string.operationSuccess), Toast.LENGTH_SHORT).show();
                                                    Looper.loop();
                                                } else {
                                                    Looper.prepare();
                                                    ToastUtil.createToast(getContext(), statusCode);
                                                    Looper.loop();
                                                }
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Course course = (Course) data.getSerializableExtra("course");
            createLeftView(course);
            createItemCourseView(course);
            saveData(course);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("==================StudyClassTableFragment Destroy===============");
    }

}
