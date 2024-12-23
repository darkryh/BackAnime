package com.ead.lib.anime.core.core

import com.ead.lib.anime.core.core.service.Service

data class Supplier(
    val baseService: Service,
    val dubService: Service
)