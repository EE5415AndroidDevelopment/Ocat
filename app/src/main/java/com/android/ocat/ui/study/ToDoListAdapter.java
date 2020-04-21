package com.android.ocat.ui.study;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ocat.R;
import com.android.ocat.global.db.ReminderDataBaseHelper;

import java.util.List;


public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {
    private Context context;
    private List<String> tasks;
    private List<Integer> taskIds;
    private List<String> taskDates;
    private ReminderDataBaseHelper dataBaseHelper;
    static  class ViewHolder extends RecyclerView.ViewHolder{
        TextView item;
        CardView cardView;

        public ViewHolder(View view){
            super(view);
            item = (TextView) view.findViewById(R.id.item);
            cardView = (CardView) view.findViewById(R.id.cardView);
        }
    }

    public ToDoListAdapter() {
        this.tasks = StudyReminderFragment.tasks;
        this.taskIds = StudyReminderFragment.taskIds;
//        this.taskDates = StudyReminderFragment.taskDates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_do_list, parent,false);
        final ViewHolder holder =new ViewHolder(view);
        this.dataBaseHelper = new ReminderDataBaseHelper(context, "reminder.db", null, 1);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.item.setText(tasks.get(position));

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, StudyReminderAddActivity.class);
//                intent.putExtra("taskId", position);
//                context.startActivity(intent);
//            }
//        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.warning)
                        .setMessage(R.string.warning_message_delete)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int id = taskIds.get(position);
                                dataBaseHelper.delete(id);
                                tasks.remove(position);
                                taskIds.remove(position);
//                                taskDates.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
