package com.holudi.moviewatchlist.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.holudi.moviewatchlist.ui.common.ConnectionIndicator
import com.holudi.moviewatchlist.ui.search.SearchScreen
import com.holudi.moviewatchlist.ui.theme.MovieWatchlistTheme
import com.holudi.moviewatchlist.ui.watchlist.WatchlistScreen
import com.holudi.moviewatchlist.utils.ConnectionState
import com.holudi.moviewatchlist.utils.observeConnectivityAsFlow

private val bottomNavItems = listOf(
    BottomNavItem.Watchlist,
    BottomNavItem.Search,
)

@OptIn(ExperimentalLifecycleComposeApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val connectivityState by LocalContext.current.observeConnectivityAsFlow()
                .collectAsStateWithLifecycle(ConnectionState.Available)


            val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    BottomNavigation {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        bottomNavItems.forEach { navItem ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        imageVector = navItem.icon,
                                        contentDescription = stringResource(id = navItem.buttonTitleResId)
                                    )
                                },
                                label = { Text(stringResource(navItem.buttonTitleResId)) },
                                selected = currentDestination?.hierarchy?.any { it.route == navItem.screen.route } == true,
                                onClick = {
                                    navController.navigate(navItem.screen.route) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        // on the back stack as users select items
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->

                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        AnimatedVisibility(
                            visible = connectivityState == ConnectionState.Unavailable,
                            enter = expandVertically(animationSpec = tween(200)),
                            exit = shrinkVertically(animationSpec = tween(200))
                        ) {
                            ConnectionIndicator()
                        }
                        // Screen content
                        NavHost(
                            navController,
                            startDestination = Screen.Watchlist.route,
                            Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.Watchlist.route) { WatchlistScreen(navController) }
                            composable(Screen.Search.route) { SearchScreen(navController) }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MovieWatchlistTheme {

    }
}

