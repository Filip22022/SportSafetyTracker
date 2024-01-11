package com.example.sportsafetytracker
import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val _accelerometerData = MutableLiveData<Triple<Float, Float, Float>>()
    val accelerometerData: LiveData<Triple<Float, Float, Float>> = _accelerometerData

    private val _crashHappened = MutableLiveData<Boolean>()
    val crashHappened: LiveData<Boolean> = _crashHappened

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

    fun loadPhoneNumber(): Flow<String> {
        return settingsManager.loadPhoneNumber()
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
            }

            override fun onFinish() {
                _timerValue.postValue(0)
            }
        }.start()
    }

    fun stopCountdownTimer() {
        countdownTimer?.cancel()
    }
}