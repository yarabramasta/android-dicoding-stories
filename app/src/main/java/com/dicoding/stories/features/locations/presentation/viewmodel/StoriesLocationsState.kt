package com.dicoding.stories.features.locations.presentation.viewmodel

import androidx.compose.runtime.Immutable
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.shared.ui.lib.UiStatus

@Immutable
data class StoriesLocationsState(
  val status: UiStatus,
  val isRefreshing: Boolean,
  val stories: List<Story>,
) {
  companion object {
    fun initial() = StoriesLocationsState(
      status = UiStatus.Loading,
      isRefreshing = false,
      stories = emptyList()
    )
  }
}
