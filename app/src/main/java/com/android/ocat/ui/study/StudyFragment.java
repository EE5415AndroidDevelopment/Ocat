package com.android.ocat.ui.study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.ocat.R;

import java.util.ArrayList;
import java.util.List;

public class StudyFragment extends Fragment implements View.OnClickListener{

    private com.android.ocat.ui.study.StudyViewModel studyViewModel;
    private TextView classTableTextView, reminderTextView;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private FragmentPagerAdapter adapter;
    static Toolbar toolbar;
    private SwipeRefreshLayout refreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study, container, false);

        classTableTextView = view.findViewById(R.id.classTable);
        reminderTextView = view.findViewById(R.id.reminder);
        viewPager = view.findViewById(R.id.viewPager);
        refreshLayout = view.findViewById(R.id.pullToRefresh);

        // init toolbar
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.class_table_add);
        classTableTextView.setTextColor(getResources().getColor(R.color.darkOrange));
        reminderTextView.setTextColor(getResources().getColor(R.color.colorText));

        classTableTextView.setOnClickListener(this);
        reminderTextView.setOnClickListener(this);

        // add fragments
        final StudyClassTableFragment f1 = new StudyClassTableFragment();
        final StudyReminderFragment f2 = new StudyReminderFragment();
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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                        toolbar.inflateMenu(R.menu.reminder_all);
                        reminderTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                        viewPager.setCurrentItem(1);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        refreshLayout.setEnabled(false);
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        refreshLayout.setEnabled(true);
                        break;
                }
            }
        });

        // pull to refresh
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (viewPager.getCurrentItem()){
                    case 0:
                        // overwrite db from server
                        f1.syncServer();
                        break;
//                    case 1:
//                        f2.GetNews();
//                        break;
                }
                refreshLayout.setRefreshing(false);
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
                toolbar.inflateMenu(R.menu.reminder_all);
                reminderTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                viewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("==================StudyFragment Destroy===============");
    }

}
