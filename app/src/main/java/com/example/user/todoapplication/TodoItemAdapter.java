package com.example.user.todoapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


/**
 * Created by User on 7.3.2017.
 */

public class TodoItemAdapter extends RealmRecyclerViewAdapter<TodoItemModel, TodoItemAdapter.MyViewHolder> {

private final MainActivity activity;


public TodoItemAdapter(MainActivity activity, OrderedRealmCollection<TodoItemModel> data) {
        super(data, true);
        this.activity = activity;
        }

@Override
public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_task, parent, false);
        return new MyViewHolder(itemView);
        }

@Override
public void onBindViewHolder(MyViewHolder holder, int position) {
        //Binds models data
        TodoItemModel model = getData().get(position);
        holder.data = model;
        //Sets data in to the textViews
        holder.name.setText(model.getName());
        holder.time.setText(model.getTime());
        holder.date.setText(model.getDate());
        }

class MyViewHolder extends RecyclerView.ViewHolder {

    public TodoItemModel data;
    public TextView name;
    public TextView time;
    public TextView date;


    public MyViewHolder(View view) {
        super(view);
        name = (TextView) view.findViewById(R.id.task_name);
        time = (TextView) view.findViewById(R.id.task_time);
        date = (TextView) view.findViewById(R.id.task_date);
    }



    }
}
