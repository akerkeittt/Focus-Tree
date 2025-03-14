package com.example.project.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project.viewmodels.ForestViewModel
import com.example.project.R
import com.example.project.util.LocalSettings

@Composable
fun ForestScreen(viewModel: ForestViewModel = viewModel()) {
    val settingsViewModel = LocalSettings.current ?: return

    val language by settingsViewModel.language.collectAsState()
    var selectedPeriod by remember { mutableStateOf("day") }
    val trees by viewModel.trees.observeAsState(emptyList())

    val periodLabels = mapOf(
        "day" to if (language == "ru") "День" else "Day",
        "week" to if (language == "ru") "Неделя" else "Week",
        "month" to if (language == "ru") "Месяц" else "Month",
        "year" to if (language == "ru") "Год" else "Year"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (language == "ru") "Мой лес" else "My Forest",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            periodLabels.forEach { (period, label) ->
                ElevatedButton(
                    onClick = { selectedPeriod = period },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(trees) { tree ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(110.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tree3),
                        contentDescription = "Tree",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
