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

data class HighlightFilters(
    val query: String? = null,
    val players: Set<String> = emptySet(),
    val teams: Set<String> = emptySet(),
    val events: Set<String> = emptySet(),
    val maps: Set<String> = emptySet()
)

data class CrateFilters(
    val query: String? = null,
    val rarities: Set<String> = emptySet()
)
data class AgentFilters(
    val query: String? = null,
    val rarities: Set<String> = emptySet(),
    val teams: Set<String> = emptySet()
)
