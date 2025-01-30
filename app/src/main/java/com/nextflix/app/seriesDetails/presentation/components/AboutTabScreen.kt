package com.nextflix.app.seriesDetails.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.nextflix.app.api.SeriesApi
import com.nextflix.app.seriesDetails.presentation.SeriesDetailsState
import com.nextflix.app.seriesDetails.presentation.SeriesDetailsUiEvent
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun AboutTabScreen(
    detailsState: SeriesDetailsState,
    onEvent: (SeriesDetailsUiEvent) -> Unit,
    navController: NavHostController
) {
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // Where to Watch Section
                Text(
                    text = "Where to Watch",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                if (detailsState.isWatchProviderLoading) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(3) {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .width(100.dp)
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .shimmer(shimmerInstance)
                                    .background(
                                        MaterialTheme.colorScheme.secondaryContainer.copy(
                                            alpha = 0.3f
                                        )
                                    )
                            )
                        }
                    }

                } else if (detailsState.watchProviderError != "") {
                    Text(
                        modifier = Modifier.padding(bottom = 16.dp),
                        text = detailsState.watchProviderError.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        detailsState.watchProviders.forEach { provider ->
                            val painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(SeriesApi.IMAGE_BASE_URL + provider.logo_path)
                                    .size(Size.ORIGINAL)
                                    .build()
                            )

                            AssistChip(
                                onClick = { /* Add click logic */ },
                                label = {
                                    Text(
                                        text = provider.provider_name,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                leadingIcon = {
                                    if (painter.state is AsyncImagePainter.State.Error || painter.state is AsyncImagePainter.State.Loading) {
                                        Icon(
                                            modifier = Modifier.size(20.dp),
                                            imageVector = Icons.Rounded.ImageNotSupported,
                                            contentDescription = provider.provider_name,
                                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    } else {
                                        Image(
                                            modifier = Modifier.size(20.dp),
                                            painter = painter,
                                            contentDescription = provider.provider_name
                                        )
                                    }
                                },
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }

                // Show Info Section
                Text(
                    text = "Show Info",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (detailsState.isLoading) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmer(shimmerInstance)
                            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                    )

                    Box(
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmer(shimmerInstance)
                            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                    )
                } else if (detailsState.error != "") {
                    Text(
                        modifier = Modifier.padding(bottom = 16.dp),
                        text = detailsState.error.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating Star",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = detailsState.details?.vote_average.toString().take(3),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        detailsState.details?.genres?.let {
                            Text(
                                text = it.joinToString(", ") { genre -> genre.name },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                    // Overview Section
                    detailsState.details?.let {
                        Text(
                            text = it.overview,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(bottom = 16.dp)

                        )
                    }
                }

                //Recommendations section
                Text(
                    text = "Recommendations",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (detailsState.isRecommendationsListLoading) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        items(detailsState.recommendationsList.size) { index ->
                            Box(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(130.dp)

                                    .clip(RoundedCornerShape(4.dp))
                                    .shimmer(shimmerInstance)
                                    .background(
                                        MaterialTheme.colorScheme.secondaryContainer.copy(
                                            alpha = 0.3f
                                        )
                                    )
                            )
                        }
                    }

                } else if (detailsState.recommendationsListError != "") {
                    Text(
                        modifier = Modifier.padding(bottom = 16.dp),
                        text = detailsState.recommendationsListError.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                } else {

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        items(detailsState.recommendationsList.size) { index ->
                            RecommendationsItem(
                                recommendations = detailsState.recommendationsList[index],
                                isWatchLaterLoading = detailsState.isWatchLaterLoading.getOrDefault(
                                    detailsState.recommendationsList[index].id,
                                    false
                                ),
                                onEvent = onEvent,
                                navHostController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}