package com.dicoding.stories.features.stories.domain.business

import androidx.compose.runtime.Immutable
import com.dicoding.stories.di.IoDispatcher
import com.dicoding.stories.features.stories.domain.repositories.StoriesRepository
import com.dicoding.stories.shared.domain.business.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.io.File
import javax.inject.Inject

class CreateStoryUseCase @Inject constructor(
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
  private val storiesRepository: StoriesRepository,
) : UseCase<CreateStoryUseCase.Params, Boolean>(dispatcher) {

  override suspend fun execute(params: Params): Result<Boolean> {
    if (params.image.isEmpty()) {
      return Result.failure(IllegalArgumentException("Image path is empty."))
    }

    val image = File(params.image)
    return storiesRepository.createStory(
      image = image,
      description = params.description,
      lat = params.lat,
      lon = params.lon
    )
  }

  @Immutable
  data class Params(
    val image: String,
    val description: String,
    val lat: Double? = null,
    val lon: Double? = null,
  )
}
