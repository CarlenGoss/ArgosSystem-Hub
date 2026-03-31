package com.argossystem.hub.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.argossystem.hub.model.ArgosNode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    nodes: List<ArgosNode>,
    onAddClick: () -> Unit,
    onNodeSelected: (String) -> Unit // ⚡ ¡Esta es la pieza que nos faltaba!
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ArgosSystem Hub 🛡️") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+", style = MaterialTheme.typography.headlineSmall)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(nodes) { node ->
                // Aquí conectamos la tarjeta directamente con la acción de la pantalla
                NodeCard(node = node, onNodeSelected = onNodeSelected)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeCard(node: ArgosNode, onNodeSelected: (String) -> Unit) {
    Card(
        // ⚡ ¡Aquí está la magia! Hacemos que la tarjeta reaccione al toque
        onClick = { onNodeSelected(node.ip) },
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = node.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "IP: ${node.ip}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
        }
    }
}