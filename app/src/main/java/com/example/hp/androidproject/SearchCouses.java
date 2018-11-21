package com.example.hp.androidproject;

import android.app.SearchManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
//import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchCouses extends AppCompatActivity {

    private ListView search_course;
    private ArrayAdapter<String> searchable;

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_courses);

        openHelper = new DatabaseHelper2(this);
        db = openHelper.getReadableDatabase();

        DatabaseHelper2 db = new DatabaseHelper2(getApplicationContext());
        List<String> courses = db.getAssignments();

        search_course = (ListView) findViewById(R.id.search_course);
        ArrayList<String> tmp = new ArrayList<String>();
        tmp.add("Hello");
        tmp.add("Hey");
        tmp.add("Sup");
        Log.d("Courses",tmp.toString());
        searchable = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                tmp
        );
        search_course.setAdapter(searchable);


        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

       // MenuInflater inflater = new MenuInflater(getApplicationContext());
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_couse);
        SearchView searchView = (SearchView) item.getActionView();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchable.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    }

