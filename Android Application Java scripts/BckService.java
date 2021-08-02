package com.example.ver1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

import java.util.Random;

import static com.example.ver1.BaseApplication.Channel_1;

public class BckService extends Service {

    private static final String TAG = "EMQTT";
    public String clientId;
    private NotificationManagerCompat notificationManager2;
    private double concentration;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast toast = Toast.makeText(this,"Service started",Toast.LENGTH_LONG);
        toast.show();
        String topic = "training/device/eduard-epurica/CO";

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
                if (lightmsg > 0) {
                    Log.i(TAG,"CO service working");
                    concentration = lightmsg;
                    Notify();
                }
                client.disconnect();
                client.close();
                stopSelf();


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


            return START_STICKY;

    }

    public void Notify()
    {
        notificationManager2 = NotificationManagerCompat.from(this);
        String concen = String.valueOf(concentration);
        Intent intent = new Intent(BckService.this, Environment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(BckService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(BckService.this, Channel_1)
                .setSmallIcon(R.drawable.ic_baseline_smoke_circle_24)
                .setContentTitle("High CO")
                .setContentText("CO concentration is very high: " + concen + " ppm")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent)
                .build();
        Random r = new Random();
        int Id = r.nextInt();
        notificationManager2.notify(Id, notification);
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
