package com.argossystem.hub

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.argossystem.hub.ui.dashboard.DashboardScreen
import com.argossystem.hub.ui.player.VideoPlayerScreen
import com.argossystem.hub.ui.theme.ArgosSystemHubTheme
import com.argossystem.hub.utils.QrDecoder
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class MainActivity : ComponentActivity() {

    // Inicializamos el ViewModel de forma profesional
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArgosSystemHubTheme {
                val scanner = GmsBarcodeScanning.getClient(this)

                // Variable para saber en qué pantalla estamos (null = Dashboard, String = Reproductor)
                var selectedIp by remember { mutableStateOf<String?>(null) }

                if (selectedIp == null) {
                    // PANTALLA 1: La lista de cámaras
                    DashboardScreen(
                        nodes = viewModel.nodes,
                        onAddClick = {
                            scanner.startScan().addOnSuccessListener { barcode ->
                                barcode.rawValue?.let { text ->
                                    QrDecoder.decodeArgosQr(text)?.let { newNode ->
                                        viewModel.addNode(newNode)
                                        Toast.makeText(this, "Nodo guardado", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            }
                        },
                        // Cuando toquemos una tarjeta, guardamos su IP y la pantalla cambiará sola
                        onNodeSelected = { ip ->
                            selectedIp = ip
                        }
                    )
                } else {
                    // PANTALLA 2: El reproductor de video
                    VideoPlayerScreen(
                        ip = selectedIp!!,
                        onBack = {
                            // Al darle atrás, volvemos a poner la IP en nulo para regresar al Dashboard
                            selectedIp = null
                        }
                    )
                }
            }
        }
    }
}