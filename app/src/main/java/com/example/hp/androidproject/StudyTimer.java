package com.example.hp.androidproject;

import android.app.KeyguardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class StudyTimer extends AppCompatActivity {
    //Declaring variables
    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle abdt;
    Chronometer studyChronometer, handlingTime, interactionTime;
    Button startBtn, stopBtn, saveBtn;
    long studyStopTime, handleStopTime, interactStopTime = 0;
    float saveStudyTime, saveHandleTime, saveInteractTime;
    int InterruptedStudyTime;
    String studyStartTime;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    public PhoneLockObject PhoneLocked;
    public PropertyChangeListener listener;
    SensorManager sm = null;
    List list;

    /*
     * Creates a phoneLock object that supports a property change listener. This notifies the app when the lock state of the device changes
     */
    public class PhoneLockObject {
        private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        public boolean phoneIsLocked;

        //Constructor
        public PhoneLockObject() {
            this(isDeviceLocked(getApplicationContext()));
        }

        //Constructor
        public PhoneLockObject(Boolean isLocked) {
            this.phoneIsLocked = isLocked;
        }

        //Setter method for the lock state which fires a property change when a new state is detected
        public void setLockState(Boolean newState) {
            Boolean oldState = this.phoneIsLocked;
            this.phoneIsLocked = newState;
            if (newState != oldState)
                this.pcs.firePropertyChange("phoneIsLocked", oldState, newState);
        }

    }

    /*
     * Checks the lock state of the phone using the keygaurd manager
     * https://stackoverflow.com/questions/8317331/detecting-when-screen-is-locked
     */
    public static boolean isDeviceLocked(Context context) {
        boolean isLocked;

        //Checking the locked state
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean inKeyguardRestrictedInputMode = keyguardManager.inKeyguardRestrictedInputMode();

        if (inKeyguardRestrictedInputMode) {
            isLocked = true;

        } else {
            // If password is not set in the settings, the inKeyguardRestrictedInputMode() returns false,
            // this checks if screen on for this case

            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                isLocked = !powerManager.isInteractive();
            } else {
                //noinspection deprecation
                isLocked = !powerManager.isScreenOn();
            }
        }

        return isLocked;
    }


    /* This responds to sensor events */
    SensorEventListener sel;

    {
        sel = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                /* Isn't required for this example */
            }

            /*
             Detects movement of device using accelerometer.
             https://stackoverflow.com/questions/30948131/how-to-know-if-android-device-is-flat-on-table
             */
            public void onSensorChanged(SensorEvent event) {
                float[] values = event.values;

                // Movement
                int[] interruptedLogging;
                float x = values[0];
                float y = values[1];
                float z = values[2];
                float norm_Of_g = (float) Math.sqrt(x * x + y * y + z * z);

                // Normalize the accelerometer vector
                x = (x / norm_Of_g);
                y = (y / norm_Of_g);
                z = (z / norm_Of_g);
                int inclination = (int) Math.round(Math.toDegrees(Math.acos(z)));

                handlingTime = (Chronometer) findViewById(R.id.handlingChron);
                // device is flat
                if (inclination < 10 || inclination > 170) {
                    handlingTime.setBase(SystemClock.elapsedRealtime() + handleStopTime);
                    handlingTime.start();
                    //Phone may be flat but still in use, check if screen is unlocked.
                    PhoneLocked.setLockState(isDeviceLocked(getApplicationContext()));
                } else {
                    //Prevent double logging of interrupt time when phone is in hand and unlocked by setting lock state to true.
                    handleStopTime = handlingTime.getBase() - SystemClock.elapsedRealtime();
                    handlingTime.stop();
                    PhoneLocked.setLockState(true);
                }
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chronometer);
        // initialising variables for nav bar
        drawerlayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        abdt = new ActionBarDrawerToggle(this, drawerlayout, R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        drawerlayout.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                int id = item.getItemId();

                if( id == R.id.myprofile){
                    openUser();
                }
                else if( id == R.id.study){
                    openStudy();
                }
                else if( id == R.id.settings){
                    openSettings();
                }
                else if( id == R.id.search){
                    openSearch();
                }
                else if(id == R.id.login){
                    openMainActivity();
                }

                return true;
            }
        });

        //Declaring variables
        openHelper = new DatabaseHelperLocalDB(this);
        studyChronometer = (Chronometer) findViewById(R.id.simpleChronometer);
        interactionTime = (Chronometer) findViewById(R.id.interactChron);
        startBtn = (Button) findViewById(R.id.btStart);
        stopBtn = (Button) findViewById(R.id.btStop);
        saveBtn = (Button) findViewById(R.id.btSave);
        final Spinner dropdown = findViewById(R.id.spinner1);
        PhoneLocked = new PhoneLockObject();
        listener = new PropertyChangeListener() {
            //Defining the listener
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (PhoneLocked.phoneIsLocked == false) {
                    //if phone is unlocked start the interaction timer
                    interactionTime.setBase(SystemClock.elapsedRealtime() + interactStopTime);
                    interactionTime.start();
                } else {
                    //else stop the interaction timer
                    interactStopTime = interactionTime.getBase() - SystemClock.elapsedRealtime();
                    interactionTime.stop();
                }
            }
        };


        //Initialising dropdown to display courses - hardcoded for now
        openHelper = new DatabaseHelperLocalDB(this);
        db = openHelper.getReadableDatabase();

        final DatabaseHelperLocalDB db = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> courseNames = db.getCourseNameOnly();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courseNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdown.setAdapter(dataAdapter);

        /*
         * Starts the study timer, begins to listen for sensor events and device lock changes, disables the start button.
         */
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studyChronometer.setBase(SystemClock.elapsedRealtime() + studyStopTime);
                interactionTime.setBase(SystemClock.elapsedRealtime() + interactStopTime);
                studyChronometer.start();
                startBtn.setVisibility(View.GONE);
                stopBtn.setVisibility(View.VISIBLE);
                PhoneLocked.pcs.addPropertyChangeListener(listener); //The Support class binds the property change listener to our Object

                //Get a SensorManager instance
                sm = (SensorManager) getSystemService(SENSOR_SERVICE);

                // Get list of accelerometers
                list = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);

                // If there are any accelerometers register a listener to the first else print an error message
                if (list.size() > 0) {
                    sm.registerListener(sel, (Sensor) list.get(0), SensorManager.SENSOR_DELAY_NORMAL);
                } else {
                    Toast.makeText(getBaseContext(), "Error: No Accelerometer.", Toast.LENGTH_LONG);
                }
            }
        });

        /*
         * Stops the study timer
         * Gets the total study time, the handling time and the interaction time with the device
         * Disables the stop button
         * unregisters the sensor listeners and unbinds the property change listener
         */
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm.unregisterListener(sel);
                PhoneLocked.setLockState(true);
                PhoneLocked.pcs.removePropertyChangeListener(listener);
                studyStopTime = studyChronometer.getBase() - SystemClock.elapsedRealtime();
                studyChronometer.stop();
                saveStudyTime = SystemClock.elapsedRealtime() - studyChronometer.getBase();
                saveHandleTime = SystemClock.elapsedRealtime() - handlingTime.getBase();
                saveInteractTime = SystemClock.elapsedRealtime() - interactionTime.getBase();
                startBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
                stopBtn.setVisibility(View.GONE);
            }
        });

        /*
         * Resets the timers
         * Passes logged times to the database to be saved
         * Unregisters the listeners and unbinds the property change listener
         */
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studyStopTime != 0) {
                    sm.unregisterListener(sel);
                    PhoneLocked.pcs.removePropertyChangeListener(listener);
                    studyChronometer.setBase(SystemClock.elapsedRealtime());
                    interactionTime.setBase(SystemClock.elapsedRealtime());
                    handlingTime.setBase(SystemClock.elapsedRealtime());
                    studyStopTime = 0;
                    handleStopTime = 0;
                    interactStopTime = 0;
                    InterruptedStudyTime = Math.round(saveHandleTime / 1000) + Math.round(saveInteractTime / 1000);
                    //getting course name from dropdown
                    String courseName = dropdown.getSelectedItem().toString();
                    updateData(courseName, Math.round(saveStudyTime / 1000), InterruptedStudyTime);
                    startBtn.setVisibility(View.VISIBLE);
                    stopBtn.setVisibility(View.GONE);
                }
            }
        });

    }

    /*
     * Takes informaton about the study session and logs it to the database
     */
    public void updateData(String courseName, int STime, int SInterrupt) {
        float productivity;
        //Getting productivity percentage
        int productiveTime = (STime - SInterrupt);
        if (productiveTime < 0){
            productivity = 0;
        }
        else{
            productivity = ((float) productiveTime / (float) STime) * 100;
        }

        //Getting current info on the course from the database
        DatabaseHelperLocalDB database = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> current_hours = database.getAll();

        int old_total = 0; int old_interupted = 0; int id = 0; String courseCode = " ";

        for (int i = 0; i < current_hours.size(); i+=5) {
            if (courseName.equals(current_hours.get(i+2))){
                id = Integer.parseInt(current_hours.get(i));
                courseCode= current_hours.get(i+1);
                old_total = Integer.parseInt(current_hours.get(i+3));
                old_interupted = Integer.parseInt(current_hours.get(i+4));
            }
        }
        int newTotal = old_total + Math.round(STime/60); int newInterupted = old_interupted + Math.round(SInterrupt/60);

        //Updating the values in the database
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelperLocalDB.COL_2, courseCode);
        contentValues.put(DatabaseHelperLocalDB.COL_3, courseName);
        contentValues.put(DatabaseHelperLocalDB.COL_4, newTotal);
        contentValues.put(DatabaseHelperLocalDB.COL_5, newInterupted);
        db.update(DatabaseHelperLocalDB.TABLE_NAME, contentValues, "Count_ID ="+id, null);
        Toast.makeText(this, "Study time logged successfully.\n" + (int) productivity + "% productivity in this session.", Toast.LENGTH_LONG).show();
    }

    public void openUser(){
        Intent intent = new Intent(this, User.class);
        startActivity(intent);
    }

    public void openSettings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void openStudy(){
        Intent intent = new Intent(this, StudyTimer.class);
        startActivity(intent);
    }

    public void openSearch(){
        Intent intent = new Intent(this, SearchCouses.class);
        startActivity(intent);
    }

    public void openMainActivity(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

}
