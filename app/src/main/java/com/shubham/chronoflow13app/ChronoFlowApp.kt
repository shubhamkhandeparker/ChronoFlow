package com.shubham.chronoflow13app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChronoFlowApp : Application(){
    override fun onCreate() {
        super.onCreate()
        Log.d("ChronoFlowApp","Application onCreate had been called ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "stopwatch_channel",    //Channel ID
                "Stopwatch",          //Channel Name
                NotificationManager.IMPORTANCE_LOW  //Importance
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}