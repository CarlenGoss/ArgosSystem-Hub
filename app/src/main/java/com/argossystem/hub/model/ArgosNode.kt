package com.argossystem.hub.model

data class ArgosNode(
    val name: String,
    val ip: String,
    // Nuevos campos para el almacenamiento
    val usedGb: Int = 0,
    val freeGb: Int = 0,
    // ⚡ NUEVO: El token de seguridad para pasar el bloqueo de Ktor
    val token: String = ""
)