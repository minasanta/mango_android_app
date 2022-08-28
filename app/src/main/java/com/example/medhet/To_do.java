package com.example.medhet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class To_do extends AppCompatActivity implements DialogCloseListener {
    DrawerLayout drawerLayout;

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private List<ToDoModel> taskList;
    private ToDoDBHelper db;
    private FloatingActionButton addTaskBtn;
    private calendarTasksDBHelper calendarDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        //getSupportActionBar().hide();
        drawerLayout = findViewById(R.id.drawer_layout4);
        db = new ToDoDBHelper(this);

        taskList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(System.currentTimeMillis());

        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6));

        calendarDB = new calendarTasksDBHelper(getApplicationContext());

        tasksRecyclerView = findViewById(R.id.tasksRecycleView);
        tasksAdapter = new ToDoAdapter(db, this);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.setAdapter(tasksAdapter);
        addTaskBtn = findViewById(R.id.addTaskBtn);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeTouch(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);


        Cursor cursor = calendarDB.getDayTasks(day, month, year);
        while (!cursor.isAfterLast()) {

            ToDoModel task = new ToDoModel();
            task.setId(cursor.getInt(4));
            task.setTask(cursor.getString(0));
            task.setStatus(0);
            db.createNewTask(task);
            taskList.add(task);
            cursor.moveToNext();
        }


        tasksAdapter.setTasks(taskList);




        taskList = db.fetchAllTasks();


        tasksAdapter.setTasks(taskList);

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.fetchAllTasks();

        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
    public void ClickMenu(View view)
    {
        //bug
        MainActivity.openDrawer(drawerLayout);

    }
    public void ClickLogo(View view)
    {
        MainActivity.closeDrawer(drawerLayout);
    }

    public void clickpomodoro(View view){
        MainActivity.redirectActivity(this,pomodoro.class);
    }
    public void clickGpa(View view){
        MainActivity.redirectActivity(this,Gpa.class);
    }
    public void clickto_do(View view){
       recreate();
    }
    public void clickCalender(View view){
        MainActivity.redirectActivity(this,Calendar.class);
    }
    public void clickLogOut(View view){
        MainActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
    }
