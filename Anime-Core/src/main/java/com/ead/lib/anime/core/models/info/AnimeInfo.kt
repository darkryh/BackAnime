package com.ead.lib.anime.core.models.info

data class AnimeInfo(
    val seo : String,
    val title : String,
    val alternativeTitle : String?,
    val status : String,
    val coverImage : String,
    val profileImage : String?,
    val episodes : Int?,
    val release : String?,
    val synopsis : String?,
    val genres : List<String>
)