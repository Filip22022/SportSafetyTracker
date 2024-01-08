package com.example.sportsafetytracker.ui

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportsafetytracker.MainViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.SavedStateHandle
import com.example.sportsafetytracker.LocalMainViewModel


@Composable
fun SettingsScreen(
    onCancelButtonClicked: () -> Unit = {}
){
    val viewModel = LocalMainViewModel.current
    val delayTime by viewModel.delayTime.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Delay time",
                fontSize = 25.sp,
                color = Color.Black
            )
            Text(
                text = delayTime.toString(),
                fontSize = 40.sp,
                color = Color.Black
            )
            Row {
                Button(onClick = {
                    viewModel.updateDelayTime((delayTime?:0) - 1)
                }) {
                    Text(text = "-")
                }
                Button(onClick = {
                    viewModel.updateDelayTime((delayTime?:0) + 1)
                }) {
                    Text(text = "+")
                }
            }
        }
        Button(
            onClick = onCancelButtonClicked,
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            Text(text = "Cancel")
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview(){
    CompositionLocalProvider(LocalMainViewModel provides previewViewModel()) {
        SettingsScreen()
    }
}

class DummyApplication : Application() {
    // Implement any necessary methods or leave it empty if not used in previews
}
fun previewViewModel(): MainViewModel {
    return MainViewModel(DummyApplication(), SavedStateHandle())
}