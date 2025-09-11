package com.shubham.chronoflow13app.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shubham.chronoflow13app.components.ActionButton
import com.shubham.chronoflow13app.domain.timer.TimerAlarmScheduler
import com.shubham.chronoflow13app.ui.theme.ButtonGray
import com.shubham.chronoflow13app.viewmodel.TimerViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.text.input.KeyboardType




@Composable
fun TimerScreen (
    viewModel: TimerViewModel = hiltViewModel()
) {
    val formattedTime by viewModel.formattedTime.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val isPickerDialogShown by viewModel.isPickerDialogShown.collectAsState()

    if (isPickerDialogShown) {

        AlertDialog(
            onDismissRequest = { viewModel.hideTimePickerDialog() },
            title = { Text("Set Timer") },
            text = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = viewModel.hoursInput.collectAsState().value,
                        onValueChange = { viewModel.hoursInput.value = it },
                        label = { Text("HH") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = viewModel.minutesInput.collectAsState().value,
                        onValueChange = { viewModel.minutesInput.value = it },
                        label = { Text("MM") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = viewModel.secondsInput.collectAsState().value,
                        onValueChange = { viewModel.secondsInput.value = it },
                        label = { Text("SS") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            },
            confirmButton = { TextButton(onClick = { viewModel.setTimerDuration() }) { Text("OK") } },
            dismissButton = { TextButton(onClick = { viewModel.hideTimePickerDialog() }) { Text("Cancel") } }

        )
    }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            //This Spacer pushed the dial from top
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
                    .aspectRatio(1f)   //This makes the box square
                    .border(width = 5.dp, color = ButtonGray, shape = CircleShape)
                    .clickable{viewModel.showTimePickerDialog()},
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = formattedTime,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            //Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    text = "Reset",
                    onClick = { viewModel.reset() },
                    backgroundColor = ButtonGray
                )
                ActionButton(
                    text = if (isRunning) "Pause" else "Start",
                    onClick = { viewModel.start() }
                )

            }
        }
    }
