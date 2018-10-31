package com.example.hp.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when user hits the submit button on login page
     */
    public void isValidPassword(View view) {
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
            // toast to say login successful, can pass on to wherever after that
            Toast toast = Toast.makeText(getApplicationContext(), "Login Successful",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();


            //this is the intent which calls the following page, right now points to
            //the make forum page, just so I can view it

            Intent intent = new Intent(this, User.class);
            startActivity(intent);
        } else {
            // toast to say username/ password incorrect
            Toast toast = Toast.makeText(getApplicationContext(), "Username/ Password Incorrect",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
