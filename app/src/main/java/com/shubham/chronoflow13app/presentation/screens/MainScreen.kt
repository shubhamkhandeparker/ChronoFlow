package com.shubham.chronoflow13app.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.shubham.chronoflow13app.presentation.navigation.BottomNavItem
import com.shubham.chronoflow13app.ui.theme.DarkGray
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.shubham.chronoflow13app.domain.service.VoiceRecognizerService
import com.shubham.chronoflow13app.domain.service.VoiceRecognizerState
import com.shubham.chronoflow13app.viewmodel.StopwatchViewModel
import com.shubham.chronoflow13app.viewmodel.TimerViewModel
import java.util.Locale
import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.isGranted


@OptIn(ExperimentalPermissionsApi::class) //Required for permissions
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current

    //1.Permission Handling
    val permissionState = rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)

    //2.getting viewModel & voice State
    val timerViewModel: TimerViewModel = hiltViewModel()
    val stopwatchViewModel: StopwatchViewModel = hiltViewModel()
    val voiceState by if(currentRoute == BottomNavItem.Timer.route){
        timerViewModel.voiceState.collectAsState()
    }
    else{
        stopwatchViewModel.voiceState.collectAsState()
    }

    //3.React ti voice commands
    LaunchedEffect(voiceState) {
        when (voiceState) {
            is VoiceRecognizerState.Result -> {
                val command =
                    (voiceState as VoiceRecognizerState.Result).text.lowercase(Locale.ROOT)
                Log.d("VoiceCommand","Received :'$command'")
                when {
                    "start stopwatch" in command -> stopwatchViewModel.start()
                    "pause stopwatch" in command -> stopwatchViewModel.pause()
                    "reset stopwatch" in command -> stopwatchViewModel.reset()
                    "start timer" in command -> timerViewModel.start()
                    "pause timer" in command -> timerViewModel.pause()
                    "reset timer" in command -> timerViewModel.reset()
                }
            }
            // --- THIS IS THE CRUCIAL PART YOU ARE MISSING ---
            is VoiceRecognizerState.Error -> {
                val errorMessage = (voiceState as VoiceRecognizerState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

            else -> {
                // Do nothing for Ready or Listening states
            }

        }
    }


    val items = listOf(
        BottomNavItem.Stopwatch,
        BottomNavItem.Timer,
    )
    Scaffold(
        containerColor = DarkGray,
        bottomBar = {
            NavigationBar(containerColor = DarkGray) {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(text = item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }

                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (permissionState.status.isGranted) {
                        //If we have permission , start listening
                        if(currentRoute== BottomNavItem.Timer.route){
                            timerViewModel.onVoiceCommand()
                        }
                        else{
                            stopwatchViewModel.onVoiceCommand()
                        }
                    } else {
                        permissionState.launchPermissionRequest()
                    }
                }
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Voice command")
            }
        }
    ) { innerPadding ->

        NavHost(
            navController, startDestination = BottomNavItem.Stopwatch.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Stopwatch.route) { StopwatchScreen(stopwatchViewModel) }
            composable(BottomNavItem.Timer.route) { TimerScreen(timerViewModel) }
        }
    }
}