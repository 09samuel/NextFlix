package com.nextflix.app.upcoming.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nextflix.app.seriesDetails.presentation.components.EpisodeDetailsItem
import com.nextflix.app.upcoming.presentation.components.EpisodeDetailsItemForUpcoming
import com.nextflix.app.upcoming.presentation.components.UpcomingEpisodeItem
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun UpcomingEpisodesScreen(
    upcomingEpisodesState: UpcomingEpisodesState,
    navController: NavHostController
) {

    val selectedEpisodeIndex = remember { mutableStateOf(-1) } // Track the selected episode index

    if (upcomingEpisodesState.isLoading || upcomingEpisodesState.error != "") {
        val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)

        LazyColumn(modifier = Modifier.padding(top = 24.dp)) {
            items(7) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmer(shimmerInstance)
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
//    else if (upcomingEpisodesState.error != "") {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = upcomingEpisodesState.error.toString())
//        }
//    }
    else if (upcomingEpisodesState.upcomingEpisodesList.isEmpty()) {
        // Empty state
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Error, // Replace with your preferred icon
                    contentDescription = "No results",
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "No results to display")
            }
        }
    } else {
        LazyColumn(modifier = Modifier.padding(top = 24.dp)) {
            items(upcomingEpisodesState.upcomingEpisodesList.size) { index ->
                UpcomingEpisodeItem(
                    upcomingEpisode = upcomingEpisodesState.upcomingEpisodesList[index],
                    navHostController = navController,
                    onEpisodeClick = {
                        //selectedSeasonIndex.value = seasonIndex
                        selectedEpisodeIndex.value =
                            index
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Full-Screen HorizontalPager Overlay
        if (//selectedSeasonIndex.value != -1 &&
            selectedEpisodeIndex.value != -1) {
            val episodes =
                upcomingEpisodesState.upcomingEpisodesList ?: emptyList()
            val pagerState = rememberPagerState(initialPage = selectedEpisodeIndex.value) {
                episodes.size
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f)) // Dim the background
                    .clickable { // Close pager when clicking outside
                        //selectedSeasonIndex.value = -1
                        selectedEpisodeIndex.value = -1
                    }
            ) {
                HorizontalPager(
                    state = pagerState,
                    //pageCount = episodes.size,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            // Do nothing, to avoid closing when clicking inside the pager
                        },
                    contentPadding = PaddingValues(50.dp)
                ) { page ->
                    EpisodeDetailsItemForUpcoming(
                        index = page,
                        pagerState = pagerState,
                        episode = episodes[page]
                    )
                }

                // Close Button
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    tint = Color.Black,
                    contentDescription = "Close Pager",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(24.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            //selectedSeasonIndex.value = -1
                            selectedEpisodeIndex.value = -1
                        }
                )
            }
        }
    }
}
