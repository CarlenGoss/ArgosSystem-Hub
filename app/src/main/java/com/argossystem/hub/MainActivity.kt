package com.argossystem.hub

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.argossystem.hub.model.ArgosNode
import com.argossystem.hub.ui.dashboard.DashboardScreen
import com.argossystem.hub.ui.theme.ArgosSystemHubTheme
import com.argossystem.hub.utils.QrDecoder
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArgosSystemHubTheme {
                // Nuestra lista de nodos guardados
                val nodes = remember { mutableStateListOf<ArgosNode>() }

                // Preparamos el escáner nativo de Google
                val scanner = GmsBarcodeScanning.getClient(this@MainActivity)

                DashboardScreen(
                    nodes = nodes,
                    onAddClick = {
                        // ⚡ Al tocar el "+", abrimos la cámara
                        scanner.startScan()
                            .addOnSuccessListener { barcode ->
                                // Si escaneó algo, sacamos el texto
                                val rawValue = barcode.rawValue
                                if (rawValue != null) {
                                    // Le pasamos el texto a nuestro decodificador (utils)
                                    val decodedNode = QrDecoder.decodeArgosQr(rawValue)

                                    if (decodedNode != null) {
                                        // Si el QR era válido y tenía el JSON, lo agregamos a la lista
                                        nodes.add(decodedNode)
                                        Toast.makeText(this@MainActivity, "Conectado: ${decodedNode.name}", Toast.LENGTH_SHORT).show()
                                    } else {
                                        // Si escanearon el menú de un restaurante u otro QR inválido
                                        Toast.makeText(this@MainActivity, "QR inválido. Escanea un dispositivo ArgosSystem.", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                            .addOnCanceledListener {
                                // El usuario cerró la cámara sin escanear nada (no hacemos nada)
                            }
                            .addOnFailureListener { e ->
                                // Hubo un error con la cámara
                                Toast.makeText(this@MainActivity, "Error en cámara: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                )
            }
        }
    }
}