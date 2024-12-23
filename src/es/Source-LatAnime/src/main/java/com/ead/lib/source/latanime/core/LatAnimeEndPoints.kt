package com.ead.lib.source.latanime.core

import com.ead.lib.anime.core.core.Properties.EMPTY_STRING
import com.ead.lib.anime.core.core.endpoint.EndpointProvider

object LatAnimeEndPoints : EndpointProvider {
    override val home: String = EMPTY_STRING
    override val anime: String = "anime/"
    override val search: String = "buscar?q="
    override val episode: String = "anime/"
    override val watch: String = "ver/"
}