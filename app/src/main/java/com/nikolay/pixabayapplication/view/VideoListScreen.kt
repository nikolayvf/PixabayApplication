package com.nikolay.pixabayapplication.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nikolay.pixabayapplication.model.VideoInfo
import com.nikolay.pixabayapplication.viewModel.MainViewModel
import com.nikolay.pixabayapplication.viewModel.UiState
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun VideoListScreen(
    mainViewModel: MainViewModel,
    onVideoClick: (VideoInfo) -> Unit
) {
    var query by rememberSaveable { mutableStateOf(mainViewModel.videoQuery) }
    val uiState by mainViewModel.videoUiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { newQuery ->
                query = newQuery
            },
            label = { Text("Search Videos") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    mainViewModel.searchVideos(query)
                }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is UiState.SuccessVideos -> {
                val videos = (uiState as UiState.SuccessVideos).videos
                if (videos.isNotEmpty()) {
                    LazyColumn {
                        items(videos) { video ->
                            val previewImageUrl = video.pictureId?.let {
                                "https://i.vimeocdn.com/video/${it}_295x166.jpg"
                            } ?: "default_image_url"

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable { onVideoClick(video) }
                            ) {
                                AsyncImage(
                                    model = previewImageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = video.tags.capitalize(),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "By ${video.user}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "No videos found.",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
            is UiState.Error -> {
                val message = (uiState as UiState.Error).message
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            else -> { }
        }
    }
}
