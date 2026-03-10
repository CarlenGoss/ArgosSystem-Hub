package com.carlengosez.open_camera_visor_house

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController // <-- ¡Aquí está el import mágico!
import com.carlengosez.open_camera_visor_house.databinding.FragmentHomeBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraAdapter: CameraCardAdapter
    private var mySavedDevices = mutableListOf<Device>()

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
        if (result.contents != null) {
            val qrLeido = result.contents

            val newCamera = Device(
                id = System.currentTimeMillis().toString(),
                name = "Cámara Nueva",
                status = "Offline",
                ipAddress = qrLeido
            )

            cameraAdapter.addDevice(newCamera)
            DeviceManager.saveDevices(requireContext(), mySavedDevices)

            Toast.makeText(requireContext(), "Cámara agregada con éxito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.action_add_device) {
                iniciarEscaner()
                true
            } else false
        }

        // Cargamos las cámaras guardadas
        mySavedDevices = DeviceManager.getDevices(requireContext())

        // Configuramos el adaptador CON el clic hacia el reproductor
        cameraAdapter = CameraCardAdapter(mySavedDevices) { deviceClickeado ->
            val bundle = Bundle()
            bundle.putString("STREAM_URL", deviceClickeado.ipAddress)

            // Viajamos al reproductor (ahora más limpio gracias al import)
            findNavController().navigate(R.id.playerFragment, bundle)
        }

        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHome.adapter = cameraAdapter
    } // <-- ESTA ES LA LLAVE QUE FALTABA

    private fun iniciarEscaner() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Apunta al código QR de la cámara")
        options.setCameraId(0)
        options.setBeepEnabled(true)
        options.setOrientationLocked(false)
        barcodeLauncher.launch(options)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}