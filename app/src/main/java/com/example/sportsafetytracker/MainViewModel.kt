package com.example.sportsafetytracker

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import android.Manifest
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _accelerometerData = MutableLiveData<Triple<Float, Float, Float>>()
    val accelerometerData: LiveData<Triple<Float, Float, Float>> = _accelerometerData

    private val _isTracking = MutableLiveData<Boolean>()
    val isTracking: LiveData<Boolean> = _isTracking

    private val _crashHappened = MutableLiveData<Boolean>()
    val crashHappened: LiveData<Boolean> = _crashHappened

    private val _messageSent = MutableLiveData<Boolean>()
    val messageSent: LiveData<Boolean> = _messageSent

    fun startIsTracking() {
        _isTracking.postValue(true)
        startTracking()
    }
    fun stopIsTracking() {
        _isTracking.postValue(false)
        stopTracking()
        crashAvoided()
    }

    fun messageSent() {
        _messageSent.postValue(true)
        stopIsTracking()
    }
    fun messageCanceled() {
        _messageSent.postValue(false)
    }

    private val sensorDataManager = SensorActivity(this, application) { data ->
        _accelerometerData.postValue(data)
    }

    init {
        val crashDetectionListener = object : SensorActivity.CrashDetectionListener {
            override fun onCrashDetected() {
                _crashHappened.postValue(true)
            }
            override fun onCrashAvoided() {
                _crashHappened.postValue(false)
                stopCountdownTimer()
                stopAlarmSound()
            }
        }

        sensorDataManager.setCrashDetectionListener(crashDetectionListener)
    }

    private val settingsManager = Settings(application)

    fun crashDetected() {
        _crashHappened.postValue(true)
    }
    fun crashAvoided() {
        _crashHappened.postValue(false)
        stopCountdownTimer()
        stopAlarmSound()
    }

    fun startTracking() {
        sensorDataManager.startTracking()
    }

    fun stopTracking() {
        sensorDataManager.stopTracking()
    }

    override fun onCleared() {
        super.onCleared()
        sensorDataManager.stopTracking()
        countdownTimer?.cancel()
    }

    fun loadDelayTime(): Flow<Int> {
        return settingsManager.loadDelayTime()
    }

    fun getDelayTime(): Int {
        val delayTime: Int
        runBlocking(Dispatchers.IO) {
            delayTime = loadDelayTime().first()
        }
        return delayTime
    }

    fun loadPhoneNumber(): Flow<String> {
        return settingsManager.loadPhoneNumber()
    }

    fun getPhoneNumber(): String {
        val phoneNumber: String
        runBlocking(Dispatchers.IO) {
            phoneNumber = loadPhoneNumber().first()
        }
        return phoneNumber
    }

    fun loadCustomMessage(): Flow<String> {
        return settingsManager.loadCustomMessage()
    }

    fun getCustomMessage(): String {
        val customMessage: String
        runBlocking(Dispatchers.IO) {
            customMessage = loadCustomMessage().first()
        }
        return customMessage
    }

     fun updateDelayTime(delayTime: Int) {
        runBlocking {
            launch {
                settingsManager.saveDelayTime(delayTime)
            }
        }
    }

    fun updatePhoneNumber(number: String) {
        runBlocking {
            launch {
                settingsManager.savePhoneNumber(number)
            }
        }
    }

    fun updateCustomMessage(message: String) {
        runBlocking {
            launch {
                settingsManager.saveCustomMessage(message)
            }
        }
    }

    fun isValidNumber(numberValue: String): Boolean {
        return settingsManager.isValidNumber(numberValue)
    }

    private val _timerValue = MutableLiveData<Long>()
    val timerValue: LiveData<Long> = _timerValue

    private var countdownTimer: CountDownTimer? = null

    fun startCountdownTimer(durationInMillis: Long) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timerValue.postValue(millisUntilFinished / 1000)

                if (millisUntilFinished / 1000 <= 15.0) {
                    playAlarmSound()
                }
            }

            override fun onFinish() {
                _timerValue.postValue(0)
                stopAlarmSound()
                fetchLocationAndSendSMS()
            }
        }.start()
    }

    fun stopCountdownTimer() {
        countdownTimer?.cancel()
    }

    private fun fetchLocationAndSendSMS() {
        try {
            val locationManager = getApplication<Application>().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            var locationListener: LocationListener? = null
            locationListener = LocationListener { location ->
                val locationMessage = R.string.location_message_prefix.toString() + toDMS(location.latitude, location.longitude)
                sendSMS(locationMessage)

                locationListener?.let { listener ->
                    locationManager.removeUpdates(listener)
                }
            }

            if (ContextCompat.checkSelfPermission(getApplication<Application>().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, locationListener)

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 0f, locationListener)
            } else {
                Toast.makeText(getApplication<Application>().applicationContext, "Location permission not granted", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("LocationDebug", "Failed to fetch location: ${e.message}")
        }
    }

    private fun sendSMS(locationMessage: String) {
        val phoneNumber = getPhoneNumber()
        val customMessage = getCustomMessage()
        var message = R.string.default_message.toString()

        val smsManager: SmsManager = SmsManager.getDefault()
        if (customMessage != "") {
            message = customMessage + locationMessage
        } else {
            message = R.string.default_message.toString() + locationMessage
        }
        //smsManager.sendTextMessage(phoneNumber, null, message, null, null)

        Toast.makeText(getApplication<Application>().applicationContext, "$phoneNumber: $message", Toast.LENGTH_LONG).show()

        Log.d("SMS", "Message sent successfully")

        messageSent()
    }

    private fun toDMS(latitude: Double, longitude: Double): String {
        val latDegree = Math.abs(latitude).toInt()
        val latMinute = ((Math.abs(latitude) - latDegree) * 60).toInt()
        val latSecond = ((Math.abs(latitude) - latDegree - latMinute / 60.0) * 3600)

        val lonDegree = Math.abs(longitude).toInt()
        val lonMinute = ((Math.abs(longitude) - lonDegree) * 60).toInt()
        val lonSecond = ((Math.abs(longitude) - lonDegree - lonMinute / 60.0) * 3600)

        val latDirection = if (latitude >= 0) "N" else "S"
        val lonDirection = if (longitude >= 0) "E" else "W"

        return "${latDegree}°${latMinute}'${String.format(Locale.US, "%.2f", latSecond)}\"$latDirection ${lonDegree}°${lonMinute}'${String.format(
            Locale.US, "%.2f", lonSecond)}\"$lonDirection"
    }

    private var mediaPlayer: MediaPlayer? = null
    fun playAlarmSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplication(), R.raw.severe_warning_alarm)
            mediaPlayer?.setVolume(1.0f, 1.0f)
        }
        mediaPlayer?.start()
    }

    fun stopAlarmSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}