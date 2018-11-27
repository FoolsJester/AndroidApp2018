package com.example.hp.androidproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*Page with form for a new entry into the discussion forum

will need to take an argument of which module it came from
otherwise the user will need to add manually and will lead to
screw ups*/


public class DiscussionForumMake extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_forum_make);
    }
    public void postDiscussion(View view){
        EditText title = (EditText)findViewById(R.id.title);
        EditText content =  (EditText)findViewById(R.id.content);
        String concatText = title.getText().toString() + ": is being posted to the forum";

        Toast toast = Toast.makeText(getApplicationContext(), concatText, Toast.LENGTH_SHORT);
        toast.show();

        /* this now needs to take this argument and add it to a file or db.
        If we use a file it will be easier to access, update and nest arguments in

        Problem exists for all creation aspects of project- Users, Courses, Forums
         */

    }

}

