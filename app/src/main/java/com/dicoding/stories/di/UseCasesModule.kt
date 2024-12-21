package com.dicoding.stories.di

import com.dicoding.stories.features.auth.domain.business.GetSessionUseCase
import com.dicoding.stories.features.auth.domain.repositories.AuthRepository
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
    authRepository: AuthRepository,
  ): GetSessionUseCase = GetSessionUseCase(dispatcher, authRepository)
}
