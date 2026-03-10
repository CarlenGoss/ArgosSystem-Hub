package com.carlengosez.open_camera_visor_house

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(
    private val events: List<String>,
    private val onEventClick: (String) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvEventName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val eventFileName = events[position]

        // Limpiamos un poco el nombre para que se vea más bonito (Opcional)
        val cleanName = eventFileName.replace(".mp4", "").replace("_", " ")
        holder.tvName.text = cleanName

        // Cuando toques este evento en la lista, disparamos el clic
        holder.itemView.setOnClickListener {
            onEventClick(eventFileName)
        }
    }

    override fun getItemCount() = events.size
}