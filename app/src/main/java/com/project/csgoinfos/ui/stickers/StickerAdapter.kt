package com.project.csgoinfos.ui.stickers

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
import com.project.csgoinfos.model.Sticker

class StickerAdapter(
    private val onStickerClick: (Sticker) -> Unit,
    private val onStickerLongClick: (Sticker) -> Unit
) : ListAdapter<Sticker, StickerAdapter.StickerViewHolder>(StickerDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sticker, parent, false)
        return StickerViewHolder(view)
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        val sticker = getItem(position)
        holder.bind(sticker)
        holder.itemView.setOnClickListener { onStickerClick(sticker) }
        holder.itemView.setOnLongClickListener { onStickerLongClick(sticker); true }
    }

    class StickerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.image)
        private val title: TextView = view.findViewById(R.id.title)
        private val badge: Chip = view.findViewById(R.id.badge)

        fun bind(sticker: Sticker) {
            image.load(sticker.image) { crossfade(true) }
            title.text = sticker.name
            badge.text = sticker.rarity?.name
            val color = sticker.rarity?.color?.let { runCatching { it.toColorInt() }.getOrNull() }
                ?: itemView.context.getColor(R.color.brand_primary)
            badge.chipBackgroundColor = android.content.res.ColorStateList.valueOf(color)
            badge.setTextColor(if (androidx.core.graphics.ColorUtils.calculateLuminance(color) < 0.5) android.graphics.Color.WHITE else android.graphics.Color.BLACK)
        }
    }
}

object StickerDiffCallback : DiffUtil.ItemCallback<Sticker>() {
    override fun areItemsTheSame(oldItem: Sticker, newItem: Sticker): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Sticker, newItem: Sticker): Boolean {
        return oldItem == newItem
    }
}
