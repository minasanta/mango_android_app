package com.example.medhet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class updateSubject extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_subject);
        //getSupportActionBar().hide();

        String[] degrees = {"A+","A","A-","B+","B","B-","C+","C","C-","D+","D","F"};
        ArrayAdapter<String> adapterDegrees = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, degrees);
        adapterDegrees.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner degreesSpinner = (Spinner) findViewById(R.id.spinnerNew);
        degreesSpinner.setAdapter(adapterDegrees);

        final Subject subject = new Subject(this);
        final EditText newName = (EditText) findViewById(R.id.newName);
        final EditText newHour = (EditText) findViewById(R.id.newHour);

        Button update = (Button) findViewById(R.id.btnUpdate);

        newName.setText(getIntent().getExtras().getString("name"));
        newHour.setText(getIntent().getExtras().getString("hour"));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validName = true;
                boolean validHour = true;
                if(newName.getText().toString().equals("")) {
                    Toast.makeText(updateSubject.this, "Please enter a valid name ", Toast.LENGTH_LONG).show();
                    validName=false;
                }
                if(newHour.getText().toString().equals("")) {
                    Toast.makeText(updateSubject.this, "Please enter a valid hours ", Toast.LENGTH_LONG).show();
                    validHour=false;
                }
                if(validName&&validHour) {
                    subject.updateSubject(getIntent().getExtras().getString("name"),
                            newName.getText().toString(),
                            degreesSpinner.getSelectedItem().toString(),
                            Float.parseFloat(newHour.getText().toString()));
                    Intent i = new Intent(updateSubject.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });

    }
}