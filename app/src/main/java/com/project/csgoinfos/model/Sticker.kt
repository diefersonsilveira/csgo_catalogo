package com.project.csgoinfos.model

import com.google.gson.annotations.SerializedName

data class Sticker(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("rarity") val rarity: Rarity?,
    @SerializedName("crates") val crates: List<CrateRef>?,
    @SerializedName("tournament_event") val tournamentEvent: String?,
    @SerializedName("tournament_team") val tournamentTeam: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("effect") val effect: String?,
    @SerializedName("image") val image: String?
)
