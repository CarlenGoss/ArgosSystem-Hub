package com.argossystem.hub.ui.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(ip: String, onBack: () -> Unit) {
    val context = LocalContext.current

    // 1. Configuramos el "Motor" del reproductor (ExoPlayer)
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // OJO: Aquí armamos la URL.
            // Suponemos que tu servidor de cámara transmite en el puerto 8080 en la ruta /video
            // Si tu OpenCameraHouse usa otro puerto o ruta, cámbialo aquí.
            val streamUrl = "http://$ip:8080/video"

            setMediaItem(MediaItem.fromUri(streamUrl))
            prepare()
            playWhenReady = true // Que arranque apenas cargue
        }
    }

    // 2. Limpieza: Cuando le demos atrás, debemos destruir el reproductor para no gastar RAM
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // 3. La Interfaz
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cámara: $ip") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        // 4. El "Puente" para mostrar el reproductor en Jetpack Compose
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    // Ocultamos los controles de pausa/adelantar (es en vivo, no Netflix)
                    useController = false
                }
            }
        )
    }
}