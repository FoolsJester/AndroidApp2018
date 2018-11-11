package com.example.hp.androidproject.Objects;
import java.util.ArrayList;

public class UserObject {
    String Firstname;
    String Surname;
    String Username;
    String Password;
//    Bitmap ProfilePicture;
    ArrayList<String> CourseList;
    ArrayList<UserObject> FriendList;
    ArrayList<String> CalendarList;

    UserObject(String Fname, String Sname, String Username, String Password){
        Firstname = Fname;
        Surname = Sname;
        Username = Username;
        Password = Password;
    }

    public String getName(){
        return Firstname + Surname;
    }

    public ArrayList<String> getCourseList(){
            return CourseList;
    }

    public ArrayList<UserObject> getFriendList(){
        return FriendList;
    }

    public ArrayList<String> getCalendarList(){
        return CalendarList;
    }

    public void AddCourse(String CourseCode){
        this.CourseList.add(CourseCode);
    }

    public void AddFriend(UserObject Friend){
        this.FriendList.add(Friend);
    }

    public void AddDate(String Date){
        this.CourseList.add(Date);
    }
}
