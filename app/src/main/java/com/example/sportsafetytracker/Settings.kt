package com.example.sportsafetytracker

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class Settings(private val context: Context) {

    object PreferencesKeys {
        val DELAY_KEY = intPreferencesKey("delay_key")
    }

    //suspend fun saveSettings() {
    suspend fun saveDelayTime(delayTime: Int) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.DELAY_KEY] = (delayTime ?: 60) as Int
        }
    }

    fun loadDelayTime(): Flow<Int> {
        var delayTime: Flow<Int> = context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.DELAY_KEY] ?: 60
            }
        return delayTime
    }
}