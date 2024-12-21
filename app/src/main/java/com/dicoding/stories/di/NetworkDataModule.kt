package com.dicoding.stories.di

import com.dicoding.stories.BuildConfig
import com.dicoding.stories.features.auth.data.remote.AuthService
import com.dicoding.stories.features.auth.domain.business.GetSessionUseCase
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
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkDataModule {

  @Provides
  @Singleton
  fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BODY
    }
  }

  @Provides
  @Singleton
  fun provideAuthTokenInterceptor(
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    getSessionUseCase: GetSessionUseCase,
  ): AuthTokenInterceptor = AuthTokenInterceptor(
    dispatcher = ioDispatcher,
    getSessionUseCase = getSessionUseCase
  )

  @Provides
  @Singleton
  fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor,
    authTokenInterceptor: AuthTokenInterceptor,
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .connectTimeout(10, TimeUnit.SECONDS)
      .addInterceptor(loggingInterceptor)
      .addInterceptor(authTokenInterceptor)
      .build()
  }

  @Provides
  @Singleton
  fun provideRetrofit(
    okHttpClient: OkHttpClient,
  ): Retrofit {
    val mimetype = "application/json; charset=UTF8".toMediaType()
    return Retrofit.Builder()
      .baseUrl(BuildConfig.DICODING_EVENTS_API_BASE_URL)
      .addConverterFactory(Json.asConverterFactory(mimetype))
      .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
      .client(okHttpClient)
      .build()
  }

  @Provides
  @Singleton
  fun provideAuthService(
    retrofit: Retrofit,
  ): AuthService = retrofit.create(AuthService::class.java)
}
