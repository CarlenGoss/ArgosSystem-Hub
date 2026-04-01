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
fun VideoPlayerScreen(node: com.argossystem.hub.model.ArgosNode, onBack: () -> Unit) { // ⚡ CAMBIO: Ahora recibe el 'node' completo
    val context = LocalContext.current

    // ⚡ ESTADOS DE LA INTERFAZ
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 1. Configuramos el motor del reproductor
    // Usamos 'remember(node)' para que si por alguna razón el objeto cambia, el player se reinicie
    val exoPlayer = remember(node) {
        ExoPlayer.Builder(context).build().apply {
            // ⚡ AQUÍ ESTÁ LA MAGIA: Usamos la IP y el Token del objeto node
            val streamUrl = "http://${node.ip}:8080/stream?token=${node.token}"

            setMediaItem(MediaItem.fromUri(streamUrl))
            prepare()
            playWhenReady = true
        }
    }

    // 2. ESCUCHAMOS EL ESTADO DEL REPRODUCTOR
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_BUFFERING) {
                    isLoading = true
                } else if (playbackState == Player.STATE_READY) {
                    isLoading = false
                    errorMessage = null
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                isLoading = false
                // Error más descriptivo
                errorMessage = "Error de conexión con ${node.name}.\nVerifica el Token y la red Tailscale."
            }
        }

        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    // 3. LA INTERFAZ VISUAL
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(node.name) }, // ⚡ Ahora muestra el nombre bonito de la cámara
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false
                    }
                }
            )

            if (isLoading && errorMessage == null) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
            }

            if (errorMessage != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
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