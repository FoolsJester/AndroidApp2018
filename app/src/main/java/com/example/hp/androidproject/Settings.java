package com.example.hp.androidproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class Settings extends AppCompatActivity {

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        createTextField();
        setText();

        Button addStudy = (Button) findViewById(R.id.button4);
        addStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditSettingsActivity();
            }
        });

    }

    public void setText(){

        TextView name = (TextView) findViewById(R.id.name_settings);
        TextView email = (TextView) findViewById(R.id.email_settings);
        TextView uni = (TextView) findViewById(R.id.uni_settings);
        TextView course = (TextView) findViewById(R.id.course_settings);

        openHelper = new Database(this);
        db = openHelper.getReadableDatabase();

        Database db = new Database(getApplicationContext());
        List<String> info = db.getUserInfo();

        Log.d("info", info.toString());

        name.setText(info.get(0));
        email.setText(info.get(1));
        uni.setText(info.get(2));
        course.setText(info.get(3));

    }


    public void createTextField() {

        LinearLayout check = (LinearLayout) findViewById(R.id.LinearLayout);

        openHelper = new DatabaseHelper2(this);
        db = openHelper.getReadableDatabase();

        DatabaseHelper2 db = new DatabaseHelper2(getApplicationContext());
        List<String> courses = db.getAll();

        for (int i = 0; i < courses.size(); i += 4) {

            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            final TextView coursecode = new TextView(this);
            final TextView productiveHours = new TextView(this);
            final TextView TotalHours = new TextView(this);


            coursecode.setLayoutParams(lparams);
            coursecode.setTextSize(18);
            coursecode.setText(courses.get(i+1)+"\t\t\t\t\t\t\t"+courses.get(i+2)+"\t\t\t\t\t\t\t\t\t\t"+courses.get(i+3));
            check.addView(coursecode);


        }

    }
    public void openEditSettingsActivity() {
        Intent intent = new Intent(this, EditSettings.class);
        startActivity(intent);
    }
}

