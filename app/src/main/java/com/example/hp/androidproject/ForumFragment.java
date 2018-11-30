package com.example.hp.androidproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ForumFragment extends Fragment {
    // initializing buttons, text fields and length holders
    Button forumButton;
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
        // getting relevant xml features and assigning to variables
        forumButton = (Button)view.findViewById(R.id.forumButton);
        topicname = (EditText)view.findViewById(R.id.topicname);
        topicdescription = (EditText)view.findViewById(R.id.topicdescription);



        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // getting length of input for error handling
                input1 = topicname.getText().toString().trim().length();
                input2 = topicdescription.getText().toString().trim().length();
                // if statements to check input fields not empty
                if (input1> 0 & + input2 >  0) {
                    // getting string of inputs
                    String name = topicname.getText().toString();
                    String desc =topicdescription.getText().toString();
                    // getting the activity
                    Activity act = getActivity();
                    // if statements to check which activity fragment called from,
                    // calls the relevant function for the activity fragment called from
                    if (act instanceof DataAnalytics) {
                        ((DataAnalytics) act).sendForum(name, desc);
                    }
                    else if(act instanceof AndroidProgramming){
                        ((AndroidProgramming) act).sendForum(name, desc);
                    }
                    else if(act instanceof IOTprogramming){
                        ((IOTprogramming) act).sendForum(name, desc);
                    }
                    else if(act instanceof JavaProgramming){
                        ((JavaProgramming) act).sendForum(name, desc);
                    }
                }

                else{
                    Toast.makeText(getContext(), "Error... empty field " , Toast.LENGTH_LONG).show();
                }


            }
        });


    }





}
