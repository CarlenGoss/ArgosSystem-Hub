package com.carlengosez.open_camera_visor_house

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.carlengosez.open_camera_visor_house.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotificationsBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}