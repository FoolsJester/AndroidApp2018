package com.example.hp.androidproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class EditSettings extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
    private StorageReference mStorageRef;
    // initialising variables for form, button, and length holders
    Button cameraButton, galleryButton, saveButton;
    private ImageView newProfilePic;
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

        mStorageRef = FirebaseStorage.getInstance().getReference("profilePicture.jpg");
        newProfilePic = (ImageView) findViewById(R.id.imageView2);
        cameraButton = (Button) findViewById(R.id.cameraButton);
        galleryButton = (Button) findViewById(R.id.galleryButton);
        Button submit = (Button) findViewById(R.id.submit);
        final EditText name = (EditText) findViewById(R.id.edit_name);
        final EditText email = (EditText) findViewById(R.id.edit_email);
        final EditText uni = (EditText) findViewById(R.id.edit_uni);
        final EditText course = (EditText) findViewById(R.id.edit_course);
        // Reference to an image file in Cloud Storage
        final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("profilePicture.jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        mStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                newProfilePic.setImageBitmap(bm);
            }
        });


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Retrieve bitmap https://stackoverflow.com/questions/26865787/get-bitmap-from-imageview-in-android-l/27030439
                newProfilePic.invalidate();
                BitmapDrawable drawable = (BitmapDrawable) newProfilePic.getDrawable();
                Bitmap getphoto = drawable.getBitmap();
                //Get the uri https://www.codeproject.com/Questions/741165/why-capture-image-path-return-null-in-android
                Uri storageUri = getImageUri(getApplicationContext(), getphoto);
                //Store in Firebase Storage
                mStorageRef.putFile(storageUri);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK ) {
            Uri uri = data.getData();
            try {
                Bitmap getPhoto = (Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                newProfilePic.setImageBitmap(getPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Tutorial on how to upload files from gallery to phone and display in imageview https://www.youtube.com/watch?v=oTBkrTsKyPc

        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            //Get image captured
            Uri uri = data.getData();
            Bitmap getphoto = (Bitmap) data.getExtras().get("data");
            //Set image as new bitmap
            newProfilePic.setImageBitmap(getphoto);
            //Store to phone
//            ContentValues values = new ContentValues();
//            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            //Tutorial on how to store access camera https://www.youtube.com/watch?v=Zy2DKo0v-OY"
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "profilePicture", null);
            return Uri.parse(path);
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

        for (int i = 1; i < courses.size(); i += 5) {

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
            coursecode.setText(courses.get(i));
            coursecode.setGravity(Gravity.CENTER);
            coursecode.setId(View.generateViewId());

            productiveHours.setLayoutParams(letters);
            int prodMins = Integer.parseInt(courses.get(i + 3));
            double prodHours = (double)prodMins/60;
            double roundProdHours = (double) Math.round(prodHours* 100) / 100;
            productiveHours.setText(Double.toString(roundProdHours));
            productiveHours.setGravity(Gravity.CENTER);
            productiveHours.setInputType(InputType.TYPE_CLASS_NUMBER);
            productiveHours.setId(i);
            productiveHours.setId(idTracker--);

            TotalHours.setLayoutParams(letters);
            int totalMins = Integer.parseInt(courses.get(i+2));
            double totalHours = (double)totalMins/60;
            double roundHours = (double) Math.round(totalHours * 100) / 100;
            TotalHours.setText(Double.toString(roundHours));
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
