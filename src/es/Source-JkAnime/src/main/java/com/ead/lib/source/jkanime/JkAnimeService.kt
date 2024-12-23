@file:OptIn(ExperimentalEncodingApi::class)

package com.ead.lib.source.jkanime

import com.ead.lib.anime.core.core.connection.ConnectionImpl
import com.ead.lib.anime.core.core.service.Service
import com.ead.lib.anime.core.models.directory.Anime
import com.ead.lib.anime.core.models.home.HomeEpisode
import com.ead.lib.anime.core.models.info.AnimeInfo
import com.ead.lib.anime.core.models.info.Episode
import com.ead.lib.source.jkanime.core.JkAnimeEndPoints
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.json.JSONArray
import org.jsoup.Jsoup
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class JkAnimeService(
    private val connection: ConnectionImpl = ConnectionImpl(
        baseUrl = BASE_URL
    )
) : Service {

    override val regexId: Regex = Regex("https://jkanime\\.net/[^\\s'\"]*")

    override suspend fun getHome(): List<HomeEpisode> {
        return connection.request(JkAnimeEndPoints.home).let { response ->
            Jsoup.parse(response).body().let { document ->
                document.select(homeEpisodeStructure.classList).map { element ->
                    val url = element.attr(homeEpisodeStructure.url)

                    val matches = regexEpisode.find(url)

                    val seo = matches?.groupValues?.get(1) ?: return@map null
                    val episodeNumber = matches.groups[2]?.value

                    HomeEpisode(
                        seo = "$seo/$episodeNumber",
                        title = element.select(homeEpisodeStructure.title).text(),
                        number = episodeNumber?.toIntOrNull() ?: -1,
                        type = null,
                        lastUpdate = element.select(homeEpisodeStructure.lastUpdate ?: "").text().trim(),
                        image = element.select(homeEpisodeStructure.image).attr("src"),
                        url = url
                    )
                }
            }.requireNoNulls()
        }
    }

    override suspend fun getAnimeInfo(seo: String): AnimeInfo? {
        return connection.request(JkAnimeEndPoints.anime + seo).let { response ->
            Jsoup.parse(response).let { document ->
                AnimeInfo(
                    seo = seo,
                    title = document.select(animeInfoStructure.title).attr("content"),
                    alternativeTitle = null,
                    status = document.select(animeInfoStructure.status).text(),
                    coverImage = document.select(animeInfoStructure.coverImage).attr("content"),
                    profileImage = null,
                    episodes = document.select(animeInfoStructure.episodes)
                        .first()?.ownText()?.trim()?.toIntOrNull(),
                    release = document.select(animeInfoStructure.release)
                        .first()?.ownText()?.trim() ?: return null,
                    synopsis = document.select(animeInfoStructure.synopsis).attr("content"),
                    genres = document.select(animeInfoStructure.genres).map { element -> element.text() }
                )
            }
        }
    }

    override suspend fun getSearch(query: String): List<Anime> {
        return connection.request(JkAnimeEndPoints.search + query).let { response ->
            Jsoup.parse(response).body().select(animeStructure.classList).map { element ->
                val url = element.select(animeStructure.url).attr("href")
                val matches = regexSeoOnly.find(url)
                val seo = matches?.groupValues?.get(1) ?: return@map null

                Anime(
                    seo = seo,
                    title = element.select(animeStructure.title).text(),
                    type = element.select(animeStructure.type).text(),
                    year = null,
                    image = element.select(animeStructure.image).attr("data-setbg"),
                    url = url
                )
            }.requireNoNulls()
        }
    }

    override suspend fun getEpisodes(seo: String): List<Episode> = coroutineScope {
        val response = connection.request(JkAnimeEndPoints.anime + seo)
        val document = Jsoup.parse(response).body()

        val ajaxCode = document.select("div#guardar-anime[data-anime]").attr("data-anime")

        val lastEpisode = JSONArray(
            connection.getRequest("${BASE_URL}/ajax/last_episode/$ajaxCode")
        ).getJSONObject(0).getString("number").toInt()


        val totalPages = (lastEpisode / EP_PER_PAGINATION) + 1

        val episodes = mutableListOf<Episode>()

        val deferredResults = (1..totalPages).chunked(2).map { pageChunk ->
            async {
                pageChunk.flatMap { page ->
                    fetchEpisodesForPage(ajaxCode, page, seo)
                }
            }
        }

        val results = deferredResults.awaitAll()
        episodes.addAll(results.flatten())

        episodes
    }

    override suspend fun getWatchInfoEpisode(seo: String): List<String> {
        return connection.request(JkAnimeEndPoints.watch + seo).let { response ->
            regexWatch.findAll(response).map { matchResult -> Base64.decode(matchResult.groupValues[1]).toString(Charsets.UTF_8) }
                .requireNoNulls()
                .toList()
        }
    }

    private suspend fun fetchEpisodesForPage(ajaxCode: String, page: Int, seo: String): List<Episode> {
        val episodes = mutableListOf<Episode>()

        val items = JSONArray(
            connection.getRequest("$BASE_URL/ajax/pagination_episodes/$ajaxCode/$page")
        )

        for (i in 0 until items.length()) {
            val jsonObject = items.getJSONObject(i)
            val number = jsonObject.getInt("number")
            val url = "$BASE_URL$seo/$number"
            val matches = regexEpisode.find(url)

            val seoMatch = matches?.groupValues?.get(1) ?: continue

            episodes.add(
                Episode(
                    seo = "$seoMatch/$number",
                    number = number,
                    image = IMAGE_REFERENCE + jsonObject.getString("image"),
                    url = url
                )
            )
        }
        return episodes
    }

    companion object {
        const val BASE_URL = "https://jkanime.net/"
        const val IMAGE_REFERENCE = "https://cdn.jkdesu.com/assets/images/animes/video/image_thumb/"
        const val EP_PER_PAGINATION = 12
    }
}