package com.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.project.navigation.AppNavHost
import com.example.project.ui.theme.FocusTreeTheme
import com.example.project.util.LocalSettings
import com.example.project.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FocusTreeTheme {
                val navController = rememberNavController()
                val settingsViewModel: SettingsViewModel = viewModel()

                // Передаём ViewModel в CompositionLocalProvider
                CompositionLocalProvider(LocalSettings provides settingsViewModel) {
                    AppNavHost(navController)
                }
            }
        }
    }
}
