package com.example.hp.androidproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper2 extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="StudyBudy.db";
    public static final String TABLE_NAME="StudyTimeLog";
    public static final String COL_1="Count_ID ";
    public static final String COL_2="DateTime ";
    public static final String COL_3="TotalStudy ";
    public static final String COL_4="Interrupted ";

    public DatabaseHelper2(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_1 +"INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL_2+ "VARCHAR, "+ COL_3+ " INTEGER, "+COL_4+ "INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME); //Drop older table if exists
        onCreate(db);
    }
}