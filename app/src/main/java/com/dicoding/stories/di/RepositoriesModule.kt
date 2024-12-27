package com.dicoding.stories.di

import com.dicoding.stories.features.auth.data.repositories.AuthRepositoryImpl
import com.dicoding.stories.features.auth.domain.repositories.AuthRepository
import com.dicoding.stories.features.stories.data.repositories.StoriesPagingRepositoryImpl
import com.dicoding.stories.features.stories.data.repositories.StoriesRepositoryImpl
import com.dicoding.stories.features.stories.domain.repositories.StoriesPagingRepository
import com.dicoding.stories.features.stories.domain.repositories.StoriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

  @Binds
  @Singleton
  abstract fun bindAuthRepository(
    authRepositoryImpl: AuthRepositoryImpl,
  ): AuthRepository

  @Binds
  @Singleton
  abstract fun bindStoriesRepository(
    storiesRepositoryImpl: StoriesRepositoryImpl,
  ): StoriesRepository

  @Binds
  @Singleton
  abstract fun bindStoriesPagingRepository(
    storiesPagingRepository: StoriesPagingRepositoryImpl,
  ): StoriesPagingRepository
}
