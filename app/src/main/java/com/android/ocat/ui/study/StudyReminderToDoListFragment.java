package com.android.ocat.ui.study;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.ocat.R;

import java.util.ArrayList;
import java.util.List;

public class StudyReminderToDoListFragment extends Fragment {

    static Toolbar toolbar;
    private FragmentPagerAdapter adapter;
    private View view;
    private ViewPager viewPager;
    private List<Fragment> viewList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_study_reminder_to_do_list, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.reminder_add);
        viewPager = view.findViewById(R.id.viewPager);
        viewList = new ArrayList<>();
        final ToDoListRecycleViewFragment f1 = new ToDoListRecycleViewFragment();
        viewList.add(f1);
        adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return viewList.get(position);
            }

            @Override
            public int getCount() {
                return viewList.size();
            }
        };
        viewPager.setAdapter(adapter);
        return view;
    }
}
