package com.nikolay.pixabayapplication.viewModel

import com.nikolay.pixabayapplication.model.ImageInfo
import com.nikolay.pixabayapplication.model.VideoInfo

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class SuccessImages(val images: List<ImageInfo>) : UiState()
    data class SuccessVideos(val videos: List<VideoInfo>) : UiState()
    data class Error(val message: String) : UiState()
}
