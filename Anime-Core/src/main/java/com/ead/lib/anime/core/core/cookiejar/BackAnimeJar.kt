package com.ead.lib.anime.core.core.cookiejar

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

object BackAnimeJar : CookieJar {
    private val cookieStore = HashMap<String, List<Cookie>>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
    }
}