package com.project.csgoinfos.model

import org.junit.Assert.*
import org.junit.Test

class HighlightTest {
    @Test
    fun `test highlight creation with valid data`() {
        val highlight = Highlight(
            id = "highlight_001",
            title = "s1mple 1v4 Clutch",
            player = "s1mple",
            team = "NAVI",
            event = "IEM Katowice 2021",
            map = "Dust2",
            video = "https://example.com/video.mp4",
            thumbnail = "https://example.com/thumbnail.jpg"
        )
        
        assertEquals("highlight_001", highlight.id)
        assertEquals("s1mple 1v4 Clutch", highlight.title)
        assertEquals("s1mple", highlight.player)
        assertEquals("NAVI", highlight.team)
        assertEquals("IEM Katowice 2021", highlight.event)
        assertEquals("Dust2", highlight.map)
        assertEquals("https://example.com/video.mp4", highlight.video)
        assertEquals("https://example.com/thumbnail.jpg", highlight.thumbnail)
    }
}

