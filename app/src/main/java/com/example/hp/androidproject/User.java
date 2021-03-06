package com.example.hp.androidproject;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.hp.androidproject.BaseApp.Channel_2_ID;

public class User extends AppCompatActivity {
    /*
    This class displays all the user information, form the database
    and gets a list of all their enrolled courses, displays the assignments,
    study stats, allows the user to add to their stats and view their friends
     */

    private NotificationManagerCompat notificationManager;


    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle abdt;
    private Button timeStudy;
    private DatabaseReference myRef;
    private final String TAG = "UserInfo";
    private DataSnapshot globalSnapshot;


    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    BarChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

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

        DownloadTask task = new DownloadTask();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q=Dublin,ie&units=metric&appid=00a79b7ecabf9125273b86a8736392d6");

        notificationManager = NotificationManagerCompat.from(this);


            // initialising variables for nav bar
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        abdt = new ActionBarDrawerToggle(this, drawerlayout, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        drawerlayout.addDrawerListener(abdt);
        abdt.syncState();
        timeStudy = (Button)findViewById(R.id.button1);
        // Reference to an image file in Cloud Storage
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("profilePicture.jpg");
        //Setting profile photo to photo in cloud stoarage
        final ImageView profilePhoto = findViewById(R.id.imageView);
        final long ONE_MEGABYTE = 1024 * 1024;
        mStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            /*
            * Converts byte array from firebase storage to bitmap and sets profile photo of user
            */
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                profilePhoto.setImageBitmap(bm);
            }
            //Tutorial https://stackoverflow.com/questions/39702304/retrieve-stored-image-from-firebase-storage
        });

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
                } else if (id == R.id.login) {
                    openMainActivity();
                }
                else if(id == R.id.settings){
                    openSettings();
                }
                else if( id == R.id.search){
                    openSearch();
                }

                return true;
            }
        });


        setSpinner();               //functions to set contents of page text, bar chart and spinner
        barChart();
        setText();

        openHelper = new DatabaseHelperLocalDB(this);


        final Button set = (Button) findViewById(R.id.button2);
        final EditText hours = (EditText) findViewById(R.id.editText);
        final Spinner dropdown = findViewById(R.id.spinner1);


        Button shane = (Button) findViewById(R.id.ShaneButton);         //onClick functions to redirect user to friends page depending on the buttons they select
        shane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShane();
            }
        });

        Button amy = (Button) findViewById(R.id.AmyButton);
        amy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAmy();
            }
        });

        Button muireann = (Button) findViewById(R.id.MuireannButton);
        muireann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMuireann();
            }
        });


        Button addStudy = (Button) findViewById(R.id.button3);      //View and hide adding study time contents onclick
        addStudy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(dropdown.getVisibility()==View.GONE){
                    dropdown.setVisibility(View.VISIBLE);
                    set.setVisibility(View.VISIBLE);
                    hours.setVisibility(View.VISIBLE);
                    }
                else if(dropdown.getVisibility()==View.VISIBLE){
                    dropdown.setVisibility(View.GONE);
                    set.setVisibility(View.GONE);
                    hours.setVisibility(View.GONE);
                    }

            }
        });

        timeStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStudyTimerActivity();
            }
        });         //opening study time activity onclick

        set.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*
                Onclick function to get user inputted data and update the
                database with it
                 */
                db = openHelper.getWritableDatabase();

                String course = dropdown.getSelectedItem().toString();
                String study = hours.getText().toString();

                if (study == null) {            //ensure they entered a study time
                    Toast.makeText(getApplicationContext(), "Please enter study duration", Toast.LENGTH_LONG).show();
                }
                else {
                double studyInt = Double.parseDouble(study);

                updateData(course, studyInt);       //update the database
                Toast.makeText(getApplicationContext(), "register successfully", Toast.LENGTH_LONG).show();}
            }
        });
    }

    public void createTextField() {
        /*
        Function to take the information from the database
        and use it to populate course and assignment information
         */

        LinearLayout check = (LinearLayout) findViewById(R.id.linearLayout);

        openHelper = new DatabaseHelperLocalDB(this);
        db = openHelper.getReadableDatabase();


        DatabaseHelperLocalDB db = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> courses = db.getCourseName(); //getting course names and codes from DB

        for (int i = 0; i < courses.size(); i+=2) {

            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(              //setting layout parameters
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lparams.setMargins(35,5,0,10);

            LinearLayout.LayoutParams newActivityParams = new LinearLayout.LayoutParams(   //setting layout parameters
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            newActivityParams.setMargins(80,0,80,10);

            Button course = new Button(this);                   //Creating new Views
            final TextView assignment = new TextView(this);
            final Button toCourse = new Button(this);

            course.setLayoutParams(lparams);                            //adding parameters and contents to new views
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
            toCourse.setBackgroundColor(getResources().getColor(R.color.colorAccent, getTheme()));
            toCourse.setVisibility(View.GONE);
            //toCourse.setTransformationMethod(null);

            final String onClickCourse = courses.get(i+1);

            toCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    Onclick function to redirect the user to the correct course activity
                     */
                    if (onClickCourse.equals("Android Programming")){
                        Intent intent=new Intent(getBaseContext(),AndroidProgramming.class);
                        startActivity(intent);
                    }
                    else if (onClickCourse.equals("Programming for IoT")){
                        Intent intent=new Intent(getBaseContext(),IOTprogramming.class);
                        startActivity(intent);
                    }
                    else if (onClickCourse.equals("Java Programming")){
                        Intent intent=new Intent(getBaseContext(),JavaProgramming.class);
                        startActivity(intent);
                    }
                    else if (onClickCourse.equals("Data Analytics")){
                        Intent intent=new Intent(getBaseContext(),DataAnalytics.class);
                        startActivity(intent);
                    }
                }
            });

            /*
            * Just a lil' function to take assignments from DB
            *
            * Takes items from the global dataSnapshot for each of the courses the user is
            * enrolled in. If there are no assignments in that course it says so.
            * */
            String courseCode = courses.get(i);
            String allAssignments = "";
            if(globalSnapshot.child(courses.get(i)).hasChild("assignments")){//checks to see if has any assignments

                for (DataSnapshot ds: globalSnapshot.child(courseCode).child("assignments").getChildren()){// for each assignment
                    //initialise 2 variables for returning
                    String dbAassig = ds.child("title").getValue().toString();
                    String dbComp;

                    if (ds.child("title").getValue().toString()=="true"){
                        dbComp = "COMPLETED";
                    }
                    else{
                        dbComp = "INCOMPLETE";
                    }
                    int randomNum = ThreadLocalRandom.current().nextInt(20, 120);

                    //Add the contents all into one string to be set
                    allAssignments+=("\t\t"+dbAassig + ": " + dbComp + "\n\t\tAverage Completion Time: "+randomNum+" minutes\n\n");
                }
                //set contents of the assignment TextView
                assignment.setText(allAssignments);
            }
            else{
                assignment.setText("No assignments yet in this course\n");
            }

            course.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Makes the text field for assignments visibile/invisible onclick
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
        /*
        Function to populate the spinner with course information from the database
         */

        openHelper = new DatabaseHelperLocalDB(this);
        db = openHelper.getReadableDatabase();
        Spinner dropdown = findViewById(R.id.spinner1);


        DatabaseHelperLocalDB db = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> courseName = db.getCourseNameOnly();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courseName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdown.setAdapter(dataAdapter);

    }

    public void setText() {
        /*
        Populates text fields in the activity with informatio  from the
        Student Info database
         */

        TextView name = (TextView) findViewById(R.id.name_user);
        TextView uni = (TextView) findViewById(R.id.uni_user);
        TextView course = (TextView) findViewById(R.id.course_user);

        openHelper = new DatabaseHelperLocalDB(this);
        db = openHelper.getReadableDatabase();

        DatabaseHelperLocalDB db = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> info = db.getUserInfo();

        name.setText(info.get(0));
        uni.setText(info.get(2));
        course.setText(info.get(3));

    }

    public void updateData(String courseName, double hours) {
        /*
        Function to update the study time for specific modules within the database
         */

        DatabaseHelperLocalDB database = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> current_hours = database.getAll();     //return all information from the database

        int old_total = 0; int old_interupted = 0; int id = 0; String courseCode = " ";

        //loop through the return string and find the entry that is the course being updated
        for (int i = 0; i < current_hours.size(); i+=5) {
            if (courseName.equals(current_hours.get(i+2))){
                //get the old values for the productive and interupted study time
                id = Integer.parseInt(current_hours.get(i));
                courseCode= current_hours.get(i+1);
                old_total = Integer.parseInt(current_hours.get(i+3));
                old_interupted = Integer.parseInt(current_hours.get(i+4));
            }
        }
        // setting new total time, retrieving assumed interupted time of study (uses the average percentage of interupted time)
        int newTotal = old_total + (int) hours*60; double interuptedDouble = ((double)old_interupted/(double)old_total) * (hours*60);
        int newInterupted = (int) interuptedDouble + old_interupted;

        //saving data to local database
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelperLocalDB.COL_2, courseCode);
        contentValues.put(DatabaseHelperLocalDB.COL_3, courseName);
        contentValues.put(DatabaseHelperLocalDB.COL_4, newTotal);
        contentValues.put(DatabaseHelperLocalDB.COL_5, newInterupted);
        db.update(DatabaseHelperLocalDB.TABLE_NAME, contentValues, "Count_ID ="+id, null);

        //recreate bar chart with new values
        barChart();

    }

    public void barChart() {
        /*
        Function to retrieve study information from the database and
        plot that information with a bar chart.
         */

        openHelper=new DatabaseHelperLocalDB(this);
        db = openHelper.getReadableDatabase();

        DatabaseHelperLocalDB db = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> assignment = db.getHours(); //get the total and interupted hours from the database

        chart = (BarChart) findViewById(R.id.BarChart);

        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
        //add the total and interupted times as bar entries to the graph
        for (int i =0; i < assignment.size(); i+=2){
            int minutes = Integer.parseInt(assignment.get(i));
            float total_hour = (float) minutes/60;
            int interupted_mins = Integer.parseInt(assignment.get(i+1));
            float interupted = (float) interupted_mins/60;
            barEntries.add(new BarEntry(i, new float[] {total_hour-interupted, interupted}));

        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "  ");              //setting the data
        barDataSet.setColors(new int[]{ContextCompat.getColor(this, R.color.colorAccent),   //colour code the graph
                ContextCompat.getColor(this, R.color.colorPrimary),});
        barDataSet.setStackLabels(new String[]{"Productive Study", "Unproductive Study"});      //setting the lables for thr stacked plot
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);


        BarData data = new BarData(dataSets);

        List<String> labels = db.getCourseName();   //getting course names for the bar labels


        XAxis xAxis = chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);   //styling labels
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12);
        xAxis.setLabelCount(labels.size()/2);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);

        chart.setData(data);                //sets data and changes bar chart if there is a change in data
        chart.notifyDataSetChanged();
        chart.invalidate();


        //https://github.com/PhilJay/MPAndroidChart#examples reference

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    // intents to open activities for nav bar

    public void openMainActivity(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
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

    public void openShaneV(View view) {
        assignmentPostedNotification();
        Intent intent = new Intent(this, shaneUser.class);
        startActivity(intent);
    }

    public void openAmyV(View view) {
        Intent intent = new Intent(this, amyUser.class);
        startActivity(intent);
    }

    public void openMuireannV(View view) {
        Intent intent = new Intent(this, muireannUser.class);
        startActivity(intent);
    }

    public void openEimearV(View view) {
        Intent intent = new Intent(this, eimearUser.class);
        startActivity(intent);
    }

    public void openAmy() {
        Intent intent = new Intent(this, amyUser.class);
        startActivity(intent);
    }

    public void openMuireann() {
        Intent intent = new Intent(this, muireannUser.class);
        startActivity(intent);
    }

    public void openSettings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void openSearch(){
        courseJoinNotification();
        Intent intent = new Intent(this, SearchCouses.class);
        startActivity(intent);

    }

    /*
    * Asynchronous task that makes a call to an OpenWeatherMap API and reads the data that is returned
    */
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /*
        Reads the JSON data returned from the API call and assigns a title and message depending on weather information observed
        Title and message are passed to the weatherNotification method
        */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                String tempInfo = jsonObject.getString("main");
                JSONArray weatherArr = new JSONArray(weatherInfo);
                JSONObject weatherDescription =  weatherArr.getJSONObject(0);
                JSONObject temp = new JSONObject(tempInfo);
                String description = weatherDescription.getString("main");
                String temperature = temp.getString("temp");
                float tempFloat = Float.parseFloat(temperature);
                int tempInt = (int) Math.round(tempFloat);
                String title = "Today's weather: "+description+", "+tempInt+"°C";
                String message = "";
                if (tempInt < 6){
                    message = "Brrrrr ⛄! It's cold out today, best stay indoors and study. \uD83D\uDE09";
                }
                else if (description.equals("Thunderstorm") || description.equals("Drizzle") || description.equals("Rain") || description.equals("Snow") || description.equals("Atmosphere")){
                    message = "Oh dear \uD83D\uDE25! Not a great day today, best stay indoors and study. \uD83D\uDE09";
                }
                else{
                    message = "Nice out today. Remember fresh air helps improve productivity, we recommend taking a walk outside during your study break. \uD83D\uDEB6";
                }
                weatherNotification(title, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /*
    * Notifies the user of the weather conditions by making a call to an OpenWeatherMap API and advises on how best to spend their day
    */
    public void weatherNotification(String Title, String Message){
        Intent intent = new Intent(this, User.class);
        //Setting flags to bring user back to previous activity after clicking on notification
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        //Building notification
        //Building notification
        Notification notification = new NotificationCompat.Builder(this, Channel_2_ID)
                .setSmallIcon(R.drawable.ic_weather_update)
                .setContentTitle(Title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(Message))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);
    }
    /*
    * Notifies the user that an assignment has been posted
    */
    public void assignmentPostedNotification(){
        Intent intent = new Intent(this, AndroidProgramming.class);
        //Setting flags to bring user back to previous activity after clicking on notification
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        //Building notification
        Notification notification = new NotificationCompat.Builder(this, Channel_2_ID)
                .setSmallIcon(R.drawable.ic_assignment)
                .setContentTitle("New Assignment Posted")
                .setContentText("Android Group Report for COMP41690 is due on 12/12/18")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);
        //Tutorial for stack builder https://stackoverflow.com/questions/17677994/pressing-back-home-from-activity-launched-in-notification-exits-app
    }
    /*
     * Notifies the user that a member has joined a course
     */
    public void courseJoinNotification() {
        Intent intent = new Intent(this, JavaProgramming.class);
        //Setting flags to bring user back to previous activity after clicking on notification
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        //Building notification
        Notification notification = new NotificationCompat.Builder(this, Channel_2_ID)
                .setSmallIcon(R.drawable.ic_course_join)
                .setContentTitle("COMP41530")
                .setContentText("Eimear has joined COMP41530")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(2, notification);
        //Tutorial for stack builder https://stackoverflow.com/questions/17677994/pressing-back-home-from-activity-launched-in-notification-exits-app
    }
}