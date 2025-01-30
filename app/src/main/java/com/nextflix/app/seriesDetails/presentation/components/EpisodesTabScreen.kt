package com.nextflix.app.seriesDetails.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nextflix.app.seriesDetails.presentation.SeriesDetailsState
import com.nextflix.app.seriesDetails.presentation.SeriesDetailsUiEvent
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EpisodesTabScreen(detailsState: SeriesDetailsState, onEvent: (SeriesDetailsUiEvent) -> Unit) {
//    val dropdownStates = remember(detailsState.details?.number_of_seasons) {
//        mutableStateListOf<Boolean>().apply {
//            repeat(detailsState.details?.number_of_seasons ?: 0) {
//                add(false)
//            }
//        }
//    }
//
//    Surface(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background)
//    ) {
//        if (detailsState.details?.number_of_seasons==0) {
//            val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)
//
//            LazyColumn(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
//                items(3) { index ->
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(50.dp)
//                            .shimmer(shimmerInstance)
//                            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//            }
//        } else {
//            Column(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
//                LazyColumn {
//                    detailsState.details?.let { details ->
//                        items(details.number_of_seasons) { index ->
//                            Column(modifier = Modifier.fillMaxWidth()) {
//                                // Season Header
//                                Box(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .background(MaterialTheme.colorScheme.primaryContainer)
//                                        .padding(16.dp)
//                                ) {
//                                    Row(
//                                        modifier = Modifier.fillMaxWidth(),
//                                        horizontalArrangement = Arrangement.SpaceBetween
//                                    ) {
//                                        Text(
//                                            text = "Season ${index + 1}",
//                                            style = MaterialTheme.typography.bodyLarge,
//                                            color = MaterialTheme.colorScheme.onPrimaryContainer
//                                        )
//                                        Icon(
//                                            imageVector = if (dropdownStates[index]) {
//                                                Icons.Default.KeyboardArrowUp
//                                            } else {
//                                                Icons.Default.KeyboardArrowDown
//                                            },
//                                            contentDescription = "Toggle Dropdown",
//                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
//                                            modifier = Modifier.clickable {
//                                                if (!dropdownStates[index]) {
//                                                    onEvent(
//                                                        SeriesDetailsUiEvent.OnEpisodes(
//                                                            seriesId = details.id,
//                                                            seasonNo = index + 1
//                                                        )
//                                                    )
//                                                }
//                                                dropdownStates[index] = !dropdownStates[index]
//                                            }
//                                        )
//                                    }
//                                }
//
//                                // Episodes List
//                                if (dropdownStates[index]) {
//                                    Column(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .background(MaterialTheme.colorScheme.secondaryContainer)
//                                            .padding(16.dp)
//                                    ) {
//                                        val season = detailsState.episodesList.getOrNull(index + 1)
//                                        if (season.isNullOrEmpty()) {      //loading placeholder
//                                            val shimmerInstance =
//                                                rememberShimmer(shimmerBounds = ShimmerBounds.View)
//
//                                            Row (modifier = Modifier.fillMaxWidth()){
//                                                Box(
//                                                    modifier = Modifier
//                                                        .width(86.dp)
//                                                        .height(50.dp)
//                                                        .shimmer(shimmerInstance)
//                                                        .background(
//                                                            Color.White.copy(
//                                                                alpha = 0.3f
//                                                            )
//                                                        )
//                                                )
//                                                Spacer(modifier = Modifier.width(16.dp))
//
//                                                Column(
//                                                    modifier = Modifier
//                                                        .fillMaxHeight()
//                                                        .weight(1f)
//                                                        .padding(end = 16.dp)
//                                                ) {
//                                                    // Top Box
//                                                    Box(
//                                                        modifier = Modifier
//                                                            .width(90.dp)
//                                                            .height(20.dp)
//                                                            .shimmer(shimmerInstance)
//                                                            .background(
//                                                                Color.White.copy(
//                                                                    alpha = 0.3f
//                                                                )
//                                                            )
//                                                    )
//
//                                                    // Spacer to push the bottom Box to the bottom
//                                                    Spacer(modifier = Modifier.height(8.dp))
//
//                                                    // Bottom Box
//                                                    Box(
//                                                        modifier = Modifier
//                                                            .width(140.dp)
//                                                            .height(20.dp)
//                                                            .shimmer(shimmerInstance)
//                                                            .background(
//                                                                Color.White.copy(
//                                                                    alpha = 0.3f
//                                                                )
//                                                            )
//                                                    )
//                                                }
//
//                                                Box(
//                                                    modifier = Modifier
//                                                        .size(28.dp)
//                                                        .shimmer(shimmerInstance)
//                                                        .background(
//                                                            Color.White.copy(
//                                                                alpha = 0.3f
//                                                            )
//                                                        )
//                                                        .clip(RoundedCornerShape(26.dp))
//                                                        .padding(8.dp)
//                                                        .align(Alignment.CenterVertically)
//                                                )
//                                            }
//                                        } else {
//                                            season.forEachIndexed { episodeIndex, episode ->
//                                                EpisodeItem(
//                                                    episode = episode,
//                                                    //title = "Episode ${episodeIndex + 1}",
//                                                    onEvent = onEvent,
//                                                    isWatchedLoading = detailsState.isWatchedLoading.getOrDefault(
//                                                        episode.id,
//                                                        false
//                                                    ),
//                                                    seasonNo = index + 1,
//                                                    seriesId = detailsState.details.id,
//                                                    seriesName = detailsState.details.name,
//                                                    posterPath = detailsState.details.poster_path
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            Spacer(modifier = Modifier.height(16.dp))
//                        }
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun EpisodesTabScreen(detailsState: SeriesDetailsState, onEvent: (SeriesDetailsUiEvent) -> Unit) {
    val dropdownStates = remember(detailsState.details?.number_of_seasons) {
        mutableStateListOf<Boolean>().apply {
            repeat(detailsState.details?.number_of_seasons ?: 0) {
                add(false)
            }
        }
    }

    val selectedSeasonIndex = remember { mutableStateOf(-1) } // Track the selected season index
    val selectedEpisodeIndex = remember { mutableStateOf(-1) } // Track the selected episode index

    Box(modifier = Modifier.fillMaxSize()) {
        // Main Content
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (detailsState.details?.number_of_seasons == 0) {
                val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)

                LazyColumn(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
                    items(3) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .shimmer(shimmerInstance)
                                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            } else {
                Column(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
                    LazyColumn {
                        detailsState.details?.let { details ->
                            items(details.number_of_seasons) { seasonIndex ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    // Season Header
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.primaryContainer)
                                            .padding(16.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Season ${seasonIndex + 1}",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                            Icon(
                                                imageVector = if (dropdownStates[seasonIndex]) {
                                                    Icons.Default.KeyboardArrowUp
                                                } else {
                                                    Icons.Default.KeyboardArrowDown
                                                },
                                                contentDescription = "Toggle Dropdown",
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.clickable {
                                                    if (!dropdownStates[seasonIndex]) {
                                                        onEvent(
                                                            SeriesDetailsUiEvent.OnEpisodes(
                                                                seriesId = details.id,
                                                                seasonNo = seasonIndex + 1
                                                            )
                                                        )
                                                    }
                                                    dropdownStates[seasonIndex] =
                                                        !dropdownStates[seasonIndex]
                                                }
                                            )
                                        }
                                    }

                                    // Episodes List
                                    if (dropdownStates[seasonIndex]) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                                .padding(16.dp)
                                        ) {
                                            val episodes =
                                                detailsState.episodesList.getOrNull(seasonIndex + 1)


                                            if (episodes.isNullOrEmpty()) {      //loading placeholder
                                                val shimmerInstance =
                                                    rememberShimmer(shimmerBounds = ShimmerBounds.View)

                                                Row(modifier = Modifier.fillMaxWidth()) {
                                                    Box(
                                                        modifier = Modifier
                                                            .width(86.dp)
                                                            .height(50.dp)
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .shimmer(shimmerInstance)
                                                            .background(
                                                                Color.White.copy(
                                                                    alpha = 0.3f
                                                                )
                                                            )
                                                    )
                                                    Spacer(modifier = Modifier.width(16.dp))

                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxHeight()
                                                            .weight(1f)
                                                            .padding(end = 16.dp)
                                                    ) {
                                                        // Top text Box
                                                        Box(
                                                            modifier = Modifier
                                                                .width(90.dp)
                                                                .height(20.dp)
                                                                .shimmer(shimmerInstance)
                                                                .background(
                                                                    Color.White.copy(
                                                                        alpha = 0.3f
                                                                    )
                                                                )
                                                        )

                                                        // Spacer to push the bottom Box to the bottom
                                                        Spacer(modifier = Modifier.height(8.dp))

                                                        // Bottom text Box
                                                        Box(
                                                            modifier = Modifier
                                                                .width(140.dp)
                                                                .height(20.dp)
                                                                .shimmer(shimmerInstance)
                                                                .background(
                                                                    Color.White.copy(
                                                                        alpha = 0.3f
                                                                    )
                                                                )
                                                        )
                                                    }

                                                    Box(
                                                        modifier = Modifier
                                                            .size(28.dp) // Diameter of the circle
                                                            .clip(CircleShape) // Use predefined CircleShape
                                                            .shimmer(shimmerInstance)
                                                            .background(
                                                                Color.White.copy(alpha = 0.3f)
                                                            )
                                                            .align(Alignment.CenterVertically)
                                                    )

                                                }
                                            } else {
                                                episodes.forEachIndexed { episodeIndex, episode ->
                                                    EpisodeItem(
                                                        episode = episode,
                                                        onEvent = onEvent,
                                                        isWatchedLoading = detailsState.isWatchedLoading.getOrDefault(
                                                            episode.id,
                                                            false
                                                        ),
                                                        seasonNo = seasonIndex + 1,
                                                        seriesId = details.id,
                                                        seriesName = details.name,
                                                        posterPath = details.poster_path,
                                                        onEpisodeClick = {
                                                            selectedSeasonIndex.value = seasonIndex
                                                            selectedEpisodeIndex.value =
                                                                episodeIndex
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }

        // Full-Screen HorizontalPager Overlay
        if (selectedSeasonIndex.value != -1 && selectedEpisodeIndex.value != -1) {
            val episodes =
                detailsState.episodesList.getOrNull(selectedSeasonIndex.value + 1) ?: emptyList()
            val pagerState = rememberPagerState(initialPage = selectedEpisodeIndex.value) {
                episodes.size
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f)) // Dim the background
                    .clickable { // Close pager when clicking outside
                        selectedSeasonIndex.value = -1
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
                    EpisodeDetailsItem(
                        index = page,
                        pagerState = pagerState,
                        episode = episodes[page]
                    )
                }

                // Close Button
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
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
                            selectedSeasonIndex.value = -1
                            selectedEpisodeIndex.value = -1
                        }
                )
            }
        }
    }
}
