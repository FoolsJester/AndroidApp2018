package com.example.hp.androidproject.Objects;
import java.util.ArrayList;

public class ForumObject {
    String CourseCode;
    String MainPost;
    ArrayList<String> ReplyList;

    ForumObject(String Code, String Post){
        CourseCode = Code;
        MainPost = Post;
    }

    public String getCode(){
        return CourseCode;
    }

    public String getMainPost(){
        return MainPost;
    }

    public ArrayList<String> getReplyList(){
        return ReplyList;
    }


    public void AddReply(String Reply){
        this.ReplyList.add(Reply);
    }
}
