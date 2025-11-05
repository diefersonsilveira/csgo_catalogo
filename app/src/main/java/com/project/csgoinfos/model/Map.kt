package com.project.csgoinfos.model

enum class Map(val mapName: String) {
    DE_INFERNO("Inferno"),
    DE_MIRAGE("Mirage"),
    DE_NUKE("Nuke"),
    DE_OVERPASS("Overpass"),
    DE_VERTIGO("Vertigo"),
    DE_ANCIENT("Ancient"),
    DE_ANUBIS("Anubis"),
    DE_DUST2("Dust II"),
    DE_TRAIN("Train"),
    DE_CACHE("Cache"),
    DE_COBBLESTONE("Cobblestone");

    companion object {
        fun getMapName(mapCode: String): String {
            return entries.find { it.name.equals(mapCode, ignoreCase = true) }?.mapName ?: mapCode
        }
    }
}
