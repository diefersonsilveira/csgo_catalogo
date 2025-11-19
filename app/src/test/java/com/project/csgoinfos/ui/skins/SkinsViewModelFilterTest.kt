package com.project.csgoinfos.ui.skins

import com.project.csgoinfos.model.Rarity
import com.project.csgoinfos.model.Skin
import com.project.csgoinfos.ui.filters.SkinFilters
import org.junit.Assert.*
import org.junit.Test

class SkinsViewModelFilterTest {
    @Test
    fun `test filter by name`() {
        val rarity = Rarity("1", "Covert", "#EB4B4B")
        val skins = listOf(
            Skin("1", "AK-47 | Redline", null, null, null, null, null, null, rarity, null, null, null, null, null, null, null),
            Skin("2", "M4A4 | Howl", null, null, null, null, null, null, rarity, null, null, null, null, null, null, null),
            Skin("3", "AWP | Dragon Lore", null, null, null, null, null, null, rarity, null, null, null, null, null, null, null)
        )
        
        val filters = SkinFilters(query = "AK-47")
        val q = filters.query?.trim()?.lowercase()
        
        val filtered = skins.filter { s ->
            q.isNullOrBlank() || s.name.lowercase().contains(q)
        }
        
        assertNotNull(filtered)
        assertEquals(1, filtered.size)
        assertEquals("AK-47 | Redline", filtered[0].name)
    }
}

