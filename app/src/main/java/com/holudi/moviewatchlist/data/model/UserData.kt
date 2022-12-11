package com.holudi.moviewatchlist.data.model


data class UserData(
    val ignoredIds: Set<String>,
    val watchlistIds: Set<String>,
)