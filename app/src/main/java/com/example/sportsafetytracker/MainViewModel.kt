package com.example.sportsafetytracker
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(application: Application, private val savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {

    private val _accelerometerData = MutableLiveData<Triple<Float, Float, Float>>()
    val accelerometerData: LiveData<Triple<Float, Float, Float>> = _accelerometerData

    private val sensorDataManager = SensorActivity(application) { data ->
        _accelerometerData.postValue(data)
    }

    private val settingsManager = Settings(application)

    fun updateDelayTime(newDelayTime: Int) {
        saveSettings(newDelayTime)
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
    }

    fun loadSettings(): Flow<Int> {
        return settingsManager.loadDelayTime()
    }

    private fun saveSettings(delayTime: Int) {
        runBlocking {
            launch {
                settingsManager.saveDelayTime(delayTime)
            }
        }
    }
}