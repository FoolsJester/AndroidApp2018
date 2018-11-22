package com.example.hp.androidproject.Objects;

import java.util.Date;

/*
 * Object for assignments
 *
 * Used when creating an assignment object to push to the database and also
 * when pulling data from the database. The query is pulled and cast to the
 * assignment object. Much easier to deal with
 *
 * */
public class AssignmentObject {
    //initialise fields for content including title, due date, description, complete
    private String title;
    private String dueDate;
    private String description;
    private Integer percentWorth;
    private boolean complete;

    public AssignmentObject(){//need a default constructor when pulling from DB

    }

    public AssignmentObject(String title, String date, String desc, Integer percentWorth){//overloaded constructor for pushing to DB
        this.title=title;
        this.dueDate=date;
        this.description=desc;
        this.percentWorth = percentWorth;
        this.complete=false;
    }

    //Getter and setter methods for above fields. All auto-generated
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPercentWorth() {
        return percentWorth;
    }

    public void setPercentWorth(Integer percentWorth) {
        this.percentWorth = percentWorth;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}