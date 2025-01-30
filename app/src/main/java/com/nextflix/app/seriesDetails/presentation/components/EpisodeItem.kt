package com.nextflix.app.seriesDetails.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.nextflix.app.api.SeriesApi
import com.nextflix.app.seriesDetails.domain.model.Episode
import com.nextflix.app.seriesDetails.presentation.SeriesDetailsUiEvent

@Composable
fun EpisodeItem(
    episode: Episode,
    onEvent: (SeriesDetailsUiEvent) -> Unit,
    isWatchedLoading: Boolean,
    seasonNo: Int,
    seriesId: Int,
    seriesName: String,
    posterPath: String,
    onEpisodeClick: (Episode) -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(SeriesApi.IMAGE_BASE_URL + episode.still_path)
            .size(Size.ORIGINAL)
            .build()
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(12.dp)
            .clickable {
                onEpisodeClick(episode)
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image (placeholder or episode thumbnail)
            Box(
                modifier = Modifier
                    .width(86.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.secondary)

            ) {
                if (painter.state is AsyncImagePainter.State.Error || painter.state is AsyncImagePainter.State.Loading) {
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center),
                        imageVector = Icons.Rounded.ImageNotSupported,
                        contentDescription = episode.name,
                        tint = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f)
                    )
                } else {
                    Image(
                        painter = painter,
                        contentDescription = episode.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title and Description
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = "Episode ${episode.episode_number}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = episode.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }

            // Tick Mark Icon
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = if (episode.watched) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(50)
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            if (episode.isReleased) Color.White else Color.White.copy(alpha = 0.3f) // Adjust border alpha
                        ), RoundedCornerShape(50)
                    )
                    .then(
                        if (episode.isReleased) {
                            Modifier.clickable {
                                if (!episode.watched) {
                                    // Trigger add to watch later
                                    onEvent(
                                        SeriesDetailsUiEvent.OnWatched(
                                            episodeId = episode.id,
                                            seriesId = seriesId,
                                            seasonNo = seasonNo,
                                            seriesName = seriesName,
                                            posterPath = posterPath
                                        )
                                    )
                                } else {
                                    // Trigger remove from watch later
                                    onEvent(
                                        SeriesDetailsUiEvent.OnRemoveFromWatched(
                                            seriesId = seriesId,
                                            episodeId = episode.id
                                        )
                                    )
                                }
                            }
                        } else {
                            Modifier.clickable {}
                            Modifier.alpha(0.3f) // Adjust alpha for unreleased episodes
                        }
                    )
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                if (isWatchedLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Selected",
                        tint = if (episode.watched) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
