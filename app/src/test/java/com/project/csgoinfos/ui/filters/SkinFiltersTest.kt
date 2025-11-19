package com.project.csgoinfos.ui.filters

import org.junit.Assert.*
import org.junit.Test

class SkinFiltersTest {
    @Test
    fun `test default values`() {
        val filters = SkinFilters()
        
        assertNull(filters.query)
        assertTrue(filters.rarities.isEmpty())
        assertTrue(filters.categories.isEmpty())
        assertTrue(filters.wears.isEmpty())
    }
    
    @Test
    fun `test defined values`() {
        val filters = SkinFilters(
            query = "AK-47",
            rarities = setOf("Covert", "Classified"),
            categories = setOf("Rifle"),
            wears = setOf("Factory New", "Minimal Wear")
        )
        
        assertEquals("AK-47", filters.query)
        assertEquals(2, filters.rarities.size)
        assertTrue(filters.rarities.contains("Covert"))
        assertTrue(filters.rarities.contains("Classified"))
        assertEquals(1, filters.categories.size)
        assertTrue(filters.categories.contains("Rifle"))
        assertEquals(2, filters.wears.size)
        assertTrue(filters.wears.contains("Factory New"))
        assertTrue(filters.wears.contains("Minimal Wear"))
    }
}

