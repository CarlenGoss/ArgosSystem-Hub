package com.argossystem.hub.utils

import com.argossystem.hub.model.ArgosNode
import org.json.JSONObject

object QrDecoder {
    fun decodeArgosQr(qrText: String): ArgosNode? {
        return try {
            // 1. Convertimos el texto del QR en un objeto JSON
            val json = JSONObject(qrText)

            // 2. Extraemos los datos y creamos el Nodo
            ArgosNode(
                name = json.getString("deviceName"),
                ip = json.getString("ip"),
                // ⚡ Datos dummy para la v1.0-alpha.
                // Luego el Nodo enviará sus valores reales en el JSON.
                usedGb = 96,
                freeGb = 32
            )
        } catch (e: Exception) {
            // Si el QR es inválido o no es un JSON, imprimimos el error y retornamos null
            e.printStackTrace()
            null
        }
    }
}