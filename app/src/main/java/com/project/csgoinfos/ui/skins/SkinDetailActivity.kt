package com.project.csgoinfos.ui.skins

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.project.csgoinfos.databinding.ActivityDetailSkinBinding

class SkinDetailActivity : AppCompatActivity() {
    private lateinit var b: ActivityDetailSkinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailSkinBinding.inflate(layoutInflater)
        setContentView(b.root)

        val name = intent.getStringExtra("name") ?: ""
        val desc = intent.getStringExtra("desc") ?: ""
        val img = intent.getStringExtra("image") ?: ""
        val weapon = intent.getStringExtra("weapon") ?: ""
        val rarity = intent.getStringExtra("rarity") ?: ""
        val rarityColor = intent.getStringExtra("rarityColor") ?: "#000000"

        setSupportActionBar(b.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = name
        b.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        b.image.load(img)
        b.weapon.text = weapon
        b.rarity.text = rarity
        runCatching { b.rarity.setTextColor(Color.parseColor(rarityColor)) }
        b.description.text = if (desc.isBlank()) "—" else desc
    }
}
