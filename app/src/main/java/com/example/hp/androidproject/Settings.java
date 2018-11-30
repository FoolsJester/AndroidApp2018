package com.example.hp.androidproject;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static com.example.hp.androidproject.BaseApp.Channel_2_ID;
public class Settings extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;


    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle abdt;

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        notificationManager = NotificationManagerCompat.from(this);
        createTextField();
        setText();

        // initialising variables for nav bar
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        abdt = new ActionBarDrawerToggle(this, drawerlayout, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        drawerlayout.addDrawerListener(abdt);
        abdt.syncState();
        // Reference to an image file in Cloud Storage
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("profilePicture.jpg");
        final ImageView profilePhoto = findViewById(R.id.imageView2);
        final long ONE_MEGABYTE = 1024 * 1024;
        mStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                profilePhoto.setImageBitmap(bm);
            }
        });

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

        for (int i = 1; i < courses.size(); i += 5) {

            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            final TextView coursecode = new TextView(this);


            coursecode.setLayoutParams(lparams);
            coursecode.setTextSize(18);
            int prodMins = Integer.parseInt(courses.get(i + 3));
            double prodHours = (double)prodMins/60;
            double roundHours = (double) Math.round(prodHours * 100) / 100;
            int totalMins = Integer.parseInt(courses.get(i+2));
            double totalHours = (double)totalMins/60;
            double totalRoundHours = (double) Math.round(totalHours* 100) / 100;
            coursecode.setText(courses.get(i) + "\t\t\t\t\t\t\t\t\t\t\t" + roundHours + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + totalRoundHours);
            check.addView(coursecode);


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void openEditSettingsActivity() {
        discussionForum();
        Intent intent = new Intent(this, EditSettings.class);
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
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void discussionForum(){
        Intent intent = new Intent(this, IOTprogramming.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, Channel_2_ID)
                .setSmallIcon(R.drawable.ic_discussion_forum)
                .setContentTitle("New Discussion Forum")
                .setContentText("A new discussion forum has been created in COMP47520")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(2, notification);
    }

}

