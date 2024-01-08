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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    var isTracking = remember {
        mutableStateOf(false)
    }

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
                    isTracking.value = !isTracking.value
                    toggleTracking()
                },
                modifier = Modifier
                    .size(120.dp)
            ) {
                Text(text = if (!isTracking.value) "Start" else "Stop")
            }
            Text(text = if (isTracking.value) "Tracking Enabled" else "Tracking disabled")

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
    // TODO
}