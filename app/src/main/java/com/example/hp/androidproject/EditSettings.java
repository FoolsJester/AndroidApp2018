package com.example.hp.androidproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class EditSettings extends AppCompatActivity {

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;

//    EditText name;
//    EditText email;
//    EditText uni;
//    EditText course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_settings);

        createTextField();
        setText();


        Button submit = (Button) findViewById(R.id.submit);
        final EditText name = (EditText) findViewById(R.id.edit_name);
        final EditText email = (EditText) findViewById(R.id.edit_email);
        final EditText uni = (EditText) findViewById(R.id.edit_uni);
        final EditText course = (EditText) findViewById(R.id.edit_course);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String new_name = name.getText().toString();
                String new_mail = email.getText().toString();
                String new_uni = uni.getText().toString();
                String new_course = course.getText().toString();

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseHelper2.STUDENT_2, new_name);
                contentValues.put(DatabaseHelper2.STUDENT_3, new_mail);
                contentValues.put(DatabaseHelper2.STUDENT_4, new_uni);
                contentValues.put(DatabaseHelper2.STUDENT_5, new_course);
                db.update(DatabaseHelper2.STUDENT_TABLE, contentValues, "Count_ID ="+1, null);

                openHelper = new DatabaseHelper2(EditSettings.this);
                db = openHelper.getReadableDatabase();

                for (int i = 0; i < 2; i ++) {

                    Toast.makeText(getApplicationContext(), "Changed Hours must be approved by Course Administrator", Toast.LENGTH_LONG).show();
                }
                openSettingsActivity();

            }
        });

    }



    public void setText(){

        openHelper = new DatabaseHelper2(this);
        db = openHelper.getReadableDatabase();

        DatabaseHelper2 db = new DatabaseHelper2(getApplicationContext());
        List<String> info = db.getUserInfo();

        EditText name = (EditText) findViewById(R.id.edit_name);
        EditText email = (EditText) findViewById(R.id.edit_email);
        EditText uni = (EditText) findViewById(R.id.edit_uni);
        EditText course = (EditText) findViewById(R.id.edit_course);

        name.setText(info.get(0));
        email.setText(info.get(1));
        uni.setText(info.get(2));
        course.setText(info.get(3));





    }



    public void createTextField() {

        int idTracker = 100;

        LinearLayout check = (LinearLayout) findViewById(R.id.LinearLayout);

        openHelper = new DatabaseHelper2(this);
        db = openHelper.getReadableDatabase();

        final DatabaseHelper2 db1 = new DatabaseHelper2(getApplicationContext());
        final List<String> courses = db1.getAll();

        for (int i = 0; i < courses.size(); i += 4) {

            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.33f);

            textparams.gravity = Gravity.CENTER_VERTICAL;

            final TextView coursecode = new TextView(this);
            final EditText productiveHours = new EditText(this);
            final EditText TotalHours = new EditText(this);
            final LinearLayout layout = new LinearLayout(this);
            final Button delete = new Button(this);

            layout.setLayoutParams(lparams);

            coursecode.setLayoutParams(textparams);
            coursecode.setTextSize(18);
            coursecode.setText(courses.get(i+1));
            coursecode.setGravity(Gravity.CENTER);
            coursecode.setId(View.generateViewId());

            productiveHours.setLayoutParams(textparams);
            productiveHours.setText(courses.get(i + 2));
            productiveHours.setGravity(Gravity.CENTER);
            productiveHours.setInputType(InputType.TYPE_CLASS_NUMBER);
            productiveHours.setId(i);
            productiveHours.setId(idTracker--);

            TotalHours.setLayoutParams(textparams);
            TotalHours.setText(courses.get(i + 3));
            TotalHours.setGravity(Gravity.CENTER);
            TotalHours.setInputType(InputType.TYPE_CLASS_NUMBER);
            TotalHours.setId(idTracker--);

            delete.setLayoutParams(textparams);
            delete.setText("Remove Course");

            final String id = courses.get(i);

            delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                  //  db.execSQL("DELETE FROM "+DatabaseHelper2.TABLE_NAME+" WHERE "+DatabaseHelper2.COL_1+" = '"+id+"'");
                  db.delete(DatabaseHelper2.TABLE_NAME, DatabaseHelper2.COL_1+"=?", new String[]{id});

                  layout.removeAllViews();

                }
            });

            layout.addView(coursecode);
            layout.addView(productiveHours);
            layout.addView(TotalHours);
            layout.addView(delete);

            check.addView(layout);

        }

    }

    public void openSettingsActivity() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

}
