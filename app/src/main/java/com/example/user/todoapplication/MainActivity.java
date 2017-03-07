package com.example.user.todoapplication;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing Realm
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //FloatingButton onClickListener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CLICKED");
                // Change to AddTask activity
                startActivity(new Intent(MainActivity.this,AddTask.class));
            }
        });

        setRecyclerView();

    }

    //Method to set adapter to recyclerview
    public void setRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RealmResults<TodoItemModel> toDoItems = realm.where(TodoItemModel.class).findAllAsync();
        TodoItemAdapter adapter = new TodoItemAdapter(this, toDoItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setAdapter(adapter);
    }

}
