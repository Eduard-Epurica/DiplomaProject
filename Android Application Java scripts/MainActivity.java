package com.example.ver1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EMQTT";
    private String clientId ;

    double x=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void graphClick(View view)
    {
     Intent intent = new Intent(MainActivity.this, Graphs.class);
     intent.putExtra("key","30");
     MainActivity.this.startActivity(intent);
    }

    public void safetyClick(View view)
    {
        Intent intent3 = new Intent(MainActivity.this, Safety.class);
        //intent.putExtra("key","34");
        MainActivity.this.startActivity(intent3);
    }

    public void environmentClick(View view)
    {
        Intent intent2 = new Intent(MainActivity.this, Environment.class);
        //intent.putExtra("key","34");
        MainActivity.this.startActivity(intent2);
    }

    public void securityClick(View view)
    {
        Intent intent2 = new Intent(MainActivity.this, Security.class);
        //intent.putExtra("key","34");
        MainActivity.this.startActivity(intent2);
    }

}