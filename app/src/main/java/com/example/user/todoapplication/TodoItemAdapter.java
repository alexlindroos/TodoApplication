package com.example.user.todoapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.internal.Context;


/**
 * Created by User on 7.3.2017.
 */

public class TodoItemAdapter extends RealmRecyclerViewAdapter<TodoItemModel, TodoItemAdapter.MyViewHolder> {

    private final MainActivity activity;
    private final OrderedRealmCollection<TodoItemModel> data;
    private Realm realm;

    public TodoItemAdapter(MainActivity activity, OrderedRealmCollection<TodoItemModel> data) {
        super(data, true);
        this.activity = activity;
        this.data = data;
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
        holder.desc.setText(model.getDescription());
        holder.time.setText(model.getTime());
        holder.date.setText(model.getDate());
        holder.notifyTime.setText("Today, " + model.getNotifyTime());
    }

    public void removeItem(final int position) {

        Realm realm = null;

        //Realm transaction that deletes item from Realm and Recyclerview
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    RealmResults<TodoItemModel> results = realm.where(TodoItemModel.class)
                            .findAll();
                    results.deleteFromRealm(position);
                    notifyItemRemoved(position);
                    System.out.println(results);
                }
            });
        } finally {
            if(realm != null) {
                //Closes Realm connection
                realm.close();
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TodoItemModel data;
        public TextView name;
        public TextView time;
        public TextView date;
        public TextView desc;
        public TextView notifyTime;
        public ImageView delete;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.task_name);
            desc = (TextView) view.findViewById(R.id.task_description);
            time = (TextView) view.findViewById(R.id.task_time);
            date = (TextView) view.findViewById(R.id.task_date);
            notifyTime = (TextView) view.findViewById(R.id.task_notifyTime);
            delete = (ImageView) view.findViewById(R.id.task_delete);

            delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            System.out.println("CLICKED:" + getAdapterPosition());
            removeItem(getAdapterPosition());
        }
    }
}

