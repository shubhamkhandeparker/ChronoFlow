package com.shubham.chronoflow13app.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StopwatchScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //This Box will take up all the available space pushing the button down
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "00:00.00",
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        //This Row hold actions button at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {/*TODO*/ }) { Text("Reset") }
            Button(onClick = {/*TODO*/ }) { Text("Start") }
        }
    }
}