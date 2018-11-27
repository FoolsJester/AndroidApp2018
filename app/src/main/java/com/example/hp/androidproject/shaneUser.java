package com.example.hp.androidproject;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;


import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class shaneUser extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private Button timeStudy;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;

    BarChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shane_user);

//        // initialising variables for nav bar
//        dl = (DrawerLayout) findViewById(R.id.dl);
//        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
//        abdt.setDrawerIndicatorEnabled(true);
//        dl.addDrawerListener(abdt);
//        abdt.syncState();
//        timeStudy = (Button) findViewById(R.id.button1);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
//        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//
//                if (id == R.id.myprofile) {
//                    openUserActivity();
//                } else if (id == R.id.study) {
//                    Toast.makeText(shaneUser.this, "Study Page", Toast.LENGTH_SHORT).show();
//                } else if (id == R.id.course) {
//                    openCoursesActivity();
//                } else if (id == R.id.login) {
//                    openMainActivity();
//                }
//
//                return true;
//            }
//
//        });
//
//
        barChart();
        createTextField();

        Button david = (Button) findViewById(R.id.DavidButton);
        david.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserActivity();
            }
        });

        Button amy = (Button) findViewById(R.id.AmyButton);
        amy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAmy();
            }
        });

        Button eimear = (Button) findViewById(R.id.EimearButton);
        eimear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEimear();
            }
        });

    }

    public void createTextField() {

        LinearLayout check = (LinearLayout) findViewById(R.id.linearLayout);

        openHelper = new DatabaseHelperLocalDB(this);
        db = openHelper.getReadableDatabase();


        DatabaseHelperLocalDB db = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> courses = db.getCourseName();

        for (int i = 2; i < courses.size()-2; i+=2) {

            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lparams.setMargins(35,5,0,5);

            LinearLayout.LayoutParams newActivityParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 70);
            newActivityParams.setMargins(80,0,80,5);

            Button course = new Button(this);
            final TextView assignment = new TextView(this);
            final Button toCourse = new Button(this);

            course.setLayoutParams(lparams);
            course.setText(courses.get(i)+" - "+courses.get(i+1));
            course.setId(i+1);
            course.setPadding(10,0,10,0);
            course.setBackgroundColor(Color.WHITE);
            course.setTransformationMethod(null);

            assignment.setLayoutParams(lparams);
            assignment.setVisibility(View.GONE);
            assignment.setTextSize(15);
            assignment.setText("\tProgramming Lab 2: INCOMPLETE\n\tAverage Completion Time: 30 minutes\n");

            toCourse.setLayoutParams(newActivityParams);
            toCourse.setText("View "+courses.get(i+1));
            toCourse.setId(i+1);
            toCourse.setPadding(0,0,0,0);
            toCourse.setBackgroundColor(getResources().getColor(R.color.colorAccent, getTheme()));
            toCourse.setVisibility(View.GONE);
            toCourse.setTransformationMethod(null);

            course.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if(assignment.getVisibility()==View.GONE){
                        assignment.setVisibility(View.VISIBLE);
                        toCourse.setVisibility(View.VISIBLE);}
                    else if(assignment.getVisibility()==View.VISIBLE){
                        assignment.setVisibility(View.GONE);
                        toCourse.setVisibility(View.GONE);}
                }
            });

            check.addView(course);
            check.addView(assignment);
            check.addView(toCourse);

        }


        //https://stackoverflow.com/questions/4203506/how-to-add-a-textview-to-a-linearlayout-dynamically-in-android
    }

    public void barChart(){
        openHelper=new DatabaseHelperLocalDB(this);
        db = openHelper.getReadableDatabase();

        DatabaseHelperLocalDB db = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> assignment = db.getHours();

        chart = (BarChart) findViewById(R.id.BarChart);

//        int size = assignment.size();
//        String sizeString = Integer.toString(size);
//        Log.d("AssignmentSize", sizeString);

        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
        for (int i =2; i < assignment.size()-2; i+=2){

            int hour = ThreadLocalRandom.current().nextInt(10, 35);
            int interupted = ThreadLocalRandom.current().nextInt(1, 10);

            barEntries.add(new BarEntry(i, new float[] {hour-interupted, interupted}));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "  ");
        barDataSet.setColors(new int[]{ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimary),});
        barDataSet.setStackLabels(new String[]{"Productive Study", "Unproductive Study"});
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);


        BarData data = new BarData(dataSets);

        List<String> labels = db.getCourseName();
        labels.remove(labels.size()-2);

        XAxis xAxis = chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12);

        //xAxis.setLabelRotationAngle(30);
        xAxis.setLabelCount(3);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));


        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setData(data);
    }

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

        public void openAmy() {
            Intent intent = new Intent(this, amyUser.class);
            startActivity(intent);
        }

        public void openEimear() {
            Intent intent = new Intent(this, eimearUser.class);
            startActivity(intent);
        }

        public void openHome(View view) {
            Intent intent = new Intent(this, User.class);
            startActivity(intent);
        }


    public void openAmyV(View view) {
        Intent intent = new Intent(this, amyUser.class);
        startActivity(intent);
    }


    public void openEimearV(View view) {
        Intent intent = new Intent(this, eimearUser.class);
        startActivity(intent);
    }
    }
