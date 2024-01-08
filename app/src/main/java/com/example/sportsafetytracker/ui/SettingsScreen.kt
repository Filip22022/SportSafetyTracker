package com.example.sportsafetytracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Composable
fun SettingsScreen(
    onCancelButtonClicked: () -> Unit = {}
){

    //TOD0 move variables to proper place
    var count = remember {
        mutableStateOf(0)
    }

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
                fontSize = 25.sp
            )
            Text(
                text = count.value.toString(),
                fontSize = 40.sp
            )
            Row() {
                Button(onClick = {
                    count.value--
                }) {
                    Text(text = "-")
                }
                Button(onClick = {
                    count.value++
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
    SettingsScreen()
}