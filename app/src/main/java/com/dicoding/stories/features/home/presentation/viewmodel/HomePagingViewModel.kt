package com.dicoding.stories.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.stories.features.stories.domain.business.GetStoriesPagingUseCase
import com.dicoding.stories.features.stories.domain.models.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
@Suppress("CanBeParameter")
class HomePagingViewModel @Inject constructor(
  private val getStoriesPagingUseCase: GetStoriesPagingUseCase,
) : ViewModel() {

  val storiesPagingDataFlow: Flow<PagingData<Story>> =
    getStoriesPagingUseCase.invoke()
      .cachedIn(viewModelScope)
}
