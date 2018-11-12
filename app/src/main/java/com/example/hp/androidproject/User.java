package com.example.hp.androidproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class User extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private Button timeStudy;

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    BarChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        // initialising variables for nav bar
        dl = (DrawerLayout) findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        timeStudy = (Button)findViewById(R.id.button1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.myprofile) {
                    openUserActivity();
                } else if (id == R.id.study) {
                    Toast.makeText(User.this, "Study Page", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.course) {
                    openCoursesActivity();
                } else if (id == R.id.login) {
                    openMainActivity();
                }

                return true;
            }
        });


        setSpinner();
        barChart();

        openHelper = new DatabaseHelper2(this);


//        chart = (BarChart) findViewById(R.id.bar_chart);
//
//
//        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
//        barEntries.add(new BarEntry(3f, 0));
//        barEntries.add(new BarEntry(2f, 1));
//        barEntries.add(new BarEntry(3f, 2));
//        barEntries.add(new BarEntry(4f, 3));
//        barEntries.add(new BarEntry(5f, 4));
//        barEntries.add(new BarEntry(6f, 5));
//
//        BarDataSet barDataSet = new BarDataSet(barEntries, "Study Statistics");
//        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//        dataSets.add(barDataSet);
//
//
//        BarData data = new BarData(dataSets);
//        chart.setData(data);
//        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
//        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);


        // final Spinner dropdown = findViewById(R.id.spinner1);
        final Button set = (Button) findViewById(R.id.button2);
        final EditText hours = (EditText) findViewById(R.id.editText);
        final Spinner dropdown = findViewById(R.id.spinner1);
        final EditText productive = findViewById(R.id.editText2);

//        String[] labels = {"COMP30650", "COMP50650", "COMP20650", "COMP40650", "COMP10650", "COMP60650"};
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        dropdown.setAdapter(dataAdapter);


        Button addStudy = (Button) findViewById(R.id.button3);
        addStudy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dropdown.setVisibility(View.VISIBLE);
                set.setVisibility(View.VISIBLE);
                hours.setVisibility(View.VISIBLE);
                productive.setVisibility(View.VISIBLE);

            }
        });

        timeStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStudyTimerActivity();
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db = openHelper.getWritableDatabase();

                String course = dropdown.getSelectedItem().toString();
                String study = hours.getText().toString();
                int studyInt = Integer.parseInt(study);

                String prodStudy = productive.getText().toString();
                int prodStudyInt = Integer.parseInt(prodStudy);


                updateData(course, studyInt, prodStudyInt);
                Toast.makeText(getApplicationContext(), "register successfully", Toast.LENGTH_LONG).show();
                barChart();
            }
        });
    }


    public void setSpinner() {

        openHelper = new DatabaseHelper2(this);
        db = openHelper.getReadableDatabase();
        Spinner dropdown = findViewById(R.id.spinner1);


        DatabaseHelper2 db = new DatabaseHelper2(getApplicationContext());
        List<String> assignment = db.getAssignments();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, assignment);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdown.setAdapter(dataAdapter);

    }

    public void insertdata(String courseCode, int hours, int productiveStudy) {


        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper2.COL_2, courseCode);
        contentValues.put(DatabaseHelper2.COL_3, "12/11/2018");
        contentValues.put(DatabaseHelper2.COL_4, hours);
        contentValues.put(DatabaseHelper2.COL_5, productiveStudy);

        long id = db.insert(DatabaseHelper2.TABLE_NAME, null, contentValues);
        Toast.makeText(getApplicationContext(), "ID="+id, Toast.LENGTH_LONG).show();
    }

    public void updateData(String courseCode, int hours, int productiveStudy) {

        DatabaseHelper2 database = new DatabaseHelper2(getApplicationContext());
        List<String> current_hours = database.getAll();

        int old_total = 0; int old_interupted = 0; int id = 0;

        for (int i = 1; i < current_hours.size(); i+=4) {
            if (courseCode.equals(current_hours.get(i))){
                id = Integer.parseInt(current_hours.get(i-1));
                old_total = Integer.parseInt(current_hours.get(i+1));
                old_interupted = Integer.parseInt(current_hours.get(i+2));
            }
        }
        int newTotal = old_total + hours; int newInterupted = old_interupted + productiveStudy;
        
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper2.COL_2, courseCode);
        contentValues.put(DatabaseHelper2.COL_3, "12/11/2018");
        contentValues.put(DatabaseHelper2.COL_4, newTotal);
        contentValues.put(DatabaseHelper2.COL_5, newInterupted);
        db.update(DatabaseHelper2.TABLE_NAME, contentValues, "Count_ID ="+id, null);

    }

    public void barChart() {

        openHelper=new DatabaseHelper2(this);
        db = openHelper.getReadableDatabase();

        DatabaseHelper2 db = new DatabaseHelper2(getApplicationContext());
        List<String> assignment = db.getHours();

        chart = (BarChart) findViewById(R.id.BarChart);

        int size = assignment.size();
        String sizeString = Integer.toString(size);
        Log.d("AssignmentSize", sizeString);

        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
        for (int i =0; i < assignment.size(); i++){
            Log.d("AssignmentList", assignment.get(i));
            int hour = Integer.parseInt(assignment.get(i));
            barEntries.add(new BarEntry(i, hour));
        }

//        barEntries.add(new BarEntry(2f, 1));
//        barEntries.add(new BarEntry(3f, 2));
//        barEntries.add(new BarEntry(4f, 3));
//        barEntries.add(new BarEntry(5f, 4));
//        barEntries.add(new BarEntry(6f, 5));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Study Statistics");
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);


        BarData data = new BarData(dataSets);

        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.invalidate();

//        if (first == true){
//            first = false;
//            chart.setData(data);
//        }
//        else {
//
//
//        }


        //chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        //chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    // intents to open activities for nav bar

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openCoursesActivity(){
        Intent intent = new Intent(this, Courses.class);
        startActivity(intent);
    }

    public void openUserActivity(){
        Intent intent = new Intent(this, User.class);
        startActivity(intent);
    }

    public void openStudyTimerActivity() {
        Intent intent = new Intent(this, StudyTimer.class);
        startActivity(intent);
    }
    }