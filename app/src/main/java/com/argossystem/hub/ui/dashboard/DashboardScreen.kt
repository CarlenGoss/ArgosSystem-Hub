package com.argossystem.hub.ui.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.argossystem.hub.model.ArgosNode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    nodes: List<ArgosNode>,
    onAddClick: () -> Unit,
    onNodeSelected: (String) -> Unit,
    onDeleteNode: (ArgosNode) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Panel de Control 🛡️") })
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Resumen del sistema en tiempo real",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // La lista de cámaras, cada una con su info de almacenamiento
            items(nodes) { node ->
                UnifiedNodeCard(
                    node = node,
                    onNodeSelected = onNodeSelected,
                    onDelete = { onDeleteNode(node) }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UnifiedNodeCard(node: ArgosNode, onNodeSelected: (String) -> Unit, onDelete: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Diálogo de confirmación (igual que antes)
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Eliminar '${node.name}'?") },
            text = { Text("Se quitará de la lista. Tendrás que escanear el QR de nuevo para agregarla.") },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDeleteDialog = false })
                { Text("Eliminar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // Diseño Unificado (Parecido a tus capturas)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onNodeSelected(node.ip) },
                onLongClick = { showDeleteDialog = true }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Fila 1: Nombre, IP y Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = node.name, style = MaterialTheme.typography.titleLarge)
                    Text(text = "IP: ${node.ip}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }

                // Badge de "En línea"
                Surface(
                    color = Color(0xFFE8F5E9),
                    contentColor = Color(0xFF2E7D32),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text("En línea", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))

            // Fila 2: Almacenamiento (Unificado)
            Text("Almacenamiento Local", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            val total = node.usedGb + node.freeGb
            val progress = if (total > 0) node.usedGb.toFloat() / total else 0f

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.LightGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("${node.usedGb} GB usados", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text("${node.freeGb} GB libres", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}