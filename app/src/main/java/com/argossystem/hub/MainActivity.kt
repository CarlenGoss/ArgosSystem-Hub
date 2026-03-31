package com.argossystem.hub

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.argossystem.hub.ui.dashboard.DashboardScreen
import com.argossystem.hub.ui.notifications.NotificationsScreen
import com.argossystem.hub.ui.player.VideoPlayerScreen
import com.argossystem.hub.ui.settings.SettingsScreen
import com.argossystem.hub.ui.theme.ArgosSystemHubTheme
import com.argossystem.hub.utils.QrDecoder
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

// Importa tus nuevas pantallas (ajusta el paquete si es necesario)
// import com.argossystem.hub.ui.notifications.NotificationsScreen
// import com.argossystem.hub.ui.settings.SettingsScreen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArgosSystemHubTheme {
                val scanner = GmsBarcodeScanning.getClient(this)

                // 1. Estado para la IP seleccionada (Video)
                var selectedIp by remember { mutableStateOf<String?>(null) }

                // 2. Estado para la pestaña actual (Navegación)
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

                if (selectedIp != null) {
                    // PANTALLA COMPLETA: Reproductor
                    VideoPlayerScreen(
                        ip = selectedIp!!,
                        onBack = { selectedIp = null }
                    )
                } else {
                    // PANTALLA CON NAVEGACIÓN: Scaffold + BottomBar
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                val items = listOf(Screen.Home, Screen.Notifications, Screen.Settings)
                                items.forEach { screen ->
                                    NavigationBarItem(
                                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                                        label = { Text(screen.title) },
                                        selected = currentScreen == screen,
                                        onClick = { currentScreen = screen }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        // El contenido cambia según 'currentScreen'
                        Surface(modifier = Modifier.padding(innerPadding)) {
                            when (currentScreen) {
                                // ... dentro del when(currentScreen) ...
                                Screen.Notifications -> NotificationsScreen()
                                Screen.Settings -> SettingsScreen(onDeleteAll = { viewModel.clearAllNodes() })
                                Screen.Home -> DashboardScreen(
                                    nodes = viewModel.nodes,
                                    onAddClick = {
                                        scanner.startScan().addOnSuccessListener { barcode ->
                                            barcode.rawValue?.let { text ->
                                                QrDecoder.decodeArgosQr(text)?.let { newNode ->
                                                    viewModel.addNode(newNode)
                                                    Toast.makeText(this@MainActivity, "Nodo guardado", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    },
                                    onNodeSelected = { ip -> selectedIp = ip },
                                    onDeleteNode = { node -> viewModel.deleteNode(node) }
                                )
                                Screen.Notifications -> {
                                    // Aquí puedes poner el componente de notificaciones
                                    // NotificationsScreen()
                                    Text("Historial de Alertas (Próximamente)", modifier = Modifier.padding(16.dp))
                                }
                                Screen.Settings -> {
                                    // Aquí puedes poner tu diseño de Ajustes/Zona de Peligro
                                    // SettingsScreen()
                                    Text("Ajustes del Sistema (Próximamente)", modifier = Modifier.padding(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Inicio", Icons.Default.Home)
    object Notifications : Screen("notifications", "Alertas", Icons.Default.Notifications)
    object Settings : Screen("settings", "Ajustes", Icons.Default.Settings)
}