package com.argossystem.hub

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.argossystem.hub.data.NodeRepository
import com.argossystem.hub.model.ArgosNode

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NodeRepository(application)

    // Esta es la lista "viva" que ve la UI
    val nodes = mutableStateListOf<ArgosNode>()

    init {
        // Apenas arranca el ViewModel, cargamos lo que haya en el disco
        nodes.addAll(repository.loadNodes())
    }

    fun addNode(node: ArgosNode) {
        nodes.add(node)
        repository.saveNodes(nodes) // Guardamos el cambio de inmediato
    }
}