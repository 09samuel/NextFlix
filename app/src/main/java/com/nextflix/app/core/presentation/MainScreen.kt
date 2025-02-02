package com.nextflix.app.core.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nextflix.app.authentication.presentation.AuthenticationViewModel
import com.nextflix.app.authentication.presentation.ProfileScreen
import com.nextflix.app.seriesList.presentation.HomeScreen
import com.nextflix.app.seriesList.presentation.SeriesListUiEvent
import com.nextflix.app.seriesList.presentation.SeriesListViewModel
import com.nextflix.app.upcoming.presentation.UpcomingEpisodesScreen
import com.nextflix.app.upcoming.presentation.UpcomingEpisodesUiEvent
import com.nextflix.app.upcoming.presentation.UpcomingEpisodesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(seriesListViewModel: SeriesListViewModel, navController: NavHostController, authenticationViewModel: AuthenticationViewModel) {
    //val seriesListViewModel = hiltViewModel<SeriesListViewModel>()
    val seriesListState = seriesListViewModel.seriesListState.collectAsState().value
    val seriesListOnEvent = seriesListViewModel::onEvent

    val upcomingEpisodesViewModel = hiltViewModel<UpcomingEpisodesViewModel>()
    val upcomingEpisodesState =
        upcomingEpisodesViewModel.upcomingEpisodesState.collectAsState().value
    val upcomingEpisodesOnEvent = upcomingEpisodesViewModel::onEvent

    //val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    //val authenticationOnEvent = authenticationViewModel::onEvent

    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    val bottomNavController = rememberNavController()

    val bottomNavItems = remember {
        listOf(
            BottomItem("Series", Icons.AutoMirrored.Filled.List),
            BottomItem("Upcoming", Icons.Filled.Upcoming),
            BottomItem("Profile", Icons.Filled.Person)
        )
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
//                Text(
//                    text = if (movieListState.isCurrentPopularScreen) {
//                        stringResource(R.string.popular_movies)
//                    } else {
//                        stringResource(R.string.upcoming_movies)
//
//                    }, fontSize = 20.sp
//                )
                Text(text = "NextFlix")
            },

            modifier = Modifier.shadow(2.dp),
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                MaterialTheme.colorScheme.inverseOnSurface
            ),
        )
    }, bottomBar = {
        NavigationBar {
            bottomNavItems.forEachIndexed { index, bottomItem ->
                NavigationBarItem(
                    selected = selected.intValue == index,
                    onClick = {
                        selected.intValue = index
                        when (selected.intValue) {
                            0 -> {
                                seriesListOnEvent(SeriesListUiEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate("home_screen")
                            }

                            1 -> {
                                upcomingEpisodesOnEvent(UpcomingEpisodesUiEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate("upcoming_episodes_screen")
                            }

                            2 -> {
                                //upcomingEpisodesOnEvent(UpcomingEpisodesUiEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate("profile_screen")
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = bottomItem.icon,
                            contentDescription = bottomItem.title
                        )
                    },
                    label = { Text(bottomItem.title) }
                )
            }
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = "home_screen"
            ) {
                composable(
//                        exitTransition = {
//                        slideOutOfContainer(
//                            AnimatedContentTransitionScope.SlideDirection.Left,
//                            tween(400)
//                        ) + fadeOut()
//                    }, popEnterTransition = {
//                        slideIntoContainer(
//                            AnimatedContentTransitionScope.SlideDirection.Right,
//                            tween(400)
//                        ) + fadeIn()
//                    },
                    enterTransition = {
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
                    route = "home_screen"
                ) {
                    HomeScreen(
                        viewModel = seriesListViewModel,
                        //seriesListState = seriesListState,
                        onEvent = seriesListViewModel::onEvent,
                        navController = navController
                    )
                }

                composable(enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(400)
                    ) + fadeIn()
                }, popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(400)
                    ) + fadeOut()
                }, route = "upcoming_episodes_screen") { backStackEntry ->
                    UpcomingEpisodesScreen(
                        upcomingEpisodesState = upcomingEpisodesState,
                        //upcomingEpisodesViewModel::onEvent,
                        navController = navController
                    )
                }

                composable(enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(400)
                    ) + fadeIn()
                }, popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(400)
                    ) + fadeOut()
                }, route = "profile_screen") { backStackEntry ->
                    ProfileScreen(
                        authenticationViewModel = authenticationViewModel,
                        onEvent = authenticationViewModel::onEvent,
                        navController = navController
                    )
                }
            }
        }
    }
}

data class BottomItem(val title: String, val icon: ImageVector)