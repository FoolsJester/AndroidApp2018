package com.example.hp.androidproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Settings extends AppCompatActivity {

    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle abdt;

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        createTextField();
        setText();

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
                    openUser();
                } else if (id == R.id.study) {
                    Toast.makeText(Settings.this, "Study Page", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.course) {
                    openCourses();
                } else if (id == R.id.settings) {
                    openSettings();
                } else if (id == R.id.login) {
                    openMainActivity();
                }
                else if( id == R.id.search){
                    openSearch();
                }

                return true;
            }
        });


        Button addStudy = (Button) findViewById(R.id.button4);
        addStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditSettingsActivity();
            }
        });

    }

    public void setText() {

        TextView name = (TextView) findViewById(R.id.name_settings);
        TextView email = (TextView) findViewById(R.id.email_settings);
        TextView uni = (TextView) findViewById(R.id.uni_settings);
        TextView course = (TextView) findViewById(R.id.course_settings);

        openHelper = new DatabaseHelperLocalDB(this);
        db = openHelper.getReadableDatabase();

        DatabaseHelperLocalDB db = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> info = db.getUserInfo();

        name.setText(info.get(0));
        email.setText(info.get(1));
        uni.setText(info.get(2));
        course.setText(info.get(3));

    }


    public void createTextField() {

        LinearLayout check = (LinearLayout) findViewById(R.id.LinearLayout);

        openHelper = new DatabaseHelperLocalDB(this);
        db = openHelper.getReadableDatabase();

        DatabaseHelperLocalDB db = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> courses = db.getAll();

        for (int i = 0; i < courses.size(); i += 4) {

            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            final TextView coursecode = new TextView(this);


            coursecode.setLayoutParams(lparams);
            coursecode.setTextSize(18);
            coursecode.setText(courses.get(i + 1) + "\t\t\t\t\t\t\t" + courses.get(i + 2) + "\t\t\t\t\t\t\t\t\t\t" + courses.get(i + 3));
            check.addView(coursecode);


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void openEditSettingsActivity() {
        Intent intent = new Intent(this, EditSettings.class);
        startActivity(intent);
    }

    public void openCourses() {
        Intent intent = new Intent(this, Courses.class);
        startActivity(intent);
    }

    public void openUser() {
        Intent intent = new Intent(this, User.class);
        startActivity(intent);
    }

    public void openSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void openSearch(){
        Intent intent = new Intent(this, SearchCouses.class);
        startActivity(intent);
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}

