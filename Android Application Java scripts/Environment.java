package com.example.ver1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
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

import java.sql.Timestamp;

public class Environment extends AppCompatActivity {

    private static final String TAG = "EMQTT";
    public String clientId;
    double x =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habitat);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Environment.this);
        //double temp = dataBaseHelper.getLast("TEMPERATURE_TABLE");
        showLight();
        showSound();
        humMQTT();
        airGraph();
        tempGraph();
        //TextView tempString = (TextView) findViewById(R.id.humEnviroText);
        //tempString.setText(String.valueOf(temp));


    }


    public void humMQTT(){
        String broker = "tcp://mqtt.beia-telemetrie.ro:1883";
        //The adress of the broker
        String topic = "training/device/eduard-epurica/hum";
        //The Name of the topic
        clientId = MqttClient.generateClientId();
        //Function used to obtain a randomly generated client ID
        MqttAndroidClient client = new MqttAndroidClient(this.getApplicationContext(),broker , clientId);
        //Creating a new Mqtt client, giving as parameters the client ID and the MQTT Broker

        try {
            IMqttToken token = client.connect();
            //This token object connects to an MQTT Broker using default actions
            token.setActionCallback(new IMqttActionListener() {
                //Checking if connection has been successful
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // If we enter this function, it means that we have successfully connected to the broker
                    Log.d(TAG, "onSuccess");
                    try {
                        client.subscribe(topic, 0, null, new IMqttActionListener() {
                            //subscribing to the topic set up earlier, selecting a Quality of Service of 0
                            //and using and MqttActionListener to check if the action has been completed
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "subscribed successfully to humidity ");
                                //sending a log message if the subscription has been successful
                            }
                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribed failed to humidity in habitat");
                                //sending a log message if the subscription has failed
                            }
                        });
                    } catch (MqttException e) { e.printStackTrace();}
                        //Giving an error if the communication to the broker has failed
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "onFailure"); }
                    // If we enter this function, it means the connection to the broker has timed out
            });
        } catch (MqttException e) { e.printStackTrace(); }


        client.setCallback(new MqttCallback() {
            //Creating a callback to verify when a message has been received
            @Override
            public void connectionLost(Throwable cause) {
                    //Function used if the connection to the broker has timed out
                    //In this case we don't do any action
            }
            //double x = 0;
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                //If the message has arrived we enter this function and do various commands
                String[] split = message.toString().split(":");
                split[1] = split[1].substring(0,split[1].length()-1);
                //Since the messages from the sensors come in the form of "Humidity : xx.xx",
                //We have to separate the message in order to obtain the numerical value of the measuerement
                //Therefore we split the message in order to obtain the number that interests us
                double TempMsg = Double.parseDouble(split[1]);
                //Transform the number from string into a double that we can use to enter into our database
                Log.i(TAG,"Humidity: " + split[1] + " " + x);
                TextView temp = (TextView) findViewById(R.id.humEnviroText);
                //Creating a TextView object that we will use to modify the visible Humidity value in our activity
                String humFinal = split[1] + " %";
                temp.setText(humFinal);
                //Changing the old humidity value to the new one.



                tempClass humidityModel;

                try {
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    humidityModel = new tempClass(-1, TempMsg, time.toString());
                    Toast toast = Toast.makeText(Environment.this, humidityModel.toString(), Toast.LENGTH_LONG);
                   // toast.show();
                }
                catch (Exception e){
                    Toast toast = Toast.makeText(Environment.this, "Failure", Toast.LENGTH_LONG);
                  //  toast.show();
                    humidityModel = new tempClass(-1, 0, "Error");

                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(Environment.this);

                boolean success = dataBaseHelper.addHum(humidityModel);


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                    //Here commmands can be done when the message delivery has been completed
                    //We do not need to modify this function for our application.
            }
        });
    }

    public void airGraph(){
        String topic = "training/device/eduard-epurica/air";
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
                        client.subscribe("training/device/eduard-epurica/CO2", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "subscribed succeed to Air-quality in habitat");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribed failed to air-quality");
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
                String[] split = message.toString().split(":");
                split[1] = split[1].substring(0,split[1].length()-1);
                Log.i(TAG,"Air quality: " + split[1] + " " + x);
                TextView temp = (TextView) findViewById(R.id.airEnviroQual);
                String tempFinal = split[1] + "ppm";
                temp.setText(tempFinal);

                double TempMsg = Double.parseDouble(split[1]);

                tempClass CO2Model;

                try {
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    CO2Model = new tempClass(-1, TempMsg, time.toString());
                    Toast toast = Toast.makeText(Environment.this, CO2Model.toString(), Toast.LENGTH_LONG);
                   // toast.show();
                }
                catch (Exception e){
                    Toast toast = Toast.makeText(Environment.this, "Failure", Toast.LENGTH_LONG);
                   // toast.show();
                    CO2Model = new tempClass(-1, 0, "Error");

                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(Environment.this);

                boolean success = dataBaseHelper.addCO2(CO2Model);

                //Toast.makeText(Environment.this,"Success CO2= " + success,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void tempGraph(){
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
                        client.subscribe("training/device/eduard-epurica/temp", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "subscribed succeed to temperature in habitat");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribed failed to temperature");
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
                String[] split = message.toString().split(":");
                split[1] = split[1].substring(0,split[1].length()-1);
                Log.i(TAG,"Temperature: " + split[1] + " " + x);
                TextView temp = (TextView) findViewById(R.id.tempText);
                String tempFinal = split[1] + " \u2103";
                temp.setText(tempFinal);
                double TempMsg = Double.parseDouble(split[1]);

                tempClass temperatureModel;

                try {
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    temperatureModel = new tempClass(-1, TempMsg, time.toString());
                    Toast toast = Toast.makeText(Environment.this, temperatureModel.toString(), Toast.LENGTH_LONG);
                //    toast.show();
                }
                catch (Exception e){
                    Toast toast = Toast.makeText(Environment.this, "Failure", Toast.LENGTH_LONG);
                //   toast.show();
                    temperatureModel = new tempClass(-1, 0, "Error");

                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(Environment.this);

                boolean success = dataBaseHelper.addTemp(temperatureModel);

                //Toast.makeText(MainActivity.this,"Success= " + success,Toast.LENGTH_SHORT).show();


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void showLight(){
        String topic = "training/device/eduard-epurica/light";
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
                                Log.i(TAG, "subscribed succeed to light");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribed failed to light");
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
                Log.i(TAG,"Light: " + split[1] );

                double lightmsg = Double.parseDouble(split[1]);

                if(lightmsg==1){
                TextView txt = findViewById(R.id.lightEnviroText);
                txt.setText("Room is Lit");}
                else
                {
                    TextView txt = findViewById(R.id.lightEnviroText);
                    txt.setText("Room is Dark");
                }



                double TempMsg = Double.parseDouble(split[1]);


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void showSound(){
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
                        client.subscribe("training/device/eduard-epurica/sound", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "subscribed succeed to sound bish");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribed failed");
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


        // graphView = findViewById(R.id.tempGraph);



        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }
            //double x = 0;
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                TextView tv1 = (TextView)findViewById(R.id.soundText);
                tv1.setText(message.toString());
                Log.i(TAG,"Message: " + message.toString());
                ImageView sound = (ImageView) findViewById(R.id.soundEnviro);
                sound.setBackgroundColor(getResources().getColor(R.color.backround_darkgreen));

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }


    public void graphEnviroClick(View view)
    {
        Intent intent = new Intent(Environment.this, Graphs.class);
        intent.putExtra("key","30");
        Environment.this.startActivity(intent);
    }


    public void safetyEnviroClick(View view)
    {
        Intent intent3 = new Intent(Environment.this, Safety.class);
        //intent.putExtra("key","34");
        Environment.this.startActivity(intent3);
    }

    public void securityEnviroClick(View view)
    {
        Intent intent3 = new Intent(Environment.this, Security.class);
        //intent.putExtra("key","34");
        Environment.this.startActivity(intent3);
    }

    public void homeClick(View view)
    {
        Intent intent4 = new Intent(Environment.this, MainActivity.class);
        //intent.putExtra("key","34");
        Environment.this.startActivity(intent4);
    }

    public void serviceClick(View view)
    {
        startService(new Intent(this,BckService.class));
    }

    public void stopServiceClick(View view)
    {
        stopService(new Intent(this,BckService.class));
    }




}
