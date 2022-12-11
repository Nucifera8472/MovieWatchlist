package com.holudi.moviewatchlist.ui.watchlist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holudi.moviewatchlist.data.Resource
import com.holudi.moviewatchlist.data.local.UserDataRepository
import com.holudi.moviewatchlist.data.manager.MediaRepository
import com.holudi.moviewatchlist.data.model.Media
import com.holudi.moviewatchlist.ui.common.MediaListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val userDataRepository: UserDataRepository
) :
    ViewModel() {

    var selectedItem = mutableStateOf<Media?>(null)
    val fetchInProgress = MutableStateFlow(false)

    val mediaFlow: Flow<Resource<List<MediaListItem>>> = userDataRepository.watchlistIdsFlow()
        .flatMapLatest { watchlistIds ->
            fetchInProgress.value = false
            if (watchlistIds.isEmpty())
                return@flatMapLatest flowOf(Resource.Success(emptyList()))
            try {
                fetchInProgress.value = true
                val watchlist = mediaRepository.getMediaDetails(watchlistIds.toList())
                fetchInProgress.value = false
                flowOf(Resource.Success(watchlist.map { MediaListItem(it, true) }))
            } catch (t: Throwable) {
                fetchInProgress.value = false
                Timber.e(t)
                flowOf(Resource.Error(t))
            }
        }.flowOn(Dispatchers.IO)

    fun toggleWatchlist(media: Media) = viewModelScope.launch {
        userDataRepository.removeFromWatchlist(media.imdbID)
    }

    fun ignoreMedia(media: Media) = viewModelScope.launch {
        userDataRepository.removeFromWatchlist(media.imdbID)
        userDataRepository.setMediaIgnored(media.imdbID)
    }
}
