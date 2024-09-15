package com.nikolay.pixabayapplication.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.nikolay.pixabayapplication.model.ImageInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailsScreen(image: ImageInfo, navController: NavHostController) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Image Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                Image(
                    painter = rememberAsyncImagePainter(image.largeImageURL),
                    contentDescription = "Image preview",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(image.imageWidth.toFloat() / image.imageHeight),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tags
                Text(
                    text = image.tags.capitalize(Locale.current),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    if (image.userImageURL.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(image.userImageURL),
                            contentDescription = "Uploader profile image",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = image.user, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "Uploaded by ${image.user}",
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
                    StatItem(label = "Views", value = image.views)
                    StatItem(label = "Downloads", value = image.downloads)
                    StatItem(label = "Likes", value = image.likes)
                    StatItem(label = "Comments", value = image.comments)
                }
                HorizontalDivider()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Image Details",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                InfoRow(label = "Type", value = image.type.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        java.util.Locale.ROOT
                    ) else it.toString()
                })
                InfoRow(label = "Dimensions", value = "${image.imageWidth} x ${image.imageHeight}")
                InfoRow(label = "Size", value = "${image.imageSize / 1024} KB")
                InfoRow(label = "Preview Size", value = "${image.previewWidth} x ${image.previewHeight}")
                InfoRow(label = "Web Format Size", value = "${image.webformatWidth} x ${image.webformatHeight}")

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}
