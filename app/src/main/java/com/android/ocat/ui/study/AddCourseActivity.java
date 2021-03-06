package com.android.ocat.ui.study;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ocat.AppBottomActivity;
import com.android.ocat.MainActivity;
import com.android.ocat.R;
import com.android.ocat.global.entity.Course;

public class AddCourseActivity extends AppCompatActivity {
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setFinishOnTouchOutside(false);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        uid = bundle.getInt("uid");

        final EditText inputCourseName = (EditText) findViewById(R.id.course_name);
        final EditText inputTeacher = (EditText) findViewById(R.id.teacher_name);
        final EditText inputClassRoom = (EditText) findViewById(R.id.class_room);
        final EditText inputDay = (EditText) findViewById(R.id.week);
        final EditText inputStart = (EditText) findViewById(R.id.classes_begin);
        final EditText inputEnd = (EditText) findViewById(R.id.classes_ends);

        Button okButton = (Button) findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = inputCourseName.getText().toString();
                String teacher = inputTeacher.getText().toString();
                String classRoom = inputClassRoom.getText().toString();
                String day = inputDay.getText().toString();
                String start = inputStart.getText().toString();
                String end = inputEnd.getText().toString();

                if (courseName.equals("") || day.equals("") || start.equals("") || end.equals("")) {
                    Toast.makeText(AddCourseActivity.this, getResources().getString(R.string.inputEmpty), Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(day) < 1 ||
                        Integer.parseInt(day) > 7 ||
                        Integer.parseInt(start) > 24 ||
                        Integer.parseInt(start) < 0 ||
                        Integer.parseInt(end) > 24 ||
                        Integer.parseInt(end) < 0 ||
                        Integer.parseInt(end) < Integer.parseInt(start)) {
                    Toast.makeText(AddCourseActivity.this, getResources().getString(R.string.illegalParams), Toast.LENGTH_SHORT).show();

                } else {
                    Course course = new Course(courseName, teacher, classRoom,
                            Integer.valueOf(day), Integer.valueOf(start), Integer.valueOf(end));

                    /**
                     * issues here!!!
                     */
                    Intent intent = new Intent(AddCourseActivity.this, AppBottomActivity.class);
                    intent.putExtra("course", course);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("==================AddCourseActivity Destroy===============");
    }

}
