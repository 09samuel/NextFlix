package com.nextflix.app.seriesList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nextflix.app.seriesList.presentation.components.MiniSeriesItem
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun HomeScreen(
    //seriesListState: SeriesListState,
    onEvent: (SeriesListUiEvent) -> Unit,
    navController: NavHostController,
    viewModel: SeriesListViewModel
) {

//    var refreshState by remember { mutableStateOf(false) }

    // Recompose when refreshState is toggled
//    LaunchedEffect(refreshState) {
//        viewModel.refreshSeries()
//        viewModel.getUserSeries()
//    }

    LaunchedEffect(Unit) {
        //viewModel.initializeData() // Ensures it's called only once
        viewModel.getSeries(false)
        viewModel.getUserSeries()
    }



    val seriesListState by viewModel.seriesListState.collectAsState()

    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //trending shows
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Trending Shows",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
            )
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(20.dp)

                    .clickable {
                        navController.navigate("series_screen")
                    },
                imageVector = Icons.Filled.Search,
                contentDescription = "go to trending shows"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (seriesListState.isSeriesLoading || seriesListState.seriesError != "") {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(3) {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .width(130.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmer(shimmerInstance)
                            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                    )
                }
            }
        }

        else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(seriesListState.seriesList.size) { index ->
                    MiniSeriesItem(
                        series = seriesListState.seriesList[index],
                        isWatchLaterLoading = seriesListState.isWatchLaterLoading.getOrDefault(
                            seriesListState.seriesList[index].id,
                            false // Default to false if the value is not found
                        ),
                        onEvent = onEvent,
                        navHostController = navController,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        //user list
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Your list",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (seriesListState.isUserSeriesLoading
        // || seriesListState.userSeriesList.isEmpty()
            ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(3) {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .width(130.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmer(shimmerInstance)
                            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                    )
                }
            }
        }
        else if(seriesListState.userSeriesError != ""){
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Error, // Replace with your preferred icon
                        contentDescription = "No shows added yet",
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "No shows added yet")
                }
            }
        }
        else{
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(seriesListState.userSeriesList.size) { index ->
                    MiniSeriesItem(
                        series = seriesListState.userSeriesList[index],
                        isWatchLaterLoading = seriesListState.isWatchLaterLoading.getOrDefault(
                            seriesListState.userSeriesList[index].id,
                            false // Default to false if the value is not found
                        ),
                        onEvent = onEvent,
                        navHostController = navController,
                    )
                }
            }
        }
    }
}