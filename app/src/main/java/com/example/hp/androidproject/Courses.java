package com.example.hp.androidproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.List;
import android.widget.ArrayAdapter;
import java.util.ArrayList;





public class Courses extends AppCompatActivity  {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    Button enrolButton, assignmentButton;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    EditText _txtname, _txtduedate, _txtdescription, _txtpercentworth;
    Spinner spinner;
    private Button topic1, topic2, topic3;
    private Spinner spin;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // initialising variables
        dl = (DrawerLayout)findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
//        addListenerOnSpinnerItemSelection();



        // initialising variables for assignment form and forum links
        assignmentButton = (Button)findViewById(R.id.assignmentButton);
        openHelper = new DatabaseHelper(this);
        _txtname = (EditText)findViewById(R.id.txtname);
        _txtduedate = (EditText)findViewById(R.id.txtduedate);
        _txtdescription = (EditText)findViewById(R.id.txtdescription);
        _txtpercentworth = (EditText)findViewById(R.id.txtpercentworth);
        topic1 = (Button)findViewById(R.id.topic1);
        topic2 = (Button)findViewById(R.id.topic2);
        topic3 = (Button)findViewById(R.id.topic3);
        spinner = (Spinner) findViewById(R.id.spinner);
        spin = (Spinner) findViewById(R.id.spin);
        loadAssignmentData();

        // event to add values from form into database when assignment button is clicked
        assignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = openHelper.getWritableDatabase();
                String name = _txtname.getText().toString();
                String dueData = _txtduedate.getText().toString();
                String description = _txtdescription.getText().toString();
                Integer percentWorth = Integer.valueOf(_txtpercentworth.getText().toString());
                insertData(name, dueData, description, percentWorth);
                Toast.makeText(getApplicationContext(), "assignment is added", Toast.LENGTH_LONG).show();
                loadAssignmentData();
            }
        });

        // tutorials for creating form with database: https://www.youtube.com/watch?v=B2avB5tmTMM
        // https://techsupportnep.com/programming/android/android-login-and-register-with-sqlite-database.html



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                int id = item.getItemId();

                if( id == R.id.myprofile){
                    Toast.makeText(Courses.this, "MyProfile", Toast.LENGTH_SHORT).show();
                }
                else if( id == R.id.study){
                    Toast.makeText(Courses.this, "Study Page", Toast.LENGTH_SHORT).show();
                }
                else if( id == R.id.course){
                    Toast.makeText(Courses.this, "Course Page", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        Spinner s = (Spinner) findViewById(R.id.spin);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Courses.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                openActivityAssignments();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        Button enrol = (Button) findViewById(R.id.enrolButton);
        enrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "you are now enrolled", Toast.LENGTH_LONG).show();
            }
        });

//       button tutorial: https://abhiandroid.com/ui/button

        topic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityTopicOne();
            }
        });
        topic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityTopicTwo();
            }
        });
        topic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityTopicThree();
            }
        });


    }

//    private void addListenerOnSpinnerItemSelection() {
//        spin = (Spinner) findViewById(R.id.spin);
//        spin.setOnItemSelectedListener(new CustomOnItemSelectedListener());
//    }

    public void openActivityTopicOne(){
        Intent intent = new Intent(this, TopicOne.class);
        startActivity(intent);
    }
    public void openActivityTopicTwo(){
        Intent intent = new Intent(this, TopicTwo.class);
        startActivity(intent);
    }
    public void openActivityTopicThree(){
        Intent intent = new Intent(this, TopicThree.class);
        startActivity(intent);
    }

    public void openActivityAssignments(){
        Intent intent = new Intent(this, Assignments.class);
        startActivity(intent);
    }


    // method that inserts value from form into database
    public void insertData(String name, String dueData, String description, Integer percentWorth){
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.COL_2, name);
            contentValues.put(DatabaseHelper.COL_3, dueData);
            contentValues.put(DatabaseHelper.COL_4, description);
            contentValues.put(DatabaseHelper.COL_5, percentWorth);
            long id = db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
            Toast.makeText(Courses.this,"New row added, row id: " + id + name + dueData + description + percentWorth, Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(Courses.this,"Something wrong", Toast.LENGTH_SHORT).show();
        }

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    // function to load data from database into spinner (drop down menu)
    private void loadAssignmentData() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List <String> assignment = db.getAssignments();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, assignment);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        // tutorial to get data from database into spinner: https://www.androidhive.info/2012/06/android-populating-spinner-data-from-sqlite-database/
    }

}



