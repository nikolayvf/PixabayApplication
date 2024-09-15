package com.nikolay.pixabayapplication.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.nikolay.pixabayapplication.model.VideoInfo
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDetailsScreen(video: VideoInfo, navController: NavHostController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Select the best available video format
    val videoFormat = video.videos.large ?: video.videos.medium ?: video.videos.small ?: video.videos.tiny

    if (videoFormat == null) {
        // Handle the case where no video format is available
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Video Details") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            },
            content = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No video available")
                }
            }
        )
        return
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(videoFormat.url)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Video Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
            ) {
                AndroidView(
                    factory = {
                        PlayerView(it).apply {
                            player = exoPlayer
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(videoFormat.width.toFloat() / videoFormat.height)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = video.tags.capitalize(Locale.current),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    if (!video.userImageURL.isNullOrEmpty()) {
                        AsyncImage(
                            model = video.userImageURL,
                            contentDescription = "Uploader profile image",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = video.user, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "Uploaded by ${video.user}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider()
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    StatItem(label = "Views", value = video.views)
                    StatItem(label = "Downloads", value = video.downloads)
                    StatItem(label = "Likes", value = video.likes)
                    StatItem(label = "Comments", value = video.comments)
                }
                HorizontalDivider()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Video Details",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                InfoRow(label = "Type", value = video.type.capitalize(Locale.current))
                InfoRow(label = "Duration", value = "${video.duration} seconds")
                InfoRow(
                    label = "Resolution",
                    value = "${videoFormat.width} x ${videoFormat.height}"
                )
                InfoRow(
                    label = "Size",
                    value = "${videoFormat.size / (1024 * 1024)} MB"
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}

