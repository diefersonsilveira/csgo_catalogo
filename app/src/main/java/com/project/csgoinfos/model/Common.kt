package com.project.csgoinfos.model

import com.google.gson.annotations.SerializedName

data class Rarity(@SerializedName("id") val id: String?, @SerializedName("name") val name: String?, @SerializedName("color") val color: String?)
data class Weapon(@SerializedName("id") val id: String?, @SerializedName("weapon_id") val weaponId: Int?, @SerializedName("name") val name: String?)
data class Category(@SerializedName("id") val id: String?, @SerializedName("name") val name: String?)
data class Pattern(@SerializedName("id") val id: String?, @SerializedName("name") val name: String?)
data class Team(@SerializedName("id") val id: String?, @SerializedName("name") val name: String?)
data class Wear(@SerializedName("id") val id: String?, @SerializedName("name") val name: String?)
data class CrateRef(@SerializedName("id") val id: String?, @SerializedName("name") val name: String?, @SerializedName("image") val image: String?)
