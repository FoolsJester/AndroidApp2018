package com.example.hp.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hp.androidproject.Objects.ForumObject;
import com.example.hp.androidproject.Objects.ReplyObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*
* Activity to display content of the clicked forum
*
* This activity is responsible for rendering both the forum  header as
* well as all the replies associated with that foru, (provided there are
* any). Takes information sent through intent as argument for database instance.
* The database snapshot will be localised to the forum tree, but the intent
* that generates the activity will pass the relevant forum within that tree
*
* The same value passed through the intent will be responsible for honing in on
* relevant replies as well
*
* Page is also responsible for the generation of new forums and replies (this may change)
*
* */
public class DisplayContent extends AppCompatActivity {
    //generate tage and initialise xml components
    private static final String TAG = "displayContent";
    private TextView mTitle, mContent;
    private EditText mAuthor, mReply;
    private Button mSendReply;
    private String intentKey;
    private String courseName;
    ArrayList<ReplyObject> replies;
    RecyclerView rvReplyObjects;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_content);
        Bundle extras = getIntent().getExtras();
        intentKey = extras.getString("key");
        courseName = extras.getString("course");
        //Instantiate xml components for writing new forums and generating replies


        mTitle = findViewById(R.id.title);
        mContent = findViewById(R.id.content);

        mAuthor = findViewById(R.id.author);
        mReply = findViewById(R.id.replyText);
        mSendReply = findViewById(R.id.replyButton);

        //initialising recycler view to display replies
        rvReplyObjects = (RecyclerView) findViewById(R.id.rvReplyList);

        //Get firebase instance and initialise reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(courseName);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                populateData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //On click listener for generating new forum
        mSendReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Attempting to add object to database.");
                String author = mAuthor.getText().toString();
                String reply = mReply.getText().toString();

                if (!author.equals("") && !reply.equals("")) {
                    //myRef.child("replies").child(intentKey).child(author).setValue(new ReplyObject(author, reply));

                    myRef.child("replies").child(intentKey).push().setValue(new ReplyObject(author, reply));
                }
            }


        });
    }

    /*
    * Method called on launch/ on data change. Populates forum object and replies in
    * recycler view
    *
    * Takes datasnapshot and forum child to render forum. Second part of the function is
    * for rendering replies if there are any. Both use the same concept of reading from DB
    * and casting to relevant object before rendering
    * */
    public void populateData(DataSnapshot dataSnapshot){

        ForumObject forumEntry = dataSnapshot.child("forum/"+intentKey).getValue(ForumObject.class);//casting red to forum class
        String title = forumEntry.getTitle();
        String content = forumEntry.getContent();
        Log.d(TAG, "Object reads in as: " + title + "    " + content);
        mTitle.setText(title);//render title and content
        mContent.setText(content);



        //second read is for the list of replies, if there are any
        if(dataSnapshot.child("replies/"+intentKey) != null){
            ArrayList<ReplyObject> replies = new ArrayList<>();// new array list to store all reply objects

            for(DataSnapshot ds : dataSnapshot.child("replies").child(intentKey).getChildren()){//for loop iterating through each relevant reply
                ReplyObject replyObject = ds.getValue(ReplyObject.class);//casting to reply object
                String author = replyObject.getAuthor();
                String reply = replyObject.getReply();

                Log.d(TAG, "Reply reads as:  " + author + "    " + reply);
                replies.add(replyObject);
            }
            Log.d(TAG, "Replies list is :"+ replies);//debugging


            RepliesAdapter adapter = new RepliesAdapter(replies); //rendering Adapter using array list of reply objects

            rvReplyObjects.setAdapter(adapter);
            rvReplyObjects.setLayoutManager(new LinearLayoutManager(this));
        }

    }


}
