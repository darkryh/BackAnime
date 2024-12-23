package com.ead.lib.anime.core.core.client

import com.ead.lib.anime.core.core.connection.ConnectionManager.Companion.BASE_URL
import com.ead.lib.anime.core.core.cookiejar.BackAnimeJar
import okhttp3.OkHttpClient
import okhttp3.Request

open class BackAnimeClient {
    protected open val httpClient = OkHttpClient.Builder()
        .cookieJar(BackAnimeJar)
        .followRedirects(true)
        .followSslRedirects(true)
        .build()

    protected open val requestHttpClient = Request.Builder()
        .url(BASE_URL)
        .build()
}