package com.example.sportsafetytracker

import android.Manifest;
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        //zgoda na smsy
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SEND_SMS),
                0
            )
        }
    }
}
