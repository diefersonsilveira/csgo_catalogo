package com.project.csgoinfos.ui.crates

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
import android.content.Intent
import com.project.csgoinfos.model.Crate
import com.project.csgoinfos.ui.crates.CrateDetailActivity

class CrateAdapter : ListAdapter<Crate, CrateAdapter.VH>(DIFF) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Crate>() {
            override fun areItemsTheSame(oldItem: Crate, newItem: Crate) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Crate, newItem: Crate) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val subtitle: TextView = itemView.findViewById(R.id.subtitle)
        private val btnInfo: ImageButton = itemView.findViewById(R.id.btnInfo)
        private val badge: View = itemView.findViewById(R.id.badge)
        fun bind(item: Crate) {
            badge.visibility = View.GONE
            title.text = item.name ?: "Crate"
            subtitle.visibility = View.VISIBLE
            subtitle.text = item.rarity?.name ?: ""
            image.load(item.image)
            btnInfo.visibility = View.VISIBLE

            btnInfo.setOnClickListener {
                val intent = Intent(itemView.context, CrateDetailActivity::class.java).apply {
                    putExtra("name", item.name)
                    putExtra("desc", item.description)
                    putExtra("image", item.image)
                    putExtra("rarity", item.rarity?.name)
                    putExtra("rarityColor", item.rarity?.color)
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}


