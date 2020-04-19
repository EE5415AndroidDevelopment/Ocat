package com.android.ocat.ui.study;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

    private Toolbar toolbar;
    static List<String> tasks;
    private ListView listView;
    static ArrayAdapter arrayAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_study_reminder_to_do_list, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        tasks = new ArrayList<>();
        listView = view.findViewById(R.id.listView);
        toolbar.inflateMenu(R.menu.reminder_add);
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, tasks);
        listView.setAdapter(arrayAdapter);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), StudyReminderAddActivity.class);
                intent.putExtra("taskId", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                tasks.remove(position);
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });

        return view;
    }
}
