package com.ead.lib.anime.core.core.connection

import okhttp3.FormBody
import okhttp3.Headers

interface Connection {
    suspend fun get(endpoint: String): String
    suspend fun post(endpoint: String, headers: Headers, body: FormBody): String
}