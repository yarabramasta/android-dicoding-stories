package com.dicoding.stories.features.stories.domain.repositories

import com.dicoding.stories.features.stories.domain.models.Story
import kotlinx.coroutines.flow.Flow

interface StoriesRepository {
  fun getAllStories(
    page: Int = 0,
    size: Int = 10,
    location: Int = 0,
  ): Flow<Result<List<Story>>>

  suspend fun getStoryDetails(id: String): Result<Story>
}
