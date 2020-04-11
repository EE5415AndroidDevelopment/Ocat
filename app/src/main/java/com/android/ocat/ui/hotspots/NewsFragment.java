package com.android.ocat.ui.hotspots;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocat.R;
import com.android.ocat.ui.study.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment implements View.OnClickListener {
    private ViewPager viewPager;
    static List<TextView> tag;
    List<Fragment> viewList;
    static int tagPointer = 0;
    static ProgressDialog progressDialog;
    private FragmentPagerAdapter adapter;
    private TextView financeTextView, filmTextView, entertainmentTextView, tvTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
//        setHasOptionsMenu(true);

        financeTextView = view.findViewById(R.id.newsFinance);
        filmTextView = view.findViewById(R.id.newsFilm);
        entertainmentTextView = view.findViewById(R.id.newsEntertainment);
        tvTextView = view.findViewById(R.id.newsTV);
        viewPager = view.findViewById(R.id.viewPager);

        financeTextView.setOnClickListener(this);
        filmTextView.setOnClickListener(this);
        entertainmentTextView.setOnClickListener(this);
        tvTextView.setOnClickListener(this);

        viewList = new ArrayList<>();
        NewsFinanceFragment f1 = new NewsFinanceFragment();
        NewsFilmFragment f2 = new NewsFilmFragment();
        NewsEntertainmentFragment f3 = new NewsEntertainmentFragment();
        NewsTvFragment f4 = new NewsTvFragment();
        viewList.add(f1);
        viewList.add(f2);
        viewList.add(f3);
        viewList.add(f4);

//        ChannelPager channelPager = new ChannelPager(getFragmentManager(), viewList, view.getContext());
//        viewPager.setAdapter(channelPager);
//        tag=new ArrayList<>();
//        tag.add((TextView)financeTextView);
//        tag.add((TextView)filmTextView);
//        tag.add((TextView)entertainmentTextView);
//        tag.add((TextView)tvTextView);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newsFinance:
                viewPager.setCurrentItem(0);
                break;
            case R.id.newsFilm:
                viewPager.setCurrentItem(1);
                break;
            case R.id.newsEntertainment:
//                Toast.makeText(getContext(), "33333", Toast.LENGTH_SHORT).show();
                viewPager.setCurrentItem(2);
                break;
            case R.id.newsTV:
//                Toast.makeText(getContext(), "44444", Toast.LENGTH_SHORT).show();
                viewPager.setCurrentItem(3);
                break;
        }

    }
}
