package com.project.csgoinfos.ui.filters

data class SkinFilters(
    val query: String? = null,
    val rarities: Set<String> = emptySet(),
    val categories: Set<String> = emptySet(),
    val wears: Set<String> = emptySet()
)

data class StickerFilters(
    val query: String? = null,
    val rarities: Set<String> = emptySet(),
    val types: Set<String> = emptySet(),
    val events: Set<String> = emptySet()
)
