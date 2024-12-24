package com.dicoding.stories.features.stories.domain.business

import com.dicoding.stories.di.IoDispatcher
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.features.stories.domain.repositories.StoriesRepository
import com.dicoding.stories.shared.domain.business.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetStoryDetailUseCase @Inject constructor(
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
  private val storiesRepository: StoriesRepository,
) : UseCase<String, Story>(dispatcher) {

  override suspend fun execute(params: String): Result<Story> {
    return storiesRepository.getStoryDetails(params)
  }
}
