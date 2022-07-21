package com.wg.androidcharttest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.transition.MaterialSharedAxis;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LineChart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
            actionBar.setDisplayShowCustomEnabled(true);
        }
        mChart = findViewById(R.id.chart);

        mChart.setDoubleTapToZoomEnabled(false);
        // 不显示数据描述
        mChart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        mChart.setNoDataText("暂无数据");
        //禁止x轴y轴同时进行缩放
        mChart.setPinchZoom(false);
        //启用/禁用缩放图表上的两个轴。
        mChart.setScaleEnabled(false);
        //设置为false以禁止通过在其上双击缩放图表。
        mChart.getAxisRight().setEnabled(false);//关闭右侧Y轴
        mChart.setDrawGridBackground(false);
        //显示边界
        mChart.setDrawBorders(true);
        mChart.setBorderColor(Color.parseColor("#d5d5d5"));


        //折线图例 标签 设置 这里不显示图例
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        //绘制X轴
        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisMaximum(1000);
        xAxis.setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(Color.parseColor("#d8d8d8"));
        //设置最后和第一个标签不超出x轴
        xAxis.setAvoidFirstLastClipping(true);
//        设置线的宽度
        xAxis.setAxisLineWidth(1.0f);
        xAxis.setAxisLineColor(Color.parseColor("#d5d5d5"));
        xAxis.setLabelCount(6,true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return super.getFormattedValue(value);
            }
        });

        //绘制Y轴
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisMaximum(30000);
        yAxis.setAxisMinimum(-30000);


        List<Entry> entryList = new ArrayList<Entry>();
        Entry data1 = new Entry(10, 0);
        Entry data2 = new Entry(20,5);
        Entry data3 = new Entry(30,10);
        Entry data4 = new Entry(40,15);

        entryList.add(data1);
        entryList.add(data2);
        entryList.add(data3);
        entryList.add(data4);

        LineDataSet dataSet = new LineDataSet(entryList,"linechart");
        dataSet.setLineWidth(1.5f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.BLUE);
        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);
    }
}