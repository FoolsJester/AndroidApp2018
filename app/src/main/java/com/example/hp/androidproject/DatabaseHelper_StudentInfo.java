package com.example.hp.androidproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper_StudentInfo extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="StudyBuddy.db";
    public static final String TABLE_NAME="StudentInfo";
    public static final String COL_1="Count_ID ";
    public static final String COL_2="Name ";
    public static final String COL_3="Email ";
    public static final String COL_4="University ";
    public static final String COL_5="Course ";

    public DatabaseHelper_StudentInfo(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_1 +"INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL_2+ "VARCHAR, "+ COL_3+ "VARCHAR, "+COL_4+ "VARCHAR, "+COL_5+ "VARCHAR)");
        populate(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME); //Drop older table if exists
        onCreate(db);
    }

    public void populate(SQLiteDatabase db){

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, "Amy McCormack");
        contentValues.put(COL_3, "amy.mccormack@ucdconnect.ie");
        contentValues.put(COL_4, "University College Dublin");
        contentValues.put(COL_5, "MSc Computer Science");

        db.insert(TABLE_NAME, null, contentValues);

    }

    public List<String> getUserInfo(){

        List<String> userInfo = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                userInfo.add(cursor.getString(1));
                userInfo.add(cursor.getString(2));
                userInfo.add(cursor.getString(3));
                userInfo.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();


        return userInfo;

        // tutorial on adding from database into spinner: https://www.androidhive.info/2012/06/android-populating-spinner-data-from-sqlite-database/
    }


    public List<String> getHours(){
        List<String> assignmentNames = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                assignmentNames.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return assignmentNames;

        // tutorial on adding from database into spinner: https://www.androidhive.info/2012/06/android-populating-spinner-data-from-sqlite-database/
    }

}