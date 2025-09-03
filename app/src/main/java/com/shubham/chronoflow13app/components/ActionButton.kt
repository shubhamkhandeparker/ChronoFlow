package com.shubham.chronoflow13app.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shubham.chronoflow13app.ui.theme.DarkGray
import com.shubham.chronoflow13app.ui.theme.Orange

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Orange
) {
    Button(
        onClick=onClick,
        modifier=modifier.size(100.dp),      //Makes the button larger & circular
        shape = CircleShape,
        colors= ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ){
        Text(text=text.uppercase())
    }
}