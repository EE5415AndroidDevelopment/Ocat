package com.android.ocat.ui.me;

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

public class MeFragment extends Fragment {

    private TextView usernameText;
    private Button logOut;
    private SharedPreferenceUtil util;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        usernameText = view.findViewById(R.id.meUsernameValueText);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        logOut = getActivity().findViewById(R.id.meLogOut);
        util = new SharedPreferenceUtil(Constant.FILE_NAME, getActivity());

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
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

        usernameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalInfoCenterActivity.class);
                startActivity(intent);
            }
        });
    }
}
