@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.example.sportsafetytracker.ui

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import com.example.sportsafetytracker.LocalMainViewModel
import com.example.sportsafetytracker.MainViewModel


@Composable
fun SettingsScreen(
    onCancelButtonClicked: () -> Unit = {}
){
    val keyboardController = LocalSoftwareKeyboardController.current
    val viewModel = LocalMainViewModel.current
    val delayTime by viewModel.loadDelayTime().collectAsState(initial = 60)
    val numberValue by viewModel.loadPhoneNumber().collectAsState(initial = "")
    var newNumberValue by remember {mutableStateOf("")}
    var isValidPhoneNumber by remember {
        mutableStateOf(true)
    }
    var inputSuccessText by remember {
        mutableStateOf("")
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
                    viewModel.updateDelayTime(delayTime - 1)
                }) {
                    Text(text = "-")
                }
                Button(onClick = {
                    viewModel.updateDelayTime(delayTime + 1)
                }) {
                    Text(text = "+")
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(40.dp)
            ) {
                Text(text = "Current saved number:")
                Text(text = numberValue)
                TextField(
                    value = newNumberValue,
                    placeholder = {Text(text = numberValue)},
                    onValueChange = {
                        newNumberValue = it
                        isValidPhoneNumber = true
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    label = { Text("Emergency Phone Number") },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = if (isValidPhoneNumber) Color.Unspecified else Color.Red,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = if (isValidPhoneNumber) Color.Transparent else Color.Red,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Button(
                    onClick = {
                        isValidPhoneNumber = viewModel.isValidNumber(newNumberValue)
                        if (isValidPhoneNumber) {
                            viewModel.updatePhoneNumber(newNumberValue)
                            newNumberValue = ""
                            inputSuccessText = "Phone number saved"

                        } else {
                            inputSuccessText = "Invalid number input"
                        }
                        keyboardController?.hide()
                    }
                ) {
                    Text(text = "Save")
                }
                val textColor = if (isValidPhoneNumber) Color.Green else Color.Red
                Text(
                    text = inputSuccessText,
                    color = textColor
                )
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
    return MainViewModel(DummyApplication())
}