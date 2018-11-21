package com.example.hp.androidproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper2 extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="StudyBudy.db";
    public static final String TABLE_NAME="StudyTimeLog";
    public static final String COL_1="Count_ID ";
    public static final String COL_2="CourseCode ";
    public static final String COL_3="CourseName ";
    public static final String COL_4="TotalStudy ";
    public static final String COL_5="Interrupted ";

    public DatabaseHelper2(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_1 +"INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL_2+ "VARCHAR, "+ COL_3+ "VARCHAR, "+COL_4+ "INTEGER, "+COL_5+ "INTEGER)");
        populate(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME); //Drop older table if exists
        onCreate(db);
    }

    public void populate(SQLiteDatabase db){


            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper2.COL_2, "COMP41690");
            contentValues.put(DatabaseHelper2.COL_3, "Android Programming");
            contentValues.put(DatabaseHelper2.COL_4, 40);
            contentValues.put(DatabaseHelper2.COL_5, 3);

           // long id = db.insert(TABLE_NAME, null, contentValues);

            contentValues.put(DatabaseHelper2.COL_2, "COMP47520");
            contentValues.put(DatabaseHelper2.COL_3, "Programming for IoT");
            contentValues.put(DatabaseHelper2.COL_4, 20);
            contentValues.put(DatabaseHelper2.COL_5, 7);

            contentValues.put(DatabaseHelper2.COL_2, "COMP41530");
            contentValues.put(DatabaseHelper2.COL_3, "Java Programming");
            contentValues.put(DatabaseHelper2.COL_4, 32);
            contentValues.put(DatabaseHelper2.COL_5, 10);

            long id = db.insert(TABLE_NAME, null, contentValues);



    }

    public List<String> getAssignments(){
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

    public List<String> getHours(){
        List<String> assignmentNames = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                assignmentNames.add(cursor.getString(3));
                assignmentNames.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return assignmentNames;

        // tutorial on adding from database into spinner: https://www.androidhive.info/2012/06/android-populating-spinner-data-from-sqlite-database/
    }

    public List<String> getAll(){
        List<String> assignmentNames = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                assignmentNames.add(cursor.getString(0));
                assignmentNames.add(cursor.getString(1));
                assignmentNames.add(cursor.getString(3));
                assignmentNames.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();


        return assignmentNames;

        // tutorial on adding from database into spinner: https://www.androidhive.info/2012/06/android-populating-spinner-data-from-sqlite-database/
    }

}