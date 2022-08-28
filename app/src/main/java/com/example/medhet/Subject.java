package com.example.medhet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Subject extends SQLiteOpenHelper {
    private static String databaseName = "subjectDatabase";
    private static float GPA;
    SQLiteDatabase subjectDatabase;

    public Subject(Context context) { super(context,databaseName,null,1); GPA = this.calcGPA();}

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table subject (id integer primary key, name text not null, degree text not null, hours number not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int newVer) {
        db.execSQL("drop table if exists subject");
        onCreate(db);
    }

    public void addNewSubject(String name, String degree, float hours) {
        ContentValues row = new ContentValues();
        row.put("name",name);
        row.put("degree",degree);
        row.put("hours",hours);
        subjectDatabase = getWritableDatabase();
        subjectDatabase.insert("subject",null,row);
        subjectDatabase.close();
        GPA = calcGPA();
    }

    public String getSubjectDegree(String name) {
        subjectDatabase = getReadableDatabase();
        String[] arg = {name};
        Cursor cursor =subjectDatabase.rawQuery("Select degree from subject where name like ?",arg);
        cursor.moveToFirst();
        subjectDatabase.close();
        return cursor.getString(0);
    }
    public String getSubjectHours(String name) {
        subjectDatabase = getReadableDatabase();
        String[] arg = {name};
        Cursor cursor =subjectDatabase.rawQuery("Select hours from subject where name like ?",arg);
        cursor.moveToFirst();
        subjectDatabase.close();
        return cursor.getString(0);
    }

    public Cursor FetchAllSubjects() {
        subjectDatabase = getReadableDatabase();
        String[] rowDetails = {"name","degree","hours","id"};
        Cursor cursor = subjectDatabase.query("subject",rowDetails,null,null,null,null,null);
        if(cursor!=null) {
            cursor.moveToFirst();
        }
        subjectDatabase.close();
        return cursor;
    }

    public float calcGPA() {
        Cursor cursor = this.FetchAllSubjects();
        float totalHour=0;
        float totalGarde=0;
        float localGPA=0;
        while(!cursor.isAfterLast()) {
            totalHour += Float.parseFloat(cursor.getString(2));

            if(cursor.getString(1).equalsIgnoreCase("A+")) {
                totalGarde += Float.parseFloat(cursor.getString(2))*4;
            }
            else if (cursor.getString(1).equalsIgnoreCase("A")) {
                totalGarde += Float.parseFloat(cursor.getString(2))*4;
            }
            else if (cursor.getString(1).equalsIgnoreCase("A-")) {
                totalGarde += Float.parseFloat(cursor.getString(2))*3.7;
            }
            else if (cursor.getString(1).equalsIgnoreCase("B+")) {
                totalGarde += Float.parseFloat(cursor.getString(2))*3.3;
            }
            else if (cursor.getString(1).equalsIgnoreCase("B")) {
                totalGarde += Float.parseFloat(cursor.getString(2))*3;
            }
            else if (cursor.getString(1).equalsIgnoreCase("B-")) {
                totalGarde += Float.parseFloat(cursor.getString(2))*2.7;
            }
            else if (cursor.getString(1).equalsIgnoreCase("C+")) {
                totalGarde += Float.parseFloat(cursor.getString(2))*2.3;
            }
            else if (cursor.getString(1).equalsIgnoreCase("C")) {
                totalGarde += Float.parseFloat(cursor.getString(2))*2;
            }
            else if (cursor.getString(1).equalsIgnoreCase("C-")) {
                totalGarde += Float.parseFloat(cursor.getString(2))*1.7;
            }
            else if (cursor.getString(1).equalsIgnoreCase("D+")) {
                totalGarde += Float.parseFloat(cursor.getString(2))*1.3;
            }
            else if (cursor.getString(1).equalsIgnoreCase("D")) {
                totalGarde += Float.parseFloat(cursor.getString(2))*1;
            }
            cursor.moveToNext();
        }
        try {
            localGPA= totalGarde/totalHour;
            return localGPA;
        }
        catch (Exception e) {
            return 0;
        }
    }
    public static void setGPA(float GPA) { Subject.GPA = GPA; }
    public static float getGPA() {
        return GPA;
    }

    public void deleteSubject(String name) {
        subjectDatabase = getWritableDatabase();
        subjectDatabase.delete("subject","name='"+name+"'",null);
        subjectDatabase.close();
    }

    public void updateSubject(String name, String newName,String newDegree,float newHours) {
        subjectDatabase = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("name",newName);
        row.put("degree",newDegree);
        row.put("hours",newHours);
        subjectDatabase.update("subject",row,"name like ?",new String[] {name});
        subjectDatabase.close();
    }
}
