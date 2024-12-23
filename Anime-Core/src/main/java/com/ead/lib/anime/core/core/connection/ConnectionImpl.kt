package com.ead.lib.anime.core.core.connection

import com.ead.lib.anime.core.core.client.BackAnimeClient
import com.ead.lib.anime.core.core.system.extensions.await

class ConnectionImpl(
    private val isDebug: Boolean = false,
    private val baseUrl: String = BASE_URL
) : BackAnimeClient() {

    private val client = httpClient
    private val requestClient = requestHttpClient

    suspend fun request(endPoint: String): String {
        runCatching {
            val response = client
                .newCall(
                    requestClient
                        .newBuilder()
                        .url(baseUrl + endPoint)
                        .build()
                )
                .await()

            val status = response.code

            val body = response.body

            if (status !in 200..299) {
                if (status in 500..599) {
                    val ex =
                        Exception("An internal server error has occurred, code: $status")
                    if (isDebug) throw ex else exceptionHandler(ex)
                } else {
                    val ex = Exception(
                        "Service API returns code $status and body $body",
                    )

                    if (isDebug) throw ex
                    else exceptionHandler(ex)
                }
            }

            return body?.string() ?: throw Exception("Empty body")
        }.getOrElse { throwable -> throw throwable }
    }

    suspend fun getRequest(url : String): String {
        runCatching {
            val response = client
                .newCall(
                    requestClient
                        .newBuilder()
                        .url(url)
                        .get()
                        .build()
                )
                .await()

            val status = response.code

            val responseBody = response.body

            if (status !in 200..299) {
                if (status in 500..599) {
                    val ex =
                        Exception("An internal server error has occurred, code: $status")
                    if (isDebug) throw ex else exceptionHandler(ex)
                } else {
                    val ex = Exception(
                        "Server API returns code $status and body $responseBody",
                    )

                    if (isDebug) throw ex
                    else exceptionHandler(ex)
                }
            }

            return responseBody?.string() ?: throw Exception("Empty body")
        }.getOrElse { throwable -> throw throwable }
    }

    private fun exceptionHandler(ex: Exception, message: String? = null) {
        if (message.isNullOrEmpty()) println("Something went wrong! Exception: ${ex.localizedMessage}")
        else println("Something went wrong! Exception: ${ex.localizedMessage}")
    }

    companion object {
        const val BASE_URL = "https://www.google.com"
    }
}