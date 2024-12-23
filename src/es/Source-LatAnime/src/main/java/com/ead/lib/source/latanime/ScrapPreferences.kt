package com.ead.lib.source.latanime

import com.ead.lib.anime.core.models.structure.directory.AnimeStructure
import com.ead.lib.anime.core.models.structure.home.HomeEpisodeStructure
import com.ead.lib.anime.core.models.structure.info.AnimeInfoStructure
import com.ead.lib.anime.core.models.structure.info.EpisodeStructure
import com.ead.lib.anime.core.models.structure.player.PlayerStructure

val regexEpisode = Regex("(?:Capitulo|Episodio)\\s*(\\d+)", RegexOption.IGNORE_CASE)
val regexSeoOnly = """/anime/([^/]+)""".toRegex()
val regexTitleNumber = """(\d+)\s*-\s*(.*)""".toRegex()

internal val homeEpisodeStructure = HomeEpisodeStructure(
    classList = "div.col-6.col-md-6.col-lg-3.mb-3",
    title = "h2",
    number = "h2",
    type = "div.info_cap span",
    lastUpdate = "span.span-tiempo",
    image = "img",
    url = "a[href]"
)

internal val animeInfoStructure = AnimeInfoStructure(
    title = "div.col-lg-9.col-md-8 h2",
    alternativeTitle = "div.col-lg-9.col-md-8 h3.fs-6",
    status = "div.my-2 button.btn-estado",
    coverImage = "meta[property='og:image']",
    profileImage = "div.serieimgficha img",
    episodes = "div.col-lg-9.col-md-8 p:contains(Episodios)",
    release = "div.col-lg-9.col-md-8 div.my-2 span.span-tiempo",
    synopsis = "meta[property='og:description']",
    genres = "div.col-lg-9.col-md-8 a[href*='/genero/']"
)

internal val animeStructure = AnimeStructure(
    classList = "div.col-md-4.col-lg-3.col-xl-2.col-6.my-3",
    title = "div.seriedetails h3",
    type = "div.seriedetails span:first-child",
    year = "div.seriedetails span svg + text",  // Selector para el año
    image = "div.serieimg img",  // Elimina 'data-setbg' y usa directamente 'src'
    url = "a[href]"
)

internal val episodeStructure = EpisodeStructure(
    classList = "div.row div a[href]:not([href*='genero'])",  // Excluye los <a> con 'genero' en href
    title = "div",
    image = "img",
    url = "a[href]:not([href*='genero'])",
    number = "div"// Aplica el mismo filtro para evitar géneros
)


internal val playStructure = PlayerStructure(
    optionsClassList = "ul.cap_repro li",  // Selector para reproductores
    downloadsClassList = "div.descarga2 div"  // Selector para enlaces de descarga
)
