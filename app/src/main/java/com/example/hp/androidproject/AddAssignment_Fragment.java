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
    EditText _txtname, _txtduedate, _txtdescription, _txtpercentworth;

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
        assignmentButton = (Button)view.findViewById(R.id.assignmentButton);
        openHelper = new DatabaseHelper(getActivity());
        _txtname = (EditText)view.findViewById(R.id.txtname);
        _txtduedate = (EditText)view.findViewById(R.id.txtduedate);
        _txtdescription = (EditText)view.findViewById(R.id.txtdescription);
        _txtpercentworth = (EditText)view.findViewById(R.id.txtpercentworth);


        assignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = openHelper.getWritableDatabase();
                String name = _txtname.getText().toString();
                String dueData = _txtduedate.getText().toString();
                String description = _txtdescription.getText().toString();
                Integer percentWorth = Integer.valueOf(_txtpercentworth.getText().toString());

                Activity act = getActivity();
                if (act instanceof Courses) {
                    ((Courses) act).populateSpinner(name, dueData, description, percentWorth);
                }


            }
        });


    }




}
