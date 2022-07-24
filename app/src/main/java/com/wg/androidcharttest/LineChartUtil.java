package com.wg.androidcharttest;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LineChartUtil {
    private LineChart lineChart;
    private List<Entry> dataList = new ArrayList<>();
    private List<String> XLabel = new ArrayList<>();
    private LineData lineData;
    private LineDataSet lineDataSet;

    public LineChartUtil(LineChart lineChart) {
        this.lineChart = lineChart;
        Setting();
        SetXAxis();
        SetYAxis();
        initLineDataSet("方波图",Color.BLUE);
    }

    private void Setting(){
        lineChart.setDoubleTapToZoomEnabled(false);
        // 不显示数据描述
        lineChart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        lineChart.setNoDataText("暂无数据");
        //禁止x轴y轴同时进行缩放
        lineChart.setPinchZoom(false);
        //启用/禁用缩放图表上的两个轴。
        lineChart.setScaleEnabled(false);
        //设置为false以禁止通过在其上双击缩放图表。
        lineChart.getAxisRight().setEnabled(false);//关闭右侧Y轴
        lineChart.setDrawGridBackground(false);
        //显示边界
        lineChart.setDrawBorders(true);
        lineChart.setBorderColor(Color.parseColor("#d5d5d5"));


        //折线图例 标签 设置 这里不显示图例
//        Legend legend = lineChart.getLegend();
//        legend.setEnabled(false);
    }

    private void SetXAxis(){
        //绘制X轴
        XAxis xAxis = lineChart.getXAxis();
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

//        xAxis.setValueFormatter(new ValueFormatter() {
//            private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SS");
//            @Override
//            public String getFormattedValue(float value) {
////                return String.valueOf((int) value + 1).concat("月");
//                return format.format((long)value);
//            }
//        });

    }

    private void SetYAxis(){
        //绘制Y轴
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMaximum(30000);
        yAxis.setAxisMinimum(-30000);
    }

    /**
     * 初始化一条折线
     */
    private void initLineDataSet(String name, int color) {

        for (int i = 0;i<1000;i++){
            Entry entry = new Entry(i,0);
            Log.d("initLineDataSet", "initLineDataSet: "+entry.getX()+","+entry.getY());
            XLabel.add(new SimpleDateFormat("HH:mm:ss:SS").format(new Date().getTime()));
            dataList.add(entry);
        }

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(XLabel));

        lineDataSet = new LineDataSet(dataList, name);
        lineDataSet.setLineWidth(1.5f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setColor(Color.BLUE);
        //添加一个空的 LineData
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

//        LineChartMarkView mv = new LineChartMarkView(context, xAxis.getValueFormatter());
//        mv.setChartView(lineChart);
//        lineChart.setMarker(mv);
        lineChart.invalidate();

    }

    /**
     * 动态添加数据
     */
    public void addEntry(){
        if (lineDataSet.getEntryCount() == 0){
            lineData.addDataSet(lineDataSet);
        }
        lineChart.setData(lineData);
        lineDataSet.setDrawValues(false);//不显示折线上的值
        Random random = new Random();

       for (int i = 0; i < 10; i++){
            //list.add(new Entry(i,random.nextInt(20000)-10000));
            lineData.addEntry(new Entry(lineData.getEntryCount(),random.nextInt(20000)-10000),0);
        }
        lineDataSet.notifyDataSetChanged();
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();//对图表数据进行更新
        //lineChart.setVisibleXRangeMaximum(1000);
        lineChart.moveViewToX(lineData.getEntryCount() - 10);
        //lineChart.moveViewTo(lineData.getEntryCount()-10,55f, YAxis.AxisDependency.LEFT);
        lineChart.invalidate();
    }

    public void UpdateData(){
         Random random = new Random();
         for (int i = 0; i<10;i++){
             dataList.remove(0);
             XLabel.remove(0);
         }
         for (int i = 0;i<dataList.size();i++){
             Entry entry = dataList.get(i);
             dataList.set(i,new Entry(i,entry.getY()));
         }
         for (int i = 0; i<10;i++){
             Entry entry = new Entry(dataList.size(),random.nextInt(20000)-10000);
             dataList.add(entry);
             //更新x轴标签的数据
             XLabel.add(new SimpleDateFormat("HH:mm:ss:SS").format(new Date().getTime()));
         }

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(XLabel));

        lineDataSet.setValues(dataList);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        //lineChart.moveViewTo(lineData.getEntryCount() - 10,50f, YAxis.AxisDependency.LEFT);
        lineChart.notifyDataSetChanged();

        lineChart.invalidate();
    }

}
