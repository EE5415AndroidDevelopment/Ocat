package com.android.ocat.ui.study;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.Course;
import com.android.ocat.global.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class StudyFragment extends Fragment implements View.OnClickListener{

    private com.android.ocat.ui.study.StudyViewModel studyViewModel;
    private TextView classTableTextView, reminderTextView;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private FragmentPagerAdapter adapter;
    static Toolbar toolbar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study, container, false);

        classTableTextView = view.findViewById(R.id.classTable);
        reminderTextView = view.findViewById(R.id.reminder);
        viewPager = view.findViewById(R.id.viewPager);

        // init toolbar
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.class_table_add);
        classTableTextView.setTextColor(getResources().getColor(R.color.darkOrange));
        reminderTextView.setTextColor(getResources().getColor(R.color.colorText));

        classTableTextView.setOnClickListener(this);
        reminderTextView.setOnClickListener(this);

        // add fragments
        StudyClassTableFragment f1 = new StudyClassTableFragment();
        StudyReminderFragment f2 = new StudyReminderFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(f1);
        fragmentList.add(f2);

        adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        resetColor();
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.class_table_add);
                        classTableTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                        viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        resetColor();
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.reminder_options);
                        reminderTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                        viewPager.setCurrentItem(1);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }


    private void resetColor() {
        classTableTextView.setTextColor(getResources().getColor(R.color.colorText));
        reminderTextView.setTextColor(getResources().getColor(R.color.colorText));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.classTable:
                resetColor();
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.class_table_add);
                classTableTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                viewPager.setCurrentItem(0);
                break;
            case R.id.reminder:
                resetColor();
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.reminder_options);
                reminderTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                viewPager.setCurrentItem(1);
                break;
        }
    }
}
