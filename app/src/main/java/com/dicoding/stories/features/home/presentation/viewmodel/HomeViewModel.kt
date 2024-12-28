package com.dicoding.stories.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dicoding.stories.di.IoDispatcher
import com.dicoding.stories.features.stories.domain.repositories.StoriesPagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
@Suppress("CanBeParameter")
class HomeViewModel @Inject constructor(
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
  private val repository: StoriesPagingRepository,
) : ViewModel() {

  val storiesPaging = repository.getStoriesList()
    .flowOn(dispatcher)
    .cachedIn(viewModelScope)
}
