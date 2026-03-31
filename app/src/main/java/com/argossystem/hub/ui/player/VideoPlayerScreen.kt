package com.argossystem.hub.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(ip: String, onBack: () -> Unit) {
    val context = LocalContext.current

    // ⚡ ESTADOS DE LA INTERFAZ
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 1. Configuramos el motor del reproductor
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val streamUrl = "http://$ip:8080/video"
            setMediaItem(MediaItem.fromUri(streamUrl))
            prepare()
            playWhenReady = true
        }
    }

    // 2. ESCUCHAMOS EL ESTADO DEL REPRODUCTOR
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                // Si está cargando el video
                if (playbackState == Player.STATE_BUFFERING) {
                    isLoading = true
                }
                // Si el video ya empezó a reproducirse
                else if (playbackState == Player.STATE_READY) {
                    isLoading = false
                    errorMessage = null
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                // Si la cámara está apagada o no hay conexión
                isLoading = false
                errorMessage = "No se pudo conectar a la cámara.\nVerifica que esté encendida y en la misma red."
            }
        }

        // Conectamos el oyente
        exoPlayer.addListener(listener)

        // Limpieza al salir de la pantalla
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    // 3. LA INTERFAZ VISUAL
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
        // Un "Box" nos permite poner elementos uno encima del otro
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black), // Fondo negro como en el cine
            contentAlignment = Alignment.Center
        ) {
            // Capa 1: El reproductor de video al fondo
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false // Escondemos los controles porque es en vivo
                    }
                }
            )

            // Capa 2: Si está cargando, dibujamos la rueda
            if (isLoading && errorMessage == null) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
            }

            // Capa 3: Si hay un error, mostramos el aviso visual
            if (errorMessage != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error de conexión",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage!!,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}