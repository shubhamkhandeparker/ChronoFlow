package com.shubham.chronoflow13app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.ui.unit.Constraints
import com.shubham.chronoflow13app.common.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChronoFlowApp : Application(){
    override fun onCreate() {
        super.onCreate()
        Log.d("ChronoFlowApp","Application onCreate had been called ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val stopwatchChannel = NotificationChannel(
                Constants.STOPWATCH_CHANNEL_ID,
                "Stopwatch",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(stopwatchChannel)

            val timerChannel = NotificationChannel(
                Constants.TIMER_CHANNEL_ID,
                "Timer",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(timerChannel)
        }
    }
}