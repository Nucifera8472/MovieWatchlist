package com.holudi.moviewatchlist.ui.search

import androidx.lifecycle.ViewModel
import com.holudi.moviewatchlist.data.Resource
import com.holudi.moviewatchlist.data.manager.MediaRepository
import com.holudi.moviewatchlist.data.model.Media
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(private val mediaRepository: MediaRepository) :
    ViewModel() {

    val searchQuery = MutableStateFlow("")
    val searchInProgress = MutableStateFlow(false)

    // TODO fetch ignored media ids list
    private val ignoredIds = emptyList<String>()

    val mediaFlow: Flow<Resource<List<Media>>?> = searchQuery
        .debounce(400)
        .flatMapLatest { searchText ->
            Timber.d("Searching for $searchText")
            searchInProgress.value = false
            if (searchText.isBlank() || searchText.length < 2)
                return@flatMapLatest flowOf(null)
            try {
                searchInProgress.value = true
                val searchResult = mediaRepository.searchMedia(searchText)
                Timber.d("RESULTS ${searchResult.size}")
                searchInProgress.value = false
                flowOf(Resource.Success(searchResult.filter { !ignoredIds.contains(it.imdbID) }))
            } catch (t: Throwable) {
                searchInProgress.value = false
                Timber.e(t)
                flowOf(Resource.Error<List<Media>>(t, null))
            }
        }/*.map { list ->
            list.map { media ->
                MediaListItem(
                    media,
                    isOnWatchlist
                )
            }
        }*/.flowOn(Dispatchers.IO)


}