package com.example.hp.androidproject;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.hp.androidproject.BaseApp.Channel_2_ID;

public class User extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;


    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle abdt;
    private Button timeStudy;



    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    BarChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        DownloadTask task = new DownloadTask();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q=Dublin,ie&units=metric&appid=00a79b7ecabf9125273b86a8736392d6");

        notificationManager = NotificationManagerCompat.from(this);


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
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        abdt = new ActionBarDrawerToggle(this, drawerlayout, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        drawerlayout.addDrawerListener(abdt);
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
                else if(id == R.id.settings){
                    openSettings();

                }
                else if( id == R.id.search){
                    openSearch();
                }

                return true;
            }
        });


        setSpinner();
        barChart();
        createTextField();
        setText();

        openHelper = new DatabaseHelperLocalDB(this);


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

                if(dropdown.getVisibility()==View.GONE){
                    dropdown.setVisibility(View.VISIBLE);
                    set.setVisibility(View.VISIBLE);
                    hours.setVisibility(View.VISIBLE);
                    productive.setVisibility(View.VISIBLE);}
                else if(dropdown.getVisibility()==View.VISIBLE){
                    dropdown.setVisibility(View.GONE);
                    set.setVisibility(View.GONE);
                    hours.setVisibility(View.GONE);
                    productive.setVisibility(View.GONE);}

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

        openHelper = new DatabaseHelperLocalDB(this);
        db = openHelper.getReadableDatabase();

        DatabaseHelperLocalDB db = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> courses = db.getCourseName();

        for (int i = 0; i < courses.size(); i+=2) {

            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            Button course = new Button(this);
            final TextView assignment = new TextView(this);
            final Button toCourse = new Button(this);

            course.setLayoutParams(lparams);
            course.setText(courses.get(i)+" - "+courses.get(i+1));
            course.setId(i+1);
            assignment.setLayoutParams(lparams);
            assignment.setVisibility(View.GONE);
            assignment.setText("\tProgramming Lab 2: INCOMPLETE\n\t Average Completion Time: 30 minutes");
            toCourse.setLayoutParams(lparams);
            toCourse.setText("View "+courses.get(i+1));
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

    public void insertdata(String courseCode, int hours, int productiveStudy) {


        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelperLocalDB.COL_2, courseCode);
        contentValues.put(DatabaseHelperLocalDB.COL_3, "12/11/2018");
        contentValues.put(DatabaseHelperLocalDB.COL_4, hours);
        contentValues.put(DatabaseHelperLocalDB.COL_5, productiveStudy);

        long id = db.insert(DatabaseHelperLocalDB.TABLE_NAME, null, contentValues);
        Toast.makeText(getApplicationContext(), "ID="+id, Toast.LENGTH_LONG).show();
    }

    public void updateData(String courseCode, int hours, int productiveStudy) {

        DatabaseHelperLocalDB database = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> current_hours = database.getAll();

        int old_total = 0; int old_interupted = 0; int id = 0; String courseName = " ";

        for (int i = 1; i < current_hours.size(); i+=4) {
            if (courseCode.equals(current_hours.get(i))){
                id = Integer.parseInt(current_hours.get(i-1));
                courseName = current_hours.get(i);
                old_total = Integer.parseInt(current_hours.get(i+1));
                old_interupted = Integer.parseInt(current_hours.get(i+2));
            }
        }
        int newTotal = old_total + hours; int newInterupted = old_interupted + productiveStudy;

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelperLocalDB.COL_2, courseCode);
        contentValues.put(DatabaseHelperLocalDB.COL_3, courseName);
        contentValues.put(DatabaseHelperLocalDB.COL_4, newTotal);
        contentValues.put(DatabaseHelperLocalDB.COL_5, newInterupted);
        db.update(DatabaseHelperLocalDB.TABLE_NAME, contentValues, "Count_ID ="+id, null);

    }

    public void barChart() {

        openHelper=new DatabaseHelperLocalDB(this);
        db = openHelper.getReadableDatabase();

        DatabaseHelperLocalDB db = new DatabaseHelperLocalDB(getApplicationContext());
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

        List<String> labels = db.getCourseName();

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
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

    public void openSettings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void openSearch(){
        Intent intent = new Intent(this, SearchCouses.class);
        startActivity(intent);
    }

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
    public void weatherNotification(String Title, String Message){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this, Channel_2_ID)
                .setSmallIcon(R.drawable.ic_weather_update)
                .setContentTitle(Title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(Message))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManager.notify(0, notification);
    }
    }