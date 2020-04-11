package com.android.ocat.ui.hotspots;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.ocat.R;

import java.util.ArrayList;
import java.util.List;

public class ChannelPager extends FragmentPagerAdapter {
    List<Fragment> fragmentList=new ArrayList<>();
    Context context;
    public  ChannelPager(FragmentManager fm , List<Fragment> list, Context context)
    {
        super(fm);
        fragmentList = list;
        this.context = context;
    }
    @Override
    public Fragment getItem(int position) {
        Log.d("getItem","good");

        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList!= null ? fragmentList.size() : 0;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        NewsFragment.tagPointer=position;
        switch (position) {
            case 0: {
                NewsFragment.tag.get(0).setBackgroundColor(context.getResources().getColor(R.color.colorDark));
                List<TextView> others = new ArrayList<TextView>();
                others.addAll(NewsFragment.tag);
                others.remove(0);
                for (TextView textView : others) {
                    textView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                }}
            break;
            case 1: {
                NewsFragment.tag.get(1).setBackgroundColor(context.getResources().getColor(R.color.colorDark));
                List<TextView> others = new ArrayList<TextView>();
                others.addAll(NewsFragment.tag);
                others.remove(1);
                for (TextView textView : others) {
                    textView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                }

            }
            break;
            case 2: {
                NewsFragment.tag.get(2).setBackgroundColor(context.getResources().getColor(R.color.colorDark));
                List<TextView> others = new ArrayList<TextView>();
                others.addAll(NewsFragment.tag);
                others.remove(2);
                for (TextView textView : others) {
                    textView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                }

            }
            break;
            case 3: {
                NewsFragment.tag.get(3).setBackgroundColor(context.getResources().getColor(R.color.colorDark));
                List<TextView> others = new ArrayList<TextView>();
                others.addAll(NewsFragment.tag);
                others.remove(3);
                for (TextView textView : others) {
                    textView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                }

            }
            break;
        }
    }

}
