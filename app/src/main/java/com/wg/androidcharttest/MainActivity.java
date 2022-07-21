package com.wg.androidcharttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineChart = findViewById(R.id.chart);

        List<Entry> entryList = new ArrayList<Entry>();
        Entry data1 = new Entry(10,0);
        Entry data2 = new Entry(20,5);
        Entry data3 = new Entry(30,10);
        Entry data4 = new Entry(40,15);

        entryList.add(data1);
        entryList.add(data2);
        entryList.add(data3);
        entryList.add(data4);

        LineDataSet dataSet = new LineDataSet(entryList,"linechart");
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
    }
}