package com.example.sportsafetytracker

import android.Manifest;
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
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

        requestPermissionsIfNeeded()
    }

    private fun requestPermissionsIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), PERMISSIONS_REQUEST_SEND_SMS)
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_FINE_LOCATION)
        } else {
            setupContent()
        }
    }

    private fun setupContent() {
        setContent {
            SportSafetyTrackerTheme {
                CompositionLocalProvider(LocalMainViewModel provides mainViewModel) {
                    SafetyTrackerApp()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if ((requestCode == PERMISSIONS_REQUEST_SEND_SMS || requestCode == PERMISSIONS_REQUEST_FINE_LOCATION) && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestPermissionsIfNeeded()
        } else {
            Toast.makeText(this, "SMS sending and location tracking permissions are required to run the app", Toast.LENGTH_LONG).show()
        }
    }
    companion object {
        private const val PERMISSIONS_REQUEST_SEND_SMS = 1
        private const val PERMISSIONS_REQUEST_FINE_LOCATION = 2
    }

}