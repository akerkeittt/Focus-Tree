package com.example.project.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.R

@Composable
fun CircularSeekBar(time: Int, onTimeChange: (Int) -> Unit, language: String) {
    val labels = mapOf(
        "en" to "Time: $time min",
        "ru" to "Время: $time мин"
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
        CircularProgressIndicator(
            progress = { time / 60f }, // Прогресс = время / макс. время (60 мин)
            modifier = Modifier.fillMaxSize(),
            color = Color.Green, // Цвет прогресса
            trackColor = Color.Gray // Цвет фона
        )

        Image(
            painter = painterResource(id = R.drawable.tree1),
            contentDescription = "Growing Tree",
            modifier = Modifier.size(80.dp)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Slider(
        value = time.toFloat(),
        onValueChange = { onTimeChange(it.toInt()) },
        valueRange = 5f..60f, // Мин 5 минут, макс 60 минут
        steps = 11 // Шаги: 5, 10, 15, ..., 60
    )

    Text(text = labels[language] ?: "Time: $time min", fontSize = 20.sp)
}
