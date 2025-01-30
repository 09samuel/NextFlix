package com.nextflix.app.seriesDetails.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.nextflix.app.api.SeriesApi
import com.nextflix.app.seriesDetails.domain.model.Recommendations
import com.nextflix.app.seriesDetails.presentation.SeriesDetailsUiEvent

@Composable
fun RecommendationsItem(
    recommendations: Recommendations,
    isWatchLaterLoading: Boolean,
    onEvent: (SeriesDetailsUiEvent) -> Unit,
    navHostController: NavHostController
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(SeriesApi.IMAGE_BASE_URL + recommendations.poster_path)
            .size(Size.ORIGINAL)
            .build()
    )

    val dominantColor = MaterialTheme.colorScheme.secondaryContainer

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .width(130.dp)

            .clip(RoundedCornerShape(4.dp))
            .background(dominantColor)
            .clickable {
                navHostController.navigate("details_screen/${recommendations.id}")
            },
        contentAlignment = Alignment.Center
    ) {
        if (painter.state is AsyncImagePainter.State.Error || painter.state is AsyncImagePainter.State.Loading) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp),
//                contentAlignment = Alignment.Center
//            ) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = recommendations.name
                )
//            }
        } else {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painter,
                contentDescription = recommendations.name,
                contentScale = ContentScale.Crop
            )
        }

        // Watch later/Check icon
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(34.dp)
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(8.dp))
                .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(8.dp))
                .background(if (recommendations.watch_later) Color.White else Color.Black.copy(alpha = 0.5f))
                .clickable {
                    if (!recommendations.watch_later) {
                        // Trigger add to watch later
                        onEvent(SeriesDetailsUiEvent.OnWatchLaterSeries(seriesId = recommendations.id, seriesName = recommendations.name, posterPath = recommendations.poster_path))
                    } else {
                        // Trigger remove from watch later
                        onEvent(SeriesDetailsUiEvent.OnRemoveWatchLaterSeries(recommendations.id))
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (isWatchLaterLoading) {
                CircularProgressIndicator(
                    color = Color.DarkGray,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(
                    imageVector = if (recommendations.watch_later) Icons.Filled.Check else Icons.Outlined.WatchLater,
                    contentDescription = if (recommendations.watch_later) "Remove from watch list" else "Add to watch list",
                    tint = if (recommendations.watch_later) Color.Black else Color.White
                )
            }
        }
    }
}
