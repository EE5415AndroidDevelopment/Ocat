package com.android.ocat.ui.me;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.ocat.MainActivity;
import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.User;
import com.android.ocat.global.utils.SharedPreferenceUtil;
import com.android.ocat.global.db.ClassDatabaseHelper;

import java.util.List;

public class MeFragment extends Fragment {

    private TextView usernameText;
    private Button logOut;
    private Button quit;
    private SharedPreferenceUtil util;
    private ClassDatabaseHelper databaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        usernameText = view.findViewById(R.id.meUsernameValueText);
        logOut = view.findViewById(R.id.meLogOut);
        quit = view.findViewById(R.id.meQuit);
        util = new SharedPreferenceUtil(Constant.FILE_NAME, getContext());
        databaseHelper = new ClassDatabaseHelper(view.getContext(), "database.db", null, 1);

        boolean isSignIn = util.getBoolean(Constant.IS_SGININ);
        if (isSignIn) {
            User user = (User) util.getObject(Constant.USER_JSON, User.class);
            usernameText.setText(user.getUsername());
        }

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // 请求网络接口 --> okhttp
//                String url = Constant.URL +"/user/logout";
//                OkHttpUtil.get(url, new MyCallBack());
//
                util.clear();
                databaseHelper.clearAll();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager activityManager = (ActivityManager) getContext().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
                for (ActivityManager.AppTask appTask : appTaskList) {
                    appTask.finishAndRemoveTask();
                }
                System.exit(0);
            }
        });

        usernameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalInfoCenterActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("==================MeFragment Destroy===============");
    }

}
