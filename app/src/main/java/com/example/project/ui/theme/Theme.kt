package com.example.project.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Цветовые схемы для темной и светлой темы
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAF50), // Зеленый
    secondary = Color(0xFFFFC107) // Желтый
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4CAF50), // Зеленый
    secondary = Color(0xFFFFC107) // Желтый
)

// Определяем формы (в Material 3 они больше не идут в комплекте)
private val AppShapes = Shapes()

@Composable
fun FocusTreeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,  // Добавили AppShapes, чтобы избежать ошибки
        content = content
    )
}
