package com.project.csgoinfos.ui.stickers

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.project.csgoinfos.R
import com.project.csgoinfos.databinding.ItemCardBinding
import com.project.csgoinfos.model.Sticker

class StickerAdapter(
    private val onClick: (Sticker) -> Unit,
    private val onInfo: (Sticker) -> Unit
) : RecyclerView.Adapter<StickerAdapter.VH>() {
    private val items = mutableListOf<Sticker>()

    fun submit(list: List<Sticker>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(val b: ItemCardBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.b.title.text = item.name
        holder.b.subtitle.visibility = View.GONE
        holder.b.image.load(item.image)

        val colorHex = item.rarity?.color
        val accent = try {
            if (colorHex.isNullOrBlank()) null else Color.parseColor(colorHex)
        } catch (_: Exception) {
            null
        } ?: holder.b.root.context.getColor(R.color.brand_primary)

        holder.b.accent.backgroundTintList = ColorStateList.valueOf(accent)
        holder.b.thumb.backgroundTintList = ColorStateList.valueOf(ColorUtils.setAlphaComponent(accent, 32))

        val name = item.rarity?.name
        if (!name.isNullOrBlank()) {
            val bg = accent
            val stroke = ColorUtils.blendARGB(accent, Color.BLACK, 0.2f)
            val text = if (ColorUtils.calculateLuminance(bg) < 0.5) android.graphics.Color.WHITE else android.graphics.Color.BLACK
            holder.b.badge.text = name
            holder.b.badge.chipBackgroundColor = ColorStateList.valueOf(bg)
            holder.b.badge.chipStrokeColor = ColorStateList.valueOf(stroke)
            holder.b.badge.setTextColor(text)
            holder.b.badge.visibility = View.VISIBLE
        } else {
            holder.b.badge.visibility = View.GONE
        }

        holder.b.root.setOnClickListener { onClick(item) }
        holder.b.btnInfo.setOnClickListener { onInfo(item) }
    }

    override fun getItemCount() = items.size
}
