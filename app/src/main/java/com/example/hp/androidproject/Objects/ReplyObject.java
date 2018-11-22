package com.example.hp.androidproject.Objects;

/*
 * Object for replies
 *
 * Used when creating a reply object to push to the database and also
 * when pulling data from the database. The query is pulled and cast to the
 * reply object. Much easier to deal with
 *
 * */
public class ReplyObject {
    //initialise relevant variables
    private String author;
    private String reply;

    public ReplyObject(){
        //default constructor used when casting objects from DB
    }

    public ReplyObject(String author, String reply) {
        //overloaded constructor used when pushing objects to DB
        this.author = author;
        this.reply = reply;
    }

    //relevant getter and setter methods for above variables
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

/*    public static ArrayList<ReplyObject> createREplyList(){
        ArrayList<ReplyObject> replies = new ArrayList<>();

    }*/
}
