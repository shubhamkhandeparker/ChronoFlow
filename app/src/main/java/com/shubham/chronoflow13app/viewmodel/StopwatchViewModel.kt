package com.shubham.chronoflow13app.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.chronoflow13app.domain.service.StopwatchService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopwatchViewModel @Inject constructor(private val application: Application) : ViewModel() {


    private val _timeMillis = MutableStateFlow(0L)
    val timeMillis = _timeMillis.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private val _laps = MutableStateFlow<List<String>>(emptyList())
    val laps = _laps.asStateFlow()

    val formattedTime = timeMillis.map { millis ->
        val totalSeconds = millis / 1000
        val minutes = (totalSeconds / 60).toString().padStart(2, '0')
        val seconds = (totalSeconds % 60).toString().padStart(2, '0')
        val centiSeconds = (millis % 1000 / 10).toString().padStart(2, '0')
        "$minutes:$seconds.$centiSeconds"
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = "00:00.00"
    )


    private var timerJob: Job? = null

    fun start() {
        if (_isRunning.value) {  //if it's running treat it as pause
            pause()
            return
        }

        val intent = Intent(application, StopwatchService::class.java)
        application.startService(intent)

        _isRunning.value = true
        timerJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis() - _timeMillis.value
            while (true) {
                _timeMillis.value = System.currentTimeMillis() - startTime
                delay(10L)  //Update every 10 milliSeconds
            }
        }
    }


    fun pause() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun lap() {
        if (!_isRunning.value) return      //can't lap if the timer isn't running
        val currentLapTime = formattedTime.value
        val lapNumber = (_laps.value.size + 1).toString().padStart(2, '0')
        _laps.value = listOf("Lap $lapNumber:$currentLapTime")  + _laps.value       //Adding new lap to the list
    }

    fun reset() {
        _isRunning.value = false
        timerJob?.cancel()
        _timeMillis.value = 0L
        _laps.value=emptyList()
    }


}
