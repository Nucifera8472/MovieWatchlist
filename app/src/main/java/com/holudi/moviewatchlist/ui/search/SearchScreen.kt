package com.holudi.moviewatchlist.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.holudi.moviewatchlist.R
import com.holudi.moviewatchlist.data.Resource
import com.holudi.moviewatchlist.ui.common.MediaItemView
import com.holudi.moviewatchlist.ui.common.SearchView

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchText = viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchInProgress = viewModel.searchInProgress.collectAsStateWithLifecycle()

    val searchResults = viewModel.mediaFlow.collectAsStateWithLifecycle(initialValue = null)

    if (searchInProgress.value)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.search),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(16.dp)
        )
        SearchView(text = searchText.value,
            onTextChange = { viewModel.searchQuery.value = it },
            onClearClicked = { viewModel.searchQuery.value = "" })
        Spacer(modifier = Modifier.height(16.dp))

        when (searchResults.value) {
            is Resource.Error ->
                Text(text = "Something went wrong, please try again")
            is Resource.Success -> {
                val data = (searchResults.value as Resource.Success<List<MediaListItem>>).data
                LazyColumn(
                    modifier = Modifier.weight(1f),
                ) {
                    items(items = data, key = { item -> item.media.imdbID }) { mediaItem ->
                        MediaItemView(mediaItem = mediaItem, toggleWatchlist = {
                            viewModel.toggleWatchlist(it)
                        }, ignoreMedia = {
                            viewModel.ignoreMedia(it)
                        })
                    }
                }
            }
            null -> {
                Text(text = "Type more text to search OMDB")
            }
        }
    }
}