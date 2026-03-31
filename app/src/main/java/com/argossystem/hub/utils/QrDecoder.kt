package com.argossystem.hub.utils

import com.argossystem.hub.model.ArgosNode
import org.json.JSONObject

object QrDecoder {
    fun decodeArgosQr(qrText: String): ArgosNode? {
        return try {
            val json = JSONObject(qrText)
            ArgosNode(
                name = json.getString("deviceName"),
                ip = json.getString("ip")
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}