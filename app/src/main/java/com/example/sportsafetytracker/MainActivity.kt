package com.example.sportsafetytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.example.sportsafetytracker.ui.theme.SportSafetyTrackerTheme

val LocalMainViewModel = compositionLocalOf<MainViewModel> {
    error("No ViewModel provided")
}
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SportSafetyTrackerTheme {
                // Provide the ViewModel to the composable tree
                CompositionLocalProvider(LocalMainViewModel provides mainViewModel) {
                    SafetyTrackerApp()
                }
            }
        }
    }
}
