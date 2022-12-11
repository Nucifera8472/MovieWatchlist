package com.holudi.moviewatchlist.injection

import kotlinx.serialization.json.Json

val JSON = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}