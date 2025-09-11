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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
        }
    ) { innerPadding ->

        NavHost(navController, startDestination = BottomNavItem.Stopwatch.route,
            modifier= Modifier.padding(innerPadding)){
            composable(BottomNavItem.Stopwatch.route){StopwatchScreen()}
            composable(BottomNavItem.Timer.route){TimerScreen()}
        }
    }
}