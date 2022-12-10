package com.holudi.moviewatchlist.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.holudi.moviewatchlist.R

sealed class Screen(val route: String) {
    object Watchlist : Screen("watchlist")
    object Search : Screen("search")
}

sealed class BottomNavItem(
    var screen: Screen,
    var icon: ImageVector,
    @StringRes var buttonTitleResId: Int
) {
    object Watchlist : BottomNavItem(Screen.Watchlist, Icons.Filled.Bookmark, R.string.watchlist)
    object Search : BottomNavItem(Screen.Search, Icons.Filled.Search, R.string.search)
}

