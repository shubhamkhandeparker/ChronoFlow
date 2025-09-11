package com.shubham.chronoflow13app.domain.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


//This sealed interface represent all the possible state of out voice recognizer


sealed interface VoiceRecognizerState {
    object Ready : VoiceRecognizerState
    object Listening : VoiceRecognizerState
    data class Result(val text: String) : VoiceRecognizerState
    data class Error(val message: String) : VoiceRecognizerState

}

class VoiceRecognizerService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    //Private mutable state that only this class can change
    private val _state = MutableStateFlow<VoiceRecognizerState>(VoiceRecognizerState.Ready)

    //Public immutable state for the UI to observe
    val state: StateFlow<VoiceRecognizerState> = _state.asStateFlow()

    private val recognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    //This is the listener that5 translates callback to our stateFlow
    private val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            _state.value = VoiceRecognizerState.Error("Ready for Speech...")
        }


        override fun onBeginningOfSpeech() {
            _state.value = VoiceRecognizerState.Listening
        }


        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onEndOfSpeech() {
            _state.value = VoiceRecognizerState.Ready     //User finished speaking
        }


        override fun onError(error: Int) {
            val errorMessage = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "Audio Recording error"
                SpeechRecognizer.ERROR_SERVER -> "Client side error"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient Permission"
                SpeechRecognizer.ERROR_NETWORK -> "Network error"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network Timeout"
                SpeechRecognizer.ERROR_NO_MATCH -> "No Speech Match"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer is busy"
                SpeechRecognizer.ERROR_SERVER -> "Error From Server"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "NO Speech input"
                else -> "Unknown error"

            }
            _state.value = VoiceRecognizerState.Error(errorMessage)
        }

        override fun onResults(results: Bundle?) {
            results
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.getOrNull(0)  //Get the first & most likely result
                ?.let { text ->
                    _state.value = VoiceRecognizerState.Result(text)
                }
            _state.value = VoiceRecognizerState.Ready    //Reset after getting result
        }

        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}

    }

    init {
        recognizer.setRecognitionListener(recognitionListener)
    }

    fun startListening() {
        _state.value = VoiceRecognizerState.Ready //Reset state before starting

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US") //set Language to English
        }
        recognizer.startListening(intent)
    }

    fun stopListening() {
        recognizer.stopListening()
    }


}
