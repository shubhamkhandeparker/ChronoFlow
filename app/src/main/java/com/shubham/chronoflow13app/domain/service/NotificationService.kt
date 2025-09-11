package com.shubham.chronoflow13app.domain.service

import android.content.Context
import android.media.Ringtone
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager



class NotificationService @Inject constructor(
    @ApplicationContext private val context: Context

) {

    //keeping a reference to the Ringtone to stop it later
    private var ringtone : Ringtone? = null
    fun playSound(){

        //1.Getting the URI of the default alarm sound
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        //2.If the URI is null ( no alarm sound set ) , try the notification sound.
        if(alarmUri==null){
            //Fallback to the default notification sound  if no alarm is set
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        //3.Get Ringtone object from the context & the URI
        ringtone = RingtoneManager.getRingtone(context,alarmUri)
        ringtone?.play()

        //4.Play the sound
        ringtone?.play()

    }

    fun vibrate(){

        //Get the system's vibrator service
        val vibrator = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        }else{
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        //Create a Vibration pattern : wait 0ms , vibrate 500ms, pause  1000ms
        val vibrationPattern = longArrayOf(0,500,1000)

        //Create the effect with pattern , repeating from the start (index 0)
        val vibrationEffect = VibrationEffect.createWaveform(vibrationPattern,0)

        //vibrate with the created effect
        vibrator.vibrate(vibrationEffect)
    }

    fun stop(){
        //Stop the sound if it's playing
        ringtone?.stop()

        //stop the vibration
        val vibrator = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        }
        else{
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        vibrator.cancel()
    }
}