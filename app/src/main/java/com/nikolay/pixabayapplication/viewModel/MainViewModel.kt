package com.nikolay.pixabayapplication.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikolay.pixabayapplication.model.ImageInfo
import com.nikolay.pixabayapplication.model.VideoInfo
import com.nikolay.pixabayapplication.repositories.PixabayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PixabayRepository
) : ViewModel() {

    var tabIndex = mutableStateOf(0)

    private val _imageUiState = MutableStateFlow<UiState>(UiState.Idle)
    val imageUiState: StateFlow<UiState> get() = _imageUiState

    private val _videoUiState = MutableStateFlow<UiState>(UiState.Idle)
    val videoUiState: StateFlow<UiState> get() = _videoUiState

    var imageQuery: String = "nature"
    var videoQuery: String = "nature"

    var selectedImage: ImageInfo? = null
    var selectedVideo: VideoInfo? = null

    init {
        searchImages(imageQuery)
        searchVideos(videoQuery)
    }

    fun searchImages(query: String, page: Int = 1, perPage: Int = 20) {
        imageQuery = query
        viewModelScope.launch {
            _imageUiState.value = UiState.Loading
            try {
                val response = repository.fetchImages(query, page, perPage)
                _imageUiState.value = UiState.SuccessImages(response.hits)
            } catch (e: IOException) {
                _imageUiState.value = UiState.Error("Please check your internet connection.")
            } catch (e: HttpException) {
                _imageUiState.value = UiState.Error("Server error: ${e.message}")
            } catch (e: SerializationException) {
                _imageUiState.value = UiState.Error("An unexpected error occurred.")
            } catch (e: Exception) {
                _imageUiState.value = UiState.Error("An unexpected error occurred.")
            }
        }
    }

    fun searchVideos(query: String, page: Int = 1, perPage: Int = 20) {
        videoQuery = query
        viewModelScope.launch {
            _videoUiState.value = UiState.Loading
            try {
                val response = repository.fetchVideos(query, page, perPage)
                _videoUiState.value = UiState.SuccessVideos(response.hits)
            } catch (e: IOException) {
                _videoUiState.value = UiState.Error("Please check your internet connection.")
            } catch (e: HttpException) {
                _videoUiState.value = UiState.Error("Server error: ${e.message}")
            } catch (e: SerializationException) { // Catch serialization errors
                _videoUiState.value = UiState.Error("Data parsing error: ${e.localizedMessage}")
            } catch (e: Exception) {
                _videoUiState.value = UiState.Error("An unexpected error occurred: ${e.localizedMessage}")
            }
        }
    }

}
