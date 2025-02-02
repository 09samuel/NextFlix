package com.nextflix.app.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nextflix.app.authentication.presentation.AuthenticationViewModel
import com.nextflix.app.authentication.presentation.LoginScreen
import com.nextflix.app.seriesDetails.presentation.SeriesDetailsScreen
import com.nextflix.app.seriesList.presentation.SearchSeriesScreen
import com.nextflix.app.seriesList.presentation.SeriesListViewModel
import com.nextflix.app.seriesList.presentation.SeriesScreen
import com.nextflix.app.ui.theme.NextFlixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NextFlixTheme {
                val navController = rememberNavController()

                val seriesListViewModel = hiltViewModel<SeriesListViewModel>()
                val seriesListState = seriesListViewModel.seriesListState.collectAsState().value
                val seriesListOnEvent = seriesListViewModel::onEvent

                val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
                val authenticationOnEvent = authenticationViewModel::onEvent

                NavHost(navController = navController, startDestination = "login_screen") {
                    composable( enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(400)
                        ) + fadeIn()
                    },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                tween(400)
                            ) + fadeOut()
                        },
                        route = "login_screen"
                    ) {
                        LoginScreen(
                            authenticationViewModel = authenticationViewModel,
                            onEvent = authenticationOnEvent,
                            navController = navController
                        )
                    }

                    composable(
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                tween(400)
                            )
                        },
                        route = "main_screen"
                    ) {
                        MainScreen(
                            seriesListViewModel = seriesListViewModel,
                            navController = navController,
                            authenticationViewModel = authenticationViewModel
                        )
                    }

                    composable(route = "search_series_screen") { backStackEntry ->
                        SearchSeriesScreen(
                            //seriesListState,
                            viewModel = seriesListViewModel,
                            onEvent = seriesListOnEvent,
                            navController = navController
                        )
                    }

                    composable(enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(400)
                        )
                    }, popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(400)
                        )
                    }, route = "details_screen/{seriesId}") { backStackEntry ->
                        //val seriesId = backStackEntry.arguments?.getString("seriesId")
                        SeriesDetailsScreen(navController = navController)
                    }

                    composable(enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(400)
                        )
                    }, popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(400)
                        )
                    }, route = "series_screen") { backStackEntry ->
                        //val seriesId = backStackEntry.arguments?.getString("seriesId")
                        SeriesScreen(
                            viewModel = seriesListViewModel,
                            //seriesListState = seriesListState,
                            onEvent = seriesListViewModel::onEvent,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

