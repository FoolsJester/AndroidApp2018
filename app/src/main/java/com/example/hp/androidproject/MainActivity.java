package com.example.hp.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle abdt;
    private EditText username;
    private EditText password;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();

        //assigning listener to check if user is logged in or not
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    //if user is logged in, go straight to user page
                    openUser();
                }
            }
        };

        //invokes signIn method when clicked
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

//***************************THIS IS NAV BAR

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
    }
//ALL THIS CODE TO GO *****************************




    /*
    * Assigns the authentication state listened defined in onCreate method
    */
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }





//***************************THIS IS NAV BAR
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

    //ALL THIS CODE TO GO *****************************

    /**
     * Called when user hits the submit button on login page
     */
    private void signIn() {
        //retrieve user input for email and password
        String email = username.getText().toString();
        String pass = password.getText().toString();

        //Ensure fields at not empty
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast toast = Toast.makeText(getApplicationContext(), "Empty fields, please try again.",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            //authenticate user
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //ensure email and password are correct
                    if(!task.isSuccessful()){
                        Toast toast = Toast.makeText(getApplicationContext(), "Username/ Password Incorrect",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }
    }

    //Tutorial for firebase authentication - https://www.youtube.com/watch?v=oi-UAwiBigQ
}
