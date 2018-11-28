package com.example.hp.androidproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class EditSettings extends AppCompatActivity {
    private Button uploadImage;
    private ImageView newProfilePic;
    private static final int CAMERA_REQUEST_CODE = 1;
    private ProgressBar imageUploadProgress;
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

        uploadImage = (Button) findViewById(R.id.uploadImage);
        newProfilePic = (ImageView) findViewById(R.id.imageView2);
        Button submit = (Button) findViewById(R.id.submit);
        final EditText name = (EditText) findViewById(R.id.edit_name);
        final EditText email = (EditText) findViewById(R.id.edit_email);
        final EditText uni = (EditText) findViewById(R.id.edit_uni);
        final EditText course = (EditText) findViewById(R.id.edit_course);
        imageUploadProgress = new ProgressBar(this);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String new_name = name.getText().toString();
                String new_mail = email.getText().toString();
                String new_uni = uni.getText().toString();
                String new_course = course.getText().toString();

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseHelperLocalDB.STUDENT_2, new_name);
                contentValues.put(DatabaseHelperLocalDB.STUDENT_3, new_mail);
                contentValues.put(DatabaseHelperLocalDB.STUDENT_4, new_uni);
                contentValues.put(DatabaseHelperLocalDB.STUDENT_5, new_course);
                db.update(DatabaseHelperLocalDB.STUDENT_TABLE, contentValues, "Count_ID =" + 1, null);

                openHelper = new DatabaseHelperLocalDB(EditSettings.this);
                db = openHelper.getReadableDatabase();

                for (int i = 0; i < 2; i++) {

                    Toast.makeText(getApplicationContext(), "Changed Hours must be approved by Course Administrator", Toast.LENGTH_LONG).show();
                }
                openSettingsActivity();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            Bitmap getphoto = (Bitmap) data.getExtras().get("data");
            newProfilePic.setImageBitmap(getphoto);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Tutorial on how to store access camera https://www.youtube.com/watch?v=Zy2DKo0v-OY"
        }
    }

    public void setText() {

        openHelper = new DatabaseHelperLocalDB(this);
        db = openHelper.getReadableDatabase();

        DatabaseHelperLocalDB db = new DatabaseHelperLocalDB(getApplicationContext());
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

        openHelper = new DatabaseHelperLocalDB(this);
        db = openHelper.getReadableDatabase();

        final DatabaseHelperLocalDB db1 = new DatabaseHelperLocalDB(getApplicationContext());
        final List<String> courses = db1.getAll();

        for (int i = 0; i < courses.size(); i += 4) {

            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(
                    400, LinearLayout.LayoutParams.MATCH_PARENT, 1);

            LinearLayout.LayoutParams letters= new LinearLayout.LayoutParams(
                    200, LinearLayout.LayoutParams.MATCH_PARENT, 1);

       //     textparams.gravity = Gravity.CENTER_VERTICAL;

            lparams.setMargins(0,0,0,0);
            textparams.setMargins(0,0,0,10);

            final TextView coursecode = new TextView(this);
            final EditText productiveHours = new EditText(this);
            final EditText TotalHours = new EditText(this);
            final LinearLayout layout = new LinearLayout(this);
            final Button delete = new Button(this);

            layout.setLayoutParams(lparams);

            coursecode.setLayoutParams(textparams);
            coursecode.setTextSize(18);
            coursecode.setText(courses.get(i + 1));
            coursecode.setGravity(Gravity.CENTER);
            coursecode.setId(View.generateViewId());

            productiveHours.setLayoutParams(letters);
            productiveHours.setText(courses.get(i + 2));
            productiveHours.setGravity(Gravity.CENTER);
            productiveHours.setInputType(InputType.TYPE_CLASS_NUMBER);
            productiveHours.setId(i);
            productiveHours.setId(idTracker--);

            TotalHours.setLayoutParams(letters);
            TotalHours.setText(courses.get(i + 3));
            TotalHours.setGravity(Gravity.CENTER);
            TotalHours.setInputType(InputType.TYPE_CLASS_NUMBER);
            TotalHours.setId(idTracker--);

            delete.setLayoutParams(textparams);
            delete.setPadding(5,0,5,0);
            delete.setBackgroundColor(getResources().getColor(R.color.colorAccent, getTheme()));
            delete.setText("Remove Course");

            final String id = courses.get(i);

            delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //  db.execSQL("DELETE FROM "+DatabaseHelperLocalDB.TABLE_NAME+" WHERE "+DatabaseHelperLocalDB.COL_1+" = '"+id+"'");
                    db.delete(DatabaseHelperLocalDB.TABLE_NAME, DatabaseHelperLocalDB.COL_1 + "=?", new String[]{id});

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
