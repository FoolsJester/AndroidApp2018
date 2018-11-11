package com.example.hp.androidproject.Objects;
import java.util.ArrayList;

public class CourseObject {
    String CourseCode;
    ArrayList<UserObject> StudentList;
    ArrayList<ForumObject> ForumList;
    ArrayList<AssignmentObject> AssignmentList;

    CourseObject(String Code){
        CourseCode = Code;
    }

    public String getCode(){
        return CourseCode;
    }

    public ArrayList<UserObject> getStudentList(){
        return StudentList;
    }

    public ArrayList<ForumObject> getForumList(){
        return ForumList;
    }

    public ArrayList<AssignmentObject> getAssignmentList(){
        return AssignmentList;
    }

    public void AddStudent(UserObject Student){
        this.StudentList.add(Student);
    }

    public void AddForum(ForumObject Forum){
        //Add the name to string list
        this.ForumList.add(Forum);
        //Write details to Firebase
    }

    public void AddAssignmnet(AssignmentObject Assignment){
        this.AssignmentList.add(Assignment);
    }
}
