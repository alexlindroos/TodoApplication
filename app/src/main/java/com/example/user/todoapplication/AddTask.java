package com.example.user.todoapplication;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by User on 7.3.2017.
 */

public class AddTask extends Activity {

    private EditText taskName;
    private EditText taskDescription;
    private Button addTask;
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

        //Add task button onClickListener
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CLICKED");
                //Saves data to the Realm
                saveDataToDatabase();
            }
        });
    }

    public void saveDataToDatabase() {
        final String name = taskName.getText().toString();
        final String description = taskDescription.getText().toString();
        final String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        // If EditTexts are empty make a toast
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description)) {
            Toast.makeText(AddTask.this, R.string.error_values, Toast.LENGTH_SHORT).show();
        }else {

            //Get current time
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
            Date currentLocalTime = cal.getTime();
            DateFormat dateFormatter = new SimpleDateFormat("HH:mm a");
            dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
            final String localTime = dateFormatter.format(currentLocalTime);


            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Add a task
                    TodoItemModel todoItemModel = realm.createObject(TodoItemModel.class);
                    todoItemModel.setName(name);
                    todoItemModel.setDescription(description);
                    todoItemModel.setDate(date);
                    todoItemModel.setTime(localTime);
                    System.out.println("Task added");

                    RealmResults<TodoItemModel> results = realm.where(TodoItemModel.class)
                            .findAll();
                    System.out.println(results);
                }
            });
            finish();

        }
    }

        @Override
        protected void onDestroy () {
            super.onDestroy();
            // Closes Realm connection
            realm.close();
        }


}
