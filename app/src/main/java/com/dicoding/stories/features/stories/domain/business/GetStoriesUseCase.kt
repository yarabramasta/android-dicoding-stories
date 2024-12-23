package com.dicoding.stories.features.stories.domain.business

import androidx.compose.runtime.Immutable
import com.dicoding.stories.di.IoDispatcher
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.features.stories.domain.repositories.StoriesRepository
import com.dicoding.stories.shared.domain.business.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoriesUseCase @Inject constructor(
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
  private val storiesRepository: StoriesRepository,
) : FlowUseCase<GetStoriesUseCase.Params, List<Story>>(
  dispatcher
) {

  override fun execute(params: Params): Flow<Result<List<Story>>> {
    return storiesRepository.getAllStories(
      page = params.page,
      size = params.size,
      location = params.location
    )
  }

  @Immutable
  class Params(
    val page: Int = 0,
    val size: Int = 10,
    val location: Int = 0,
  )
}
