package com.shubham.chronoflow13app.domain.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.shubham.chronoflow13app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.shubham.chronoflow13app.common.Constants.ACTION_PAUSE
import com.shubham.chronoflow13app.common.Constants.ACTION_RESET
import com.shubham.chronoflow13app.common.Constants.ACTION_START
import com.shubham.chronoflow13app.common.Constants.STOPWATCH_CHANNEL_ID
import com.shubham.chronoflow13app.common.Constants.ACTION_LAP

class StopwatchService : Service() {
    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var timerJob: Job? = null


    companion object{
        private val _timeMillis = MutableStateFlow(0L)
        val timeMillis = _timeMillis.asStateFlow()

        private val _isRunning = MutableStateFlow(false)
        val isRunning = _isRunning.asStateFlow()

        private val _laps = MutableStateFlow<List<String>>(emptyList())
        val laps = _laps.asStateFlow()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

            when(intent?.action){
                ACTION_START -> start()
                ACTION_PAUSE -> pause()
                ACTION_RESET -> reset()
                ACTION_LAP -> lap()
            }
            return START_STICKY
        }


    private fun start(){
        if(_isRunning.value)return

        _isRunning.value=true

        val notification = buildNotification(formatTime(_timeMillis.value))
        startForeground(1,notification)

        timerJob = serviceScope.launch {
            val startTime = System.currentTimeMillis() - _timeMillis.value
            while (true){
                val currentTime = System.currentTimeMillis() - startTime
                _timeMillis.value = currentTime
                val formattedTime = formatTime(currentTime)
                notificationManager.notify(1,buildNotification(formattedTime))  //update notification
                delay(10L)
            }
        }
    }

    private fun pause (){
        _isRunning.value=false
        timerJob?.cancel()
    }

    private fun reset(){
        pause()     //Cancel the timer job
        stopSelf() //stop the service
        _timeMillis.value = 0L
        _laps.value=emptyList()
    }

    private fun lap(){
        if(!_isRunning.value) return

        val currentLapTime = formatTime(_timeMillis.value)
        val lapNUmber =(_laps.value.size + 1) . toString().padStart(2,'0')
        _laps.value = listOf("Lap $lapNUmber:$currentLapTime") + _laps.value
    }
    private fun buildNotification(text : String): Notification{
        return NotificationCompat.Builder(this,STOPWATCH_CHANNEL_ID)
            .setContentTitle("Stopwatch")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)   //this makes it non swipeable
            .build()
    }

    private  fun formatTime(millis:Long):String{
        val totalSeconds = millis/1000
        val minutes= (totalSeconds/60).toString().padStart(2,'0')
        val seconds= (totalSeconds%60).toString().padStart(2,'0')
        val centiSeconds = (millis % 1000/10).toString().padStart(2,'0')
        return "$minutes:$seconds.$centiSeconds"
    }
    override fun onDestroy(){
        super.onDestroy()
        serviceScope.cancel()
    }

    }