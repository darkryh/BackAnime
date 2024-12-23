@file:Suppress("unused")

package com.ead.lib.anime.core

import com.ead.lib.anime.core.core.Supplier

object BackAnimeService {

    private var _supplier : Supplier? = null
    private val supplier : Supplier get() = _supplier ?: throw IllegalStateException("Config not initialized")

    fun onSupplier(supplier: Supplier) { _supplier = supplier }

    suspend fun getHome() = supplier.baseService.getHome()

    suspend fun getHomeDub() = supplier.dubService.getHome()

    suspend fun getSearch(query : String) = supplier.baseService.getSearch(query)

    suspend fun getSearchDub(query : String) = supplier.dubService.getSearch(query)

    suspend fun getAnimeInfo(seo : String) = supplier.baseService.getAnimeInfo(seo)

    suspend fun getAnimeInfoDub(seo : String) = supplier.dubService.getAnimeInfo(seo)

    suspend fun getEpisodes(seo : String) = supplier.baseService.getEpisodes(seo)

    suspend fun getEpisodesDub(seo : String) = supplier.dubService.getEpisodes(seo)

    suspend fun getWatchInfoEpisode(seo : String) = supplier.baseService.getWatchInfoEpisode(seo)

    suspend fun getWatchInfoEpisodeDub(seo : String) = supplier.dubService.getWatchInfoEpisode(seo)
}
