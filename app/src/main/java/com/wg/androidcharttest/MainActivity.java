package com.wg.androidcharttest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.transition.MaterialSharedAxis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    private LineChart mChart;
    private LineChartUtil lineChartUtil;
    private NavigationView navigationView;
    private static final int IMPORT_CODE = 1;
    private DrawerLayout drawerLayout;
    private Button open_Menu;
    private Queue<Integer> dataQueue = new LinkedList<>();
    private Boolean isRead = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
            actionBar.setDisplayShowCustomEnabled(true);
        }
        drawerLayout = findViewById(R.id.drawer_layout);
        mChart = findViewById(R.id.chart);
        lineChartUtil = new LineChartUtil(mChart);
        open_Menu = (Button)findViewById(R.id.open_Menu);
        open_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        navigationView = (NavigationView)findViewById(R.id.nav_vew);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.start:
                        ChartUpdateThread chartUpdateThread = new ChartUpdateThread();
                        isRead = true;
                        new Thread(chartUpdateThread).start();
                        break;
                    case R.id.stop:
                        isRead = false;
                        break;
                    case R.id.Import:
                         ImportFile();
                         //关闭侧滑菜单
                         drawerLayout.closeDrawer(GravityCompat.END);
                        break;
                    case R.id.save:

                        break;
                }
                return false;
            }
        });


    }


    private void ImportFile(){
        Uri uri = MediaStore.Files.getContentUri("external");
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
        startActivityIfNeeded(intent,IMPORT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMPORT_CODE && resultCode == RESULT_OK){
            //进度条对话框
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("正在导入");
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setIcon(R.drawable.ic_launcher_background);
            progressDialog.show();

            Log.d("是否选取到文件:", "onActivityResult: ");
            Uri uri = data.getData();
            Log.d(TAG, "文件的uri: "+uri);
            InputStream inputStream = null;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                inputStream = getContentResolver().openInputStream(uri);

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = reader.readLine()) != null){
                    stringBuilder.append(line);
                }
                progressDialog.cancel();
                Log.d(TAG, "读取到的数据: "+stringBuilder);
                DataSolution(stringBuilder.toString());
                Toast.makeText(MainActivity.this, "读取成功", Toast.LENGTH_SHORT).show();

                //read_text.setText(stringBuilder);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 数据解析
     */
    private void DataSolution(String dataStr){
        String[] dataArr = dataStr.split(" ");
        Log.d(TAG, "DataSolution: "+ Arrays.toString(dataArr));
        for (int i = 0; i < dataArr.length; i++){
            dataQueue.add(Integer.parseInt(dataArr[i]));
        }
    }

    class ChartUpdateThread implements Runnable{
        @Override
        public void run() {
            while (isRead){
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //lineChartUtil.addEntry();
                List<Integer> dataPoints = new ArrayList<>();
                for (int i = 0; i<10; i++){
                   dataPoints.add(dataQueue.poll());
                }
                lineChartUtil.UpdateData(dataPoints);
            }

        }
    }
}