package com.example.ver1;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Timestamp;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class DataBaseTest extends AppCompatActivity {

    Button btnDataTemp;
    Button btnDataHum;
    Button btnDataCO2;
    TextView textData;
    ListView lv;
    ArrayAdapter adapter;
    DataBaseHelper dataBaseHelper;
    List<tempClass> all;

    int i=1;
    Timestamp time = new Timestamp(System.currentTimeMillis());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database);

        btnDataTemp = findViewById(R.id.btnDataTemp);
        btnDataHum = findViewById(R.id.btnDataHum);
        btnDataCO2 = findViewById(R.id.btnDataCO2);
        textData = findViewById(R.id.textData);

        btnDataTemp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dataBaseHelper = new DataBaseHelper(DataBaseTest.this);
                all = dataBaseHelper.getAllTemp();
                adapter = new ArrayAdapter<tempClass>(DataBaseTest.this, android.R.layout.simple_list_item_1,all);
                lv=findViewById(R.id.databaseList);
                textData.setText("Temperature data");
                lv.setAdapter(adapter);
            }
        });

        btnDataHum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dataBaseHelper = new DataBaseHelper(DataBaseTest.this);
                all = dataBaseHelper.getAllHum();
                adapter = new ArrayAdapter<tempClass>(DataBaseTest.this, android.R.layout.simple_list_item_1,all);
                lv=findViewById(R.id.databaseList);
                textData.setText("Humidity data");
                lv.setAdapter(adapter);
            }
        });

        btnDataCO2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dataBaseHelper = new DataBaseHelper(DataBaseTest.this);
                all = dataBaseHelper.getAllCO2();
                adapter = new ArrayAdapter<tempClass>(DataBaseTest.this, android.R.layout.simple_list_item_1,all);
                lv=findViewById(R.id.databaseList);
                textData.setText("CO2 data");
                lv.setAdapter(adapter);
            }
        });

    }

}


