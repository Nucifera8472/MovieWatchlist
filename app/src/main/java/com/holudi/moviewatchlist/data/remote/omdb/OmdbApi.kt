package com.holudi.moviewatchlist.data.remote.omdb

import com.holudi.moviewatchlist.data.model.Media
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    @GET(".")
    suspend fun search(@Query("apikey") apikey: String, @Query("s") searchText: String): SearchResponse

    @GET(".")
    suspend fun getMedia(@Query("apikey") apikey: String, @Query("i") imdbID: String): Media


    @Serializable
    data class SearchResponse(
        @SerialName("Search")
        var searchResults: List<Media>? = null
    )

}

