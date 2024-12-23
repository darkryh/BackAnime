package com.ead.lib.source.jkanime

import com.ead.lib.anime.core.models.structure.directory.AnimeStructure
import com.ead.lib.anime.core.models.structure.home.HomeEpisodeStructure
import com.ead.lib.anime.core.models.structure.info.AnimeInfoStructure
import com.ead.lib.anime.core.models.structure.info.EpisodeStructure

internal val regexHomeNumber = """jkanime\.net/.+?/(\d+)/?$""".toRegex()
internal val regexAjaxCode = """/ajax/votado/(\d+)/""".toRegex()

internal val homeEpisodeStructure = HomeEpisodeStructure(
    classList = "div.anime_programing a",
    title = "h5",
    number = "h6",
    type = null,
    lastUpdate = "span",
    image = "img",
    url = "href"
)

internal val animeInfoStructure = AnimeInfoStructure(
    title = "meta[property='og:title']",
    alternativeTitle = null,
    status = "li:contains(Estado)",
    coverImage = "meta[property='og:image']",
    profileImage = null,
    episodes = "li:contains(Episodios)",
    release = "li:contains(Emitido)",
    synopsis = "meta[property='og:description']",
    genres = "li:contains(Genero) a:not(:contains(Genero))"
)

internal val animeStructure = AnimeStructure(
    classList = "div.col-lg-2.col-md-6.col-sm-6",
    title = "div.anime__item__text h5 a",
    type = "div.anime__item__text ul li.anime",
    year = null,
    image = "div.anime__item__pic",
    url = "div.anime__item a[href]"
)

internal val episodeStructure = EpisodeStructure(
    classList = "div.anime_programing a",
    title = "h5",
    number = "h6",
    image = "",
    url = ""
)