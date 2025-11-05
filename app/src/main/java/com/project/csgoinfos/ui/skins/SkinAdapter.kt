package com.project.csgoinfos.ui.skins

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.chip.Chip
import com.project.csgoinfos.R
import com.project.csgoinfos.model.Skin

class SkinAdapter(
    private val onSkinClick: (Skin) -> Unit,
    private val onSkinLongClick: (Skin) -> Unit
) : ListAdapter<Skin, SkinAdapter.SkinViewHolder>(SkinDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkinViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_skin, parent, false)
        return SkinViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkinViewHolder, position: Int) {
        val skin = getItem(position)
        holder.bind(skin)
        holder.itemView.setOnClickListener { onSkinClick(skin) }
        holder.itemView.setOnLongClickListener { onSkinLongClick(skin); true }
    }

    class SkinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.image)
        private val title: TextView = view.findViewById(R.id.title)
        private val badge: Chip = view.findViewById(R.id.badge)

        fun bind(skin: Skin) {
            image.load(skin.image) { crossfade(true) }
            title.text = skin.name
            badge.text = skin.rarity?.name
            val color = skin.rarity?.color?.let { runCatching { it.toColorInt() }.getOrNull() }
                ?: itemView.context.getColor(R.color.brand_primary)
            badge.chipBackgroundColor = android.content.res.ColorStateList.valueOf(color)
            badge.setTextColor(if (androidx.core.graphics.ColorUtils.calculateLuminance(color) < 0.5) android.graphics.Color.WHITE else android.graphics.Color.BLACK)
        }
    }
}

object SkinDiffCallback : DiffUtil.ItemCallback<Skin>() {
    override fun areItemsTheSame(oldItem: Skin, newItem: Skin): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Skin, newItem: Skin): Boolean {
        return oldItem == newItem
    }
}
