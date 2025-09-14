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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
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
    val voiceState by if (currentRoute == BottomNavItem.Timer.route) {
        timerViewModel.voiceState.collectAsState()
    } else {
        stopwatchViewModel.voiceState.collectAsState()
    }

    fun parseVoiceCommand(
        command: String
    ) {
        val lowercaseCommand = command.lowercase(Locale.ROOT)

        //Defining the keywords for action
        val actionStart = listOf(
            "start",
            "begin",
            "go",
            "resume",
            "continue",
            "play",
            "commence",
            "launch",
            "activate",
            "initiate",
            "run",
            "kickoff",
            "proceed",
            "engage",
            "trigger",
            "set"
        )
        val actionStop = listOf(
            "stop",
            "pause",
            "hold",
            "wait",
            "freeze",
            "halt",
            "end",
            "break",
            "suspend",
            "interrupt",
            "terminate",
            "standby",
            "cease",
            "cut",
            "abort"
        )
        val actionReset = listOf(
            "reset",
            "clear",
            "restart",
            "refresh",
            "reboot",
            "redo",
            "wipe",
            "restore",
            "initialize",
            "zero",
            "back",
            "erase",
            "start over",
            "again"
        )

        //check for target a7 then the action
        if (lowercaseCommand.contains("timer")) {
            //Regex to find pattern like "10 minutes" or "5 seconds",etc
            var totalMillis = 0L
            val regex = "(\\d+)\\s*(hour|minute|second)".toRegex()
            val matches = regex.findAll(lowercaseCommand)


            if (matches.any()) {
                matches.forEach { matchResult ->
                    val (value, unit) = matchResult.destructured
                    val timeValue = value.toLongOrNull() ?: 0L
                    when {
                        "hour".contains(unit) -> totalMillis += timeValue * 3_600_000 //1 hour = 3,600,000
                        "minute".contains(unit) -> totalMillis += timeValue * 60000
                        "second".contains(unit) -> totalMillis += timeValue * 1000
                    }
                }
                if (totalMillis > 0) {
                    timerViewModel.setDurationAndSave(totalMillis)
                }
            }

            when {
                actionStart.any { lowercaseCommand.contains(it) } && totalMillis > 0 -> timerViewModel.start()
                actionStart.any { lowercaseCommand.contains(it) } -> timerViewModel.start()
                actionStop.any { lowercaseCommand.contains(it) } -> timerViewModel.pause()
                actionReset.any { lowercaseCommand.contains(it) } -> timerViewModel.reset()
            }
        } else if (lowercaseCommand.contains("stopwatch")) {
            when {
                actionStart.any { lowercaseCommand.contains(it) } -> stopwatchViewModel.start()
                actionStop.any { lowercaseCommand.contains(it) } -> stopwatchViewModel.pause()
                actionReset.any { lowercaseCommand.contains(it) } -> stopwatchViewModel.reset()
            }
        }
    }

    //3.React ti voice commands
    LaunchedEffect(voiceState) {
        when (voiceState) {
            is VoiceRecognizerState.Result -> {
                val command =
                    (voiceState as VoiceRecognizerState.Result).text
                Log.d("VoiceCommand", "Received :'$command'")
                //simple logic
                parseVoiceCommand(command)
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
        floatingActionButtonPosition = FabPosition.Center,

        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = {
                    if (permissionState.status.isGranted) {
                        if (currentRoute == BottomNavItem.Timer.route) {
                            timerViewModel.onVoiceCommand()
                        } else {
                            stopwatchViewModel.onVoiceCommand()
                        }

                    } else {
                        permissionState.launchPermissionRequest()
                    }
                }
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Voice Command")
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = DarkGray

            ) {
                NavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.Transparent
                ) {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text (text= item.title) },
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