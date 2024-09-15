package com.nikolay.pixabayapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class PixabayVideoResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<VideoInfo>
)