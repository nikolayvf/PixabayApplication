package com.nikolay.pixabayapplication.api

import com.nikolay.pixabayapplication.model.PixabayImageResponse
import com.nikolay.pixabayapplication.model.PixabayVideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayService {
    @GET("/api/")
    suspend fun getImages(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("image_type") imageType: String = "photo",
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): PixabayImageResponse

    @GET("/api/videos/")
    suspend fun getVideos(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): PixabayVideoResponse
}