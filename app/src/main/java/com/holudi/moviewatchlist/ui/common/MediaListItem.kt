package com.holudi.moviewatchlist.ui.common

import com.holudi.moviewatchlist.data.model.Media

data class MediaListItem(
    val media: Media,
    val isOnWatchlist: Boolean,
)