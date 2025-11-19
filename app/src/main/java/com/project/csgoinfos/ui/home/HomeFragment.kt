package com.project.csgoinfos.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.csgoinfos.R

class HomeFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        view.findViewById<View>(R.id.card_skins)?.setOnClickListener {
            navigateToTab(1) // Skins is at position 1
        }
        
        view.findViewById<View>(R.id.card_stickers)?.setOnClickListener {
            navigateToTab(2) // Stickers is at position 2
        }
        
        view.findViewById<View>(R.id.card_highlights)?.setOnClickListener {
            navigateToTab(3) // Highlights is at position 3
        }
        
        view.findViewById<View>(R.id.card_crates)?.setOnClickListener {
            navigateToTab(4) // Crates is at position 4
        }
        
        view.findViewById<View>(R.id.card_agents)?.setOnClickListener {
            navigateToTab(5) // Agents is at position 5
        }
    }
    
    private fun navigateToTab(position: Int) {
        (activity as? com.project.csgoinfos.ui.MainActivity)?.navigateToTab(position)
    }
}

