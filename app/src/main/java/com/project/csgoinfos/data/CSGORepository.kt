package com.project.csgoinfos.data

import com.project.csgoinfos.model.Skin
import com.project.csgoinfos.model.Sticker

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
}
