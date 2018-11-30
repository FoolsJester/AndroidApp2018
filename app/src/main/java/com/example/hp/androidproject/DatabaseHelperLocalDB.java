package com.example.hp.androidproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperLocalDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="StudyBuddy.db";
    public static final String TABLE_NAME="StudyTimeLog";
    public static final String COL_1="Count_ID ";
    public static final String COL_2="CourseCode ";
    public static final String COL_3="CourseName ";
    public static final String COL_4="TotalStudy ";
    public static final String COL_5="Interrupted ";
    public static final String STUDENT_TABLE="StudentInfo";
    public static final String STUDENT_1="Count_ID ";
    public static final String STUDENT_2="Name ";
    public static final String STUDENT_3="Email ";
    public static final String STUDENT_4="University ";
    public static final String STUDENT_5="Course ";

    public DatabaseHelperLocalDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        Creates tables with specified columns on creation
         */
        db.execSQL("CREATE TABLE " + STUDENT_TABLE + "(" + STUDENT_1 +"INTEGER PRIMARY KEY AUTOINCREMENT, "+ STUDENT_2 + "VARCHAR, "+ STUDENT_3 + "VARCHAR, "+STUDENT_4+ "VARCHAR, "+STUDENT_5+ "VARCHAR)");
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_1 +"INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL_2+ "VARCHAR, "+ COL_3+ "VARCHAR, "+COL_4+ "INTEGER, "+COL_5+ "INTEGER)");
        populate(db);
        populate_student(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME); //Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " +STUDENT_TABLE); //Drop older table if exists
        onCreate(db);
    }

    public void populate_student(SQLiteDatabase db){
        /*
        Function to populate the Student Info tables
         */

        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_2, "David Coyle");
        contentValues.put(STUDENT_3, "d.coyle@ucd.ie");
        contentValues.put(STUDENT_4, "University College Dublin");
        contentValues.put(STUDENT_5, "PhD Computer Science");

        db.insert(STUDENT_TABLE, null, contentValues);

    }

    public void populate(SQLiteDatabase db){
         /*
        Function to populate the StudyTimeLog table
         */


            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_2, "COMP41690");
            contentValues.put(COL_3, "Android Programming");
            contentValues.put(COL_4, 40*60);
            contentValues.put(COL_5, 3*60);

            db.insert(TABLE_NAME, null, contentValues);

            contentValues.put(COL_2, "COMP47520");
            contentValues.put(COL_3, "Programming for IoT");
            contentValues.put(COL_4, 20*60);
            contentValues.put(COL_5, 7*60);

            db.insert(TABLE_NAME, null, contentValues);

            contentValues.put(COL_2, "COMP41530");
            contentValues.put(COL_3, "Java Programming");
            contentValues.put(COL_4, 32*60);
            contentValues.put(COL_5, 10*60);

            db.insert(TABLE_NAME, null, contentValues);

            contentValues.put(COL_2, "COMP47350");
            contentValues.put(COL_3, "Data Analytics");
            contentValues.put(COL_4, 28*60);
            contentValues.put(COL_5, 6*60);

            db.insert(TABLE_NAME, null, contentValues);



    }

    public List<String> getUserInfo(){
        /*
        Function to select and return all the information in the user table
         */

        List<String> userInfo = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + STUDENT_TABLE;

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

    public List<String> getCourseName(){
        /*
        Function to select and return all the courseName and Code in the studylog table
         */
        List<String> courseNames = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                courseNames.add(cursor.getString(1));
                courseNames.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();


        return courseNames;

        // tutorial on adding from database into spinner: https://www.androidhive.info/2012/06/android-populating-spinner-data-from-sqlite-database/
    }

    public List<String> getCourseNameOnly(){
        /*
        Function to select and return all the courseName in the studylog table
         */
        List<String> courseNames = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                courseNames.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return courseNames;

        // tutorial on adding from database into spinner: https://www.androidhive.info/2012/06/android-populating-spinner-data-from-sqlite-database/
    }

    public List<String> getHours(){
        /*
        Function to select and return all the total and interupted study hours in the studylog table
         */
        List<String> hours = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                hours.add(cursor.getString(3));
                hours.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return hours;

        // tutorial on adding from database into spinner: https://www.androidhive.info/2012/06/android-populating-spinner-data-from-sqlite-database/
    }

    public List<String> getAll(){
        /*
        Function to select and return all the all the information in the studylog table
         */
        List<String> allInfo = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                allInfo.add(cursor.getString(0));
                allInfo.add(cursor.getString(1));
                allInfo.add(cursor.getString(2));
                allInfo.add(cursor.getString(3));
                allInfo.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();


        return allInfo;

        // tutorial on adding from database into spinner: https://www.androidhive.info/2012/06/android-populating-spinner-data-from-sqlite-database/
    }

}