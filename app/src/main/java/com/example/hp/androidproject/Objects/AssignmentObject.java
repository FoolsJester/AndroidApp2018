package com.example.hp.androidproject.Objects;

import java.util.Date;

public class AssignmentObject {
        String CourseCode;
        Date DueDate;
        String ProjectSpec;
        Float PercentageWeight;

        AssignmentObject(String Code, Date Date, String Spec, Float Weight){
            CourseCode = Code;
            DueDate = Date;
            ProjectSpec = Spec;
            PercentageWeight = Weight;
        }

        public String getCode(){
            return CourseCode;
        }

         public Date getDueDate(){
            return DueDate;
        }

        public String getSpec(){
            return ProjectSpec;
        }

        public Float getWeight(){
            return PercentageWeight;
        }

}
