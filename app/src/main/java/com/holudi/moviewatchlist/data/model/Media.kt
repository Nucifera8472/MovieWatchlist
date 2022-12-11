package com.holudi.moviewatchlist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Media(
    val imdbID: String,
    @SerialName("Title")
    val title: String,
    @SerialName("Year")
    val year: String,
    @SerialName("Poster")
    val poster: String,
    val imdbRating: String? = null // not returned in Search Results
)