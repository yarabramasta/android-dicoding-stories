package com.dicoding.stories.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stories.features.stories.domain.business.GetStoriesUseCase
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.shared.ui.lib.UiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getStoriesUseCase: GetStoriesUseCase,
) : ViewModel(), ContainerHost<HomeState, HomeSideEffect> {
  override val container = viewModelScope.container<HomeState, HomeSideEffect>(
    HomeState.initial()
  ) {
    intent {
      reduce { state.copy(status = UiStatus.Loading) }
      fetch(GetStoriesUseCase.Params())
    }
  }

  fun refresh() {
    intent {
      reduce { state.copy(status = UiStatus.Loading) }
      fetch(GetStoriesUseCase.Params())
    }
  }

  fun onStoryClick(story: Story) {
    intent {
      postSideEffect(HomeSideEffect.OnStoryClickNavigate(story))
    }
  }

  private fun fetch(params: GetStoriesUseCase.Params) {
    intent {
      container.scope.launch {
        getStoriesUseCase(params).collect {
          it.fold(
            onSuccess = { stories ->
              reduce {
                state.copy(
                  status = UiStatus.Success,
                  stories = stories
                )
              }
            },
            onFailure = { e ->
              reduce {
                state.copy(
                  status = UiStatus.Failure(e.message ?: "UnknownException"),
                )
              }
            }
          )
        }
      }
    }
  }
}
