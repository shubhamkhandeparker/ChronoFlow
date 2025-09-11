package com.shubham.chronoflow13app.domain.service

//This sealed interface represent all the possible state of out voice recognizer


sealed interface VoiceRecognizerState {
    object Ready : VoiceRecognizerState
    object Listening : VoiceRecognizerState
    data class Result (val text : String) : VoiceRecognizerState
    data class Error(val message:String) : VoiceRecognizerState

}