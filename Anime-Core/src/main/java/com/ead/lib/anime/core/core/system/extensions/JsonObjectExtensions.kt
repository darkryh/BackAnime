package com.ead.lib.anime.core.core.system.extensions

import org.json.JSONArray
import org.json.JSONObject


fun JSONArray.toJsonObject(): JSONObject {
    return getJSONObject(0)
}

/*
fun JSONObject.toHomeStructure() : HomeStructure {
    val homeChapterStructure = getJSONObject("homeChapterStructure")
    val homeAnimeStructure = getJSONObject("homeAnimeStructure")

    return HomeStructure(
        homeEpisodeStructure = HomeEpisodeStructure(
            classList = homeChapterStructure.getString("classList"),
            title = homeChapterStructure.getString("title"),
            number = homeChapterStructure.getString("number"),
            type = homeChapterStructure.getString("type"),
            image = homeChapterStructure.getString("image"),
            url = homeChapterStructure.getString("url")
        ),
        homeAnimeStructure = HomeAnimeStructure(
            classList = homeAnimeStructure.getString("classList"),
            title = homeAnimeStructure.getString("title"),
            type = homeAnimeStructure.getString("type"),
            image = homeAnimeStructure.getString("image"),
            url = homeAnimeStructure.getString("url")
        )
    )
}

fun JSONObject.toAnimeInfoStructure() : AnimeInfoStructure {
    return AnimeInfoStructure(
        title = getString("title"),
        alternativeTitle = getString("alternativeTitle"),
        status = getString("status"),
        coverImage = getString("coverImage"),
        profileImage = getString("profileImage"),
        release = getString("release"),
        synopsis = getString("synopsis"),
        genres = getString("genres")
    )
}

fun JSONObject.toAnimeStructure() : AnimeStructure {
    return AnimeStructure(
        classList = getString("classList"),
        title = getString("title"),
        type = getString("type"),
        year = getString("year"),
        image = getString("image"),
        url = getString("url")
    )
}

fun JSONObject.toPlayerStructure() : PlayerStructure {
    return PlayerStructure(
        optionsClassList = getString("optionsClassList"),
        optionAttribute = getString("optionAttribute"),
        downloadsClassList = getString("downloadsClassList"),
    )
}

fun JSONArray.toEpisodeList(): List<Episode> {
    val episodes = mutableListOf<Episode>()

    for (i in 0 until length()) {

        val jsonObject: JSONObject = getJSONObject(i)

        val number = jsonObject.getInt("episodio")
        val image = jsonObject.getString("thumb")
        val url = jsonObject.getString("url")

        episodes.add(
            Episode(
                number = number,
                image = image,
                url = url
            )
        )

    }
    return episodes
}*/
