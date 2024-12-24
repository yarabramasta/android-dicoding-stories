package com.dicoding.stories.di

import com.dicoding.stories.features.auth.domain.business.GetSessionUseCase
import com.dicoding.stories.features.auth.domain.business.SignInUseCase
import com.dicoding.stories.features.auth.domain.business.SignOutUseCase
import com.dicoding.stories.features.auth.domain.business.SignUpUseCase
import com.dicoding.stories.features.auth.domain.repositories.AuthRepository
import com.dicoding.stories.features.stories.domain.business.CreateStoryUseCase
import com.dicoding.stories.features.stories.domain.business.GetStoriesUseCase
import com.dicoding.stories.features.stories.domain.business.GetStoryDetailUseCase
import com.dicoding.stories.features.stories.domain.repositories.StoriesRepository
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

  @Provides
  @Singleton
  fun provideSignInUseCase(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    authRepository: AuthRepository,
  ): SignInUseCase = SignInUseCase(dispatcher, authRepository)

  @Provides
  @Singleton
  fun provideSignUpUseCase(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    authRepository: AuthRepository,
  ): SignUpUseCase = SignUpUseCase(dispatcher, authRepository)

  @Provides
  @Singleton
  fun provideSignOutUseCase(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    authRepository: AuthRepository,
  ): SignOutUseCase = SignOutUseCase(dispatcher, authRepository)

  @Provides
  @Singleton
  fun provideGetStoriesUseCase(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    storiesRepository: StoriesRepository,
  ) = GetStoriesUseCase(dispatcher, storiesRepository)

  @Provides
  @Singleton
  fun provideGetStoryDetailUseCase(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    storiesRepository: StoriesRepository,
  ) = GetStoryDetailUseCase(dispatcher, storiesRepository)

  @Provides
  @Singleton
  fun provideCreateStoryUseCase(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    storiesRepository: StoriesRepository,
  ) = CreateStoryUseCase(dispatcher, storiesRepository)
}
