package com.ead.lib.anime.core.models.structure.info

data class AnimeInfoStructure(
    val title : String,
    val alternativeTitle : String?,
    val status : String,
    val coverImage : String,
    val profileImage : String?,
    val episodes : String,
    val release : String,
    val synopsis : String,
    val genres : String
)