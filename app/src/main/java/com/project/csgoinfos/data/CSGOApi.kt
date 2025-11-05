package com.project.csgoinfos.data

import com.project.csgoinfos.model.Skin
import com.project.csgoinfos.model.Sticker
import com.project.csgoinfos.model.Highlight
import com.project.csgoinfos.model.Crate
import retrofit2.http.GET

interface CSGOApi {
    @GET("skins.json")
    suspend fun getSkins(): List<Skin>
    @GET("stickers.json")
    suspend fun getStickers(): List<Sticker>
    @GET("highlights.json")
    suspend fun getHighlights(): List<Highlight>
    @GET("crates.json")
    suspend fun getCrates(): List<Crate>
}
