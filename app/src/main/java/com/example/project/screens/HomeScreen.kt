package com.example.project.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project.util.LocalSettings
import com.example.project.components.CircularSeekBar

@Composable
fun HomeScreen(navController: NavController) {
    val settingsViewModel = LocalSettings.current ?: return

    val language by settingsViewModel.language.collectAsState()
    var time by remember { mutableIntStateOf(25) }

    val labels = mapOf(
        "en" to "Plant",
        "ru" to "Посадить"
    )

    var isClicked by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isClicked) 0.9f else 1f)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularSeekBar(time = time, onTimeChange = { time = it }, language = language)

        Spacer(modifier = Modifier.height(20.dp))

        ElevatedButton(
            onClick = {
                isClicked = true
                navController.navigate("focus/$time")
            },
            modifier = Modifier.scale(scale)
        ) {
            Text(labels[language] ?: "Plant")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

