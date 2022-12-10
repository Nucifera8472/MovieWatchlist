package com.holudi.moviewatchlist.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holudi.moviewatchlist.R

@Composable
fun ConnectionIndicator() {

    Text(
        text = stringResource(R.string.no_internet_connection),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red)
            .padding(4.dp),
        style = MaterialTheme.typography.subtitle2,
        color = Color.White,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
@Preview
fun ConnectionIndicatorPreview() {
    ConnectionIndicator()
}