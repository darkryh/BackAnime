@file:OptIn(ExperimentalEncodingApi::class)

package com.ead.lib.source.latanime

import com.ead.lib.anime.core.core.connection.ConnectionImpl
import com.ead.lib.anime.core.core.service.Service
import com.ead.lib.anime.core.models.directory.Anime
import com.ead.lib.anime.core.models.home.HomeEpisode
import com.ead.lib.anime.core.models.info.AnimeInfo
import com.ead.lib.anime.core.models.info.Episode
import com.ead.lib.source.latanime.core.LatAnimeEndPoints
import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class LatAnimeService(
    private val connection: ConnectionImpl = ConnectionImpl(
        baseUrl = BASE_URL
    )
) : Service {

    override val regexId: Regex = Regex("https://latanime\\.org/[^\\s'\"]*")

    override suspend fun getHome(): List<HomeEpisode> {
        return connection.request(LatAnimeEndPoints.home).let { response ->
            Jsoup.parse(response).body().let { document ->
                document.select(homeEpisodeStructure.classList).mapNotNull { element ->

                    val url = element.select(homeEpisodeStructure.url).attr("href")

                    val titleNumberText = element.select(homeEpisodeStructure.title).text()

                    val matches = regexTitleNumber.find(titleNumberText)
                    val episodeNumber = matches?.groupValues?.get(1)?.toIntOrNull() ?: -1
                    val title = matches?.groupValues?.get(2) ?: "Unknown Title"

                    val image = element.select(homeEpisodeStructure.image).attr("data-src")
                    val type = homeEpisodeStructure.type?.let { element.select(it).text() }
                    val lastUpdate =
                        homeEpisodeStructure.lastUpdate?.let { element.select(it).text().trim() }

                    val seo = url.substringAfterLast("/ver/").substringBefore("-episodio")

                    HomeEpisode(
                        seo = "$seo-episodio-$episodeNumber",
                        title = title,
                        number = episodeNumber,
                        type = type,
                        lastUpdate = lastUpdate,
                        image = image,
                        url = url
                    )
                }
            }
        }
    }

    override suspend fun getAnimeInfo(seo: String): AnimeInfo? {
        return connection.request(LatAnimeEndPoints.anime + seo).let { response ->
            Jsoup.parse(response).let { document ->
                AnimeInfo(
                    seo = seo,
                    title = document.select(animeInfoStructure.title).text(),
                    alternativeTitle = animeInfoStructure.alternativeTitle?.let {
                        document.select(it).text()
                            .ifBlank { null }
                    },
                    status = document.select(animeInfoStructure.status).text()
                        .ifBlank { return null },
                    coverImage = document.select(animeInfoStructure.coverImage).attr("content")
                        .ifBlank { "https://latanime.org/public/img/capblank.png" },
                    profileImage = animeInfoStructure.profileImage?.let {
                        document.select(it).attr("src")
                            .ifBlank { null }
                    },
                    episodes = document.select(animeInfoStructure.episodes)
                        .text()
                        .substringAfter("Episodios: ")
                        .toIntOrNull(),
                    release = document.select(animeInfoStructure.release)
                        .text()
                        .substringAfter("Estreno: ")
                        .ifBlank { null },  // Fallback si no hay fecha
                    synopsis = document.select(animeInfoStructure.synopsis).attr("content")
                        .ifBlank { null },  // Sinopsis opcional,
                    genres = document.select(animeInfoStructure.genres)
                        .map { it.text() }
                )
            }
        }
    }


    override suspend fun getSearch(query: String): List<Anime> {
        return connection.request(LatAnimeEndPoints.search + query).let { response ->
            Jsoup.parse(response).body().select(animeStructure.classList).map { element ->
                val url = element.select(animeStructure.url).attr("href")
                val matches = regexSeoOnly.find(url)
                val seo = matches?.groupValues?.get(1) ?: return@map null

                Anime(
                    seo = seo,
                    title = element.select(animeStructure.title).text().trim(),
                    type = element.select(animeStructure.type).text().trim(),
                    year = animeStructure.year?.let {
                        element.select(it)
                            .firstOrNull()?.text()?.trim()?.toIntOrNull()
                    },
                    image = element.select(animeStructure.image).attr("src"),  // Toma el 'src' de la imagen
                    url = url
                )
            }.requireNoNulls()
        }
    }

    override suspend fun getEpisodes(seo: String): List<Episode> = coroutineScope {
        val response = connection.request(LatAnimeEndPoints.anime + seo)
        val document = Jsoup.parse(response).body()

        val episodeElements = document.select("div.row a[href]:not([href*='genero'])")

        episodeElements.mapNotNull { element ->
            val url = element.attr("href")
            val image = element.selectFirst("img")?.attr("data-src") ?: ""  // Extraer imagen del <img>

            val titleText = element.text().trim()

            val episodeNumber = extractEpisodeNumber(titleText)

            Episode(
                seo = url.substringAfter("/ver/"),
                number = episodeNumber,
                image = image,
                url = url
            )
        }
    }

    private fun extractEpisodeNumber(title: String): Int {
        val match = regexEpisode.find(title)
        return match?.groupValues?.get(1)?.toIntOrNull() ?: -1
    }



    override suspend fun getWatchInfoEpisode(seo: String): List<String> {
        val response = connection.request(LatAnimeEndPoints.watch + seo)
        val document = Jsoup.parse(response).body()

        val playerLinks = document.select(playStructure.optionsClassList)
            .select("a[data-player]")
            .mapNotNull { element ->
                element.attr("data-player").takeIf {
                    it.isNotBlank()
                }?.let {
                    Base64.decode(
                        it
                    ).toString(Charsets.UTF_8)
                }
            }

        val downloadLinks = playStructure.downloadsClassList?.let {
            document.select(it)
                .select("a.direct-link")  // Busca elementos <a> con class direct-link
                .mapNotNull { element ->
                    element.attr("href").takeIf { data -> data.isNotBlank() }
                }
        } ?: emptyList()

        return (playerLinks + downloadLinks).distinct()
    }


    companion object {
        const val BASE_URL = "https://latanime.org/"
    }
}