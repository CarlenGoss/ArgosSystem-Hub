package com.carlengosez.open_camera_visor_house

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DeviceManager {
    private const val PREFS_NAME = "MisCamarasPrefs"
    private const val KEY_DEVICES = "lista_camaras"

    // Guarda la lista completa en la memoria del teléfono
    fun saveDevices(context: Context, devices: List<Device>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(devices)
        prefs.edit().putString(KEY_DEVICES, json).apply()
    }

    // Lee la memoria y devuelve la lista de cámaras (o una lista vacía si no hay nada)
    fun getDevices(context: Context): MutableList<Device> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_DEVICES, null)

        return if (json != null) {
            val type = object : TypeToken<MutableList<Device>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf() // Devuelve una lista vacía si es la primera vez que abres la app
        }
    }
}