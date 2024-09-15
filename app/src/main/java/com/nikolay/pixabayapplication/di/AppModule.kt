package com.nikolay.pixabayapplication.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nikolay.pixabayapplication.api.PixabayService
import com.nikolay.pixabayapplication.repositories.PixabayRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://pixabay.com/"

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json { ignoreUnknownKeys = true }
    }

    @Provides
    @Singleton
    fun providePixabayService(json: Json): PixabayService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(PixabayService::class.java)
    }

    @Provides
    @Singleton
    fun providePixabayRepository(pixabayService: PixabayService): PixabayRepository {
        return PixabayRepository(pixabayService)
    }
}
