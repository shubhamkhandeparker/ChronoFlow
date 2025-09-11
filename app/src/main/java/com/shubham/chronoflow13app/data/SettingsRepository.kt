package com.shubham.chronoflow13app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import  kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

//This create a delegate to handle the DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    //This is the key we will use to save & retrieve the timer duration
    private object PreferenceKeys {
        val TIMER_DURATION = longPreferencesKey("timer_duration_ms")

    }

    //function to save the duration
    suspend fun saveTimerDuration(duration: Long) {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.TIMER_DURATION] = duration
        }

    }

    //Function to READ the duration
    val getTimerDuration: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.TIMER_DURATION]?:0L //Default value for now
    }

}

