package com.dicoding.stories.features.stories.data.repositories

import com.dicoding.stories.features.stories.data.remote.StoriesService
import com.dicoding.stories.features.stories.domain.exceptions.StoryDetailNotFound
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.features.stories.domain.repositories.StoriesRepository
import com.skydoves.sandwich.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor(
  private val storiesService: StoriesService,
) : StoriesRepository {

  override fun getAllStories(
    page: Int,
    size: Int,
    location: Int,
  ): Flow<Result<List<Story>>> = flow {
    storiesService.getStories(page = page, size = size, location = location)
      .suspendOnSuccess { emit(Result.success(data.listStory)) }
      .suspendOnFailure { emit(Result.failure(Throwable("UnknownError"))) }
      .suspendOnError {
        val statusCode = (payload as? Response<*>)?.code() ?: 500
        when (statusCode) {
          401 -> emit(Result.failure(Throwable("Unauthorized")))
          else -> emit(Result.failure(Throwable("UnkownError")))
        }
      }
  }

  override suspend fun getStoryDetails(id: String): Result<Story> {
    var result = Result.failure<Story>(StoryDetailNotFound())

    storiesService.getStoryDetails(id)
      .onSuccess { result = Result.success(data.story) }
      .onFailure { result = Result.failure(Throwable("UnknownError")) }
      .onError {
        val statusCode = (payload as? Response<*>)?.code() ?: 500
        if (statusCode != 404) {
          result = Result.failure(Throwable("UnknownError"))
        }
      }

    return result
  }
}
