package com.example.user.todoapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by User on 7.3.2017.
 */

public class AddTask extends Activity {

    private EditText taskName;
    private EditText taskDescription;
    private Button addTask;
    private Date date;
    private Date time;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        setTitle("Add new task");

        //Initializing Realm
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        taskName = (EditText)findViewById(R.id.add_task_name);
        taskDescription = (EditText)findViewById(R.id.add_task_description);
        addTask = (Button)findViewById(R.id.btn_add);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CLICKED");
                saveDataToDatabase();
            }
        });
    }

    public void saveDataToDatabase(){
        final String name = taskName.getText().toString();
        final String description = taskDescription.getText().toString();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Add a task
                TodoItemModel todoItemModel = realm.createObject(TodoItemModel.class);
                todoItemModel.setId(1);
                todoItemModel.setName(name);
                todoItemModel.setDescription(description);
                System.out.println("Task added");

                RealmResults<TodoItemModel> results = realm.where(TodoItemModel.class)
                        .findAll();
                System.out.println(results);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Closes Realm connection
        realm.close();
    }



}
