package com.ead.lib.anime.core.core.system.extensions

fun String.delete(string: String) : String {
    return this.replace(string,"")
}