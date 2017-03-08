package com.example.user.todoapplication;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class AddTask extends FragmentActivity implements TimePickerFragment.OnCompleteListener {

    private TextView taskTime;
    private EditText taskName;
    private EditText taskDescription;
    private Button addTask,setTime;
    private Realm realm;
    private String notifyTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        //Initializing Realm
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        taskName = (EditText)findViewById(R.id.add_task_name);
        taskDescription = (EditText)findViewById(R.id.add_task_description);
        addTask = (Button)findViewById(R.id.btn_add);
        setTime = (Button)findViewById(R.id.btn_time);
        taskTime = (TextView)findViewById(R.id.txt_time);

        //AlarmService
        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        final PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Set 30second timer for notification just for demo purpose
        final android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
        cal.add(android.icu.util.Calendar.SECOND, 30);

        //Add task button onClickListener
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CLICKED");
                //Saves data to the Realm
                saveDataToDatabase();
                //Launch alarm
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
                Toast.makeText(AddTask.this, R.string.taskCreated, Toast.LENGTH_SHORT).show();
            }
        });

        //Add time button onClickListener
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CLICKED");
                showTimePickerDialog(view);
            }
        });

    }

    //Method for saving data in to database
    public void saveDataToDatabase() {
        final String name = taskName.getText().toString();
        final String description = taskDescription.getText().toString();
        final String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        final String notify = taskTime.getText().toString();

        // If EditTexts are empty make a toast
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(notify)) {
            Toast.makeText(AddTask.this, R.string.error_values, Toast.LENGTH_SHORT).show();
        }else{

            //Get current time
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
            Date currentLocalTime = cal.getTime();
            DateFormat dateFormatter = new SimpleDateFormat("HH:mm");
            dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
            final String localTime = dateFormatter.format(currentLocalTime);

            //Realm transaction
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Add a task
                    TodoItemModel todoItemModel = realm.createObject(TodoItemModel.class);
                    todoItemModel.setName(name);
                    todoItemModel.setDescription(description);
                    todoItemModel.setDate(date);
                    todoItemModel.setTime(localTime);
                    todoItemModel.setNotifyTime(notifyTime);
                    System.out.println("Task added");
                    //Checking what is in the realm
                    RealmResults<TodoItemModel> results = realm.where(TodoItemModel.class)
                            .findAll();
                    System.out.println(results);
                }
            });
            finish();

        }
    }
    //Method makes a new TimePickerFragment
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
    //When TimePicking completed puts time in to textview
    public void onComplete(String time) {
        notifyTime = time;
        taskTime.setText(notifyTime);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        // Closes Realm connection
        realm.close();
    }


}
