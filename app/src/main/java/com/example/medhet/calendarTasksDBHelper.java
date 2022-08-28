package com.example.medhet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class calendarTasksDBHelper extends SQLiteOpenHelper {

    //Fields
    private static String databaseName = "calendarTasks";
    SQLiteDatabase calendarTasksDatabase;


    //Methods

    public calendarTasksDBHelper(Context context)
    {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tasks (id integer primary key, name text not null, description text, day integer, month integer, year integer, hour integer, minute integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists tasks");
        onCreate(db);
    }

    public void addNewTask(String name, String description, int day, int month, int year, int hour, int minute){
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("description", description);
        row.put("day", day);
        row.put("month", month);
        row.put("year", year);
        row.put("hour", hour);
        row.put("minute", minute);
        calendarTasksDatabase = getWritableDatabase();
        calendarTasksDatabase.insert("tasks" , null, row); //insert the task
        calendarTasksDatabase.close();
    }

    public Cursor getDayTasks(int day, int month, int year)//Takes strings not ints
    {
        calendarTasksDatabase = getReadableDatabase();
        String[] rowDetails = {"name" , "description" , "hour" , "minute" , "id"};
        String[] args = { String.valueOf(day) , String.valueOf(month) , String.valueOf(year)};
        Cursor cursor = calendarTasksDatabase.query("tasks" , rowDetails, "day =? AND month =? AND year =?" , args , null , null , null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        calendarTasksDatabase.close();
        return cursor; //Returns a cursor pointing to tasks of a specific day.

    }


    public String getTaskDescription(int id)
    {
        calendarTasksDatabase = getReadableDatabase();
        String[] arg = {String.valueOf(id)};
        Cursor cursor = calendarTasksDatabase.rawQuery("select description from tasks where id like ?" , arg);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        calendarTasksDatabase.close();
        return  cursor.getString(0);
    }

    public void deleteTask(int id)
    {
        calendarTasksDatabase = getWritableDatabase();
        calendarTasksDatabase.delete("tasks", "id=" + id, null);
        calendarTasksDatabase.close();

    }

    public void updateTask(int id, String name, String description, int day, int month, int year, int hour, int minute)
    {
     calendarTasksDatabase = getWritableDatabase();
     ContentValues row = new ContentValues();
     row.put("name", name);
     row.put("description", description);
     row.put("day", day);
     row.put("month", month);
     row.put("year", year);
     row.put("hour", hour);
     row.put("minute", minute);
     calendarTasksDatabase.update("tasks", row, "id=" + id ,null);
     calendarTasksDatabase.close();


    }

    public Cursor getTaskWithId(int id){
        calendarTasksDatabase = getReadableDatabase();
        String[] rowDetails = {"name" , "description" , "hour" , "minute"};
        Cursor cursor = calendarTasksDatabase.query("tasks" , rowDetails, "id=" + id , null , null , null , null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        calendarTasksDatabase.close();
        return cursor; //Returns a cursor pointing to one unique task.
    }

    public int getTaskCount(int day, int month, int year){
        calendarTasksDatabase = getWritableDatabase();
        String[] args = { String.valueOf(day) , String.valueOf(month) , String.valueOf(year)};
        Cursor cursor= calendarTasksDatabase.rawQuery("SELECT COUNT (*) FROM tasks WHERE day =? AND month =? AND year =?", args);
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        calendarTasksDatabase.close();
        cursor.close();
        return count;
    }


}
