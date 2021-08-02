package com.example.ver1;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;

import static com.example.ver1.BaseApplication.Channel_1;
import static com.example.ver1.BaseApplication.Channel_2;
import static com.example.ver1.BaseApplication.Channel_4;
import static com.example.ver1.BaseApplication.Channel_5;

public class SafetyService extends Service {

    private static final String TAG = "EMQTT";
    public String clientId;
    private NotificationManagerCompat notificationManager2;
    private double concentration;


    String topic = "training/device/eduard-epurica/CO";
    String topic2 = "training/device/eduard-epurica/fire";
    String topic3 = "training/device/eduard-epurica/LPG";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast toast = Toast.makeText(this,"Safety service connected",Toast.LENGTH_LONG);
        toast.show();
        CONoti();
        FireNoti();
        LPGNoti();

        return START_STICKY;

    }



    public void CONoti(){

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
                                Log.i(TAG, "subscribed CO service");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribed failed to CO service");
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                    try {
                        client.subscribe(topic2, 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "subscribed fire service");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribed failed to fire service");
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
                Log.i(TAG,"CO: " + split[1] );
                double lightmsg = Double.parseDouble(split[1]);
                if (lightmsg > 100) {
                    Log.i(TAG,"CO service working");
                    concentration = lightmsg;
                    Notify();
                    client.disconnect();
                    client.close();
                    stopSelf();
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    public void Notify()
    {
        notificationManager2 = NotificationManagerCompat.from(this);
        String concen = String.valueOf(concentration);
        Intent intent = new Intent(SafetyService.this, Environment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(SafetyService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(SafetyService.this, Channel_1)
                .setSmallIcon(R.drawable.ic_baseline_smoke_circle_24)
                .setContentTitle("High CO")
                .setContentText("CO concentration is high: " + concen + " ppm")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent)
                .build();
        Random r = new Random();
        int Id = r.nextInt();
        notificationManager2.notify(Id, notification);
    }


    public void LPGNoti(){

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
                        client.subscribe(topic3, 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "subscribed LPG service");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribed failed to LPG service");
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
                Log.i(TAG,"LPG: " + split[1] );
                double lightmsg = Double.parseDouble(split[1]);
                if (lightmsg > 1000) {
                    Log.i(TAG,"LPG service working");
                    concentration = lightmsg;
                    NotifyLPG();
                    client.disconnect();
                    client.close();
                    stopSelf();
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    public void FireNoti(){
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
                        client.subscribe(topic2, 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "subscribed to Fire service");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribed failed to Fire service");
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                    try {
                        client.subscribe(topic2, 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "subscribed fire service");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribed failed to fire service");
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
                Log.i(TAG,"Fire: " + split[1] );
                double firemsg = Double.parseDouble(split[1]);
                //When receiving a message, we split it into two strings and convert the
                //second part of the string into a double type variable
                //in order to utilise that variable for comparing
                if (firemsg == 0) {
                    Log.i(TAG,"Fire service working");
                    NotifyFire();
                    //If fire has been detected, call the Fire notification building method
                    //Close the client and stop the service after the fire has been detected
                    //This is done in order to not slow down the main application if the user opens it
                    client.disconnect();
                    client.close();
                    stopSelf();
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }


    public void NotifyLPG()
    {
        notificationManager2 = NotificationManagerCompat.from(this);
        String concen = String.valueOf(concentration);
        Intent intent = new Intent(SafetyService.this, Environment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(SafetyService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(SafetyService.this, Channel_5)
                .setSmallIcon(R.drawable.ic_baseline_local_gas_station_24)
                .setContentTitle("LPG leak")
                .setContentText("LPG concentration is too high: " + concen + " ppm")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent)
                .build();
        Random r = new Random();
        int Id = r.nextInt();
        notificationManager2.notify(Id, notification);
    }



    public void NotifyFire()
    {
        notificationManager2 = NotificationManagerCompat.from(this);
        //Creating the notification manager object used to display the notification
        Intent intent = new Intent(SafetyService.this, Safety.class);
        //Creating an intent that opens up the Safety activity when activated
        PendingIntent pendingIntent = PendingIntent.getActivity(SafetyService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //Building the pendingIntent object, that uses the previously created Intent
        //The pending intent is necessary in the notification builder to enable the user to press on the notification
        Log.i(TAG,"Fire notification creating");
        Notification notification = new NotificationCompat.Builder(SafetyService.this, Channel_4)
                //Creating notification object
                .setSmallIcon(R.drawable.ic_baseline_local_fire_department_24)
                .setContentTitle("Fire detected")
                .setContentText("Fire detected, evacuate room")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent)
                //Setting up various parameters such as the content, the category and the intent
                .build();
                //Building the notification with all the selected parameters
        Random r = new Random();
        int Id = r.nextInt();
        //Creating a random Id for enabling multiple notifications at once
        notificationManager2.notify(Id, notification);
        //Finally displaying the notification to the user
    }

    @Override
    public void onDestroy() {

        Toast toast = Toast.makeText(this,"destroyed",Toast.LENGTH_LONG);
        toast.show();
        super.onDestroy();

    }

    @Nullable
    @Override



    public IBinder onBind(Intent intent) {
        return null;
    }
}
