package com.project.csgoinfos.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationBarView
import com.project.csgoinfos.R
import com.project.csgoinfos.ui.home.HomeFragment
import com.project.csgoinfos.ui.skins.SkinsFragment
import com.project.csgoinfos.ui.stickers.StickersFragment
import com.project.csgoinfos.ui.highlights.HighlightsFragment
import com.project.csgoinfos.ui.crates.CratesFragment
import com.project.csgoinfos.ui.agents.AgentsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNav: NavigationBarView
    private lateinit var toolbar: MaterialToolbar
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        
        // Make toolbar title clickable to go to Home
        toolbar.setOnClickListener {
            if (viewPager.currentItem != 0) {
                navigateToTab(0)
            }
        }

        viewPager = findViewById<ViewPager2>(R.id.viewPager)
        bottomNav = findViewById<NavigationBarView>(R.id.bottomBar)

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 6
            override fun createFragment(position: Int) = when (position) {
                0 -> HomeFragment()
                1 -> SkinsFragment()
                2 -> StickersFragment()
                3 -> HighlightsFragment()
                4 -> CratesFragment()
                else -> AgentsFragment()
            }
        }

        bottomNav.setOnItemSelectedListener {
            viewPager.currentItem = when (it.itemId) {
                R.id.nav_skins -> 1
                R.id.nav_stickers -> 2
                R.id.nav_highlights -> 3
                R.id.nav_crates -> 4
                R.id.nav_agents -> 5
                else -> 0 // Default to Home
            }
            true
        }
        
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // Only update bottom nav if not on Home (position 0)
                if (position > 0) {
                    bottomNav.selectedItemId = when (position) {
                        1 -> R.id.nav_skins
                        2 -> R.id.nav_stickers
                        3 -> R.id.nav_highlights
                        4 -> R.id.nav_crates
                        5 -> R.id.nav_agents
                        else -> R.id.nav_skins
                    }
                    // Show home button in toolbar when not on Home
                    invalidateOptionsMenu()
                } else {
                    // Hide home button when on Home
                    invalidateOptionsMenu()
                }
            }
        })
        
        // Start on Home (position 0), no bottom nav item selected
        viewPager.currentItem = 0
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        // Only show home button when not on Home
        menu.findItem(R.id.action_home)?.isVisible = viewPager.currentItem != 0
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                navigateToTab(0)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    fun navigateToTab(position: Int) {
        if (::viewPager.isInitialized) {
            viewPager.currentItem = position
        }
    }
}
