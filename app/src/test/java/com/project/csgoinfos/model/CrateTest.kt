package com.project.csgoinfos.model

import org.junit.Assert.*
import org.junit.Test

class CrateTest {
    @Test
    fun `test crate creation with valid data`() {
        val rarity = Rarity("1", "Base Grade", "#8B8989")
        
        val crate = Crate(
            id = "crate_001",
            name = "Operation Broken Fang Case",
            description = "A case containing various weapon skins",
            rarity = rarity,
            image = "https://example.com/crate.jpg"
        )
        
        assertEquals("crate_001", crate.id)
        assertEquals("Operation Broken Fang Case", crate.name)
        assertEquals("A case containing various weapon skins", crate.description)
        assertEquals(rarity, crate.rarity)
        assertEquals("https://example.com/crate.jpg", crate.image)
    }
}

