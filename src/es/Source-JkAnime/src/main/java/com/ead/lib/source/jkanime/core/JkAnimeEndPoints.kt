package com.ead.lib.source.jkanime.core

import com.ead.lib.anime.core.core.Properties.EMPTY_STRING
import com.ead.lib.anime.core.core.endpoint.EndpointProvider

object JkAnimeEndPoints : EndpointProvider {
    override val home: String = EMPTY_STRING
    override val anime: String = EMPTY_STRING
    override val search: String = "buscar/"
    override val episode: String = EMPTY_STRING
    override val watch: String = EMPTY_STRING
}