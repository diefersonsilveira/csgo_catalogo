package com.project.csgoinfos.model

import com.google.gson.annotations.SerializedName

data class Skin(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("weapon") val weapon: Weapon?,
    @SerializedName("category") val category: Category?,
    @SerializedName("pattern") val pattern: Pattern?,
    @SerializedName("min_float") val minFloat: Double?,
    @SerializedName("max_float") val maxFloat: Double?,
    @SerializedName("rarity") val rarity: Rarity?,
    @SerializedName("stattrak") val stattrak: Boolean?,
    @SerializedName("souvenir") val souvenir: Boolean?,
    @SerializedName("paint_index") val paintIndex: String?,
    @SerializedName("team") val team: Team?,
    @SerializedName("legacy_model") val legacyModel: Boolean?,
    @SerializedName("wears") val wears: List<Wear>?,
    @SerializedName("image") val image: String?
)
