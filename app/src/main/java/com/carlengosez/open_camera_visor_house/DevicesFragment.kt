package com.carlengosez.open_camera_visor_house

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlengosez.open_camera_visor_house.databinding.FragmentDevicesBinding

class DevicesFragment : Fragment(R.layout.fragment_devices) {

    private var _binding: FragmentDevicesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDevicesBinding.bind(view)

        // Nuestra lista de cámaras de prueba
        val myDevices = listOf(
            Device(id = "1", name = "Cámara Oficina Río", status = "Online", ipAddress = "192.168.1.50"),
            Device(id = "2", name = "Cámara Pasillo", status = "Offline", ipAddress = "192.168.1.51"),
            Device(id = "3", name = "Cámara Almacén", status = "Online", ipAddress = "192.168.1.52")
        )

        binding.recyclerViewDevices.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDevices.adapter = DeviceAdapter(myDevices)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}