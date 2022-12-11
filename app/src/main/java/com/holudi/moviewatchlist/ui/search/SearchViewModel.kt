package com.holudi.moviewatchlist.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holudi.moviewatchlist.data.Resource
import com.holudi.moviewatchlist.data.local.UserDataRepository
import com.holudi.moviewatchlist.data.manager.MediaRepository
import com.holudi.moviewatchlist.data.model.Media
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val userDataRepository: UserDataRepository
) :
    ViewModel() {

    val searchQuery = MutableStateFlow("")
    val searchInProgress = MutableStateFlow(false)

    val mediaFlow: Flow<Resource<List<MediaListItem>>?> = searchQuery
        .debounce(400)
        .flatMapLatest { searchText ->
            Timber.d("Searching for $searchText")
            searchInProgress.value = false
            if (searchText.isBlank() || searchText.length < 2)
                return@flatMapLatest flowOf(null)
            try {
                searchInProgress.value = true
                val searchResult = mediaRepository.searchMedia(searchText)
                searchInProgress.value = false
                flowOf(Resource.Success(searchResult))
            } catch (t: Throwable) {
                searchInProgress.value = false
                Timber.e(t)
                flowOf(Resource.Error(t))
            }
        }.combine(userDataRepository.ignoredIdsFlow()) { mediaResult, ignoredIds ->
            when (mediaResult) {
                is Resource.Error -> mediaResult
                is Resource.Success -> {
                    Resource.Success(mediaResult.data?.filter {
                        !ignoredIds.contains(it.imdbID)
                    })
                }
                null -> null
            }
        }.combine(userDataRepository.watchlistIdsFlow()) { mediaResult, watchlistIds ->
            when (mediaResult) {
                is Resource.Error -> Resource.Error(mediaResult.throwable)
                is Resource.Success -> {
                    Resource.Success(mediaResult.data?.map {
                        MediaListItem(it, watchlistIds.contains(it.imdbID))
                    } ?: emptyList<MediaListItem>())
                }
                null -> null
            }
        }.flowOn(Dispatchers.IO)

    fun toggleWatchlist(media: Media)  = viewModelScope.launch {
        userDataRepository.toggleWatchlist(media.imdbID)
    }

    fun ignoreMedia(media: Media) = viewModelScope.launch {
        userDataRepository.setMediaIgnored(media.imdbID)
    }
}
