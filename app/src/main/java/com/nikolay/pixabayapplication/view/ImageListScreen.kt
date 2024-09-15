package com.nikolay.pixabayapplication.view

import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import com.nikolay.pixabayapplication.model.ImageInfo
import com.nikolay.pixabayapplication.viewModel.MainViewModel
import com.nikolay.pixabayapplication.viewModel.UiState
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalFocusManager
import coil.compose.rememberAsyncImagePainter
import java.util.Locale

@Composable
fun ImageListScreen(
    mainViewModel: MainViewModel,
    onImageClick: (ImageInfo) -> Unit
) {
    var query by rememberSaveable { mutableStateOf(mainViewModel.imageQuery) }
    val uiState by mainViewModel.imageUiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(16.dp)) {
        // Search input field
        OutlinedTextField(
            value = query,
            onValueChange = { newQuery ->
                query = newQuery
            },
            label = { Text("Search Images") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    mainViewModel.searchImages(query)
                }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is UiState.SuccessImages -> {
                val images = (uiState as UiState.SuccessImages).images
                if (images.isNotEmpty()) {
                    LazyColumn {
                        items(images) { image ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable { onImageClick(image) }
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(image.previewURL),
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = image.tags.replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.ROOT
                                            ) else it.toString()
                                        },
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "By ${image.user}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "No images found.",
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
