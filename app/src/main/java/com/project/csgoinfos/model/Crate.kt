package com.project.csgoinfos.model

import com.google.gson.annotations.SerializedName

data class Crate(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("rarity") val rarity: Rarity?,
    @SerializedName("image") val image: String?
)


