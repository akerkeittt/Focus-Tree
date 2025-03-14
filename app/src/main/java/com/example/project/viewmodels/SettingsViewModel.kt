package com.example.project.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Application.dataStore by preferencesDataStore("settings")

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = application.dataStore

    private val languageKey = stringPreferencesKey("language")
    private val notificationsKey = booleanPreferencesKey("notifications")
    private val melodyKey = stringPreferencesKey("melody")
    private val timerSoundKey = stringPreferencesKey("timer_sound")

    private val _language = MutableStateFlow("en")
    val language = _language.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled = _notificationsEnabled.asStateFlow()

    private val _melody = MutableStateFlow("Default")
    val melody = _melody.asStateFlow()

    private val _timerSound = MutableStateFlow("Beep")
    val timerSound = _timerSound.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val prefs = dataStore.data.first()
            _language.value = prefs[languageKey] ?: "en"
            _notificationsEnabled.value = prefs[notificationsKey] ?: true
            _melody.value = prefs[melodyKey] ?: "Default"
            _timerSound.value = prefs[timerSoundKey] ?: "Beep"
        }
    }

    fun setLanguage(newLanguage: String) {
        _language.value = newLanguage
        viewModelScope.launch {
            dataStore.edit { prefs -> prefs[languageKey] = newLanguage }
        }
    }

    fun setNotifications(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        viewModelScope.launch {
            dataStore.edit { prefs -> prefs[notificationsKey] = enabled }
        }
    }

    fun setMelody(newMelody: String) {
        _melody.value = newMelody
        viewModelScope.launch {
            dataStore.edit { prefs -> prefs[melodyKey] = newMelody }
        }
    }

    fun setTimerSound(newSound: String) {
        _timerSound.value = newSound
        viewModelScope.launch {
            dataStore.edit { prefs -> prefs[timerSoundKey] = newSound }
        }
    }
}
