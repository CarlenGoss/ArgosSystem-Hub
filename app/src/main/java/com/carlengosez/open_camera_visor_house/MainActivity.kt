package com.carlengosez.open_camera_visor_house

import android.os.Bundle
import android.view.View // IMPORTANTE: Este import es para ocultar/mostrar vistas
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.carlengosez.open_camera_visor_house.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Activamos el ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Conectamos la barra inferior con el Grafo de Navegación
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        // --- LA MAGIA PARA OCULTAR LA BARRA ---
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.playerFragment) {
                // Ocultar la barra si entramos al reproductor de video
                binding.bottomNavigationView.visibility = View.GONE
            } else {
                // Mostrar la barra en todas las demás pantallas (Inicio, Notificaciones, Yo)
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }
}