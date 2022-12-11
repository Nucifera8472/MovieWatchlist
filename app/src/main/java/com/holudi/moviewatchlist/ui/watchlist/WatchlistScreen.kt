@file:OptIn(ExperimentalLifecycleComposeApi::class)

package com.holudi.moviewatchlist.ui.watchlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.holudi.moviewatchlist.R
import com.holudi.moviewatchlist.data.Resource
import com.holudi.moviewatchlist.ui.common.DetailView
import com.holudi.moviewatchlist.ui.common.MediaItemView
import com.holudi.moviewatchlist.ui.common.MediaListItem

@Composable
fun WatchlistScreen(
    navController: NavHostController,
    viewModel: WatchlistViewModel = hiltViewModel()
) {
    val searchInProgress = viewModel.fetchInProgress.collectAsStateWithLifecycle()
    val mediaResults = viewModel.mediaFlow.collectAsStateWithLifecycle(initialValue = null)

    if (searchInProgress.value)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.watchlist),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (mediaResults.value) {
            is Resource.Error ->
                Text(
                    text = stringResource(R.string.something_went_wrong),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Red
                )
            is Resource.Success -> {
                val data = (mediaResults.value as Resource.Success<List<MediaListItem>>).data

                if (data.isEmpty()) {
                    Text(
                        text = stringResource(R.string.empty_watchlist),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                    ) {
                        items(items = data, key = { item -> item.media.imdbID }) { mediaItem ->
                            MediaItemView(mediaItem = mediaItem, toggleWatchlist = {
                                viewModel.toggleWatchlist(it)
                            }, ignoreMedia = {
                                viewModel.ignoreMedia(it)
                            }, onItemClicked = {
                                viewModel.selectedItem.value = it
                            })
                        }
                    }
                }
            }
            null -> {
            }
        }
    }
    viewModel.selectedItem.value?.let {
        DetailView(media = it) {
            viewModel.selectedItem.value = null
        }
    }
}