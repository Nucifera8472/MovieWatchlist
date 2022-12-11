package com.holudi.moviewatchlist.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.holudi.moviewatchlist.R
import com.holudi.moviewatchlist.data.model.Media

@Composable
fun DetailView(media: Media, onCloseClicked: () -> Unit) {

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                IconButton(modifier = Modifier.size(48.dp), onClick = { onCloseClicked() }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(id = R.string.close)
                    )
                }
            }
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = media.poster,
                    contentDescription = media.title,
                    modifier = Modifier.height(400.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = media.title, style = MaterialTheme.typography.h6)
                Text(text = media.year, style = MaterialTheme.typography.subtitle1)
                media.imdbRating?.let {
                    Text(
                        text = "${stringResource(id = R.string.rating)} ${media.imdbRating}",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun DetailViewPreview() {
    DetailView(
        media = Media(
            "tt1844624",
            "American Horror Story",
            "2011",
            "https://m.media-amazon.com/images/M/MV5BOWFlOWE1OGEtOTVlMi00M2JmLWJlMGEtOWVjOGFhOTNlYTZiXkEyXkFqcGdeQXVyMTEyMjM2NDc2._V1_SX300.jpg",
            "6.4"
        ),
    ) {

    }

}