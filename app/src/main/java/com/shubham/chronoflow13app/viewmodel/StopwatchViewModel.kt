package com.shubham.chronoflow13app.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.chronoflow13app.domain.service.StopwatchService
import com.shubham.chronoflow13app.presentation.screens.StopwatchScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.shubham.chronoflow13app.common.Constants.ACTION_PAUSE
import com.shubham.chronoflow13app.common.Constants.ACTION_RESET
import com.shubham.chronoflow13app.common.Constants.ACTION_START
import com.shubham.chronoflow13app.common.Constants.ACTION_LAP


@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    //Exposing the state directly from the service's companion object
    val isRunning = StopwatchService.isRunning
    val timeMillis = StopwatchService.timeMillis
    val laps = StopwatchService.laps

    //The formattedTime logic can now line here again based on the service's timeMillis
    val formattedTime = timeMillis.map { millis ->
        val totalSeconds = millis / 1000
        val minutes = (totalSeconds / 60).toString().padStart(2, '0')
        val seconds = (totalSeconds % 60).toString().padStart(2, '0')
        val centiSeconds = (millis % 1000 / 10).toString().padStart(2, '0')
        "$minutes:$seconds.$centiSeconds"

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "00:00.00")

    fun start() {
        val intent = Intent(application, StopwatchService::class.java).also {
            it.action = ACTION_START
            application.startService(it)

        }
    }


    fun pause() {
        Intent(application, StopwatchService::class.java).also {
            it.action = ACTION_PAUSE
            application.startService(it)
        }

    }


    fun reset() {
        Intent(application, StopwatchService::class.java).also {
            it.action = ACTION_RESET
            application.startService(it)
        }
    }

    fun lap() {
       Intent(application, StopwatchService::class.java).also {
           it.action = ACTION_LAP
           application.startService(it)
       }
    }

}
