package com.example.hp.androidproject;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class EditSettings extends AppCompatActivity {

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_settings);

        createTextField();
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

            LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.33f);

            textparams.gravity = Gravity.CENTER_VERTICAL;

            final EditText coursecode = new EditText(this);
            final EditText productiveHours = new EditText(this);
            final EditText TotalHours = new EditText(this);
            final LinearLayout layout = new LinearLayout(this);

            layout.setLayoutParams(lparams);

            coursecode.setLayoutParams(textparams);
            coursecode.setTextSize(18);
            coursecode.setText(courses.get(i+1));
            coursecode.setGravity(Gravity.CENTER);

            productiveHours.setLayoutParams(textparams);
            productiveHours.setText(courses.get(i + 2));
            productiveHours.setGravity(Gravity.CENTER);
            productiveHours.setInputType(InputType.TYPE_CLASS_NUMBER);

            TotalHours.setLayoutParams(textparams);
            TotalHours.setText(courses.get(i + 3));
            TotalHours.setGravity(Gravity.CENTER);
            productiveHours.setInputType(InputType.TYPE_CLASS_NUMBER);

            layout.addView(coursecode);
            layout.addView(productiveHours);
            layout.addView(TotalHours);
            check.addView(layout);

        }

    }

}
