package com.example.hp.androidproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;


public class AddAssignment_Fragment extends Fragment {

    Button assignmentButton;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    EditText tname, tdate, tdescription, tpercentworth;
    int input1, input2, input3, input4;

    public AddAssignment_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_assignment_, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignmentButton = (Button) view.findViewById(R.id.assignmentButton);
        openHelper = new DatabaseHelper(getActivity());
        tname = (EditText) view.findViewById(R.id.txtname);
        tdate = (EditText) view.findViewById(R.id.txtduedate);
        tdescription = (EditText) view.findViewById(R.id.txtdescription);
        tpercentworth = (EditText) view.findViewById(R.id.txtpercentworth);






            assignmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    input1 = tname.getText().toString().trim().length();
                    input2 = tdate.getText().toString().trim().length();
                    input3 = tdescription.getText().toString().trim().length();
                    input4 = tpercentworth.getText().toString().trim().length();
                    if (input1> 0 & + input2 > 0 & input3 > 0 & input4 > 0) {
                        db = openHelper.getWritableDatabase();
                        String name = tname.getText().toString();
                        String dueData =tdate.getText().toString();
                        String description = tdescription.getText().toString();
                        Integer percentWorth = Integer.valueOf(tpercentworth.getText().toString());


                        Activity act = getActivity();
                        if (act instanceof Courses) {
                            ((Courses) act).populateSpinner(name, dueData, description, percentWorth);
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "Error... empty field " , Toast.LENGTH_LONG).show();
                    }

                }
            });

    }




}
