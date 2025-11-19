package com.project.csgoinfos.model

import org.junit.Assert.*
import org.junit.Test

class StickerTest {
    @Test
    fun `test sticker creation with valid data`() {
        val rarity = Rarity("1", "High Grade", "#4B69FF")
        val crateRef = CrateRef("1", "Sticker Capsule", "https://example.com/crate.jpg")
        
        val sticker = Sticker(
            id = "sticker_001",
            name = "Team Liquid | Berlin 2019",
            description = "Team Liquid sticker from Berlin Major",
            rarity = rarity,
            crates = listOf(crateRef),
            tournamentEvent = "Berlin Major 2019",
            tournamentTeam = "Team Liquid",
            type = "Holo",
            effect = "Holographic",
            image = "https://example.com/sticker.jpg"
        )
        
        assertEquals("sticker_001", sticker.id)
        assertEquals("Team Liquid | Berlin 2019", sticker.name)
        assertEquals("Team Liquid sticker from Berlin Major", sticker.description)
        assertEquals(rarity, sticker.rarity)
        assertNotNull(sticker.crates)
        assertEquals(1, sticker.crates?.size)
        assertEquals("Berlin Major 2019", sticker.tournamentEvent)
        assertEquals("Team Liquid", sticker.tournamentTeam)
        assertEquals("Holo", sticker.type)
        assertEquals("Holographic", sticker.effect)
    }
}

