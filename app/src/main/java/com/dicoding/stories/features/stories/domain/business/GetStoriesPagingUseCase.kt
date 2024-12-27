package com.dicoding.stories.features.stories.domain.business

import androidx.paging.PagingData
import com.dicoding.stories.di.IoDispatcher
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.features.stories.domain.repositories.StoriesPagingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetStoriesPagingUseCase @Inject constructor(
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
  private val repository: StoriesPagingRepository,
) {

  operator fun invoke(): Flow<PagingData<Story>> {
    return repository.getStoriesList()
      .flowOn(dispatcher)
  }
}
