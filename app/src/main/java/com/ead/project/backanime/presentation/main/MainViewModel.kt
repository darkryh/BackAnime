package com.ead.project.backanime.presentation.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ead.lib.anime.core.BackAnimeService
import com.ead.lib.anime.core.core.Supplier
import com.ead.lib.anime.core.models.directory.Anime
import com.ead.lib.anime.core.models.home.HomeEpisode
import com.ead.lib.anime.core.models.info.AnimeInfo
import com.ead.lib.anime.core.models.info.Episode
import com.ead.lib.source.jkanime.JkAnimeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val _homeEpisodes: MutableState<List<HomeEpisode>> = mutableStateOf(emptyList())
    val homeEpisodes: State<List<HomeEpisode>> = _homeEpisodes

    private val _searchAnimes: MutableState<List<Anime>> = mutableStateOf(emptyList())
    val searchAnimes: State<List<Anime>> = _searchAnimes

    private val _animeInfo: MutableState<AnimeInfo?> = mutableStateOf(null)
    val animeInfo: State<AnimeInfo?> = _animeInfo

    private val _episodes: MutableState<List<Episode>> = mutableStateOf(emptyList())
    val episodes: State<List<Episode>> = _episodes

    private val _watchInfoEpisodes : MutableState<List<String>> = mutableStateOf(emptyList())
    val watchInfoEpisodes : State<List<String>> = _watchInfoEpisodes

    init {
        BackAnimeService.onSupplier(
            Supplier(
                baseService = JkAnimeService(),
                dubService = JkAnimeService()
            )
        )
        getHome()
    }

    fun getHome() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _homeEpisodes.value = BackAnimeService.getHome()
            }
        }
    }

    fun getSearch(query : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _searchAnimes.value = BackAnimeService.getSearch(query)
            }
        }
    }

    fun getAnimeInfo(seo : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _animeInfo.value = BackAnimeService.getAnimeInfo(seo)
            }
        }
    }

    fun getEpisodes(seo : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _episodes.value = BackAnimeService.getEpisodes(seo)
            }
        }
    }

    fun getWatchInfoEpisode(seo : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _watchInfoEpisodes.value = BackAnimeService.getWatchInfoEpisode(seo)
            }
        }
    }
}