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


public class AddAssignment_Fragment extends Fragment {

    // initialising variables for form, button, and length holders
    Button assignmentButton;
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
        // assigning relevant xml features to variables
        assignmentButton = (Button) view.findViewById(R.id.assignmentButton);
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
                // if statement to make sure all of the fields have been filled in
                if (input1> 0 & + input2 > 0 & input3 > 0 & input4 > 0) {
                    // getting string values for users input into the form
                    String name = tname.getText().toString();
                    String dueData =tdate.getText().toString();
                    String description = tdescription.getText().toString();
                    Integer percentWorth = Integer.valueOf(tpercentworth.getText().toString());

                    // get the activity
                    Activity act = getActivity();
                    // check which activity fragment is called from, send form details to relevant
                    // activity
                    if (act instanceof DataAnalytics) {
                        ((DataAnalytics) act).populateSpinner(name, dueData, description, percentWorth);
                    }
                    else if(act instanceof AndroidProgramming){
                        ((AndroidProgramming) act).populateSpinner(name, dueData, description, percentWorth);
                    }
                    else if(act instanceof IOTprogramming){
                        ((IOTprogramming) act).populateSpinner(name, dueData, description, percentWorth);
                    }
                    else if(act instanceof JavaProgramming){
                        ((JavaProgramming) act).populateSpinner(name, dueData, description, percentWorth);
                    }
                }
                else{
                    Toast.makeText(getContext(), "Error... empty field " , Toast.LENGTH_LONG).show();
                }

            }
        });

    }




}
