package com.argossystem.hub.ui.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NotificationsScreen() {
    val dummyAlerts = listOf(
        "Movimiento detectado en S10" to "Hace 5 min",
        "Nodo 'Laboratorio' desconectado" to "Hace 1 hora",
        "Grabación automática completada" to "Hace 3 horas"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Centro de Alertas", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(dummyAlerts.size) { index ->
                val alert = dummyAlerts[index]
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (index == 1) Icons.Default.Warning else Icons.Default.Notifications,
                            contentDescription = null,
                            tint = if (index == 1) Color.Red else MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(alert.first, style = MaterialTheme.typography.bodyLarge)
                            Text(alert.second, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}