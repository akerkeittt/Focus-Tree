package com.example.project.screens

import android.media.MediaPlayer
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.project.R
import com.example.project.util.playSound
import com.example.project.util.stopSound
import com.example.project.viewmodel.SettingsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project.api.QuoteApi
import com.example.project.api.TranslateApi
import com.example.project.api.TranslateRequest
import com.example.project.data.Tree
import com.example.project.data.TreeDatabase
import com.example.project.util.getTimerSoundResource
import com.example.project.util.getMelodyResource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FocusScreen(
    initialMinutes: Int,
    onTimerFinish: () -> Unit,
    onGiveUp: () -> Unit,
    onTreePlanted: (Int) -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val melody by viewModel.melody.collectAsState()
    val timerSound by viewModel.timerSound.collectAsState()


    var timeLeft by remember { mutableIntStateOf(initialMinutes * 60) }
    var progress by remember { mutableFloatStateOf(1f) }
    var isRunning by remember { mutableStateOf(true) }
    var isGiveUp by remember { mutableStateOf(false) }
    var isSoundOn by remember { mutableStateOf(false) }
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }
    var timerFinished by remember { mutableStateOf(false) }

    val treeImages = listOf(R.drawable.tree1, R.drawable.tree2, R.drawable.tree3)
    val treeStage by remember {
        derivedStateOf {
            when {
                timeLeft > (initialMinutes * 40) -> treeImages[0]
                timeLeft > (initialMinutes * 20) -> treeImages[1]
                else -> treeImages[2]
            }
        }
    }

    var quote by remember { mutableStateOf("Loading...") }

    LaunchedEffect(language) {
        try {
            val fetchedQuote = QuoteApi.instance.getRandomQuote().firstOrNull()?.q ?: "Stay motivated!"
            quote = if (language == "ru") {
                val response = TranslateApi.instance.translate(TranslateRequest(fetchedQuote, target = "ru"))
                response.firstOrNull()?.translatedText ?: fetchedQuote
            } else {
                fetchedQuote
            }
        } catch (e: Exception) {
            quote = "Error loading quote"
        }
    }


    LaunchedEffect(isRunning) {
        while (isRunning && timeLeft > 0) {
            delay(1000L)
            timeLeft--
            progress = timeLeft / (initialMinutes * 60f)
        }
        if (timeLeft == 0 && !timerFinished) {
            timerFinished = true
            onTreePlanted(initialMinutes)

            val treeDao = TreeDatabase.getDatabase(context).treeDao()

            launch {
                treeDao.insertTree(Tree(minutes = initialMinutes))
            }

            playSound(context, getTimerSoundResource(timerSound), mediaPlayer)

            withContext(kotlinx.coroutines.Dispatchers.Main) {
                delay(2000)
                stopSound(mediaPlayer)
            }

            onTimerFinish()
        }

    }


    LaunchedEffect(isSoundOn) {
        if (isSoundOn) {
            playSound(context, getMelodyResource(melody), mediaPlayer, loop = true)
        } else {
            stopSound(mediaPlayer)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (language == "ru") "Фокусировка" else "Focus Mode",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(220.dp)) {
                drawArc(
                    color = Color.Green,
                    startAngle = -90f,
                    sweepAngle = 360 * progress,
                    useCenter = false,
                    style = Stroke(12f)
                )
            }

            if (!isGiveUp) {
                AnimatedContent(
                    targetState = treeStage,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                    },
                    label = "Tree Animation"
                ) { stage ->
                    Image(
                        painter = painterResource(id = stage),
                        contentDescription = if (language == "ru") "Растущее дерево" else "Growing Tree",
                        modifier = Modifier.size(120.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = quote, fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                isRunning = false
                isGiveUp = true
                stopSound(mediaPlayer)
                onGiveUp()
            }) {
                Text(if (language == "ru") "Сдаться" else "Give Up")
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(onClick = {
                isSoundOn = !isSoundOn
            }) {
                Icon(
                    painter = painterResource(if (isSoundOn) R.drawable.ic_headphones_on else R.drawable.ic_headphones_off),
                    contentDescription = if (language == "ru") "Переключить звук" else "Toggle Focus Sound"
                )
            }
        }
    }
}

