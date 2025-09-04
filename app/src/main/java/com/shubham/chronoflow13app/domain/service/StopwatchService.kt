package com.shubham.chronoflow13app.domain.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log

class StopwatchService : Service() {
    override fun onBind(intent: Intent?): IBinder?{
        return null
    }
    override fun onStartCommand(intent: Intent?,flags:Int,startId:Int): Int{
        Log.d("StopwatchServicer","Service Starting...")
        return START_STICKY

    }
}