package com.dicoding.stories.features.stories.presentation.viewmodel.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stories.features.stories.domain.business.GetStoryDetailUseCase
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.shared.ui.lib.UiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import javax.inject.Inject

@Suppress("CanBeParameter")
@HiltViewModel
class StoryDetailsViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val getStoryDetailUseCase: GetStoryDetailUseCase,
) : ViewModel(), ContainerHost<StoryDetailsState, StoryDetailsSideEffect> {
  override val container =
    viewModelScope.container<StoryDetailsState, StoryDetailsSideEffect>(
      initialState = StoryDetailsState.initial(
        story = savedStateHandle.get<Story>("story")
      )
    ) {
      intent {
        reduce { state.copy(status = UiStatus.Loading) }
        if (state.story != null) {
          fetch(state.story?.id ?: "")
        }
      }
    }

  fun refresh() {
    intent {
      reduce { state.copy(status = UiStatus.Loading, isRefreshing = true) }
      fetch(state.story?.id ?: "")
      reduce { state.copy(isRefreshing = false) }
    }
  }

  private fun fetch(detailsId: String) {
    intent {
      container.scope.launch {
        getStoryDetailUseCase(detailsId)
          .fold(
            onSuccess = {
              reduce {
                state.copy(status = UiStatus.Success, story = it)
              }
            },
            onFailure = {
              reduce {
                state.copy(
                  status = UiStatus.Failure(it.message ?: "UnknownError"),
                  story = null
                )
              }
            }
          )
      }
    }
  }
}
