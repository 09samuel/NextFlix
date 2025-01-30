package com.nextflix.app.seriesList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nextflix.app.seriesList.presentation.components.SeriesItem
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun SearchSeriesScreen(
    //seriesListState: SeriesListState,
    onEvent: (SeriesListUiEvent) -> Unit,
    navController: NavHostController,
    viewModel: SeriesListViewModel
) {
    val seriesListState by viewModel.seriesListState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
    ) {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                // Trigger search on keystroke
                onEvent(SeriesListUiEvent.OnSearchQueryChanged(searchQuery))
            },
            placeholder = {
                Text(text = "Search...")
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(vertical = 32.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    //searchQuery = ""
                }
            )
        )

        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "Search results",
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
        )

        if (seriesListState.isSeriesLoading) {
            val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(4) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .shimmer(shimmerInstance)
                                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                        )
                    }
                }
            }
        } else if (seriesListState.seriesError != "") {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(text = seriesListState.seriesError.toString())
            }
        } else if (seriesListState.searchSeriesList.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Error, // Replace with your preferred icon
                        contentDescription = "No results",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No results to display",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(seriesListState.searchSeriesList.size) { index ->
                    SeriesItem(
                        series = seriesListState.searchSeriesList[index],
                        isWatchLaterLoading = seriesListState.isWatchLaterLoading.getOrDefault(
                            seriesListState.searchSeriesList[index].id,
                            false // Default to false if the value is not found
                        ),
                        onEvent = onEvent,
                        navHostController = navController,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

//                    if (index >= seriesListState.seriesList.size - 1 && !seriesListState.isLoading) {
//                        onEvent(SeriesListUiEvent.Paginate)
//                    }

                }
            }
        }
    }
}
