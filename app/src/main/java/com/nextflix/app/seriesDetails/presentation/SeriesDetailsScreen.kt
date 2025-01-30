package com.nextflix.app.seriesDetails.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.nextflix.app.api.SeriesApi
import com.nextflix.app.seriesDetails.presentation.components.AboutTabScreen
import com.nextflix.app.seriesDetails.presentation.components.EpisodesTabScreen

@Composable
fun SeriesDetailsScreen(navController: NavHostController) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("About", "Episodes")

    val detailsViewModel = hiltViewModel<SeriesDetailsViewModel>()
    val detailsState = detailsViewModel.detailsState.collectAsState().value


    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(SeriesApi.IMAGE_BASE_URL + detailsState.details?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    )

    val dominantColor = MaterialTheme.colorScheme.secondaryContainer

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Image Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
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
                        contentDescription = detailsState.details?.name,
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

            // Series Title and seasons
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                detailsState.details?.let {
                    Text(
                        text = it.name,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = "${it.number_of_seasons} Seasons",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Tabs Section
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                )
            }
        }

        // Tab Content
        when (selectedTabIndex) {
            0 -> AboutTabScreen(
                detailsState,
                detailsViewModel::onEvent,
                navController = navController
            )

            1 -> EpisodesTabScreen(detailsState, detailsViewModel::onEvent)
        }
    }
}
