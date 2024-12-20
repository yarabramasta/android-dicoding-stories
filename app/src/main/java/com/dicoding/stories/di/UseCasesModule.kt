package com.dicoding.stories.di

import com.dicoding.stories.features.auth.data.local.SessionManager
import com.dicoding.stories.features.auth.domain.business.GetSessionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {
  @Provides
  @Singleton
  fun provideGetSessionUseCase(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    sessionManager: SessionManager,
  ): GetSessionUseCase {
    return GetSessionUseCase(dispatcher, sessionManager)
  }
}
