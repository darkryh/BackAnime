package com.ead.lib.anime.core.models.home

data class HomeEpisode(
    val seo : String,
    val title : String,
    val number : Int,
    val type : String?,
    val lastUpdate : String?,
    val image : String,
    val url : String
)