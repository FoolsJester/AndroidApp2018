package com.example.hp.androidproject.Objects;
import java.util.ArrayList;

/*
 * Object for Forum entries
 *
 * Used when creating a forum object to push to the database and also
 * when pulling data from the database. The query is pulled and cast to the
 * forum object. Much easier to deal with and manipulate
 *
 * */

public class ForumObject {
    //initialise relevanct variables
    private String title;
    private String content;

    public ForumObject(){
        //default constructor used when casting database read
    }

    public ForumObject(String title, String content){
        //overloaded constructor used when pushing to DB
        this.title = title;
        this.content = content;
    }

    //relevant getter and setter methods all auto-generated
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

