package com.project.csgoinfos.model

import org.junit.Assert.*
import org.junit.Test

class RarityTest {
    @Test
    fun `test rarity creation with all fields`() {
        val rarity = Rarity(
            id = "rarity_001",
            name = "Covert",
            color = "#EB4B4B"
        )
        
        assertEquals("rarity_001", rarity.id)
        assertEquals("Covert", rarity.name)
        assertEquals("#EB4B4B", rarity.color)
    }
}

