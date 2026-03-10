package com.carlengosez.open_camera_visor_house

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlengosez.open_camera_visor_house.databinding.FragmentPlayerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private var player: ExoPlayer? = null
    private var baseUrl: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPlayerBinding.bind(view)

        // 1. Configurar el botón de atrás
        binding.playerToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // 2. Inicializar ExoPlayer
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        // 3. Analizar la URL que nos manda el QR
        val streamUrl = arguments?.getString("STREAM_URL") ?: ""

        // Extraemos inteligentemente la IP y el puerto de tu S10 (Ej: http://100.114.71.13:8080)
        baseUrl = if (streamUrl.contains("/video")) {
            streamUrl.substringBefore("/video")
        } else {
            streamUrl.trimEnd('/')
        }

        // 4. Reproducir el video principal
        if (streamUrl.isNotEmpty()) {
            reproducirVideo(streamUrl)
        }

        // 5. ¡A buscar los eventos en el servidor!
        if (baseUrl.isNotEmpty()) {
            cargarEventos()
        }
    }

    // Función auxiliar para cambiar de video fácilmente
    private fun reproducirVideo(urlExacta: String) {
        val mediaItem = MediaItem.fromUri(urlExacta)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
    }

    // Función que lee el JSON de tu servidor
    private fun cargarEventos() {
        // Lanzamos la petición de red en segundo plano (Corrutina)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Consultamos la ruta que vimos ayer en tu navegador web
                val jsonResponse = URL("$baseUrl/videos").readText()

                // Convertimos el texto a una lista de Kotlin
                val jsonArray = JSONArray(jsonResponse)
                val listaVideos = mutableListOf<String>()
                for (i in 0 until jsonArray.length()) {
                    listaVideos.add(jsonArray.getString(i))
                }

                // Volvemos a la pantalla principal para pintar la lista
                withContext(Dispatchers.Main) {
                    val adapter = EventAdapter(listaVideos) { nombreArchivo ->
                        // ¡La magia! Si tocan un elemento de la lista, armamos la ruta y lo reproducimos
                        val nuevaUrl = "$baseUrl/video/$nombreArchivo"
                        reproducirVideo(nuevaUrl)
                    }
                    binding.recyclerViewEvents.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerViewEvents.adapter = adapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Si algo falla, avisamos sin que la app se caiga
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error al cargar la lista de eventos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
        player = null
        _binding = null
    }
}