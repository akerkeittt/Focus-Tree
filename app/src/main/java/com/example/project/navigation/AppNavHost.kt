package com.example.project.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project.screens.HomeScreen
import com.example.project.screens.FocusScreen
import com.example.project.viewmodels.ForestViewModel
import kotlinx.coroutines.launch
import com.example.project.screens.ForestScreen
import com.example.project.screens.SettingsScreen
import com.example.project.util.LocalSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val viewModel: ForestViewModel = viewModel()
    val settingsViewModel = LocalSettings.current ?: return
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val language by settingsViewModel.language.collectAsState()

    val menuLabels = mapOf(
        "en" to listOf("Menu", "Home", "Forest", "Settings"),
        "ru" to listOf("Меню", "Главная", "Лес", "Настройки")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(200.dp)
            ) {
                Text(
                    text = menuLabels[language]?.get(0) ?: "Menu",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Button(
                    onClick = {
                        navController.navigate("home")
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text(menuLabels[language]?.get(1) ?: "Home")
                }
                Button(
                    onClick = {
                        navController.navigate("forest")
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text(menuLabels[language]?.get(2) ?: "Forest")
                }
                Button(onClick = {
                    navController.navigate("settings")
                    scope.launch { drawerState.close() }
                }, Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(menuLabels[language]?.get(3) ?: "Settings")
                }
                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Focus Tree") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") { HomeScreen(navController) }
                composable("forest") { ForestScreen(viewModel) }
                composable("focus/{time}") { backStackEntry ->
                    val time = backStackEntry.arguments?.getString("time")?.toIntOrNull() ?: 25
                    FocusScreen(
                        initialMinutes = time,
                        onTimerFinish = { navController.navigate("forest") },
                        onGiveUp = { navController.navigate("home") },
                        onTreePlanted = { minutes -> viewModel.plantTree(minutes) }
                    )
                }
                composable("settings") { SettingsScreen() }
            }
        }
    }
}
