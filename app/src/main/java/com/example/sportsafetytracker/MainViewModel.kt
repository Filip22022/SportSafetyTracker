package com.example.sportsafetytracker
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MainViewModel(application: Application, private val savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    var delayTime = savedStateHandle.getLiveData("delayTime", 15)

    private val _accelerometerData = MutableLiveData<Triple<Float, Float, Float>>()
    val accelerometerData: LiveData<Triple<Float, Float, Float>> = _accelerometerData

    private val sensorDataManager = SensorActivity(application) { data ->
        _accelerometerData.postValue(data)
    }

    fun updateDelayTime(newDelayTime: Int) {
        delayTime.value = newDelayTime
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
}