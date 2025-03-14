package com.example.project.screens

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project.R
import com.example.project.util.LocalSettings
import com.example.project.components.DropdownMenuComponent
import com.example.project.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = viewModel()) {
    val settingsViewModel = LocalSettings.current ?: return

    val language by settingsViewModel.language.collectAsState()

    var selectedMelody by remember { mutableStateOf("Default") }
    var selectedTimerSound by remember { mutableStateOf("Beep") }
    val melodies = listOf("Default", "Rain", "Birds", "Piano")
    val timerSounds = listOf("Beep", "Bell", "Clock", "None")

    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    fun playSound(context: Context, soundName: String) {
        mediaPlayer?.release()
        val soundResId = when (soundName) {
            "Rain" -> R.raw.rain
            "Birds" -> R.raw.bird
            "Piano" -> R.raw.piano
            "Beep" -> R.raw.beep
            "Bell" -> R.raw.bell
            "Clock" -> R.raw.clock
            else -> null
        }
        soundResId?.let {
            mediaPlayer = MediaPlayer.create(context, it)
            mediaPlayer?.start()
        }
    }

    fun stopSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (language == "ru") "Настройки" else "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(if (language == "ru") "Язык" else "Language")
                Row {
                    OutlinedButton(
                        onClick = { settingsViewModel.setLanguage("ru") },
                        enabled = language != "ru"
                    ) { Text("Русский") }

                    Spacer(Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = { settingsViewModel.setLanguage("en") },
                        enabled = language != "en"
                    ) { Text("English") }
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(if (language == "ru") "Фоновая мелодия" else "Focus Melody")
                DropdownMenuComponent(
                    options = melodies,
                    selectedOption = selectedMelody,
                    onOptionSelected = {
                        selectedMelody = it
                        playSound(context, it)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { stopSound() }) {
                    Text(if (language == "ru") "Остановить" else "Stop")
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(if (language == "ru") "Звук таймера" else "Timer Sound")
                DropdownMenuComponent(
                    options = timerSounds,
                    selectedOption = selectedTimerSound,
                    onOptionSelected = {
                        selectedTimerSound = it
                        playSound(context, it)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { stopSound() }) {
                    Text(if (language == "ru") "Остановить" else "Stop")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ElevatedButton(
                onClick = {
                    viewModel.setMelody(selectedMelody)
                    viewModel.setTimerSound(selectedTimerSound)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (language == "ru") "Сохранить" else "Save")
            }

            OutlinedButton(
                onClick = { settingsViewModel.setLanguage("en") },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (language == "ru") "Сбросить" else "Reset")
            }
        }
    }
}
