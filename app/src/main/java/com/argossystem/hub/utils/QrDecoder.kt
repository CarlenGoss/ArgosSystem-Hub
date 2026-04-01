package com.argossystem.hub.utils

import com.argossystem.hub.model.ArgosNode
import org.json.JSONObject

object QrDecoder {
    fun decodeArgosQr(qrText: String): ArgosNode? {
        return try {
            // 1. Convertimos el texto del QR en un objeto JSON
            val json = JSONObject(qrText)

            // ⚡ NUEVO: Extraemos el token de seguridad.
            // Usamos optString para que si escaneas un QR viejo sin token, la app no explote.
            val token = json.optString("token", "")

            // 2. Extraemos los datos y creamos el Nodo
            ArgosNode(
                name = json.getString("deviceName"),
                ip = json.getString("ip"),
                // Datos dummy para la v1.0-alpha.
                usedGb = 96,
                freeGb = 32,
                // ⚡ NUEVO: Le pasamos el token que acabamos de extraer
                token = token
            )
        } catch (e: Exception) {
            // Si el QR es inválido o no es un JSON, imprimimos el error y retornamos null
            e.printStackTrace()
            null
        }
    }
}