package com.example.ver1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.security.AccessController.getContext;

public class Graphs extends AppCompatActivity {


    private static final String TAG = "EMQTT";
    private String clientId ;
    private GraphView graphView ;
    private GraphView graphView2 ;
    private GraphView graphView3 ;
    private GraphView graphView4 ;
    private GraphView graphView5 ;
    private String topic;
    Integer x=0;
    DataBaseHelper dataBaseHelper ;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
    LineGraphSeries<DataPoint> series4 = new LineGraphSeries<DataPoint>();
    LineGraphSeries<DataPoint> series5 = new LineGraphSeries<DataPoint>();
    LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>();
    LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>();
    List<tempClass> allTemp ;
    List<tempClass> allHum ;
    List<tempClass> allCO2 ;
    List<tempClass> allCO ;
    List<tempClass> allLPG;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphs);
        dataBaseHelper = new DataBaseHelper(this);
        tempGraph();
        x=0;
        humGraph();
        x=0;
        co2Graph();
        x=0;
        coGraph();
        x=0;
        LPGGraph();
        long count = dataBaseHelper.sizeTemp();
        String ctr = String.valueOf(count);
        //Toast.makeText(this,ctr,Toast.LENGTH_LONG).show();
        Log.i(TAG,ctr);

    }

    Date date;
    public void humGraph(){
        allHum = dataBaseHelper.getAllHum();
        graphView2= findViewById(R.id.graphHum);
        while (x < allHum.size()) {
            graphView2= findViewById(R.id.graphHum);
            dataBaseHelper = new DataBaseHelper(Graphs.this);
            double msg = allHum.get(x).getValue();
            String time = allHum.get(x).getTime();
            //Saving the sensor value and the timestamp of the current row
            Log.i(TAG,time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            //Turning the date from string to DateFormat
            try { date = df.parse(time);
                System.out.println(date);
                Log.i(TAG, String.valueOf(date)); }
            catch (ParseException e) { e.printStackTrace(); }
            double ex = Double.valueOf(x);
            DataPoint d = new DataPoint(date, msg);
            series2.appendData(d, true, 1400);
            x = x + 1;
        }
        series2.setDrawBackground(true);
        series2.setBackgroundColor(Color.argb(50, 0, 0, 250));
        graphView2.addSeries(series2);
        graphView2.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(Graphs.this));
        graphView2.getGridLabelRenderer().setNumHorizontalLabels(3);
        dataBaseHelper.deleteExcessHum();
    }

    public void tempGraph() {
        allTemp = dataBaseHelper.getAllTemp();
        //Retreving all the temperature data from the database
        graphView = findViewById(R.id.graphTemp);
        //connecting the graphView object to the XML visual representation
        while (x < allTemp.size()) {
            //Using a while structure to loop through every row
            graphView = findViewById(R.id.graphTemp);
            dataBaseHelper = new DataBaseHelper(Graphs.this);
            double msg = allTemp.get(x).getValue();
            String time = allTemp.get(x).getTime();
            //Saving the sensor value and the timestamp of the current row
            Log.i(TAG,time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            //Turning the date from string to DateFormat
            try { date = df.parse(time);
                System.out.println(date);
                Log.i(TAG, String.valueOf(date)); }
            catch (ParseException e) { e.printStackTrace(); }
            //Turning the date from string to DateFormat
            double ex = Double.valueOf(x);
            DataPoint d = new DataPoint(date, msg);
            //Creating a new datapoint with the date and the value
            series.appendData(d, true, 1400);
            //Adding the datapoint to the series
            x = x + 1;

        }
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(50, 150, 0, 200));
        graphView.removeAllSeries();
        graphView.removeSeries(series);
        graphView.addSeries(series);
        //Adding the series to the graph
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(Graphs.this));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);

        dataBaseHelper.deleteExcess();
        //Deleting excess points that are too old to be relevant
    }

    public void co2Graph() {
        allCO2 = dataBaseHelper.getAllCO2();
        graphView3 = findViewById(R.id.graphCO2);

        while (x < allCO2.size()) {
            graphView3 = findViewById(R.id.graphCO2);
            dataBaseHelper = new DataBaseHelper(Graphs.this);
            double msg = allCO2.get(x).getValue();
            String time = allCO2.get(x).getTime();
            //Saving the sensor value and the timestamp of the current row
            Log.i(TAG,time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            //Turning the date from string to DateFormat
            try { date = df.parse(time);
                System.out.println(date);
                Log.i(TAG, String.valueOf(date)); }
            catch (ParseException e) { e.printStackTrace(); }

            double ex = Double.valueOf(x);
            DataPoint d = new DataPoint(date, msg);
            series3.appendData(d, true, 1400);
            x = x + 1;

        }
        series3.setDrawBackground(true);
        series3.setBackgroundColor(Color.argb(50, 200, 0, 50));
        graphView3.removeAllSeries();
        graphView3.removeSeries(series3);
        graphView3.addSeries(series3);
        graphView3.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(Graphs.this));
        graphView3.getGridLabelRenderer().setNumHorizontalLabels(3);
        dataBaseHelper.deleteExcessCO2();
    }

    public void coGraph() {
        allCO = dataBaseHelper.getAllCO();
        graphView3 = findViewById(R.id.graphCO);

        while (x < allCO.size()) {
            graphView4 = findViewById(R.id.graphCO);
            dataBaseHelper = new DataBaseHelper(Graphs.this);
            double msg = allCO.get(x).getValue();
            String time = allCO.get(x).getTime();
            //Saving the sensor value and the timestamp of the current row
            Log.i(TAG,time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            //Turning the date from string to DateFormat
            try { date = df.parse(time);
                System.out.println(date);
                Log.i(TAG, String.valueOf(date)); }
            catch (ParseException e) { e.printStackTrace(); }

            double ex = Double.valueOf(x);
            DataPoint d = new DataPoint(date, msg);
            series4.appendData(d, true, 1400);
            x = x + 1;

        }
        series4.setDrawBackground(true);
        series4.setBackgroundColor(Color.argb(50, 50, 200, 50));
        //graphView4.removeAllSeries();
        graphView4.removeSeries(series4);
        graphView4.addSeries(series4);
        graphView4.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(Graphs.this));
        graphView4.getGridLabelRenderer().setNumHorizontalLabels(3);
        dataBaseHelper.deleteExcessCO();
    }

    public void LPGGraph() {
        allLPG = dataBaseHelper.getAllLPG();
        graphView5 = findViewById(R.id.graphLPG);

        while (x < allLPG.size()) {
            graphView5 = findViewById(R.id.graphLPG);
            dataBaseHelper = new DataBaseHelper(Graphs.this);
            double msg = allLPG.get(x).getValue();
            String time = allLPG.get(x).getTime();
            //Saving the sensor value and the timestamp of the current row
            Log.i(TAG,time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            //Turning the date from string to DateFormat
            try { date = df.parse(time);
                System.out.println(date);
                Log.i(TAG, String.valueOf(date)); }
            catch (ParseException e) { e.printStackTrace(); }
            double ex = Double.valueOf(x);
            DataPoint d = new DataPoint(date, msg);
            series5.appendData(d, true, 1400);
            x = x + 1;

        }
        series5.setDrawBackground(true);
        series5.setBackgroundColor(Color.argb(50, 200, 200, 0));
        //graphView5.removeAllSeries();
        graphView5.removeSeries(series5);
        graphView5.addSeries(series5);
        graphView5.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(Graphs.this));
        graphView5.getGridLabelRenderer().setNumHorizontalLabels(3);
        dataBaseHelper.deleteExcessLPG();
    }


    public void returnClick(View view)
    {
        Intent intent = new Intent(Graphs.this, MainActivity.class);
        intent.putExtra("key","30");
        Graphs.this.startActivity(intent);
    }

    public void dbClick(View view)
    {
        Intent intent = new Intent(Graphs.this, DataBaseTest.class);
        Graphs.this.startActivity(intent);
    }
}
