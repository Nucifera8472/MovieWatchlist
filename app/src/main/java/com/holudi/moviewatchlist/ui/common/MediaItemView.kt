package com.holudi.moviewatchlist.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.holudi.moviewatchlist.R
import com.holudi.moviewatchlist.data.model.Media
import com.holudi.moviewatchlist.ui.search.MediaListItem

@Composable
fun MediaItemView(
    mediaItem: MediaListItem,
    toggleWatchlist: (Media) -> Unit,
    ignoreMedia: (Media) -> Unit
) {
    Column(
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {

            AsyncImage(
                model = mediaItem.media.poster,
                contentDescription = mediaItem.media.title,
                modifier = Modifier.height(120.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1.0f)
                    .padding(8.dp)
            ) {
                Text(text = mediaItem.media.title, style = MaterialTheme.typography.body1)
                Text(text = mediaItem.media.year, style = MaterialTheme.typography.body2)
                mediaItem.media.imdbRating?.let {
                    Text(text = mediaItem.media.imdbRating, style = MaterialTheme.typography.body2)
                }
            }

            Column(
                modifier = Modifier.width(60.dp),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { toggleWatchlist(mediaItem.media) }) {
                    if (mediaItem.isOnWatchlist) {
                        Icon(
                            imageVector = Icons.Rounded.Bookmark,
                            contentDescription = stringResource(
                                id = R.string.remove_from_watchlist
                            )
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.BookmarkBorder,
                            contentDescription = stringResource(
                                id = R.string.add_to_watchlist
                            )
                        )
                    }
                }
                IconButton(onClick = { ignoreMedia(mediaItem.media) }) {
                    Icon(
                        imageVector = Icons.Rounded.Block, contentDescription = stringResource(
                            id = R.string.ignore
                        )
                    )
                }
            }
        }
        Divider(modifier = Modifier.padding(start = 16.dp))
    }


}

@Composable
@Preview
fun MediaItemViewPreview() {
    MediaItemView(
        MediaListItem(
            Media(
                "tt1844624",
                "American Horror Story",
                "2011",
                "https://m.media-amazon.com/images/M/MV5BOWFlOWE1OGEtOTVlMi00M2JmLWJlMGEtOWVjOGFhOTNlYTZiXkEyXkFqcGdeQXVyMTEyMjM2NDc2._V1_SX300.jpg"
            ), false
        ),
        {},
        {}
    )
}