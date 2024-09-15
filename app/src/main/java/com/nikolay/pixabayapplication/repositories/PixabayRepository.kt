package com.nikolay.pixabayapplication.repositories

import com.nikolay.pixabayapplication.api.PixabayService
import com.nikolay.pixabayapplication.model.PixabayImageResponse
import com.nikolay.pixabayapplication.model.PixabayVideoResponse
import javax.inject.Inject

class PixabayRepository @Inject constructor(
    private val pixabayService: PixabayService
) {
    companion object {
        private const val API_KEY = "45356457-706800fc581e8a74a7996bdc6"
    }

    suspend fun fetchImages(query: String, page: Int, perPage: Int): PixabayImageResponse {
        return pixabayService.getImages(API_KEY, query, page = page, perPage = perPage)
    }

    suspend fun fetchVideos(query: String, page: Int, perPage: Int): PixabayVideoResponse {
        return pixabayService.getVideos(API_KEY, query, page = page, perPage = perPage)
    }
}
