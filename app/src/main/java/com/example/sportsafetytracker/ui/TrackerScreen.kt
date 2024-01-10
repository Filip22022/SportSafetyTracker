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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sportsafetytracker.LocalMainViewModel
import com.example.sportsafetytracker.MainViewModel


@Composable
fun TrackerScreen(
    onSettingsButtonClicked: () -> Unit = {}
){
    val viewModel = LocalMainViewModel.current
    val accelerometerData by viewModel.accelerometerData.observeAsState(Triple(0f, 0f, 0f))
    var isTracking by remember { mutableStateOf(false) }
    val crashHappened by viewModel.crashHappened.observeAsState(false)

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if(crashHappened) Color.Red else Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {
                    isTracking = !isTracking
                    toggleTracking(isTracking, viewModel)
                },
                modifier = Modifier
                    .size(120.dp)
            ) {
                Text(text = if (!isTracking) "Start" else "Stop")
            }
            Text(text = if (isTracking) "Tracking Enabled" else "Tracking Disabled")
            if (isTracking) {
                Text(text = "X: ${accelerometerData.first}, Y: ${accelerometerData.second}, Z: ${accelerometerData.third}")
            }

        }
        if (!isTracking) {
            Button(
                onClick = onSettingsButtonClicked,
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                Text(text = "Settings")
            }
        }
    }

}

@Preview
@Composable
fun TrackerScreenPreview(){
    CompositionLocalProvider(LocalMainViewModel provides previewViewModel()) {
        TrackerScreen()
    }
}

fun toggleTracking(isTracking : Boolean, viewModel: MainViewModel) {
    if (isTracking) {
        viewModel.startTracking()
    } else {
        viewModel.stopTracking()
        viewModel.crashAvoided()
    }
}