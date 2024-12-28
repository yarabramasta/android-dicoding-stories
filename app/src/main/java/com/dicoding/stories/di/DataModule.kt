package com.dicoding.stories.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.dicoding.stories.features.stories.data.local.StoryEntity
import com.dicoding.stories.features.stories.data.paging.StoriesRemoteMediator
import com.dicoding.stories.features.stories.data.remote.StoriesService
import com.dicoding.stories.shared.data.local.DicodingStoriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
  @OptIn(ExperimentalPagingApi::class)
  @Provides
  @Singleton
  fun provideStoriesPager(
    dicodingStoriesDatabase: DicodingStoriesDatabase,
    storiesService: StoriesService,
  ): Pager<Int, StoryEntity> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      remoteMediator = StoriesRemoteMediator(
        db = dicodingStoriesDatabase,
        api = storiesService
      ),
      pagingSourceFactory = {
        dicodingStoriesDatabase.storiesDao.pagingSource()
      }
    )
  }
}
