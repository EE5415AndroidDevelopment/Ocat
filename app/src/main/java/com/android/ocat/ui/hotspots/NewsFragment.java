package com.android.ocat.ui.hotspots;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocat.R;
import com.android.ocat.global.db.ClassDatabaseHelper;
import com.android.ocat.global.db.ReminderDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment implements View.OnClickListener {
    private ViewPager viewPager;
    List<Fragment> viewList;
    static int tagPointer = 0;
    static ProgressDialog progressDialog;
    private FragmentPagerAdapter adapter;
    private TextView financeTextView, filmTextView, entertainmentTextView, tvTextView;
    private SwipeRefreshLayout refreshLayout;

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
        refreshLayout = view.findViewById(R.id.pullToRefresh);
        viewPager = view.findViewById(R.id.viewPager);

        resetColor();
        financeTextView.setTextColor(getResources().getColor(R.color.darkOrange));

        financeTextView.setOnClickListener(this);
        filmTextView.setOnClickListener(this);
        entertainmentTextView.setOnClickListener(this);
        tvTextView.setOnClickListener(this);

        viewList = new ArrayList<>();
        final NewsFinanceFragment f1 = new NewsFinanceFragment();
        final NewsFilmFragment f2 = new NewsFilmFragment();
        final NewsEntertainmentFragment f3 = new NewsEntertainmentFragment();
        final NewsTvFragment f4 = new NewsTvFragment();
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
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        resetColor();
                        financeTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                        viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        resetColor();
                        filmTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                        viewPager.setCurrentItem(1);
                        break;
                    case 2:
                        resetColor();
                        entertainmentTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                        viewPager.setCurrentItem(2);
                        break;
                    case 3:
                        resetColor();
                        tvTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                        viewPager.setCurrentItem(3);
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

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (viewPager.getCurrentItem()){
                    case 0:
                        f1.GetNews();
                        break;
                    case 1:
                        f2.GetNews();
                        break;
                    case 2:
                        f3.GetNews();
                        break;
                    case 3:
                        f4.GetNews();
                        break;
                }
                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void resetColor() {
        financeTextView.setTextColor(getResources().getColor(R.color.colorText));
        filmTextView.setTextColor(getResources().getColor(R.color.colorText));
        entertainmentTextView.setTextColor(getResources().getColor(R.color.colorText));
        tvTextView.setTextColor(getResources().getColor(R.color.colorText));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newsFinance:
                financeTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                viewPager.setCurrentItem(0);
                break;
            case R.id.newsFilm:
                filmTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                viewPager.setCurrentItem(1);
                break;
            case R.id.newsEntertainment:
                entertainmentTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                viewPager.setCurrentItem(2);
                break;
            case R.id.newsTV:
                tvTextView.setTextColor(getResources().getColor(R.color.darkOrange));
                viewPager.setCurrentItem(3);
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("==================NewsFragment Destroy===============");
    }
}
