package com.ead.lib.anime.core.models.directory

data class Anime(
    val title : String,
    val type : String,
    val year : Int?,
    val image : String,
    val url : String
)