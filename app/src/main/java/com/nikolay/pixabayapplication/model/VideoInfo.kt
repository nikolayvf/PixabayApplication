package com.nikolay.pixabayapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoInfo(
    val id: Int,
    val pageURL: String,
    val type: String,
    val tags: String,
    val duration: Int,
    @SerialName("picture_id")
    val pictureId: String? = null,
    val videos: VideoFormats,
    val views: Int,
    val downloads: Int,
    val likes: Int,
    val comments: Int,
    @SerialName("user_id")
    val userId: Int,
    val user: String,
    val userImageURL: String? = null
)


@Serializable
data class VideoFormats(
    val large: VideoFormat? = null,
    val medium: VideoFormat? = null,
    val small: VideoFormat? = null,
    val tiny: VideoFormat? = null
)

@Serializable
data class VideoFormat(
    val url: String,
    val width: Int,
    val height: Int,
    val size: Int,
    val thumbnail: String? = null
)
