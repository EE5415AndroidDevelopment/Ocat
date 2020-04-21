package com.android.ocat.ui.study;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.ocat.R;
import com.android.ocat.global.Constant;

import java.util.ArrayList;
import java.util.List;

public class ToDoListRecycleViewFragment extends Fragment {
    static ToDoListAdapter adapter;
    private Toolbar toolbar;
    private View view;
    private RecyclerView recyclerView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            toolbar = StudyReminderToDoListFragment.toolbar;
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.reminderAdd:
                            Intent intent = new Intent(getActivity(), StudyReminderAddActivity.class);
                            startActivity(intent);
                            return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_to_do_list_recycle_view, container, false);
        recyclerView = view.findViewById(R.id.toDoListRecycleView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ToDoListAdapter();
        recyclerView.setAdapter(adapter);
        return view;
    }
}
