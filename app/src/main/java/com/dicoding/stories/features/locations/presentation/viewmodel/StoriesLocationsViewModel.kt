package com.dicoding.stories.features.locations.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stories.features.stories.domain.business.GetStoriesLocationsUseCase
import com.dicoding.stories.shared.ui.lib.UiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import javax.inject.Inject

@HiltViewModel
class StoriesLocationsViewModel @Inject constructor(
  private val getStoriesLocationsUseCase: GetStoriesLocationsUseCase,
) : ViewModel(),
  ContainerHost<StoriesLocationsState, StoriesLocationsSideEffect> {

  override val container =
    viewModelScope.container<StoriesLocationsState, StoriesLocationsSideEffect>(
      StoriesLocationsState.initial()
    ) {
      intent {
        coroutineScope {
          launch {
            getStoriesLocationsUseCase(Unit).collect {
              it.fold(
                onSuccess = { stories ->
                  reduce {
                    state.copy(status = UiStatus.Success, stories = stories)
                  }
                },
                onFailure = {
                  reduce {
                    state.copy(
                      status = UiStatus.Failure(it.message ?: "UnknownError")
                    )
                  }
                }
              )
            }
          }
        }
      }
    }

  fun refresh() {
    intent {
      reduce { state.copy(isRefreshing = true) }
      getStoriesLocationsUseCase(Unit).collect {
        it.fold(
          onSuccess = { stories ->
            reduce { state.copy(isRefreshing = false, stories = stories) }
          },
          onFailure = {
            reduce {
              state.copy(
                isRefreshing = false,
                status = UiStatus.Failure(it.message ?: "UnknownError")
              )
            }
          }
        )
      }
    }
  }
}
