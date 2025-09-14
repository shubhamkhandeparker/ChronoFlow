package com.shubham.chronoflow13app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log
import com.shubham.chronoflow13app.domain.timer.TimerAlarmScheduler
import kotlin.math.min
import com.shubham.chronoflow13app.domain.service.NotificationService
import com.shubham.chronoflow13app.data.SettingsRepository
import com.shubham.chronoflow13app.domain.service.VoiceRecognizerService
import com.shubham.chronoflow13app.domain.service.VoiceRecognizerState


@HiltViewModel

class TimerViewModel @Inject constructor(
    private val scheduler: TimerAlarmScheduler,
    private val notificationService: NotificationService,
    private val settingsRepository: SettingsRepository,
    private val voiceRecognizerService: VoiceRecognizerService
) : ViewModel() {

    //Exposing the voice state & adding control function
    val voiceState: StateFlow<VoiceRecognizerState> = voiceRecognizerService.state
    fun onVoiceCommand() {
        voiceRecognizerService.startListening()
    }

    private val _isPickerDialogShown = MutableStateFlow(false)
    val isPickerDialogShown = _isPickerDialogShown.asStateFlow()

    val hoursInput = MutableStateFlow("00")
    val minutesInput = MutableStateFlow("00")
    val secondsInput = MutableStateFlow("00")

    private val _timeDuration = MutableStateFlow(0L)   //Start with 0 Seconds
    val timeDuration = _timeDuration.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private var timerJob: Job? = null

    //Add INIT to block & load data on startup
    init {
        viewModelScope.launch {
            //This will collect the flow from DataStore & update our timer's duration
            //with the last saved value as soon as the viewModel is created .
            settingsRepository.getTimerDuration.collect { savedDuration ->
                _timeDuration.value = savedDuration
            }
        }
    }

    //Formating the Long into a String "00:00:00"
    val formattedTime = timeDuration.map { millis ->
        val totalSeconds = millis / 1000
        val hours = (totalSeconds / 3600).toString().padStart(2, '0')
        val minutes = ((totalSeconds % 3600) / 60).toString().padStart(2, '0')
        val seconds = (totalSeconds % 60).toString().padStart(2, '0')
        "$hours:$minutes:$seconds"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "00:00:00")

    fun showTimePickerDialog() {
        _isPickerDialogShown.value = true
    }

    fun hideTimePickerDialog() {
        _isPickerDialogShown.value = false
    }

    fun setTimerDuration() {
        val hours = hoursInput.value.toLongOrNull() ?: 0
        val minutes = minutesInput.value.toLongOrNull() ?: 0
        val seconds = secondsInput.value.toLongOrNull() ?: 0
        val totalMillis = (hours * 3600 + minutes * 60 + seconds) * 1000
        _timeDuration.value = totalMillis
        hideTimePickerDialog()

        //Adding this logic to save the new duration
        viewModelScope.launch {
            settingsRepository.saveTimerDuration(totalMillis)
        }

    }

    fun start() {
        Log.d("TimerVM", "Start function called isRunning=${_isRunning.value}")
        if (_isRunning.value) {
            pause()
            return
        }
        if (_timeDuration.value <= 0L) return

        val alarmTime = System.currentTimeMillis() + _timeDuration.value
        scheduler.schedule(alarmTime)
        timerJob = viewModelScope.launch {
            Log.d("TimerVM", "Coroutine launched.")
            _isRunning.value = true
            while (_timeDuration.value > 0) {
                delay(1000L)
                _timeDuration.value -= 1000L
                Log.d("TimerVm", "Trick:time left =${_timeDuration.value}")
            }
            _isRunning.value = false
            Log.d("TimerVM", "Timer finished.")

            notificationService.playSound()
            notificationService.vibrate()

        }
    }


    fun pause() {
        _isRunning.value = false
        timerJob?.cancel()
        scheduler.cancel()
    }


    fun reset() {
        pause()
        notificationService.stop()
        viewModelScope.launch {

            _timeDuration.value = settingsRepository.getTimerDuration.first()

        }

    }


}