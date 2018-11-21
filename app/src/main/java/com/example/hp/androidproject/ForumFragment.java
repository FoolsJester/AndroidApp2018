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


public class ForumFragment extends Fragment {
    Button forumButton;
//    SQLiteOpenHelper openHelper;
//    SQLiteDatabase db;
    EditText topicname, topicdescription, topicquestion;



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
//        openHelper = new DatabaseHelper(getActivity());
        topicname = (EditText)view.findViewById(R.id.topicname);
        topicdescription = (EditText)view.findViewById(R.id.topicdescription);
        topicquestion = (EditText)view.findViewById(R.id.addquestion);



        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                db = openHelper.getWritableDatabase();
//                String name = _txtname.getText().toString();
//                String dueData = _txtduedate.getText().toString();
//                String description = _txtdescription.getText().toString();
//                Integer percentWorth = Integer.valueOf(_txtpercentworth.getText().toString());
//
//                Activity act = getActivity();
//                if (act instanceof Courses) {
//                    ((Courses) act).populateSpinner(name, dueData, description, percentWorth);
 //               }


            }
        });


    }





}
