package com.example.hp.androidproject;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ForumFragment extends Fragment {
    Button forumButton;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    EditText topicname, topicdescription;
    int input1, input2;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        forumButton = (Button)view.findViewById(R.id.forumButton);
        openHelper = new DatabaseHelper(getActivity());
        topicname = (EditText)view.findViewById(R.id.topicname);
        topicdescription = (EditText)view.findViewById(R.id.topicdescription);



        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                input1 = topicname.getText().toString().trim().length();
                input2 = topicdescription.getText().toString().trim().length();
                if (input1> 0 & + input2 >  0) {
                    db = openHelper.getWritableDatabase();
                    String name = topicname.getText().toString();
                    String desc =topicdescription.getText().toString();

                    Activity act = getActivity();
                    if (act instanceof Courses) {
                        ((Courses) act).sendForum(name, desc);
                    }
                    else if(act instanceof AndroidProgramming){
                        ((AndroidProgramming) act).sendForum(name, desc);
                    }
                    else if(act instanceof IOTprogramming){
                        ((IOTprogramming) act).sendForum(name, desc);
                    }
                }

                else{
                    Toast.makeText(getContext(), "Error... empty field " , Toast.LENGTH_LONG).show();
                }


            }
        });


    }





}
