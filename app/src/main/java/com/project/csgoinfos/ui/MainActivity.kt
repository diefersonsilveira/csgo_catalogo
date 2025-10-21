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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val bottom = findViewById<NavigationBarView>(R.id.bottomBar)

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int) = if (position == 0) SkinsFragment() else StickersFragment()
        }

        bottom.setOnItemSelectedListener {
            viewPager.currentItem = if (it.itemId == R.id.nav_skins) 0 else 1
            true
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottom.selectedItemId = if (position == 0) R.id.nav_skins else R.id.nav_stickers
            }
        })
        bottom.selectedItemId = R.id.nav_skins
    }
}
