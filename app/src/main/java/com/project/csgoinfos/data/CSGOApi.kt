package com.project.csgoinfos.data

import com.project.csgoinfos.model.Skin
import com.project.csgoinfos.model.Sticker
import retrofit2.http.GET

interface CSGOApi {
    @GET("skins.json")
    suspend fun getSkins(): List<Skin>
    @GET("stickers.json")
    suspend fun getStickers(): List<Sticker>
}
