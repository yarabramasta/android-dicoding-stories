package com.dicoding.stories.features.stories.domain.repositories

import androidx.paging.PagingData
import com.dicoding.stories.features.stories.domain.models.Story
import kotlinx.coroutines.flow.Flow

interface StoriesPagingRepository {
  fun getStoriesList(): Flow<PagingData<Story>>
}
