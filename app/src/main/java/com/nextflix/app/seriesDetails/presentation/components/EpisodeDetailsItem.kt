package com.nextflix.app.seriesDetails.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.nextflix.app.api.SeriesApi
import com.nextflix.app.seriesDetails.domain.model.Episode
import kotlin.math.absoluteValue

@SuppressLint("DefaultLocale")
@Composable
fun EpisodeDetailsItem(episode: Episode, index: Int, pagerState: PagerState) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(SeriesApi.IMAGE_BASE_URL + episode.still_path)
            .size(Size.ORIGINAL)
            .build()
    )

    val dominantColor = MaterialTheme.colorScheme.secondaryContainer

    val pageOffset = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction

    Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
        .padding(2.dp)
        .graphicsLayer {
            lerp(
                start = 0.85f.dp,
                stop = 1f.dp,
                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
            ).also { scale ->
                scaleX = scale.value
                scaleY = scale.value
            }
            alpha = lerp(
                start = 0.85f.dp,
                stop = 1f.dp,
                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
            ).value
        }) {
        Column() {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(dominantColor),
                contentAlignment = Alignment.Center
            ) {
                when (painter.state) {
//                is AsyncImagePainter.State.Loading -> {
//                    // Placeholder Shimmer Effect
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(260.dp)
//                            .background(Color.Gray.copy(alpha = 0.3f))
//                    )
//                }

                    is AsyncImagePainter.State.Error -> {
                        Icon(
                            modifier = Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = "Image not available",
                            tint = Color.White
                        )
                    }

                    else -> {
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            painter = painter,
                            contentDescription = episode.name,
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Gradient overlay for readability
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                                startY = 0f,
                                endY = 500f
                            )
                        )
                )

                // Series Title and ep no
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                ) {

                        Text(
                            text = episode.name,
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis,
                            fontSize = 20.sp,
                        )
                        Text(
                            text = "Episode ${episode.episode_number} ",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 12.sp
                        )

                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Episode Info",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
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
                        text = String.format("%.1f", episode.vote_average).removeSuffix(".0"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .verticalScroll(scrollState) // Make it scrollable
                ) {
                    Text(
                        text = episode.overview,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,

                    )


                }
//                VerticalScrollbar(
//                    adapter = rememberScrollbarAdapter(scrollState),
//                    modifier = Modifier.align(Alignment.CenterEnd) // Align scrollbar to the right
//                )

            }
        }
    }
}