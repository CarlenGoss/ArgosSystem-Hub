package com.argossystem.hub.data

import android.content.Context
import com.argossystem.hub.model.ArgosNode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NodeRepository(context: Context) {
    private val prefs = context.getSharedPreferences("argos_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Guarda la lista completa convirtiéndola a un String JSON
    fun saveNodes(nodes: List<ArgosNode>) {
        val json = gson.toJson(nodes)
        prefs.edit().putString("saved_nodes", json).apply()
    }

    // Lee el String JSON y lo vuelve a convertir en una Lista de objetos
    fun loadNodes(): List<ArgosNode> {
        val json = prefs.getString("saved_nodes", null) ?: return emptyList()
        val type = object : TypeToken<List<ArgosNode>>() {}.type
        return gson.fromJson(json, type)
    }
}