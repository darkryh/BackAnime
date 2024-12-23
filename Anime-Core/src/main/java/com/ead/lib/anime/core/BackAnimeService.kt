@file:Suppress("unused")

package com.ead.lib.anime.core

import com.ead.lib.anime.core.core.service.Service

object BackAnimeService {

    lateinit var service : Service

    suspend fun getHome() = service.getHome()
}
