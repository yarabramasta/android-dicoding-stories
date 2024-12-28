package com.dicoding.stories.features.stories.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.dicoding.stories.features.stories.data.local.StoryEntity
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.features.stories.domain.repositories.StoriesPagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoriesPagingRepositoryImpl @Inject constructor(
  private val storiesPager: Pager<Int, StoryEntity>,
) : StoriesPagingRepository {

  override fun getStoriesList(): Flow<PagingData<Story>> {
    return storiesPager.flow.map { pagingData ->
      pagingData.map { it.toDomain() }
    }
  }
}
