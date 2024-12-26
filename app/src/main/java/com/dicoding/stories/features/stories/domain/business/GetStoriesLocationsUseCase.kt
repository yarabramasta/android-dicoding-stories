package com.dicoding.stories.features.stories.domain.business

import com.dicoding.stories.di.IoDispatcher
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.features.stories.domain.repositories.StoriesRepository
import com.dicoding.stories.shared.domain.business.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoriesLocationsUseCase @Inject constructor(
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
  private val storiesRepository: StoriesRepository,
) : FlowUseCase<Unit, List<Story>>(dispatcher) {

  override fun execute(params: Unit): Flow<Result<List<Story>>> {
    return storiesRepository.getStoriesLocations()
  }
}
