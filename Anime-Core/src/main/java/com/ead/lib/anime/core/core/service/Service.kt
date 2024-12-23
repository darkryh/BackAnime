package com.ead.lib.anime.core.core.service

import com.ead.lib.anime.core.models.directory.Anime
import com.ead.lib.anime.core.models.home.HomeEpisode
import com.ead.lib.anime.core.models.info.AnimeInfo
import com.ead.lib.anime.core.models.info.Episode

interface Service {
    val regexId : Regex

    suspend fun getHome(): List<HomeEpisode>
    suspend fun getAnimeInfo(seo : String) : AnimeInfo?
    suspend fun getSearch(query : String) : List<Anime>
    suspend fun getEpisodes(seo : String) : List<Episode>
    suspend fun getWatchInfoEpisode(seo : String) : List<String>
}