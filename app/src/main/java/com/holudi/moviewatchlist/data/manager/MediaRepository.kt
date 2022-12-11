package com.holudi.moviewatchlist.data.manager

import com.holudi.moviewatchlist.data.model.Media
import com.holudi.moviewatchlist.data.remote.omdb.OmdbApi
import javax.inject.Inject

class MediaRepository @Inject constructor(private val omdbApi: OmdbApi) {

    private val apiKey = "bc890fee"

    suspend fun searchMedia(searchText: String) = omdbApi.search(apiKey, searchText).searchResults

    suspend fun getMediaDetails(list: List<String>): List<Media> {
        val details = mutableListOf<Media>()
        list.forEach {
            try {
                val media = omdbApi.getMedia(apiKey, it)
                details.add(media)
            } catch (e: Exception) {
                throw e
            }
        }
        return details
    }
}