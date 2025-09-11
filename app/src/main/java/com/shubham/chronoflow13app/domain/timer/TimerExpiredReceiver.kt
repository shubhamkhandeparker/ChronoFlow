package com.shubham.chronoflow13app.domain.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.shubham.chronoflow13app.MainActivity
import com.shubham.chronoflow13app.R
import com.shubham.chronoflow13app.common.Constants.TIMER_CHANNEL_ID
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.app.PendingIntent

class TimerExpiredReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context,intent: Intent){
        Log.d("TimerExpiredReceiver","Alarm Received")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Creating an intent that will open the app when the notification is tapped
        val activityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context,TIMER_CHANNEL_ID)
            .setContentTitle("Timer Finished!")
            .setContentText("Time's UP.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent) //set for tappable intent
            .setAutoCancel(true)    //Dismiss the notification when tapped
            .build()

        notificationManager.notify(2,notification)      //use a different ID than the service

    }
}