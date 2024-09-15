package com.nikolay.pixabayapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class PixabayImageResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<ImageInfo>
)