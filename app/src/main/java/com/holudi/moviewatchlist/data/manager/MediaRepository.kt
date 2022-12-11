package com.holudi.moviewatchlist.data.manager

import com.holudi.moviewatchlist.data.remote.omdb.OmdbApi
import javax.inject.Inject

class MediaRepository @Inject constructor(private val omdbApi: OmdbApi) {

    private val apiKey = "bc890fee"

    suspend fun searchMedia(searchText: String) = omdbApi.search(apiKey, searchText).searchResults
}