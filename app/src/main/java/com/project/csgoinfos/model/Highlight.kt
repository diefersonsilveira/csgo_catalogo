package com.project.csgoinfos.model

import com.google.gson.annotations.SerializedName

data class Highlight(
    @SerializedName("id") val id: String,
    @SerializedName("name") val title: String?,
    @SerializedName("player") val player: String?,
    @SerializedName("team") val team: String?,
    @SerializedName("event") val event: String?,
    @SerializedName("map") val map: String?,
    @SerializedName("video") val video: String?,
    @SerializedName("thumbnail") val thumbnail: String?
)

