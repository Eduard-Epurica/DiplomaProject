package com.example.ver1;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static com.example.ver1.BaseApplication.Channel_1;

public class Security extends AppCompatActivity {

    public String clientId;
    private static final String TAG = "EMQTT";
    WebView videov;
    private NotificationManagerCompat notificationManager;

    private String cFeed = "Close feed";
    private String oFeed = "Open  feed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security);
        videov = (WebView) findViewById(R.id.videoFeed);
        String url = "http://video-check.go.ro:8000";
        videov.loadUrl(url);
        showDoor();
        showMove();
        notificationManager = NotificationManagerCompat.from(this);
    }

    public void videoplay(View v)
    {
        Button videoFeed = (Button) findViewById(R.id.videoButton);
        if(oFeed == videoFeed.getText()){
            String url = "http://video-check.go.ro:8000";
            videov.loadUrl(url);
            videoFeed.setText(cFeed);

        }
        else
        {
            videov.destroy();
            videoFeed.setText(oFeed);
        }


    }

    public void showDoor(){
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
                        client.subscribe("training/device/eduard-epurica/door", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "Subscribed succesfully to the door topic");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribe failed");
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
                String msg = message.toString();
                String[] split = msg.split(":");
                split[1] = split[1].substring(1,split[1].length()-1);
                Log.i(TAG,"Sound: " + split[1] );
                double lightmsg = Double.parseDouble(split[1]);

                if(lightmsg==0){
                    ImageView door = findViewById(R.id.door_img);
                    door.setBackgroundTintList(ContextCompat.getColorStateList(Security.this, R.color.backround_blue));

                }
                else
                {
                    ImageView door = findViewById(R.id.door_img);
                    door.setBackgroundTintList(ContextCompat.getColorStateList(Security.this, R.color.backround_red));

                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }


    public void returnClick(View view)
    {
        Intent intent2 = new Intent(Security.this, MainActivity.class);
        //intent.putExtra("key","34");
        Security.this.startActivity(intent2);
    }

    public void showMove(){
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
                        client.subscribe("training/device/eduard-epurica/move", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "Subscribed succesfully to the movement topic");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribe failed");
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
                Log.i(TAG,"Move: " + split[1] );
                double lightmsg = Double.parseDouble(split[1]);

                if(lightmsg==0){
                    ImageView door = findViewById(R.id.move_img);
                    door.setBackgroundTintList(ContextCompat.getColorStateList(Security.this, R.color.backround_blue));

                }
                else
                {
                    ImageView door = findViewById(R.id.move_img);
                    door.setBackgroundTintList(ContextCompat.getColorStateList(Security.this, R.color.backround_red));

                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }


    public void sendChannel1(View v){
        String topic = "training/device/eduard-epurica";
        clientId = MqttClient.generateClientId();
        MqttAndroidClient client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://mqtt.beia-telemetrie.ro:1883",
                        clientId);
        Button alarm = findViewById(R.id.Security_alarm);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    try {
                        client.subscribe("training/device/eduard-epurica/buzz", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                            //In case subscription to the topic worked, try to publish a message
                                try {
                                    String payload;
                                    if(alarm.getText() == "START ALARM"){
                                        alarm.setText("STOP ALARM");
                                        payload = "1";}
                                    else{
                                        alarm.setText("START ALARM");
                                        payload = "0";
                                    }
                                    //Setting up the contents of the message
                                    MqttMessage message = new MqttMessage();
                                    message.setPayload(payload.getBytes());
                                    message.setQos(0);
                                    //Creating a message object, setting the contents and the Quality of service
                                    client.publish("training/device/eduard-epurica/buzz", message,null, new IMqttActionListener() {
                                        @Override
                                        public void onSuccess(IMqttToken asyncActionToken) {
                                            Log.i(TAG, "Alarm notice published") ;
                                            //In case publishing the message succeded, send a message
                                        }

                                        @Override
                                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                            Log.i(TAG, "Alarm notice publish failed") ;
                                            //In case publishing the message failed, send a message
                                        }
                                    });
                                } catch (MqttException e) {
                                    Log.e(TAG, e.toString());
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribe failed");
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
    }


}
