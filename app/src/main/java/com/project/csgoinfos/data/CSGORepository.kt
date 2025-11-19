package com.project.csgoinfos.data

import com.project.csgoinfos.model.Skin
import com.project.csgoinfos.model.Sticker
import com.project.csgoinfos.model.Highlight
import com.project.csgoinfos.model.Crate
import com.project.csgoinfos.model.Agent

class CSGORepository {
    private suspend fun <T> tryPtBrThenEn(block: suspend (CSGOApi) -> T): T {
        return try {
            block(RetrofitClient.api("pt-BR"))
        } catch (_: Exception) {
            block(RetrofitClient.api("en"))
        }
    }

    suspend fun fetchSkins(): List<Skin> = tryPtBrThenEn { it.getSkins() }
    suspend fun fetchStickers(): List<Sticker> = tryPtBrThenEn { it.getStickers() }
    suspend fun fetchHighlights(): List<Highlight> = tryPtBrThenEn { it.getHighlights() }
    suspend fun fetchCrates(): List<Crate> = tryPtBrThenEn { it.getCrates() }
    suspend fun fetchAgents(): List<Agent> = tryPtBrThenEn { it.getAgents() }
}

