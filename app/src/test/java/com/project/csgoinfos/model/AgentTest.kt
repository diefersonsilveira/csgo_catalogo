package com.project.csgoinfos.model

import org.junit.Assert.*
import org.junit.Test

class AgentTest {
    @Test
    fun `test agent creation with all fields`() {
        val agent = Agent(
            id = "agent_001",
            name = "SAS",
            description = "Special Air Service agent",
            image = "https://example.com/agent.jpg"
        )
        
        assertEquals("agent_001", agent.id)
        assertEquals("SAS", agent.name)
        assertEquals("Special Air Service agent", agent.description)
        assertEquals("https://example.com/agent.jpg", agent.image)
    }
}

