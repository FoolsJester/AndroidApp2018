package com.example.hp.androidproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.lapism.searchview.Search;
import com.lapism.searchview.database.SearchHistoryTable;
import com.lapism.searchview.widget.SearchAdapter;
import com.lapism.searchview.widget.SearchItem;
import com.lapism.searchview.widget.SearchView;


import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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





//        SearchItem suggestion = new SearchItem(this);
//        suggestion.setTitle("Title");
//      //  suggestion.setIcon_1_resource(R.drawable.search_ic_search_black_24dp);
//        suggestion.setSubtitle("Subtitle");
//
//        List<SearchItem> suggestions = new ArrayList<>();
//        suggestions.add(suggestion);
//
//        final SearchHistoryTable mHistoryDatabase = new SearchHistoryTable(this);
//
//        SearchAdapter searchAdapter = new SearchAdapter(this);
//        searchAdapter.setSuggestionsList(suggestions);
//        searchAdapter.setOnSearchItemClickListener(new SearchAdapter.OnSearchItemClickListener() {
//            @Override
//            public void onSearchItemClick(int position, CharSequence title, CharSequence subtitle) {
//                SearchItem item = new SearchItem(User.this);
//                item.setTitle(title);
//                item.setSubtitle(subtitle);
//
//                mHistoryDatabase.addItem(item);
//            }
//        });
//
//        SearchView searchView = findViewById(R.id.searchView);
//        searchView.setOnQueryTextListener(new Search.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(CharSequence query) {
//                SearchItem item = new SearchItem(User.this);
//                item.setTitle(query);
//
//                mHistoryDatabase.addItem(item);
//                return true;
//            }
//
//            @Override
//            public void onQueryTextChange(CharSequence newText) {
//                //return false;
//                int x = 1;
//            }
//        });


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
        createTextField();

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

    public void createTextField() {

        LinearLayout check = (LinearLayout) findViewById(R.id.linearLayout);

        openHelper = new DatabaseHelper2(this);
        db = openHelper.getReadableDatabase();

        DatabaseHelper2 db = new DatabaseHelper2(getApplicationContext());
        List<String> courses = db.getAssignments();

        int[] myIntArray = new int[courses.size()];

        for (int i = 0; i < courses.size(); i++) {

            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            Button course = new Button(this);
            final TextView assignment = new TextView(this);
            final Button toCourse = new Button(this);

            course.setLayoutParams(lparams);
            course.setText(courses.get(i));
            course.setId(i+1);
            assignment.setLayoutParams(lparams);
            assignment.setVisibility(View.GONE);
            assignment.setText("\tProgramming Lab 2: INCOMPLETE\n\t Average Completion Time: 30 minutes");
            toCourse.setLayoutParams(lparams);
            toCourse.setText("View "+courses.get(i));
            toCourse.setId(i+1);
            toCourse.setVisibility(View.GONE);

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

//        int size = assignment.size();
//        String sizeString = Integer.toString(size);
//        Log.d("AssignmentSize", sizeString);

        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
        for (int i =0; i < assignment.size(); i+=2){
            int hour = Integer.parseInt(assignment.get(i));
            int interupted = Integer.parseInt(assignment.get(i+1));
            barEntries.add(new BarEntry(i, new float[] {hour-interupted, interupted}));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "  ");
        barDataSet.setColors(new int[]{ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimary),});
        barDataSet.setStackLabels(new String[]{"Productive Study", "Unproductive Study"});
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);


        BarData data = new BarData(dataSets);

        List<String> labels = db.getAssignments();

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setLabelCount(labels.size());


        chart.setDrawGridBackground(false);

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0){
            chart.setData(data);
        }
        else {
            chart.setData(data);
            chart.notifyDataSetChanged();
            chart.invalidate();
        }

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