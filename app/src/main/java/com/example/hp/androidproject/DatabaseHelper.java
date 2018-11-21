package com.example.hp.androidproject;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="createAssignment.db";
    public static final String TABLE_NAME="createAssignment";
    public static final String COL_1="ID";
    public static final String COL_2="name";
    public static final String COL_3="dueData";
    public static final String COL_4="description";
    public static final String COL_5="percentageWorth";

//    public static final String TABLE2_NAME="createForum";
//    public static final String COL2_12="ID";
//    public static final String COL2_2="name";
//    public static final String COL2_3="description";
//    public static final String COL2_4="comments";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT ,dueData TEXT ,description TEXT ,percentageWorth INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME );
        onCreate(db);
    }

//tutorial for database and table : https://www.youtube.com/watch?v=B2avB5tmTMM


    // method to get loop through assignment names in DB and return assignment names when called
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


}
