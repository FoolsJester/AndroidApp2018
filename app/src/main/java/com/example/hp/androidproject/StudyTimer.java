package com.example.hp.androidproject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StudyTimer extends AppCompatActivity {

    Chronometer simpleChronometer;
    Button startBtn, pauseBtn, finishBtn;
    long stopTime = 0;
    float saveTime;
    float Interrupt = 5000;
    String studyStartTime;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chronometer);

        //Declaring variables
        openHelper = new DatabaseHelper2(this);
        simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);
        startBtn = (Button) findViewById(R.id.btStart);
        pauseBtn = (Button) findViewById(R.id.btPause);
        finishBtn = (Button) findViewById(R.id.btFinish);
        final Spinner dropdown = findViewById(R.id.spinner1);

        //Initialising dropdown to display courses - hardcoded for now
        String[] labels = {"COMP30650", "COMP50650", "COMP20650", "COMP40650", "COMP10650", "COMP60650"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(dataAdapter);

        //Start timer
        startBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                studyStartTime = dateFormat.format(date);
                simpleChronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
                simpleChronometer.start();
                startBtn.setVisibility(View.GONE);
                pauseBtn.setVisibility(View.VISIBLE);
            }
        });

        // Pause timer
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTime = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
                simpleChronometer.stop();
                startBtn.setVisibility(View.VISIBLE);
                pauseBtn.setVisibility(View.GONE);
            }
        });

        //Clears timer and saves to database
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTime = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
                int paused = Math.round(SystemClock.elapsedRealtime() - saveTime);
                String course = dropdown.getSelectedItem().toString();
                Toast.makeText(StudyTimer.this, ""+paused+""+course, Toast.LENGTH_SHORT).show();
                db=openHelper.getWritableDatabase();
                insertData(course, studyStartTime, Math.round(saveTime/1000), paused);
                simpleChronometer.setBase(SystemClock.elapsedRealtime());
                stopTime = 0;
                simpleChronometer.stop();
                startBtn.setVisibility(View.VISIBLE);
                pauseBtn.setVisibility(View.GONE);

            }
        });
    }

    //Insert data to database
    private void insertData(String CourseCode, String SDate,int STime,int SInterrupt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper2.COL_2, CourseCode);
        contentValues.put(DatabaseHelper2.COL_3, SDate);
        contentValues.put(DatabaseHelper2.COL_4, STime);
        contentValues.put(DatabaseHelper2.COL_5, SInterrupt);
        long count = db.insert(DatabaseHelper2.TABLE_NAME, null, contentValues);
        Toast.makeText(StudyTimer.this, STime+ " : study logged", Toast.LENGTH_SHORT).show();
    }
}
