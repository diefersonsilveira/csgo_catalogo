package com.project.csgoinfos.ui.stickers

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.project.csgoinfos.databinding.ActivityDetailStickerBinding

class StickerDetailActivity : AppCompatActivity() {
    private lateinit var b: ActivityDetailStickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailStickerBinding.inflate(layoutInflater)
        setContentView(b.root)

        val name = intent.getStringExtra("name") ?: ""
        val desc = intent.getStringExtra("desc") ?: ""
        val img = intent.getStringExtra("image") ?: ""
        val rarity = intent.getStringExtra("rarity") ?: ""
        val rarityColor = intent.getStringExtra("rarityColor") ?: "#000000"
        val event = intent.getStringExtra("event") ?: ""
        val team = intent.getStringExtra("team") ?: ""
        val type = intent.getStringExtra("type") ?: ""
        val effect = intent.getStringExtra("effect") ?: ""

        setSupportActionBar(b.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = name
        b.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        b.image.load(img)
        b.rarity.text = rarity
        runCatching { b.rarity.setTextColor(Color.parseColor(rarityColor)) }
        b.event.text = event
        b.team.text = team
        b.type.text = type
        b.effect.text = effect
        b.description.text = if (desc.isBlank()) "—" else desc
    }
}
