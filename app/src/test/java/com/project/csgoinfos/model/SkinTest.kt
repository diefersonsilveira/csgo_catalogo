package com.project.csgoinfos.model

import org.junit.Assert.*
import org.junit.Test

class SkinTest {
    @Test
    fun `test skin creation with valid data`() {
        val rarity = Rarity("1", "Consumer Grade", "#B0C3D9")
        val weapon = Weapon("1", 1, "AK-47")
        val category = Category("1", "Rifle")
        val wear = Wear("1", "Factory New")
        
        val skin = Skin(
            id = "skin_001",
            name = "AK-47 | Redline",
            description = "A classic AK-47 skin",
            weapon = weapon,
            category = category,
            pattern = null,
            minFloat = 0.0,
            maxFloat = 0.15,
            rarity = rarity,
            stattrak = false,
            souvenir = false,
            paintIndex = "123",
            team = null,
            legacyModel = false,
            wears = listOf(wear),
            image = "https://example.com/skin.jpg"
        )
        
        assertEquals("skin_001", skin.id)
        assertEquals("AK-47 | Redline", skin.name)
        assertEquals("A classic AK-47 skin", skin.description)
        assertEquals(weapon, skin.weapon)
        assertEquals(category, skin.category)
        assertEquals(rarity, skin.rarity)
        assertEquals(0.0, skin.minFloat, 0.001)
        assertEquals(0.15, skin.maxFloat, 0.001)
        assertFalse(skin.stattrak ?: true)
        assertFalse(skin.souvenir ?: true)
        assertNotNull(skin.wears)
        assertEquals(1, skin.wears?.size)
    }
}

