package com.android.ocat.ui.hotspots;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.ocat.R;

import java.util.ArrayList;
import java.util.List;

public class HotspotsFragment extends Fragment implements View.OnClickListener {

    private com.android.ocat.ui.hotspots.HotspotsViewModel hotspotsViewModel;
    private TextView newsTextView, translationTextView;
    private List<Fragment> fragmentList;
    private FragmentPagerAdapter adapter;
    private Toolbar toolbar;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotspots, container, false);
        setHasOptionsMenu(true);

        newsTextView = view.findViewById(R.id.news);
        translationTextView = view.findViewById(R.id.translation);
        toolbar = view.findViewById(R.id.toolbar);
        viewPager = view.findViewById(R.id.viewPager);

        newsTextView.setOnClickListener(this);
        translationTextView.setOnClickListener(this);
        newsTextView.setTextColor(getResources().getColor(R.color.darkOrange));
        translationTextView.setTextColor(getResources().getColor(R.color.colorText));

        NewsFragment f1 = new NewsFragment();
        TranslationFragment f2 = new TranslationFragment();
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
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.news:
                resetColor();
                newsTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                viewPager.setCurrentItem(0);
                break;
            case R.id.translation:
                resetColor();
                translationTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                viewPager.setCurrentItem(1);
                break;
        }

    }

    private void resetColor() {
        newsTextView.setTextColor(getResources().getColor(R.color.colorText));
        translationTextView.setTextColor(getResources().getColor(R.color.colorText));
    }


}
