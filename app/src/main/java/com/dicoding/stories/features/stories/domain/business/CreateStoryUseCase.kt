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
    return storiesRepository.createStory(
      image = params.image,
      description = params.description,
      lat = params.lat,
      lon = params.lon
    )
  }

  @Immutable
  data class Params(
    val image: File,
    val description: String,
    val lat: Double? = null,
    val lon: Double? = null,
  )
}
