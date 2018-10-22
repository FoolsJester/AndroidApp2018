package com.example.hp.androidproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        BarChart barChart = (BarChart) findViewById(R.id.barchart);
//
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(38f, 0));
//        entries.add(new BarEntry(52f, 1));
//        entries.add(new BarEntry(65f, 2));
//        entries.add(new BarEntry(30f, 3));
//        entries.add(new BarEntry(85f, 4));
//        entries.add(new BarEntry(19f, 5));
//        entries.add(new BarEntry(75f, 6));
//
//        BarDataSet bardataset = new BarDataSet(entries, " ");
//
//        ArrayList<String> labels = new ArrayList<String>();
//        labels.add("Mon");
//        labels.add("Tue");
//        labels.add("Wed");
//        labels.add("Thus");
//        labels.add("Fri");
//        labels.add("Sat");
//        labels.add("Sun");
//
//        BarData data = new BarData(labels, bardataset);
//        barChart.setData(data); // set the data and list of lables into chart
//        // set the description
//
//        barChart.animateY(5000);






        BarChart chart = (BarChart) findViewById(R.id.bar_chart);


        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
        barEntries.add(new BarEntry(2f, 0));
        barEntries.add(new BarEntry(4f, 1));
        barEntries.add(new BarEntry(6f, 2));
        barEntries.add(new BarEntry(8f, 3));
        barEntries.add(new BarEntry(7f, 4));
        barEntries.add(new BarEntry(3f, 5));

        BarDataSet barDataSet = new BarDataSet(barEntries,"CRIME");
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);

        BarData data = new BarData(dataSets);
        chart.setData(data);


//        ArrayList<String> labels = new ArrayList<>();
//        labels.add("January");
//        labels.add("February");
//        labels.add("March");
//        labels.add("April");
//        labels.add("May");
//        labels.add("June");
//
//        BarData theData = new BarData(labels, dataSets);
//        chart.setData(theData);

        //dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

      //  chart.setDescription("No of Projects");




    }
}
