package com.nextflix.app.seriesList.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.nextflix.app.api.SeriesApi
import com.nextflix.app.seriesList.domain.model.Series
import com.nextflix.app.seriesList.presentation.SeriesListUiEvent

@SuppressLint("DefaultLocale")
@Composable
fun SeriesItem(
    series: Series,
    //seriesListState: SeriesListState,
    isWatchLaterLoading: Boolean,
    onEvent: (SeriesListUiEvent) -> Unit,
    navHostController: NavHostController
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(SeriesApi.IMAGE_BASE_URL + series.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    )

    val dominantColor = MaterialTheme.colorScheme.secondaryContainer

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(dominantColor)
            .clickable {
                navHostController.navigate("details_screen/${series.id}")
            },
        contentAlignment = Alignment.Center

    ) {
        if (painter.state is AsyncImagePainter.State.Error || painter.state is AsyncImagePainter.State.Loading) {
            Icon(
                modifier = Modifier.size(70.dp),
                imageVector = Icons.Rounded.ImageNotSupported,
                contentDescription = series.name
            )
        } else {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painter,
                contentDescription = series.name,
                contentScale = ContentScale.Crop
            )
        }

        // Gradient overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                        startY = 0f,
                        endY = 600f
                    )
                )
        )

        // Watch later/Check icon
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(34.dp)
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(8.dp))
                .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(8.dp))
                .background(if (series.watch_later) Color.White else Color.Black.copy(alpha = 0.5f))
                .clickable {
                    if (!series.watch_later) {
                        // Trigger add to watch later
                        onEvent(SeriesListUiEvent.OnWatchLaterSeries(series.id, series.name, series.poster_path))
                    } else {
                        // Trigger remove from watch later
                        onEvent(SeriesListUiEvent.OnRemoveWatchLaterSeries(series.id))
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
                    imageVector = if (series.watch_later) Icons.Filled.Check else Icons.Outlined.WatchLater,
                    contentDescription = if (series.watch_later) "Remove from watch list" else "Add to watch list",
                    tint = if (series.watch_later) Color.Black else Color.White
                )
            }

        }

        // Text and rating at the bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = series.name,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating Star",
                    tint = Color.LightGray,
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    text = String.format("%.1f", series.vote_average).removeSuffix(".0"),
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}
