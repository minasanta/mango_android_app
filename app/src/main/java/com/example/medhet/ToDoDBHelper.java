package com.example.medhet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class ToDoDBHelper extends SQLiteOpenHelper {
    private static String databaseName = "ToDoListDatabase";
    SQLiteDatabase ToDoListDB;

    public ToDoDBHelper(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table ToDo(_id INTEGER PRIMARY KEY, " +
                "task text, status integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists ToDoList");
        onCreate(sqLiteDatabase);
    }

    public void createNewTask(ToDoModel task) {
        ContentValues row = new ContentValues();
        row.put("_id", task.getId());
        row.put("task", task.getTask());
        row.put("status", task.getStatus());


        ToDoListDB = getWritableDatabase();
        ToDoListDB.insert("ToDo", null, row);
        ToDoListDB.close();
    }

    public List<ToDoModel> fetchAllTasks() {
        List<ToDoModel> list = new ArrayList<>();
        String[] rowDetails = {"_id", "task", "status"};
        ToDoListDB = getReadableDatabase();
        Cursor cursor = ToDoListDB.query("ToDo", rowDetails, null, null,
                null, null, null);

        if(cursor != null)
        {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {
            ToDoModel task = new ToDoModel();

            task.setId(cursor.getInt(0));
            task.setTask(cursor.getString(1));
            task.setStatus(cursor.getInt(2));

            list.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        ToDoListDB.close();

        return list;
    }

    public void updateStatus(int id, int status)
    {
        ContentValues row = new ContentValues();
        row.put("status", status);

        ToDoListDB = getWritableDatabase();

        ToDoListDB.update("ToDo", row, "_id=?", new String[] {String.valueOf(id)});
        ToDoListDB.close();
    }

    public void updateTask(int id, String task) {
        ContentValues row = new ContentValues();
        row.put("Task", task);

        ToDoListDB = getWritableDatabase();

        ToDoListDB.update("ToDo", row, "_id=?", new String[] {String.valueOf(id)});
        ToDoListDB.close();
    }

    public void deleteTask(int id) {
        ToDoListDB = getWritableDatabase();

        ToDoListDB.delete("ToDo", "_id=?", new String[] {String.valueOf(id)});
        ToDoListDB.close();
    }

    public int getLastID() {
        ToDoListDB = getReadableDatabase();

        Cursor cursor = ToDoListDB.rawQuery("SELECT MAX(_id) FROM ToDo", null);

        int lastID;

        cursor.moveToFirst();

        if(cursor.moveToFirst())
        {
            lastID = cursor.getInt(0);
        }
        else
        {
            lastID = -1;
        }

        Log.d("Debug", "last id: " + lastID);
        return lastID;
    }
}
