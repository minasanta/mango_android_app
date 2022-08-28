package com.example.medhet;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class TasksActivity extends Activity {

    //bool to check if it is add or edit.
    boolean isAdd;
    //store hour and minute for picking time:
    int hour;
    int minute;
    //retrieve date sent from main activity:
    int day;
    int year;
    int month;
    String date; //to show date on top

    //db Helper object:
    calendarTasksDBHelper dbHelper;

    //to store ids of all entries in a day.
    ArrayList<Integer> taskIDs;
    int selectedItemIndex = -1;
    //list view and adapter:
    ListView tasksList;
    ArrayAdapter<String> tasksAdapter;
    //views:
    Button btnAdd;
    Button btnUpdate;
    Button btnDelete;
    TextView txtDate; //To show date on top

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        //retrieve date sent from main activity:
        day = getIntent().getExtras().getInt("day");
        year = getIntent().getExtras().getInt("year");
        month = getIntent().getExtras().getInt("month");
        date = String.valueOf(day)  + "/" + String.valueOf(month) + "/" + String.valueOf(year); //to show date on top

        //Assigning the arraylist:
        taskIDs = new ArrayList<>();

        //list view and adapter:
        tasksList = (ListView) findViewById(R.id.lstViewTasks);
        tasksAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.listfont);
        tasksList.setAdapter(tasksAdapter);
        //views:
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        txtDate = (TextView) findViewById(R.id.txtDate); //To show date on top

        //dbHelper assignment;
        dbHelper = new calendarTasksDBHelper (getApplicationContext());
        //display selected date:
        txtDate.setText("Tasks on " + date);

        //load the tasks on that day.
        showAllTasks();

        //add new item
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdd = true;
                showDialogue();
            }
        });

        //delete selected item.
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItemIndex == -1 ) //if Nothing is selected
                {
                    Toast.makeText(getApplicationContext(), "Please select a task to delete.", Toast.LENGTH_SHORT).show();
                }
                else{

                    dbHelper.deleteTask(taskIDs.get(selectedItemIndex));
                    Toast.makeText(getApplicationContext(),  "deleted successfully!", Toast.LENGTH_SHORT).show();
                    showAllTasks();
                    selectedItemIndex = -1; //reset selected index after deletion.
                }

            }
        });

        //update selected item
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedItemIndex == -1 ) //if Nothing is selected
                {
                    Toast.makeText(getApplicationContext(), "Please select a task to update.", Toast.LENGTH_SHORT).show();
                }
                else{
                    isAdd = false;
                    showDialogue();
                }

            }
        });

        //to update selected index every time a user selects to use in updating and deleting.
        tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                selectedItemIndex = pos;}
        });

        //long press to view description:
        tasksList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                showDescriptionDialogue(dbHelper.getTaskDescription(taskIDs.get(pos)));
                return false;
            }
        });


    }




    public void showDialogue(){

        //reset hour and minute on adding only:
        if (isAdd)
        {
            hour = -1;
            minute = -1;
        }
        //create dialogue
        final Dialog dialogue = new Dialog(TasksActivity.this);
        dialogue.setCancelable(true);
        dialogue.setContentView(R.layout.add_edit_dialogue);
        //to be entered:
        final EditText title = (EditText) dialogue.findViewById(R.id.txtName);
        final EditText description = (EditText) dialogue.findViewById(R.id.txtDescription);

        final Button btnAddOrUpdate = (Button) dialogue.findViewById(R.id.btnOk); //update or add
        final Button btnTimePicker = (Button) dialogue.findViewById(R.id.btnPickTime); //update or add
        if (isAdd){
            btnAddOrUpdate.setText("Add");
        }
        else{
            btnAddOrUpdate.setText("Update");
            //get data in currently selected task and update UI elements.
            Cursor cursor = dbHelper.getTaskWithId(taskIDs.get(selectedItemIndex));
            title.setText(cursor.getString(0));
            description.setText(cursor.getString(1));
            hour = cursor.getInt(2);
            minute = cursor.getInt(3);

        }


        //add new entry to database or just update an existing one.
        btnAddOrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (title.getText().toString().matches("") || description.getText().toString().matches("") || hour == -1 || minute == -1) //If either is empty.
               {
                   Toast.makeText(getApplicationContext(), "Please fill all necessary fields.", Toast.LENGTH_LONG).show();
               }
               else //add or update
               {
                   if (isAdd)
                   {
                       dbHelper.addNewTask(title.getText().toString(), description.getText().toString(), day, month, year, hour, minute);
                       Toast.makeText(getApplicationContext(), "Task " + title.getText().toString() + " added at " + String.format(Locale.getDefault(), "%02d:%02d", hour, minute), Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                       //update database.
                       dbHelper.updateTask(taskIDs.get(selectedItemIndex), title.getText().toString(), description.getText().toString(), day , month, year, hour ,minute);
                       Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                       selectedItemIndex = -1; //after update
                   }
                   showAllTasks(); //refresh to show changes.

               }
            }
        });

        //Time picker button that displays time picker dialogue:
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog.OnTimeSetListener onTimeSetListener =  new TimePickerDialog.OnTimeSetListener()
            {
                 final Button btnPickTime = (Button) dialogue.findViewById(R.id.btnPickTime); //to pick time.
                 @Override
                 public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                     hour = selectedHour;
                     minute = selectedMinute;
                     btnPickTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                 }
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(TasksActivity.this, onTimeSetListener, hour, minute, true);
            timePickerDialog.setTitle("Select Task Time:");
            timePickerDialog.updateTime(hour,minute);
            timePickerDialog.show();

            }


        });



        //Show the dialogue
        dialogue.show();

    }

    public void showDescriptionDialogue(String description){
        //create dialogue
        final Dialog dialogue = new Dialog(TasksActivity.this);
        dialogue.setCancelable(true);
        dialogue.setContentView(R.layout.task_description);

        //views

        Button btnOk = (Button) dialogue.findViewById(R.id.btnOk);
        TextView txtDesc = (TextView)  dialogue.findViewById(R.id.txtViewDescription);
        txtDesc.setText(description);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogue.cancel();
            }
        });

        dialogue.show();


    }

    public void showAllTasks(){
        tasksAdapter.clear(); //clear existing
      taskIDs.clear();
        String toAdd; //the task to add in string format.
        Cursor cursor = dbHelper.getDayTasks(day, month, year); //query database and get cursor.
        while (!cursor.isAfterLast()) //print all required data.
        {
            toAdd =  cursor.getString(0) + " at " + String.format(Locale.getDefault(), "%02d:%02d", cursor.getInt(2), cursor.getInt(3));
            tasksAdapter.add(toAdd);
            taskIDs.add(cursor.getInt(4));
            cursor.moveToNext();
        }


    }

}
