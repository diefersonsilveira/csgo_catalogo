package com.project.csgoinfos.ui.stickers

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.project.csgoinfos.R
import com.project.csgoinfos.databinding.ItemCardBinding
import com.project.csgoinfos.model.Sticker

class StickerAdapter(private val onClick: (Sticker) -> Unit) : RecyclerView.Adapter<StickerAdapter.VH>() {
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
        holder.b.subtitle.visibility = android.view.View.GONE
        holder.b.image.load(item.image)
        val name = item.rarity?.name
        val colorHex = item.rarity?.color
        if (!name.isNullOrBlank()) {
            holder.b.badge.text = name
            val base = try {
                if (colorHex.isNullOrBlank()) null else Color.parseColor(colorHex)
            } catch (e: Exception) {
                null
            } ?: holder.b.root.context.getColor(R.color.brand_primary)
            val stroke = ColorUtils.blendARGB(base, Color.BLACK, 0.2f)
            val text = if (ColorUtils.calculateLuminance(base) < 0.5) android.graphics.Color.WHITE else android.graphics.Color.BLACK
            holder.b.badge.chipBackgroundColor = ColorStateList.valueOf(base)
            holder.b.badge.chipStrokeColor = ColorStateList.valueOf(stroke)
            holder.b.badge.setTextColor(text)
            holder.b.badge.chipStrokeWidth = holder.b.badge.resources.displayMetrics.density
            holder.b.badge.chipIcon = AppCompatResources.getDrawable(holder.b.root.context, R.drawable.ic_star_badge)
            holder.b.badge.chipIconTint = ColorStateList.valueOf(text)
            holder.b.badge.visibility = android.view.View.VISIBLE
        } else {
            holder.b.badge.visibility = android.view.View.GONE
        }
        holder.b.root.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}
