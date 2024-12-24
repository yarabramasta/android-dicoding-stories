package com.dicoding.stories.features.stories.data.repositories

import com.dicoding.stories.features.stories.data.remote.StoriesService
import com.dicoding.stories.features.stories.domain.exceptions.StoryDetailNotFound
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.features.stories.domain.repositories.StoriesRepository
import com.skydoves.sandwich.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor(
  private val storiesService: StoriesService,
) : StoriesRepository {
  override suspend fun createStory(
    image: File,
    description: String,
    lat: Double?,
    lon: Double?,
  ): Result<Boolean> {
    val body = MultipartBody.Builder()
      .setType(MultipartBody.FORM)
      .addFormDataPart("description", description)
      .addFormDataPart(
        "photo",
        image.name,
        image.asRequestBody("image/*".toMediaType())
      )
      .let { builder ->
        lat?.let { builder.addFormDataPart("lat", it.toString()) }
        lon?.let { builder.addFormDataPart("lon", it.toString()) }
        builder.build()
      }

    var result = Result.failure<Boolean>(Throwable("UnknownError"))

    storiesService
      .createStory(body)
      .onSuccess { result = Result.success(true) }
      .onFailure { result = Result.failure(Throwable("UnkownError")) }
      .onError {
        val statusCode = (payload as? Response<*>)?.code() ?: 500
        result = when (statusCode) {
          401 -> Result.failure(Throwable("Unauthorized"))
          else -> Result.failure(Throwable("UnkownError"))
        }
      }

    return result
  }

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
