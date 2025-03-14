package com.example.project.util

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState

fun playSound(context: Context, soundResId: Int, mediaPlayer: MutableState<MediaPlayer?>, loop: Boolean = false) {
    mediaPlayer.value?.release()
    mediaPlayer.value = MediaPlayer.create(context, soundResId)?.apply {
        isLooping = loop
        start()
    }
}

fun stopSound(mediaPlayer: MutableState<MediaPlayer?>) {
    mediaPlayer.value?.stop()
    mediaPlayer.value?.release()
    mediaPlayer.value = null
}
