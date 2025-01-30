package com.nextflix.app.upcoming.presentation.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.nextflix.app.api.SeriesApi
import com.nextflix.app.seriesDetails.domain.model.Episode
import com.nextflix.app.upcoming.domain.model.UpcomingEpisode

@Composable
fun UpcomingEpisodeItem(
    upcomingEpisode: UpcomingEpisode,
    //onEvent: (SeriesListUiEvent) -> Unit,
    navHostController: NavHostController,
    onEpisodeClick: (UpcomingEpisode) -> Unit
) {

//    val airDate = upcomingEpisode.air_date
//    val daysLeft = if (airDate != "") {
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val episodeAirDate = LocalDate.parse(airDate, formatter)
//        val currentDate = LocalDate.now()
//        val daysBetween = ChronoUnit.DAYS.between(currentDate, episodeAirDate)
//        if (daysBetween >= 0) daysBetween else 0
//    } else {
//        null
//    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(SeriesApi.IMAGE_BASE_URL + upcomingEpisode.still_path)
            .size(Size.ORIGINAL)
            .build()
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable {
                onEpisodeClick(upcomingEpisode)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            // Image (placeholder or episode thumbnail)
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.secondary)

            ) {
                if (painter.state is AsyncImagePainter.State.Error || painter.state is AsyncImagePainter.State.Loading) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(40.dp),
                        imageVector = Icons.Rounded.ImageNotSupported,
                        contentDescription = upcomingEpisode.name,
                        tint = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f),
                    )
                } else {
                    Image(
                        painter = painter,
                        contentDescription = upcomingEpisode.name,
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
                    .align(Alignment.CenterVertically)
            ) {
                Row {
                    Text(
                        text = upcomingEpisode.series_name.uppercase(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.clickable {
                            navHostController.navigate("details_screen/${upcomingEpisode.series_id}")
                        }
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                        contentDescription = "arrow",
                        modifier = Modifier.size(12.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "S${upcomingEpisode.season_number} | E${upcomingEpisode.episode_number}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = upcomingEpisode.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = if (upcomingEpisode.daysLeft == Int.MAX_VALUE) {
                        "N/A"
                    } else {
                        upcomingEpisode.daysLeft.toString()
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Text(text = "DAYS", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}