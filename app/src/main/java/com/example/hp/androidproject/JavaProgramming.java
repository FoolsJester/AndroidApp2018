package com.example.hp.androidproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hp.androidproject.Objects.AssignmentObject;
import com.example.hp.androidproject.Objects.ForumObject;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class JavaProgramming extends AppCompatActivity {
    /*
    Class that displays all the information, assignments,
    staistics and fourms for the cours page
     */

    private static final String courseName = "COMP41530";
    private DatabaseReference myRef;
    private DataSnapshot globalSnapshot;
    private static final String TAG = "DataAnalytics";
    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle abdt;
    Button enrolButton, assignmentButton, openGmail, addAssignmentFragJP, addForumTopic;
    Spinner Assignmentspinner, ForumSpinner, memberSpinner;
    int delaySelect = 0;
    int delayForumSelect = 0;
    int delayMemberSelect = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_programming);

        /*
        Initialise firebase DB and get reference for it. As will be writing and reading from several
        spots in the DB, reference must be empty(root of tree)
        */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(courseName);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                //initialise global variables
                globalSnapshot = dataSnapshot;
                loadAssignmentData(globalSnapshot);
                loadForumData(globalSnapshot);
                createPieChart();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // assigning relevant xml features to nav bar variables
        drawerlayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        abdt = new ActionBarDrawerToggle(this, drawerlayout, R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        drawerlayout.addDrawerListener(abdt);
        abdt.syncState();



        // assigning xml spinners and buttons to relevant variables
        assignmentButton = (Button)findViewById(R.id.assignmentButton);
        addAssignmentFragJP = (Button)findViewById(R.id.assignmentFragButtonJP);
        openGmail = (Button)findViewById(R.id.openGmailJP);
        addForumTopic = (Button)findViewById(R.id.addForumTopicJP);
        Assignmentspinner = (Spinner) findViewById(R.id.spinnerJP);
        ForumSpinner = (Spinner) findViewById(R.id.ForumSpinnerJP);
        memberSpinner = (Spinner) findViewById(R.id.memberSpinnerJP);




        // on click listener for add assignment button
        addAssignmentFragJP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to add / remove fragment
                ChangeFragment(addAssignmentFragJP);
            }
        });


        // button to add forum fragment on click
        addForumTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to add / remove forum fragment
                topicFragment(addForumTopic);
            }
        });


        // function opens users email on button click
        openGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setting up email intent, to open up in message
                Intent emailIntent = new Intent (Intent.ACTION_SEND);
                emailIntent .setType("message/rfc822");
                // setting default email address to appear
                emailIntent .putExtra(Intent.EXTRA_EMAIL, new String[]{"membersEmail@gmail.com"});
                // setting default subject line
                emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Java Programming");
                emailIntent .setPackage("com.google.android.gm");
                if (emailIntent .resolveActivity(getPackageManager())!=null)
                    startActivity(emailIntent);
                else
                    Toast.makeText(getApplicationContext(), "No email client is installed on your device",Toast.LENGTH_SHORT).show();
            }
        });


        // allow nav bar to appear
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // opens pages when item selected in nav bar
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
                else if(id == R.id.login){
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

        // on click listener for when user selects and assignment
        Assignmentspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // getting the item selected from spinner and converting to a string
                Object obj = parent.getAdapter().getItem(position);
                String key = obj.toString();
                if(delaySelect != position) {
                    AssignmentObject assignment = globalSnapshot.child("assignments").child(key).getValue(AssignmentObject.class);
                    customDialog(assignment.getTitle(),
                            "Due Date: "+assignment.getDueDate()+"\nCompleted: "+assignment.isComplete()
                                    + "\n\n"+assignment.getDescription(), assignment, assignment.isComplete());
                }
                else{
                    return;
                }
                delaySelect = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // on click listener when a member is selected from the members spinner
        memberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // getting the item selected from the spinner and converting to string
                Object obj = parent.getAdapter().getItem(position);
                String key = obj.toString();
                if(delayMemberSelect != position & !key.equals("Select a Member")) {
                    if(key.equals("Amy McCormack")){
                        openAmysPage();
                    }
                    else if(key.equals("Muireann Mac Carthy")){
                        openMuireannsPage();

                    }
                    else if(key.equals("Shane Bird")){
                        openShanesPage();
                    }
                    else if(key.equals("Eimear Galligan")){
                        openEimearsPage();
                    }
                    else{
                        Toast.makeText(JavaProgramming.this, parent.getSelectedItem().toString() + " hasn't created a profile yet", Toast.LENGTH_SHORT).show();
                    }

                }

                else{
                    return;
                }
                delayMemberSelect = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // on click listener for when the user selects a forum topic
        ForumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // getting the item selected and casting to a string
                Object obj = parent.getAdapter().getItem(position);
                String key = obj.toString();
                if(delayForumSelect != position) {
                    openActivityTopicOne(key);
                }
                else{
                    return;
                }
                delayForumSelect = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // listener to display toast when enrol button selected
        DatabaseHelperLocalDB db = new DatabaseHelperLocalDB(getApplicationContext());
        List<String> enrolledCourses = db.getCourseName();
        Boolean enrolled = false;

        Button enrol = (Button) findViewById(R.id.enrolButtonJP);

        for (int i = 0; i < enrolledCourses.size(); i+=2) {
            if(enrolledCourses.get(i).equals(courseName)){
                enrolled = true;
            }
        }
        if (enrolled == true){
            enrol.setText("Enrolled");
        }
        else {
            enrol.setText("Enroll");
          //  enrol.setCompoundDrawables(null,null,null,null);
            enrol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Waiting for admins approval", Toast.LENGTH_LONG).show();
                }
            });
        }


    }

    public void createPieChart(){
        /*
        Function to create PieChart to Display Assignment
        Completion Time
         */

        PieChart pieChart = (PieChart) findViewById(R.id.firstPie);
        ArrayList<PieEntry> values = new ArrayList<PieEntry>();

        for(DataSnapshot ds: globalSnapshot.child("assignments").getChildren()) {   //Get Assignments from Database

            String assignment = ds.child("title").getValue().toString();
            int time = ThreadLocalRandom.current().nextInt(20, 121);
            values.add(new PieEntry(time, assignment));                  //Add Assignment names to lables and assign a random completion time

        }

        PieDataSet dataSet = new PieDataSet(values,"Average Completion Time (Minutes)");
        PieData data = new PieData(dataSet);

        final int[] MY_COLORS = {Color.rgb(147,22,33), Color.rgb(134,163,168),
                Color.rgb(44,140,153)};//set colouring
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        dataSet.setColors(colors);

        pieChart.getDescription().setText("Average Completion Time (Minutes)");     //style label
        pieChart.getDescription().setTextSize(12);

        pieChart.setDrawHoleEnabled(false);
        data.setValueTextSize(13f);

        Legend legend = pieChart.getLegend();   //remove legened
        legend.setEnabled(false);


        pieChart.setTransparentCircleRadius(58f);
        pieChart.setData(data);                     //update and set data on change
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }

    // function called from the add assignment fragment
    public void populateSpinner(String name, String dueData, String description, Integer percentWorth){
        /*
        Method called from add assignment forum fragment, passes the values filled into the form to
        the insert data method (which inserts into firebase)
         */
        delaySelect = 0;
        insertData(name, dueData, description, percentWorth);
        Toast.makeText(getApplicationContext(), "Assignment is added", Toast.LENGTH_LONG).show();

    }

    public void sendForum(String name, String desc){ //method to write do db with forum object, uses try catch exception
        try {
            Log.d(TAG, "myRef is : " + myRef);
            myRef.child("forum").child(name).setValue(new ForumObject(name, desc));

            Toast.makeText(getApplicationContext(), "Forum is added", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(JavaProgramming.this,"Oops... Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }




    // intents to open new activities

    public void openActivityTopicOne(String key){
        Intent intent = new Intent(this, DisplayContent.class);
        Bundle bundle = new Bundle();
        intent.putExtra("course", courseName);
        intent.putExtra("key", key);
        startActivity(intent);
    }

    public void openUser(){
        Intent intent = new Intent(this, User.class);
        startActivity(intent);
    }

    public void openMainActivity(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
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
    public void openAmysPage(){
        Intent intent = new Intent(this, amyUser.class);
        startActivity(intent);

    }
    public void openStudy(){
        Intent intent = new Intent(this, StudyTimer.class);
        startActivity(intent);
    }

    public void openEimearsPage(){
        Intent intent = new Intent(this, eimearUser.class);
        startActivity(intent);
    }

    public void openMuireannsPage(){

        Intent intent = new Intent(this, muireannUser.class);
        startActivity(intent);
    }

    public void openShanesPage(){
        Intent intent = new Intent(this, shaneUser.class);
        startActivity(intent);
    }

    // method that inserts value from form into database
    public void insertData(String name, String dueData, String description, Integer percentWorth){
        //changed to write to Firebase instead of Local DB
        try {
            Log.d(TAG, "myRef is : " + myRef);
            myRef.child("assignments").child(name).setValue(new AssignmentObject(name, dueData, description, percentWorth));
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(JavaProgramming.this,"Oops... Something went wrong", Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    // function to load data from database into spinner (drop down menu)
    public void loadAssignmentData(DataSnapshot globalSnapshot) {
        ArrayList<String> assignment = new ArrayList<>();
        assignment.add("Please Select an Assignment");
        for(DataSnapshot ds: globalSnapshot.child("assignments").getChildren()) {
            assignment.add(ds.child("title").getValue().toString());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, assignment);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Assignmentspinner.setAdapter(dataAdapter);

        // tutorial to get data from database into spinner: https://www.androidhive.info/2012/06/android-populating-spinner-data-from-sqlite-database/
    }
    public void loadForumData(DataSnapshot globalSnapshot) {
        ArrayList <String> assignment = new ArrayList<>();
        assignment.add("Please Select a Forum");
        for(DataSnapshot ds: globalSnapshot.child("forum").getChildren()) {
            assignment.add(ds.child("title").getValue().toString());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, assignment);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ForumSpinner.setAdapter(dataAdapter);

        // tutorial to get data from database into spinner: https://www.androidhive.info/2012/06/android-populating-spinner-data-from-sqlite-database/
    }


    // function to check if add assignment fragment is already added. If it is not added the
    // fragment is added. If the fragment is already present when the method is called it is
    // removed
    public void ChangeFragment(View view){
        /*
        Method to place fragment in xml if not already there, or remove fragment from xml if already
        there
         */
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        AddAssignment_Fragment fragment = (AddAssignment_Fragment) fm.findFragmentByTag("tagJP");
        if(fragment == null) {
            fragment = new AddAssignment_Fragment();
            ft.add(R.id.placeholderJP, fragment, "tagJP");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        } else {

            ft.remove(fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        }
        ft.commit();
    }

    public void topicFragment(View view){
        /*
        Method to place forum fragment in xml if not already there, or remove forum from fragment if
        already there
         */
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ForumFragment forumfragment = (ForumFragment) fm.findFragmentByTag("forumtagJP");
        if(forumfragment == null) {
            forumfragment = new ForumFragment();
            ft.add(R.id.forumPlaceholderJP, forumfragment, "forumtagJP");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        } else {

            ft.remove(forumfragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        }
        ft.commit();
    }

    /*
     * Method for making custom Dialog Box.
     *
     * Uses Alert Dialog box and thus uses builder for same class. No need to initiate
     * buttons in the method header as these will always call the same functions, just with
     * different parameters
     * */
    public void customDialog(String title, String message,final AssignmentObject assignment, Boolean completeness){
        final android.support.v7.app.AlertDialog.Builder builderSingle = new android.support.v7.app.AlertDialog.Builder(this);
        //builderSingle.setIcon(R.mipmap.ic_notification);
        builderSingle.setTitle(title); //constructing both title and message for display in dialog
        builderSingle.setMessage(message);

        //all relevant buttons below. Negative, positive etc hold no relevance. Just method requires them be called that
        if (completeness){
            builderSingle.setNegativeButton(
                    "Mark as Incomplete",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            markIncomplete(assignment);
                        }
                    }
            );
        }

        else {
            builderSingle.setNegativeButton(
                    "Mark as Complete",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            markComplete(assignment);
                        }
                    }
            );
        }

        builderSingle.setPositiveButton(
                "Work on Assignment",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToStudy();
                    }
                }
        );

        builderSingle.setNeutralButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );

        builderSingle.show();
    }

    private void markComplete( AssignmentObject assignment){
        assignment.setComplete(true);
        myRef.child("assignments").child(assignment.getTitle()).setValue(assignment);
        toastMessage("Marked as Completed");
    }

    private void markIncomplete( AssignmentObject assignment) {
        assignment.setComplete(false);
        myRef.child("assignments").child(assignment.getTitle()).setValue(assignment);
        toastMessage("Marked as Incomplete");
    }

        private void goToStudy(){
        Intent intent = new Intent(this, StudyTimer.class);
        startActivity(intent);
    }

    private void cancelDialog(){
        toastMessage("Cancel");
    }

    public void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}

