package com.holudi.moviewatchlist.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.holudi.moviewatchlist.R

@Composable
fun SearchScreen(navController: NavHostController) {

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.search),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(16.dp)
        )
    }

}