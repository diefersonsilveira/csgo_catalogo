package com.project.csgoinfos.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationBarView
import com.project.csgoinfos.R
import com.project.csgoinfos.ui.skins.SkinsFragment
import com.project.csgoinfos.ui.stickers.StickersFragment
import com.project.csgoinfos.ui.highlights.HighlightsFragment
import com.project.csgoinfos.ui.crates.CratesFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val bottom = findViewById<NavigationBarView>(R.id.bottomBar)

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 4
            override fun createFragment(position: Int) = when (position) {
                0 -> SkinsFragment()
                1 -> StickersFragment()
                2 -> HighlightsFragment()
                else -> CratesFragment()
            }
        }

        bottom.setOnItemSelectedListener {
            viewPager.currentItem = when (it.itemId) {
                R.id.nav_skins -> 0
                R.id.nav_stickers -> 1
                R.id.nav_highlights -> 2
                else -> 3
            }
            true
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottom.selectedItemId = when (position) {
                    0 -> R.id.nav_skins
                    1 -> R.id.nav_stickers
                    2 -> R.id.nav_highlights
                    else -> R.id.nav_crates
                }
            }
        })
        bottom.selectedItemId = R.id.nav_skins
    }
}
