package com.dicoding.stories.features.stories.domain.repositories

import com.dicoding.stories.features.stories.domain.models.Story
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoriesRepository {
  suspend fun createStory(
    image: File,
    description: String,
    lat: Double? = null,
    lon: Double? = null,
  ): Result<Boolean>

  fun getAllStories(
    page: Int = 0,
    size: Int = 10,
    location: Int = 0,
  ): Flow<Result<List<Story>>>

  suspend fun getStoryDetails(id: String): Result<Story>
}
