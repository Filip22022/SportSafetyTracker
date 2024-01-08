package com.example.sportsafetytracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun TrackerScreen(
    onSettingsButtonClicked: () -> Unit = {}
){
    //TOD0 move variables to proper place
    var isTracking = false

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {
                    isTracking = !isTracking
                    toggleTracking()
                },
                modifier = Modifier
                    .size(120.dp)
            ) {
                Text(text = if (!isTracking) "Start" else "Stop")
            }
            Text(text = if (isTracking) "Tracking Enabled" else "Tracking disabled")

        }
        Button(
            onClick = onSettingsButtonClicked,
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            Text(text = "Settings")
        }
    }

}

@Preview
@Composable
fun TrackerScreenPreview(){
    TrackerScreen()
}

fun toggleTracking() {
    TODO()
}