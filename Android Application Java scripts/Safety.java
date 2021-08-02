package com.example.ver1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.Timestamp;

public class Safety extends AppCompatActivity {

    public String clientId;
    private static final String TAG = "EMQTT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safety);
        showVibration();
        showFire();
        showCO();
        showLPG();
    }

    public void EnvironmentClick(View view)
    {
        Intent intent3 = new Intent(Safety.this, Environment.class);
        //intent.putExtra("key","34");
        Safety.this.startActivity(intent3);
    }

    public void securitySafetyClick(View view)
    {
        Intent intent3 = new Intent(Safety.this, Security.class);
        //intent.putExtra("key","34");
        Safety.this.startActivity(intent3);
    }

    public void homeSafetyClick(View view)
    {
        Intent intent3 = new Intent(Safety.this, MainActivity.class);
        //intent.putExtra("key","34");
        Safety.this.startActivity(intent3);
    }

    public void alertClick(View view)
    {
        Button alert = findViewById(R.id.safetyAlert);
        String buttonText = alert.getText().toString();
        if(buttonText == "ALERTS ON")
        {startService(new Intent(this,SafetyService.class));
            alert.setText("ALERTS OFF");}
        else {
            stopService(new Intent(this,SafetyService.class));
            alert.setText("ALERTS ON");
        }

    }

    public void showVibration(){
        String topic = "training/device/eduard-epurica";
        clientId = MqttClient.generateClientId();
        MqttAndroidClient client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://mqtt.beia-telemetrie.ro:1883",
                        clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    try {
                        client.subscribe("training/device/eduard-epurica/vibration", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "subscribed to vibration");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribe fail");
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }

            });

        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }
            //double x = 0;
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String msg = message.toString();
                String[] split = msg.split(":");
                split[1] = split[1].substring(1,split[1].length()-1);
                Log.i(TAG,"Vibration value: " + split[1] );
                double vibrationmsg = Double.parseDouble(split[1]);


                if(vibrationmsg==1){
                    TextView vibra = findViewById(R.id.gasTXT);
                    LinearLayout gas = findViewById(R.id.gasLinear);
                    vibra.setText("Danger! Earthquake detected");
                    gas.setBackground(getDrawable(R.drawable.round_red));

                }
                else
                {

                    TextView vibra = findViewById(R.id.gasTXT);
                    LinearLayout gas = findViewById(R.id.gasLinear);
                    vibra.setText("No earthquake detected, all is good");
                    gas.setBackground(getDrawable(R.drawable.round_green));


                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void showFire(){
        String topic = "training/device/eduard-epurica";
        clientId = MqttClient.generateClientId();
        MqttAndroidClient client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://mqtt.beia-telemetrie.ro:1883",
                        clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    try {
                        client.subscribe("training/device/eduard-epurica/fire", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "subscribed to fire");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribe fail");
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }




            });


        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }
            //double x = 0;
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String msg = message.toString();
                String[] split = msg.split(":");
                split[1] = split[1].substring(1,split[1].length()-1);
                Log.i(TAG,"Fire detected: " + split[1] );
                double vibrationmsg = Double.parseDouble(split[1]);


                if(vibrationmsg==1){
                    TextView vibra = findViewById(R.id.fireTXT);
                    LinearLayout gas = findViewById(R.id.fireLinear);
                    vibra.setText("No fire detected, all is good");
                    gas.setBackground(getDrawable(R.drawable.round_green));

                }
                else
                {
                    TextView vibra = findViewById(R.id.fireTXT);
                    LinearLayout gas = findViewById(R.id.fireLinear);
                    vibra.setText("Danger! Fire detected");
                    gas.setBackground(getDrawable(R.drawable.round_red));


                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void showCO(){
        String topic = "training/device/eduard-epurica";
        clientId = MqttClient.generateClientId();
        MqttAndroidClient client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://mqtt.beia-telemetrie.ro:1883",
                        clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    try {
                        client.subscribe("training/device/eduard-epurica/CO", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "subscribed to CO");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribe fail");
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }




            });


        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }
            //double x = 0;
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String msg = message.toString();
                String[] split = msg.split(":");
                split[1] = split[1].substring(1,split[1].length()-1);
                Log.i(TAG,"CO value: " + split[1] );
                double CO = Double.parseDouble(split[1]);
                TextView Coco = findViewById(R.id.concentrationCO);
                Coco.setText("Concentration: " + split [1] + " ppm");

                if(CO<400){
                    TextView co = findViewById(R.id.COtext);
                    LinearLayout gas = findViewById(R.id.coLinear);
                    co.setText("Carbon monoxide concentration ok");
                    gas.setBackground(getDrawable(R.drawable.round_green));

                }
                else
                {
                    TextView co = findViewById(R.id.COtext);
                    LinearLayout gas = findViewById(R.id.coLinear);
                    co.setText("Bad CO concentration!Air unsafe!");
                    gas.setBackground(getDrawable(R.drawable.round_red));
                }

                tempClass COModel;

                try {
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    COModel = new tempClass(-1, CO, time.toString());
                    Toast toast = Toast.makeText(Safety.this, COModel.toString(), Toast.LENGTH_LONG);
                 //   toast.show();
                }
                catch (Exception e){
                    Toast toast = Toast.makeText(Safety.this, "Failure", Toast.LENGTH_LONG);
                  //  toast.show();
                    COModel = new tempClass(-1, 0, "Error");

                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(Safety.this);

                boolean success = dataBaseHelper.addCO(COModel);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void showLPG(){
        String topic = "training/device/eduard-epurica/LPG";
        clientId = MqttClient.generateClientId();
        MqttAndroidClient client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://mqtt.beia-telemetrie.ro:1883",
                        clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    try {
                        client.subscribe(topic, 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "subscribed to LPG");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribe fail for LPG");
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }




            });


        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }
            //double x = 0;
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String msg = message.toString();
                String[] split = msg.split(":");
                split[1] = split[1].substring(1,split[1].length()-1);
                Log.i(TAG,"LPG value: " + split[1] );
                double CO = Double.parseDouble(split[1]);
                TextView Coco = findViewById(R.id.concentrationLPG);
                Coco.setText("Concentration: " + split [1] + " ppm");

                if(CO<400){
                    TextView co = findViewById(R.id.LPGtext);
                    LinearLayout gas = findViewById(R.id.LPGlinear);
                    co.setText("No LPG leak detected");
                    gas.setBackground(getDrawable(R.drawable.round_green));

                }
                else
                {
                    TextView co = findViewById(R.id.LPGtext);
                    LinearLayout gas = findViewById(R.id.LPGlinear);
                    co.setText("LPG leak detected! Check gas devices");
                    gas.setBackground(getDrawable(R.drawable.round_red));
                }

                tempClass LPGModel;

                try {
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    LPGModel = new tempClass(-1, CO, time.toString());
                    Toast toast = Toast.makeText(Safety.this, LPGModel.toString(), Toast.LENGTH_LONG);
                    //toast.show();
                }
                catch (Exception e){
                    Toast toast = Toast.makeText(Safety.this, "Failure", Toast.LENGTH_LONG);
                    //  toast.show();
                    LPGModel = new tempClass(-1, 0, "Error");

                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(Safety.this);

                boolean success = dataBaseHelper.addLPG(LPGModel);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
