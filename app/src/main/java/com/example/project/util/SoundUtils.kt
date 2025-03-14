package com.example.project.util

import com.example.project.R

fun getMelodyResource(melody: String): Int {
    return when (melody) {
        "Rain" -> R.raw.rain
        "Birds" -> R.raw.bird
        "Piano" -> R.raw.piano
        else -> R.raw.focus_music
    }
}

fun getTimerSoundResource(timerSound: String): Int {
    return when (timerSound) {
        "Bell" -> R.raw.bell
        "Clock" -> R.raw.clock
        "None" -> 0 // Без звука
        else -> R.raw.beep
    }
}
