package com.carlengosez.open_camera_visor_house

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

// AÑADIMOS EL CALLBACK (onDeviceClick) AQUÍ
class CameraCardAdapter(
    private val devices: MutableList<Device>,
    private val onDeviceClick: (Device) -> Unit
) : RecyclerView.Adapter<CameraCardAdapter.CameraViewHolder>() {

    class CameraViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvCameraName)
        val tvStatus: TextView = view.findViewById(R.id.tvCameraStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_camera_card, parent, false)
        return CameraViewHolder(view)
    }

    override fun onBindViewHolder(holder: CameraViewHolder, position: Int) {
        val device = devices[position]
        holder.tvName.text = device.name
        holder.tvStatus.text = device.status

        val statusColor = if (device.status.equals("Online", ignoreCase = true)) {
            ContextCompat.getColor(holder.itemView.context, R.color.teal_700)
        } else {
            ContextCompat.getColor(holder.itemView.context, android.R.color.darker_gray)
        }
        holder.tvStatus.setTextColor(statusColor)

        // CUANDO TOCAN LA TARJETA, DISPARAMOS EL CLIC
        holder.itemView.setOnClickListener {
            onDeviceClick(device)
        }
    }

    override fun getItemCount() = devices.size

    fun addDevice(newDevice: Device) {
        devices.add(newDevice)
        notifyItemInserted(devices.size - 1)
    }
}