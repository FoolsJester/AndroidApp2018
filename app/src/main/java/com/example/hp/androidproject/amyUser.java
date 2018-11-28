package com.example.hp.androidproject;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class amyUser extends AppCompatActivity {

    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle abdt;
    private DatabaseReference myRef;
    private final String TAG = "UserInfo";
    private DataSnapshot globalSnapshot;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;

    BarChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amy_user);

        //Get firebase instance and initialise reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                globalSnapshot = dataSnapshot;
                createTextField();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        // initialising variables for nav bar
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        abdt = new ActionBarDrawerToggle(this, drawerlayout, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        drawerlayout.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.myprofile) {
                    openUserActivity();
                } else if (id == R.id.study) {
                    openStudyTimerActivity();
                } else if (id == R.id.course) {
                    openCoursesActivity();
                } else if (id == R.id.login) {
                    openMainActivity();
                }
                else if(id == R.id.settings){
                    openSettings();
                }
                else if( id == R.id.search){
                    openSearchActivity();
                }

                return true;
            }
        });


        barChart();

        Button david = (Button) findViewById(R.id.DavidButton);
        david.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserActivity();
            }
        });

        Button shane = (Button) findViewById(R.id.ShaneButton);
        shane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShane();
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

        for (int i = 2; i < courses.size(); i+=2) {

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

            toCourse.setLayoutParams(newActivityParams);
            toCourse.setText("View "+courses.get(i+1));
            toCourse.setId(i+1);
            toCourse.setPadding(0,0,0,0);
            toCourse.setBackgroundColor(getResources().getColor(R.color.colorAccent, getTheme()));
            toCourse.setVisibility(View.GONE);
            toCourse.setTransformationMethod(null);

            /*
             * Just a lil' function to take assignments from DB
             *
             * Takes items from the global dataSnapshot for each of the courses the user is
             * enrolled in. If there are no assignments in that course it says so.
             * */
            String courseCode = courses.get(i);
            if(globalSnapshot.child(courses.get(i)).hasChild("assignments")){//checks to see if has any assignments

                for (DataSnapshot ds: globalSnapshot.child(courseCode).child("assignments").getChildren()){// for each assignment
                    //initialise 2 variables for returning
                    String dbAassig = ds.child("title").getValue().toString();
                    String dbComp;
                    //Log.d("Test", dbAassig);
                    if (ds.child("title").getValue().toString()=="true"){ // this isn't the best way to do this but it'll do for now
                        dbComp = "INCOMPLETE";
                    }
                    else{
                        dbComp = "COMPLETE";
                    }
                    int randomNum = ThreadLocalRandom.current().nextInt(20, 120);

                    assignment.setText("\t\t"+dbAassig + ": " + dbComp + "\n\t\tAverage Completion Time: "+randomNum+" minutes");
                }
            }
            else{
                assignment.setText("No assignments yet in this course");
            }


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


        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
        for (int i =2; i < assignment.size(); i+=2){

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
        labels.remove(0);
        labels.remove(0);

        XAxis xAxis = chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setCenterAxisLabels(false);

        xAxis.setTextSize(12);

        //xAxis.setLabelRotationAngle(30);
        xAxis.setLabelCount(3);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));


        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setData(data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void openSearchActivity(){
        Intent intent = new Intent(this, SearchCouses.class);
        startActivity(intent);
    }

    public void openSettings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
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

    public void openShane() {
        Intent intent = new Intent(this, shaneUser.class);
        startActivity(intent);
    }


    public void openEimear() {
        Intent intent = new Intent(this, eimearUser.class);
        startActivity(intent);
    }

    public void openShaneV(View view) {
        Intent intent = new Intent(this, shaneUser.class);
        startActivity(intent);
    }

    public void openHome(View view) {
        Intent intent = new Intent(this, User.class);
        startActivity(intent);
    }

    public void openEimearV(View view) {
        Intent intent = new Intent(this, eimearUser.class);
        startActivity(intent);
    }
}
