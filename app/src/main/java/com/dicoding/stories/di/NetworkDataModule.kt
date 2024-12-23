package com.dicoding.stories.di

import com.dicoding.stories.BuildConfig
import com.dicoding.stories.features.auth.data.local.SessionManager
import com.dicoding.stories.features.auth.data.remote.AuthService
import com.dicoding.stories.features.stories.data.remote.StoriesService
import com.dicoding.stories.shared.lib.http.interceptors.AuthTokenInterceptor
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkDataModule {

  @Provides
  @Singleton
  fun provideOkHttpClient(
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    sessionManager: SessionManager,
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(
        HttpLoggingInterceptor().apply {
          level = HttpLoggingInterceptor.Level.BODY
        }
      )
      .addInterceptor(
        AuthTokenInterceptor(
          dispatcher = ioDispatcher,
          sessionManager = sessionManager
        )
      )
      .build()
  }

  @Provides
  @Singleton
  fun provideRetrofit(
    okHttpClient: OkHttpClient,
  ): Retrofit {
    val json = Json { ignoreUnknownKeys = true }
    return Retrofit.Builder()
      .baseUrl(BuildConfig.DICODING_EVENTS_API_BASE_URL)
      .addConverterFactory(
        json.asConverterFactory("application/json".toMediaType())
      )
      .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
      .client(okHttpClient)
      .build()
  }

  @Provides
  @Singleton
  fun provideAuthService(
    retrofit: Retrofit,
  ): AuthService = retrofit.create(AuthService::class.java)

  @Provides
  @Singleton
  fun provideStoriesService(
    retrofit: Retrofit,
  ): StoriesService = retrofit.create(StoriesService::class.java)
}
