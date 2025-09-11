package com.shubham.chronoflow13app.presentation.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem (val route:String,val icon: ImageVector,val title:String){
object Stopwatch : BottomNavItem("stopwatch", Icons.Default.Timer,"Stopwatch")
    object Timer : BottomNavItem("timer",Icons.Outlined.Timer,"Timer")
}