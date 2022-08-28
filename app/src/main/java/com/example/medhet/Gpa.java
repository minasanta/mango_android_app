package com.example.medhet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class Gpa extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ArrayAdapter<String> listAdapter;
    ListView list;
    Subject subjectsDatabase;
    TextView GPA;
    ProgressBar progressBar;
    private static final DecimalFormat df = new DecimalFormat("0.000");
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.delete_edit_sub,menu);
        super.onCreateContextMenu(menu,v,menuInfo);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String selectedSubject = ((TextView) info.targetView).getText().toString();

        int id = item.getItemId();
        if(id == R.id.delte) {
            listAdapter.remove(selectedSubject);
            subjectsDatabase.deleteSubject(selectedSubject);
            subjectsDatabase.setGPA(subjectsDatabase.calcGPA());
            if(Double.isNaN(subjectsDatabase.getGPA())) {
                GPA.setText("GPA: ");
                progressBar.setProgress(0);
            }
            else {
                GPA.setText("GPA: "+ String.valueOf(df.format(subjectsDatabase.getGPA())));
                updateProgressBar(subjectsDatabase.getGPA());
            }
            return true;
        } else if (id == R.id.update) {
            Intent i = new Intent(Gpa.this,updateSubject.class);
            i.putExtra("name",selectedSubject);
            i.putExtra("hour",subjectsDatabase.getSubjectHours(selectedSubject));
            startActivity(i);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa);
        drawerLayout = findViewById(R.id.drawer_layout2);
        String[] degrees = {"A+","A","A-","B+","B","B-","C+","C","C-","D+","D","F"};
        ArrayAdapter<String> adapterDegrees = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, degrees);
        adapterDegrees.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        list = (ListView) findViewById(R.id.list);
        final Button addSub = (Button) findViewById(R.id.btnAdd);
        final Button btnView = (Button) findViewById(R.id.btnList);
        GPA = (TextView) findViewById(R.id.GPA);
        final Spinner degreesSpinner = (Spinner) findViewById(R.id.spinner);
        subjectsDatabase = new Subject(this);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        degreesSpinner.setAdapter(adapterDegrees);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        list.setAdapter(listAdapter);

        registerForContextMenu(list);
        if(Double.isNaN(subjectsDatabase.getGPA())) {
            GPA.setText("GPA: ");
        }
        else {
            GPA.setText("GPA: "+ String.valueOf(df.format(subjectsDatabase.getGPA())));
            updateProgressBar(subjectsDatabase.getGPA());
        }

        addSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validName = true;
                boolean validHour = true;
                EditText name = (EditText) findViewById(R.id.newName);
                EditText hours = (EditText) findViewById(R.id.newHour);
                if(name.getText().toString().equals("")) {
                    Toast.makeText(Gpa.this, "Please enter a valid name ", Toast.LENGTH_LONG).show();
                    validName=false;
                }
                if(hours.getText().toString().equals("")) {
                    Toast.makeText(Gpa.this, "Please enter a valid hours ", Toast.LENGTH_LONG).show();
                    validHour=false;
                }
                if(validName&&validHour) {
                    subjectsDatabase.addNewSubject(name.getText().toString(),
                            degreesSpinner.getSelectedItem().toString(),
                            Float.parseFloat(hours.getText().toString()));
                    name.setText("");
                    hours.setText("");
                    GPA.setText("GPA: "+ String.valueOf(df.format(subjectsDatabase.getGPA())));
                    updateProgressBar(subjectsDatabase.getGPA());
                }
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listAdapter.clear();
                Cursor cursor = subjectsDatabase.FetchAllSubjects();
                while(!cursor.isAfterLast()) {
                    listAdapter.add(cursor.getString(0));
                    cursor.moveToNext();
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name =((TextView)view).getText().toString();
                String degree =subjectsDatabase.getSubjectDegree(name);
                String hours = subjectsDatabase.getSubjectHours(name);
                Toast.makeText(Gpa.this, name+" || "+hours+" || "+degree, Toast.LENGTH_LONG).show();
            }
        });

    }
    private void updateProgressBar(float GPA) {
        float localGPA = (GPA/4)*100;
        progressBar.setProgress(Math.round(localGPA));

    }
    public void ClickMenu(View view){
        MainActivity.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view){
        MainActivity.closeDrawer(drawerLayout);
    }

    public void clickGpa(View view){
        recreate();
    }
    public void clickpomodoro(View view){
        MainActivity.redirectActivity(this, pomodoro.class);
    }
    public void clickto_do(View view){
        MainActivity.redirectActivity(this,To_do.class);
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
