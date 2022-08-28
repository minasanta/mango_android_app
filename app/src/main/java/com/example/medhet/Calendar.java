package com.example.medhet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

public class Calendar extends AppCompatActivity {
    DrawerLayout drawerLayout;
    public int yearSel, monthSel, dayOfMonthSel;
    TextView txtNumOfTasks;
    calendarTasksDBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
       // getSupportActionBar().hide();
        drawerLayout = findViewById(R.id.drawer_layout5);

        Button btnManage = (Button) findViewById(R.id.btnManage);
        txtNumOfTasks = (TextView) findViewById(R.id.txtNumOfTasks);
        txtNumOfTasks.setText("");
        CalendarView calendar =  (CalendarView) findViewById(R.id.calendarView);
        dbHelper = new calendarTasksDBHelper(getApplicationContext());
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
            {
                int taskCount = dbHelper.getTaskCount(dayOfMonth, month + 1, year);
                txtNumOfTasks.setText("You have " + taskCount + " tasks on: " + dayOfMonth + "/" +  (month + 1) + "/" + year);
                yearSel = year;
                monthSel = month + 1;
                dayOfMonthSel = dayOfMonth;
            }
        });

        btnManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (dayOfMonthSel == 0 && monthSel == 0 && yearSel == 0){

                    Toast.makeText(getApplicationContext(), "Please Select a Date." , Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(Calendar.this, TasksActivity.class);
                    //pass date to next intent.
                    intent.putExtra("day", dayOfMonthSel);
                    intent.putExtra("month", monthSel);
                    intent.putExtra("year", yearSel);
                    startActivity(intent);
                }


            }
        });
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
        MainActivity.redirectActivity(this,To_do.class);
    }
    public void clickCalender(View view){
        recreate();
    }
    public void clickLogOut(View view){
        MainActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
    @Override
    //To refresh number of tasks in case of change
    protected void onResume() {
        super.onResume();
        if (yearSel != 0)
        {
            int taskCount = dbHelper.getTaskCount(dayOfMonthSel, monthSel, yearSel);
            txtNumOfTasks.setText("You have " + taskCount + " tasks on: " + dayOfMonthSel + "/" + monthSel + "/" + yearSel);
        }

    }
}