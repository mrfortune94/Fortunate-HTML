package com.fortunatehtml.android.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fortunatehtml.android.R
import com.fortunatehtml.android.model.TrafficEntry

class TrafficAdapter(
    private val onItemClick: (TrafficEntry) -> Unit
) : ListAdapter<TrafficEntry, TrafficAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_traffic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val methodText: TextView = itemView.findViewById(R.id.methodText)
        private val urlText: TextView = itemView.findViewById(R.id.urlText)
        private val statusText: TextView = itemView.findViewById(R.id.statusText)
        private val durationText: TextView = itemView.findViewById(R.id.durationText)
        private val schemeText: TextView = itemView.findViewById(R.id.schemeText)

        fun bind(entry: TrafficEntry) {
            methodText.text = entry.method
            urlText.text = entry.host + entry.path
            statusText.text = entry.statusText
            schemeText.text = if (entry.isHttps) "HTTPS" else "HTTP"

            durationText.text = if (entry.duration != null) "${entry.duration}ms" else ""

            // Color-code status
            val statusColor = when {
                entry.state == TrafficEntry.State.FAILED -> Color.RED
                entry.statusCode != null && entry.statusCode in 200..299 -> Color.parseColor("#4CAF50")
                entry.statusCode != null && entry.statusCode in 300..399 -> Color.parseColor("#FF9800")
                entry.statusCode != null && entry.statusCode >= 400 -> Color.RED
                else -> Color.GRAY
            }
            statusText.setTextColor(statusColor)

            // Color-code method
            val methodColor = when (entry.method) {
                "GET" -> Color.parseColor("#2196F3")
                "POST" -> Color.parseColor("#4CAF50")
                "PUT" -> Color.parseColor("#FF9800")
                "DELETE" -> Color.RED
                "PATCH" -> Color.parseColor("#9C27B0")
                else -> Color.GRAY
            }
            methodText.setTextColor(methodColor)

            itemView.setOnClickListener { onItemClick(entry) }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<TrafficEntry>() {
        override fun areItemsTheSame(oldItem: TrafficEntry, newItem: TrafficEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TrafficEntry, newItem: TrafficEntry): Boolean {
            return oldItem == newItem
        }
    }
}
