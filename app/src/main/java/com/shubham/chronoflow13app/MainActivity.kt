package com.shubham.chronoflow13app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shubham.chronoflow13app.presentation.screens.MainScreen
import com.shubham.chronoflow13app.presentation.screens.StopwatchScreen
import com.shubham.chronoflow13app.ui.theme.ChronoFlow13AppTheme
import com.shubham.chronoflow13app.ui.theme.DarkGray
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChronoFlow13AppTheme {
                Surface (modifier=Modifier.fillMaxSize(),
                    color = DarkGray
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChronoFlow13AppTheme {
        Greeting("Android")
    }
}