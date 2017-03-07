package com.example.user.todoapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by User on 7.3.2017.
 */

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView time;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.task_name);
            time = (TextView) itemView.findViewById(R.id.task_time);
            date = (TextView) itemView.findViewById(R.id.task_date);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}