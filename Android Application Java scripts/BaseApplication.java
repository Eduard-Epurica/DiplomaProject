package com.example.ver1;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class BaseApplication extends Application {

    public static final String Channel_1 = "doorChannel";
    public static final String Channel_2 = "moveChannel";
    public static final String Channel_3 = "coChannel";
    public static final String Channel_4 = "fireChannel";
    public static final String Channel_5 = "lpgChannel";
    public static final String Channel_6 = "co2Channel";
    public static final String Channel_7 = "soundChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();

    }

    private void createNotificationChannels(){
        //Checking if the build version is newer or the same as Android 8, as notification channels are needed only after Android 7
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel doorChannel = new NotificationChannel(
                    //Creating a notification channel for the door sensor, setting up the ID, name and Importance
                    Channel_1,
                    "Door Alert",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            //Setting up various parameters such as phone vibration and badge visibility
            doorChannel.enableVibration(true);
            doorChannel.setDescription("Door has been opened");
            doorChannel.canShowBadge();


            NotificationChannel moveChannel = new NotificationChannel(
                    //Creating a notification channel for the movement sensor, setting up the ID, name and Importance
                    Channel_2,
                    "Movement Alert",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            //Setting up various parameters such as phone vibration and badge visibility
            moveChannel.enableVibration(true);
            moveChannel.setDescription("Movement has been detected");
            moveChannel.canShowBadge();

            NotificationChannel coChannel = new NotificationChannel(
                    //Creating a notification channel for the co sensor, setting up the ID, name and Importance
                    Channel_3,
                    "Carbon Monoxide Alert",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            //Setting up various parameters such as phone vibration and badge visibility
            moveChannel.enableVibration(true);
            moveChannel.setDescription("High Carbon Monoxide Concentration detected");
            moveChannel.canShowBadge();

            NotificationChannel fireChannel = new NotificationChannel(
                    //Creating a notification channel for the fire sensor, setting up the ID, name and Importance
                    Channel_4,
                    "Fire Alert",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            //Setting up various parameters such as phone vibration and badge visibility
            moveChannel.enableVibration(true);
            moveChannel.setDescription("Fire detected");
            moveChannel.canShowBadge();

            NotificationChannel lpgChannel = new NotificationChannel(
                    //Creating a notification channel for the lpg sensor, setting up the ID, name and Importance
                    Channel_5,
                    "LPG Alert",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            //Setting up various parameters such as phone vibration and badge visibility
            moveChannel.enableVibration(true);
            moveChannel.setDescription("LPG detected");
            moveChannel.canShowBadge();

            NotificationChannel co2Channel = new NotificationChannel(
                    //Creating a notification channel for the co2 sensor, setting up the ID, name and Importance
                    Channel_6,
                    "CO2 Alert",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            //Setting up various parameters such as phone vibration and badge visibility
            moveChannel.enableVibration(true);
            moveChannel.setDescription("Co2 detected");
            moveChannel.canShowBadge();

            NotificationChannel soundChannel = new NotificationChannel(
                    //Creating a notification channel for the sound sensor, setting up the ID, name and Importance
                    Channel_7,
                    "Sound Alert",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            //Setting up various parameters such as phone vibration and badge visibility
            moveChannel.enableVibration(true);
            moveChannel.setDescription("Sound detected");
            moveChannel.canShowBadge();

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(doorChannel);
            manager.createNotificationChannel(moveChannel);
            manager.createNotificationChannel(coChannel);
            manager.createNotificationChannel(fireChannel);
            manager.createNotificationChannel(lpgChannel);
            manager.createNotificationChannel(co2Channel);
            manager.createNotificationChannel(soundChannel);


        }
    }
}
