package com.project.csgoinfos.ui.highlights

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.project.csgoinfos.R
import com.project.csgoinfos.model.Highlight
import com.project.csgoinfos.model.Map

class HighlightAdapter(private val onClick: (Highlight) -> Unit) : ListAdapter<Highlight, HighlightAdapter.VH>(DIFF) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Highlight>() {
            override fun areItemsTheSame(oldItem: Highlight, newItem: Highlight) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Highlight, newItem: Highlight) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val subtitle: TextView = itemView.findViewById(R.id.subtitle)
        private val btnInfo: ImageButton = itemView.findViewById(R.id.btnInfo)
        private val badge: View = itemView.findViewById(R.id.badge)

        fun bind(item: Highlight, onClick: (Highlight) -> Unit) {
            badge.visibility = View.GONE
            title.text = item.title ?: (item.player ?: "Highlight")
            val mapName = item.map?.let { Map.getMapName(it) }
            subtitle.visibility = View.VISIBLE
            subtitle.text = listOfNotNull(item.player, item.team, item.event, mapName).joinToString(" · ")
            image.load(item.thumbnail)
            itemView.setOnClickListener { onClick(item) }
            btnInfo.setOnClickListener { onClick(item) }
        }
    }
}
